package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.SiteMenuPartContentDao;
import com.easysoft.ecommerce.model.Row;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.SiteMenuPartContent;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value="/admin/design")
public class DesignAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesignAdminController.class);

    @Autowired
    private SiteMenuPartContentDao siteMenuPartContentDao;
    @Autowired
    private ServiceLocator serviceLocator;


    /***************************************************************************************************
     *  Implement Slide Management
     ***************************************************************************************************/
    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/design/" + action;
    }

    @RequestMapping(value = {"update.html", "save.html", "insert.html", "update_slide.html", "insert_slide.html", "save_slide.html",
            "insert_content.html", "update_content.html", "save_content.html"}, method = RequestMethod.GET)
    public String updateGetMethod() throws Exception {
        return "admin/menu/index";
    }
    /**
     * This will be called when delete a page
     */
    @RequestMapping(value = {"delete_slide.html","delete_content.html"}, method = RequestMethod.GET,produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deletePartContent(HttpServletRequest request, @Valid Long id) {
        Messages messages = new Messages();
        try {
            if (id > 0) {
                    SiteMenuPartContent partContent = siteMenuPartContentDao.findById(id);
                if (partContent != null) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                    //Clear Cache
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromPartContent(request, id));
                    siteMenuPartContentDao.remove(partContent);
                } else {
                    messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            messages.addError("ERROR: Cannot delete parent page.");
        }
        return messages.toString();
    }
    /**
     * This will be called when delete a page
     */
    @RequestMapping(value = {"deleterow.html"}, method = RequestMethod.GET,produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteRow(HttpServletRequest request, @Valid Long id) {
        Messages messages = new Messages();
        try {
            if (id > 0) {
                    Row row = serviceLocator.getRowDao().findById(id);
                if (row != null) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                    //Clear Cache before delete
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromRow(request, id));
                    serviceLocator.getRowDao().remove(row);
                } else {
                    messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            messages.addError("ERROR: Cannot delete parent page.");
        }
        return messages.toString();
    }

    /**
     * This method is called when insert a page
     */
    @RequestMapping(value = {"update_slide.html", "update_content.html", "insert_slide.html", "insert_content.html"} , method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String insertPartContent(HttpServletRequest request, @Valid SiteMenuPartContent entity, @Valid Long rowId, @Valid Long widgetTemplateId, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            //TODO: show error messages
        }
        String messages = serviceLocator.getAdminService().createOrUpdatePartContent(entity, rowId).toString();
        //Clear Cache
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromRow(request, rowId));
        return messages;
    }

    @RequestMapping(value = "{id}/activate_partcontent.html", method = {RequestMethod.POST, RequestMethod.GET})
    @CSRFProtection
    public String activeContent(@PathVariable Long id, @Valid String flag) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (id != null && id > 0) {
            SiteMenuPartContent partContent = serviceLocator.getSiteMenuPartContentDao().findById(id);
            if (partContent != null) {
                if (!StringUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")) {
                    partContent.setActive("Y");
                } else {
                    partContent.setActive("N");
                }
                this.serviceLocator.getSiteMenuPartContentDao().merge(partContent);
                //Clear Cache
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromPartContent(null, id));
                return "admin/ok";
            }
        }
        return "admin/fail";
    }

    /**
     * This method is called when insert a page
     */
    @RequestMapping(value = {"insert_row.html", "update_row.html"} , method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String updateRow(@Valid Row entity, @Valid Long menuId, @Valid Long widgetTemplateId, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            //TODO: show error messages
        }
        String messages = serviceLocator.getAdminService().createOrUpdateRow(entity, menuId, widgetTemplateId).toString();
        //Clear Cache
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromMenu(null, menuId));
        return messages;
    }

    @RequestMapping(value = "change_status_show_row_title.html", method = RequestMethod.GET)
    public @ResponseBody String changeRowTitleStatus(@Valid Long rowId, HttpServletRequest request) throws Exception {
        String check = request.getParameter("flag");

        if (rowId != null && rowId > 0) {
            Row row = serviceLocator.getRowDao().findById(rowId);
            if (!StringUtils.isEmpty(check) && check.equalsIgnoreCase("true")) {
                row.setShowTitle("Y");
            } else {
                row.setShowTitle("N");
            }
            serviceLocator.getRowDao().merge(row);
            //Clear Cache
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromRow(request, rowId));
        }
        return "ok";
    }

    @RequestMapping(value = "reorderRow.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String reOrder(HttpServletRequest request, @Valid Long currentItem,  @Valid String orderList) throws Exception {
        Messages messages = new Messages();

        /*Convert to List*/
        List<Long> listOrder = new ArrayList<Long>();
        String []array = orderList.split(",");
        for (String s : array) {
            listOrder.add(new Long(s));
        }
        Float seq = 0f;
        int index = listOrder.indexOf(currentItem);
        //item moved to the top of table
        if (index == 0) {
            if (listOrder.size() > 1) {
                Row row = serviceLocator.getRowDao().findById(listOrder.get(1));
                if (row != null) {
                    seq = row.getSequence() / 2f;
                }
            }
            //item moved to the bottom of table
        } else if (index == listOrder.size() - 1) {
            if (listOrder.size() > 1) {
                Row row = serviceLocator.getRowDao().findById(listOrder.get(listOrder.size() - 2));
                if (row != null) {
                    seq = row.getSequence() + 1;
                }
            }
            //item moved to middle of table
        } else if (index > 0 && index < listOrder.size() - 1) {
            Row row1 = serviceLocator.getRowDao().findById(listOrder.get(index - 1));
            Row row2 = serviceLocator.getRowDao().findById(listOrder.get(index + 1));
            if (row1 != null && row2 != null) {
                seq = (row1.getSequence() + row2.getSequence())/2f;
            }
        }

        /*Update order*/
        if (seq > 0) {
            Row row = serviceLocator.getRowDao().findById(currentItem);
            if (row != null) {
                row.setSequence(seq);
                serviceLocator.getRowDao().merge(row);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.order.change.successfully", null, LocaleContextHolder.getLocale()));
                //Clear Cache
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromRow(request, row.getId()));
            }
        }

        return messages.toString();
    }

    @RequestMapping(value = "reorderPartContent.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String reOrderPartContent(HttpServletRequest request, @Valid Long currentItem,  @Valid String orderList) throws Exception {
        Messages messages = new Messages();

        /*Convert to List*/
        List<Long> listOrder = new ArrayList<Long>();
        String []array = orderList.split(",");
        for (String s : array) {
            listOrder.add(new Long(s));
        }
        Float seq = 0f;
        int index = listOrder.indexOf(currentItem);
        //item moved to the top of table
        if (index == 0) {
            if (listOrder.size() > 1) {
                SiteMenuPartContent partContent = siteMenuPartContentDao.findById(listOrder.get(1));
                if (partContent != null) {
                    seq = partContent.getSequence() / 2f;
                }
            }
            //item moved to the bottom of table
        } else if (index == listOrder.size() - 1) {
            if (listOrder.size() > 1) {
                SiteMenuPartContent partContent = siteMenuPartContentDao.findById(listOrder.get(listOrder.size() - 2));
                if (partContent != null) {
                    seq = partContent.getSequence() + 1;
                }
            }
            //item moved to middle of table
        } else if (index > 0 && index < listOrder.size() - 1) {
            SiteMenuPartContent partContent1 = siteMenuPartContentDao.findById(listOrder.get(index - 1));
            SiteMenuPartContent partContent2 = siteMenuPartContentDao.findById(listOrder.get(index + 1));
            if (partContent1 != null && partContent2 != null) {
                seq = (partContent1.getSequence() + partContent2.getSequence())/2f;
            }
        }

        /*Update order*/
        if (seq > 0) {
            SiteMenuPartContent partContent = siteMenuPartContentDao.findById(currentItem);
            if (partContent != null) {
                partContent.setSequence(seq);
                siteMenuPartContentDao.merge(partContent);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.order.change.successfully", null, LocaleContextHolder.getLocale()));
                //Clear Cache
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromPartContent(request, partContent.getId()));
            }
        }

        return messages.toString();
    }
    @RequestMapping(value = "select_product.html", method = {RequestMethod.GET, RequestMethod.POST})
    @CSRFProtection
    public String selectProductPartContent(HttpServletRequest request, @Valid Long rowId,  @Valid Long productId, @Valid String flag) throws Exception {
        Messages messages = new Messages();
        flag = WebUtil.convertActiveFlag(flag);

        Map input = new HashMap();
        input.put("row.id", rowId);
        input.put("header", productId+"");
        List<SiteMenuPartContent> list = siteMenuPartContentDao.findBy(input, 0,1);
        /*Add a product to PartContent of the row*/
        boolean isChange = false;
        if ("Y".equals(flag)) {
            //product existed
            if (list != null && list.size() > 0) {
                isChange = true;
                //add new partContent
            } else {
                SiteMenuPartContent partContent = new SiteMenuPartContent();
                Calendar calendar = Calendar.getInstance();
                //Create news category record
                partContent.setHeader(productId+"");
                partContent.setCreatedDate(calendar.getTime());
                partContent.setStartDate(calendar.getTime());
                partContent.setActive("Y");
                calendar.add(Calendar.YEAR, 100); //TODO: Allow customer set the end date which want to show the content
                partContent.setEndDate(calendar.getTime());
                Float maxSequence = siteMenuPartContentDao.getMaxSequenceBy("row.id", rowId);
                partContent.setSequence(maxSequence+1);
                if (rowId != null && rowId > 0) {
                    Row row = serviceLocator.getRowDao().findById(rowId);
                    partContent.setRow(row);
                }
                siteMenuPartContentDao.persist(partContent);
                isChange = true;

            }
            /*Remove Product From PartContent*/
        } else {
            if (list != null && list.size() > 0) {
                for (SiteMenuPartContent partContent: list) {
                    siteMenuPartContentDao.remove(partContent);
                }
                isChange = true;
            }
        }
        if (isChange) {
            //Clear Cache
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromRow(request, rowId));
            return "admin/ok";
        }
        return "admin/fail";
    }

    @RequestMapping(value = "select_news.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String selectNewsPartContent(HttpServletRequest request, @Valid Long rowId,  @Valid Long newsId, @Valid String flag) throws Exception {
        Messages messages = new Messages();
        flag = WebUtil.convertActiveFlag(flag);

        Map input = new HashMap();
        input.put("row.id", rowId);
        input.put("header", newsId+"");
        List<SiteMenuPartContent> list = siteMenuPartContentDao.findBy(input, 0,1);
        /*Add a News to PartContent of the row*/
        boolean isChange = false;
        if ("Y".equals(flag)) {
            //news existed
            if (list != null && list.size() > 0) {
                isChange = true;
                //add new partContent
            } else {
                SiteMenuPartContent partContent = new SiteMenuPartContent();
                Calendar calendar = Calendar.getInstance();
                //Create news category record
                partContent.setHeader(newsId+"");
                partContent.setCreatedDate(calendar.getTime());
                partContent.setStartDate(calendar.getTime());
                partContent.setActive("Y");
                calendar.add(Calendar.YEAR, 100);
                partContent.setEndDate(calendar.getTime());
                Float maxSequence = siteMenuPartContentDao.getMaxSequenceBy("row.id", rowId);
                partContent.setSequence(maxSequence+1);
                if (rowId != null && rowId > 0) {
                    Row row = serviceLocator.getRowDao().findById(rowId);
                    partContent.setRow(row);
                }
                siteMenuPartContentDao.persist(partContent);
                isChange = true;

            }
            /*Remove Product From PartContent*/
        } else {
            if (list != null && list.size() > 0) {
                for (SiteMenuPartContent partContent: list) {
                    siteMenuPartContentDao.remove(partContent);
                }
                isChange = true;
            }
        }
        if (isChange) {
            //Clear Cache
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKeyFromRow(request, rowId));
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            return messages.toString();
        }
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        return messages.toString();
    }


}
