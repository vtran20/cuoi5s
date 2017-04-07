package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.MenuDao;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @deprecated
 */
@Controller
@RequestMapping("/admin/page")
public class PageAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageAdminController.class);

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private ServiceLocator serviceLocator;


    /***************************************************************************************************
     *  Implement Page Management
     ***************************************************************************************************/
//    @RequestMapping("/index.html")
//    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        return new ModelAndView("/admin/page/page");
//    }

    /**
     * Forward to page for rendering html page.
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/{uri}.html")
    public ModelAndView recentlyView() throws Exception {
        return new ModelAndView("/admin/page/page");
    }
    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/page/" + action;
    }

    @RequestMapping(value = "update.html", method = RequestMethod.GET)
    public String updateGetMethod() throws Exception {
        return "admin/page/index";
    }

    @RequestMapping(value = "insert.html", method = RequestMethod.GET)
    public String insertGetMethod() throws Exception {
        return "admin/page/index";
    }


//    @RequestMapping(value = "save.html", method = RequestMethod.POST)
//    @CSRFProtection
//    public void ajaxSavePage(@Valid String menuUri, @Valid String preContent, @Valid String preHeader, @Valid String preFooter,
//                             HttpServletRequest request, HttpServletResponse response) throws Exception {
//        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//        Menu menu = serviceLocator.getMenuDao().getMenu(site, menuUri, "N");
//        Messages messages = null;
//        if (menu != null) {
//            messages = serviceLocator.getAdminService().createOrUpdatePage(preContent, preHeader, preFooter, menu);
//        } else {
//            Page page = serviceLocator.getPageDao().getPageByURI(site, menuUri, false);
//            if (page != null) {
//                messages = serviceLocator.getAdminService().createOrUpdatePage(preContent, preHeader, preFooter, page);
//            } else {
//                response.getWriter().write("Cannot save this content. Menu doesn't exist");
//                LOGGER.error("Cannot save this content. Menu doesn't exist:" + menuUri);
//            }
//        }
//
//        //Publish content to web. This method will copy content from preContent to Content on Page table
//        String action = request.getParameter("action");
//        if ("publish".equals(action)) {
//            CacheKeyGenerator keyGenerator = new CacheKeyGenerator();
//            Page page = pageDao.getPageByMenuURI(site, menuUri, false);
//            if (page == null) {
//                page = pageDao.getPageByURI(site, menuUri, false);
//            }
//            if (messages != null && !messages.isEmpty()) messages.clean();
//            messages = serviceLocator.getAdminService().publishPage(page);
//            serviceLocator.getCacheData().removeCacheTag(keyGenerator.generateCacheKey(request, menuUri));
//        }
//
//        response.getWriter().write((messages != null && messages.getInfos() != null && messages.getInfos().size() > 0) ? messages.getInfos().get(0) : "");
//
//    }

    /**
     * Move this method to save function
     *
     * @param menuUri
     * @param request
     * @param response
     * @throws Exception
     */
    /**
    @RequestMapping(value = "publish.html", method = RequestMethod.GET)
    @CSRFProtection
    public void publishPage(@Valid String menuUri, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CacheKeyGenerator keyGenerator = new CacheKeyGenerator();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Page page = pageDao.getPageByMenuURI(site, menuUri, false);
        if (page == null) {
            page = pageDao.getPageByURI(site, menuUri, false);
        }
        Messages messages = serviceLocator.getAdminService().publishPage(page);
        response.getWriter().write((messages.getInfos() != null && messages.getInfos().size() > 0) ? messages.getInfos().get(0) : "");
        serviceLocator.getCacheData().removeCacheTag(keyGenerator.generateCacheKey(request, menuUri));
    }
    */

    /**
     * This will be called when delete a page
     */
//    @RequestMapping(value = "delete.html", method = RequestMethod.GET)
//    @CSRFProtection
//    public ModelAndView delete(@Valid Long id) {
//        Messages messages = new Messages();
//        try {
//            if (id > 0) {
//                Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//                Page page = pageDao.findById(id, site.getId());
//                if (page != null) {
//                    if ("".equals(page.getUri())) {//Uri is empty mean that this is home page
//                        messages.addInfo("We cannot remove home page");
//                    } else {
//                        messages.addInfo("Removed page <b>" + id + "</b>");
//                        pageDao.remove(page);
//                    }
//                } else {
//                    messages.addInfo("This page is not available");
//                }
//            } else {
//                messages.addInfo("Page is invalid");
//            }
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            messages.addError("ERROR: Cannot delete parent page.");
//        }
//        return new ModelAndView("admin/page/index", "messages", messages);
//    }

    /**
     * This method is called when insert a page
     */
    @RequestMapping(value = "insert.html", method = RequestMethod.POST)
    @CSRFProtection
//    public ModelAndView insert(@Valid Page entity, BindingResult result) throws Exception {
//
//        if (result.hasErrors()) {
//            //TODO: show error messages
//        }
//        Map map = new HashMap();
//        map.put("page", entity);
//        map.put("messages", serviceLocator.getAdminService().createOrUpdatePage(null, null, null, entity));
//        return new ModelAndView("admin/page/index", map);
//    }

    /**
     * This will be called when save to update the Page
     */
//    @RequestMapping(value = "update.html", method = RequestMethod.POST)
//    @CSRFProtection
//    public ModelAndView update(@Valid Page page,  BindingResult result) throws Exception {
//        if (result.hasErrors()) {
//            //TODO: show error messages
//        }
//        Map map = new HashMap();
//        map.put("page", page);
//        map.put("messages", serviceLocator.getAdminService().createOrUpdatePage(null, null, null, page));
//        return new ModelAndView("admin/page/index", map);
//    }

    /**
     * Check and get uri for page.
     */
//    @RequestMapping(value = "geturiforpage.html", method = RequestMethod.GET)
//    public void getURIForPage(HttpServletRequest request, @Valid String pageName, @Valid Long pageId, HttpServletResponse response) throws Exception {
//        if (StringUtils.isNotEmpty(pageName)) {
//            response.getWriter().write(generatePageUri(pageName, pageId));
//        }
//        response.getWriter().write("");
//    }
//
//    private String generatePageUri (String pageName, Long pageId) {
//        if (StringUtils.isNotEmpty(pageName)) {
//            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//            String uriNonHTM = WebUtil.getURI(pageName);
//            uriNonHTM = uriNonHTM.toLowerCase();
//            String uri = uriNonHTM + ".html";
//            int count = 1;
//            if (pageId != null && pageId > 0) { // old page
//                Page page = pageDao.findById(pageId);
//                if (page.getUri() != null && !page.getUri().equals(uriNonHTM+".html")) {
//                    while (isExistURI(site, uriNonHTM+".html")) {
//                        uriNonHTM = uriNonHTM + "-" + count;
//                        uri = uriNonHTM + ".html";
//                        count++;
//                    }
//                } else {
//                    //keep the current uri
//                }
//            } else { // new page
//                while (isExistURI(site, uriNonHTM+".html")) {
//                    uriNonHTM = uriNonHTM + "-" + count;
//                    uri = uriNonHTM + ".html";
//                    count++;
//                }
//            }
//            return uri;
//        } else {
//            return "";
//        }
//
//    }
//    private boolean isExistURI(Site site, String uri) {
//        Page page = pageDao.getPageByMenuURI(site, uri, false);
//        return page != null;
//    }

    public MenuDao getMenuDao() {
        return menuDao;
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }
}
