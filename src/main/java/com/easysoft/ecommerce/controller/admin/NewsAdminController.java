package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.NewsCategoryDao;
import com.easysoft.ecommerce.dao.NewsDao;
import com.easysoft.ecommerce.model.News;
import com.easysoft.ecommerce.model.NewsCategory;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value="/admin/news")
public class NewsAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsAdminController.class);

    @Autowired
    private NewsDao newsDao;
    @Autowired
    private NewsCategoryDao newsCategoryDao;
    @Autowired
    private ServiceLocator serviceLocator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, ServiceLocatorHolder.getServiceLocator().getSystemContext().getServiceLocator().getLocale());
        //SimpleDateFormat df = new SimpleDateFormat(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get("DATE_FORMAT"));
        df.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
    }


    /***************************************************************************************************
     *  Implement News Management
     ***************************************************************************************************/
    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/news/" + action;
    }

    @RequestMapping(value = {"update.html", "insert.html"}, method = RequestMethod.GET)
    public String updateGetMethod() throws Exception {
        return "admin/news/index";
    }

    @RequestMapping(value = "{newsCategoryId}/index.html", method = RequestMethod.GET)
    public ModelAndView editForm(@PathVariable Long newsCategoryId) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        NewsCategory newsCategory = newsCategoryDao.findById(newsCategoryId, site.getId());
        if (newsCategory != null) {
            Map map = new HashMap();
            map.put("newsCategory", newsCategoryDao.findById(newsCategoryId, site.getId()));

            return new ModelAndView("admin/news/form", map);
        } else {
            return new ModelAndView("admin/news/form");
        }
    }

    /**
     * Check and get uri in news category table.
     */
//    @RequestMapping(value = "geturifornewscategory.html", method = RequestMethod.GET)
//    public void getURIForNewsCategory(HttpServletRequest request, @Valid String newsCategoryName, @Valid Long newsCategoryId, HttpServletResponse response) throws Exception {
//        if (StringUtils.isNotEmpty(newsCategoryName)) {
//            response.getWriter().write(generateNewsCategoryUri(newsCategoryName, newsCategoryId));
//        }
//        response.getWriter().write("");
//    }

    /**
     * This method is called when insert a news category
     */
    @RequestMapping(value = {"insert.html","update.html"}, method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String insertOrUpdate(@Valid NewsCategory entity, @Valid Long parentId, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            //TODO: show error messages
        }
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsIndexCacheKey(null));
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsCategoryCacheKeyFromCategoryNews(null,entity.getId()));
        return serviceLocator.getAdminService().createOrUpdateNewsCategory(entity, parentId).toString();
    }

    /**
     * This will be called when delete a news category
     */
    @RequestMapping(value = "delete.html", method = RequestMethod.GET, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteCategory(@Valid Long id) {
        Messages messages = new Messages();
        try {
            if (id > 0) {
                Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
                NewsCategory newsCategory = newsCategoryDao.findById(id, site.getId());
                if (newsCategory != null) {
                    //Do not remove if the category has subcategories in it.
                    List subNewsCategories = newsCategoryDao.findBy("parentNewsCategory.id", newsCategory.getId(), site.getId());
                    if (subNewsCategories != null && subNewsCategories.size() > 0) {
                        messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("newscategory.delete.error.1", null, LocaleContextHolder.getLocale()));
                        return messages.toString();
                    }
                    //Do not remove if the category has product associate with it
                    Long count = newsDao.countNewsInNewsCategory(newsCategory.getId(), "N");
                    if (count > 0) {
                        messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("newscategory.delete.error.2", null, LocaleContextHolder.getLocale()));
                        return messages.toString();
                    }
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsIndexCacheKey(null));
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsCategoryCacheKeyFromCategoryNews(null,id));
                    newsCategory.setParentNewsCategory(null);
                    newsCategoryDao.remove(newsCategory);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                } else {
                    messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            messages.addError("ERROR: Cannot delete news category. Please make sure that all sub new categories and news of this one were deleted");
        }
        return messages.toString();
    }

    @RequestMapping(value = "reorder.html", method = RequestMethod.GET)
    public ModelAndView reOrder(HttpServletRequest request, @Valid String currentItem,  @Valid String orderList) throws Exception {
        Messages messages = new Messages();

        if (!WebUtil.reOrderValidation(orderList, currentItem)) {
            messages.addError("Reorder is invalid");
            return new ModelAndView("admin/news/index", "messages", messages);
        }

        /*Group parent and sub*/
        List<String> parents = new ArrayList<String>();
        List<String> subs = new ArrayList<String>();
        String []array = orderList.split(",");
        for (String s : array) {
            if (s.split("-").length == 2) {
                parents.add(s);
            } else if (s.split("-").length == 3) {
                //only add sub in the same group
                if (currentItem.split("-")[0].equals(s.split("-")[0])) {
                    subs.add(s);
                }
            }
        }
        /*Reorder for parent*/
        Float seq = 0f;
        if (currentItem.split("-").length == 2) {
            seq = WebUtil.getSequence(parents, currentItem);
            if (seq > 0) {
                String [] currItem = currentItem.split("-");
                NewsCategory newsCategory = newsCategoryDao.findById(Long.valueOf(currItem[0]));
                newsCategory.setSequence(seq);
                newsCategoryDao.merge(newsCategory);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.order.change.successfully", null, LocaleContextHolder.getLocale()));
            }

        } else if (currentItem.split("-").length == 3) {
            seq = WebUtil.getSequence(subs, currentItem);
            if (seq > 0) {
                String [] currItem = currentItem.split("-");
                NewsCategory newsCategory = newsCategoryDao.findById(Long.valueOf(currItem[1]));
                newsCategory.setSequence(seq);
                newsCategoryDao.merge(newsCategory);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.order.change.successfully", null, LocaleContextHolder.getLocale()));
            }
        }
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsIndexCacheKey(null));
        return new ModelAndView("admin/news/index", "messages", messages);
    }
    ////////////////////////////////////////////////////////////////////////
    // Starting implement for News.
    ////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "updatenews.html", method = RequestMethod.GET)
    public String updateNewsGetMethod() throws Exception {
        return "admin/news/create";
    }

    @RequestMapping(value = "insertnews.html", method = RequestMethod.GET)
    public String insertNewsGetMethod() throws Exception {
        return "admin/news/create";
    }

    /**
     * This method is called when insert a news category
     */
    @RequestMapping(value = {"insertnews.html","updatenews.html"}, method = RequestMethod.POST)
    @CSRFProtection
    public @ResponseBody Map updateNews(HttpServletRequest request, @Valid News entity, @Valid Long[] newsCategoryId, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            //TODO: show error messages
        }
        Map map = new HashMap();
        map.put("newscategories", newsCategoryId);
        Messages messages = serviceLocator.getAdminService().createOrUpdateNews(entity, map);

        //Publish news if need
        if (entity.getId() > 0) {
            //Publish if request
            String type = request.getParameter("type");
            if ("publish".equals(type)) {
                Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
                News news = newsDao.findById(entity.getId(), site.getId());
                messages = serviceLocator.getAdminService().publishNews(news);
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsIndexCacheKey(null));
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsCategoryCacheKeyFromNews(null,entity.getId()));
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsCacheKeyFromNews(null,entity.getId()));
                messages.clean();
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.published.success", null, LocaleContextHolder.getLocale()));
            }
            //TODO: Remove cache
            //CacheKeyGenerator keyGenerator = new CacheKeyGenerator();
            //serviceLocator.getCacheData().removeCacheTag(keyGenerator.generateCacheKey(request, menuUri));
        }
        map.put("messages", messages.toString());
        map.put("id", entity.getId());
        return map;
    }

    /**
     * This will be called when delete a news category
     */
    @RequestMapping(value = "deletenews.html", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteNews(@Valid Long id) {
        Messages messages = new Messages();
        try {
            if (id > 0) {
                Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
                News news = newsDao.findById(id, site.getId());
                if (news != null) {
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsIndexCacheKey(null));
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsCategoryCacheKeyFromNews(null,id));
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsCacheKeyFromNews(null,id));
                    newsDao.removeNewsNewsCategory(news.getId());
                    newsDao.remove(news);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                } else {
                    messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            messages.addError("ERROR: Cannot delete news. Please make sure that all sub new categories and news of this one were deleted");
        }
        return messages.toString();
    }
    /**
     * This will be called when delete a news category
     */
    @RequestMapping(value = "deletethumbnailimage.html", headers="Accept=*/*", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody Map deleteImageNews(@RequestParam(value = "id", required = false) Long id,
                                             @RequestParam(value = "thumbImg", required = true) String thumbImg
                                             ) {
        Map <String, Object> result = new HashMap<String, Object>();
        if (id != null && id > 0) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            News news = newsDao.findById(id, site.getId());
            news.setThumbImg("");
            newsDao.merge(news);
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsCacheKeyFromNews(null,id));
        }
        String imageUri = "";
        //Parse thumbImg to get image-uri
        if (StringUtils.isNotEmpty(thumbImg)) {
            String temp[] = thumbImg.split("[/.]");
            if (temp.length > 2) {
                imageUri = temp[temp.length-2];
            }
        }
        result.put("image_uri", imageUri);
        try {
            result.put("key", URLEncoder.encode(WebUtil.encrypt(imageUri), "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
        }
        return result;
    }

    /**
     * This will be called when copy a news
     */
    @RequestMapping(value = "copynews.html", method = {RequestMethod.GET/*, RequestMethod.POST*/}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String copyNews(@Valid Long id) {
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateNewsIndexCacheKey(null));
        return serviceLocator.getAdminService().copyNews(id).toString();
    }

    public NewsDao getNewsDao() {
        return newsDao;
    }

    public NewsCategoryDao getNewsCategoryDao() {
        return newsCategoryDao;
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }
}
