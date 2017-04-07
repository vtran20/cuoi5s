package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.service.AdminService;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.CacheData;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private RowDao rowDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private NewsDao newsDao;
    @Autowired
    private NewsCategoryDao newsCategoryDao;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private SiteTemplateDao siteTemplateDao;
    @Autowired
    private CacheData cacheData;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private SiteMenuPartContentDao siteMenuPartContentDao;
    @Autowired
    private WidgetTemplateDao widgetTemplateDao;

    /**
     * This will be call when insert/update menu. When insert/update menu, we do the same action to page. This make sure
     * menu and page will be consistency
     *
     *
     * @param entity
     * @param parentId
     * @return
     * @throws Exception
     */

    public Messages createOrUpdateMenu(Menu entity, Long parentId) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        boolean isUpdate = false;
        Menu parentMenu = null;
        if (parentId != null && parentId > 0) {
            parentMenu = menuDao.findById(parentId, site.getId());
        }
        if (entity.isEmptyId()) {
            Date date = new Date();
            //Create menu record
            entity.setCreatedDate(date);
            if (!"E".equals(entity.getMenuTemplate())) {
                entity.setUri(generatePageUri(entity.getName(), null));
            }
            entity.setUpdatedDate(date);
            entity.setSite(site);
            entity.setParentMenu(parentMenu);
            float maxSequence = menuDao.getMaxSequence(site.getId());
            entity.setSequence(maxSequence+1f);
            menuDao.persist(entity);

            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("menu.create.successful", null, LocaleContextHolder.getLocale()));
        } else {
            //Update Menu
            Menu originalMenu = menuDao.findById(entity.getId(), site.getId());

            if ((entity.getName() != null && !entity.getName().equals(originalMenu.getName()) || (entity.getUri() != null && !entity.getUri().equalsIgnoreCase(originalMenu.getUri())))) {
                originalMenu.setName(entity.getName());
                //if menu is not menu template and not home page, then update uri
                if (!"Y".equals(originalMenu.getMenuTemplate()) && !"Y".equals(originalMenu.getHomePage())) {
                    if (!"E".equals(originalMenu.getMenuTemplate())) {
                        originalMenu.setUri(generatePageUri(entity.getName(), originalMenu.getId()));
                    } else {
                        originalMenu.setUri(entity.getUri());
                    }

                }
                isUpdate = true;
            }

            //Update Parent Menu in 3 cases below. if both is null, don't need to update.
            Menu originalParentMenu = menuDao.getParentMenu(site, originalMenu.getId(), "N");
            if ((parentMenu != null && originalParentMenu != null && !parentMenu.getId().equals(originalParentMenu.getId())) ||
                    (parentMenu == null && originalParentMenu != null) ||
                    (parentMenu != null && originalParentMenu == null)) {
                originalMenu.setParentMenu(parentMenu);
                isUpdate = true;
            }

            if (entity.getActive() != null && !entity.getActive().equals(originalMenu.getActive())) {
                if ("Y".equals(entity.getHomePage())) {
                } else {
                    if ("Y".equals(entity.getActive())) {
                        originalMenu.setActive(entity.getActive());
                    } else {
                        originalMenu.setActive(entity.getActive());
                    }
                    isUpdate = true;
                }
            }

            //This is home page, so set uri is empty
            if ("Y".equals(entity.getHomePage())) {
                originalMenu.setUri("");
                isUpdate = true;
            }

            if (!isUpdate) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            } else {
                //update menu
                originalMenu.setUpdatedDate(new Date());
                originalMenu.setSite(site);
                this.menuDao.merge(originalMenu);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }

        }
        return messages;
    }

    private String generatePageUri (String pageName, Long pageId) {
        if (StringUtils.isNotEmpty(pageName)) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            String uriNonHTM = WebUtil.getURI(pageName);
            uriNonHTM = uriNonHTM.toLowerCase();
            String uri = uriNonHTM + ".html";
            int count = 1;
            if (pageId != null && pageId > 0) { // old page
                Menu menu = menuDao.findById(pageId, site.getId());
                if (menu.getUri() != null && !menu.getUri().equals(uriNonHTM+".html")) {
                    while (isExistURI(site, uriNonHTM+".html")) {
                        uriNonHTM = uriNonHTM + "-" + count;
                        uri = uriNonHTM + ".html";
                        count++;
                    }
                } else {
                    //keep the current uri
                }
            } else { // new page
                while (isExistURI(site, uriNonHTM+".html")) {
                    uriNonHTM = uriNonHTM + "-" + count;
                    uri = uriNonHTM + ".html";
                    count++;
                }
            }
            return uri;
        } else {
            return "";
        }

    }

    private boolean isExistURI(Site site, String uri) {
        Menu menu = menuDao.getMenu(site, uri, "N");
        return menu != null;
    }

    private String generateNewsUri(String title, Long newsId) {
        if (StringUtils.isNotEmpty(title)) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            String uriNonHTM = WebUtil.getURI(title);
            uriNonHTM = uriNonHTM.toLowerCase();
            String uri = uriNonHTM + ".html";
            int count = 1;
            if (newsId != null && newsId > 0) { // old news
                News news = newsDao.findById(newsId, site.getId());
                if (news.getUri() != null && !news.getUri().equals(uriNonHTM+".html")) {
                    while (isExistNewsURI(site, uriNonHTM + ".html")) {
                        uriNonHTM = uriNonHTM + "-" + count;
                        uri = uriNonHTM + ".html";
                        count++;
                    }
                } else {
                    //keep the current uri
                }
            } else { // new news
                while (isExistNewsURI(site, uriNonHTM + ".html")) {
                    uriNonHTM = uriNonHTM + "-" + count;
                    uri = uriNonHTM + ".html";
                    count++;
                }
            }
            return uri;
        } else {
            return "";
        }

    }
    private boolean isExistNewsURI(Site site, String uri) {
        News news = newsDao.findUniqueBy("uri", uri, site.getId());
        return news != null;
    }

    /**
     * Remove menu. This action will remove menu and page that associate with it.
     *
     * @param id
     * @return
     */
    @Override
    public Messages deleteMenu(Long id) {
        Messages messages = new Messages();
        if (id != null && id > 0) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            Menu menu = menuDao.findById(id, site.getId());
            if (menu != null) {
                if ("Y".equals(menu.getHomePage())) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("menu.cannot.delete.homepage", null, LocaleContextHolder.getLocale()));
                } else {
                    if (menu.getParentMenu() != null) {
                        menu.setParentMenu(null);
                    }
                    //remove menu
                    menuDao.remove(menu);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
            }
        }
        return messages;
    }


    /**
     * Create or update should be preContent column. We will move from preContent to content column when customers click publish
     * @param preContent
     * @return
     */
    @Override
    public Messages createOrUpdatePage(String preContent, String preHeader, String preFooter, Menu menu) {
        Messages messages = new Messages();
//        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//        if (menu != null) {
//            Page originalPage =  pageDao.getPageByMenuId(site, menu.getId(), false);
//            //Update page
//            if (originalPage != null) {
//                if ((originalPage.getPreContent() == null && preContent != null) ||
//                        (originalPage.getPreContent() != null && preContent != null && !originalPage.getPreContent().equals(preContent))) {
//                    originalPage.setPreContent(preContent.trim());
//                    messages.addInfo("Page content was updated");
//                }
//                if (messages.getInfos() != null && messages.getInfos().size() == 0) {
//                    messages.addInfo("No data changed");
//                }
//                pageDao.merge(originalPage);
//            } else {
//                //this is a new page for this menu, so create a relationship and update uri for page
//                Menu orginalMenu = menuDao.findById(menu.getId());
//                Page newPage = new Page();
//                newPage.setSite(site);
//                newPage.setTitle(orginalMenu.getName());
//                if (preContent != null) {
//                    newPage.setPreContent(preContent.trim());
//                }
//                newPage.setActive("Y");
//                newPage.setUpdatedDate(new Date());
//                newPage.setUri(generatePageUri(orginalMenu.getName(), null));
//                newPage.setId(null);
//                Float maxSequence = pageDao.getMaxSequence(site.getId());
//                newPage.setSequence(maxSequence+1f);
//                pageDao.persist(newPage);
//                orginalMenu.setPage(newPage);
//                menuDao.merge(orginalMenu);
//                messages.addInfo("Page content was created");
//            }
//
//        } else {
//            //this is new page without associate with any menu
//            Page newPage = new Page();
//            newPage.setSite(site);
//            newPage.setActive("Y");
//            newPage.setUpdatedDate(new Date());
//            newPage.setUri(generatePageUri(newPage.getTitle(), null));
//            Float maxSequence = pageDao.getMaxSequence(site.getId());
//            newPage.setSequence(maxSequence+1);
//            pageDao.persist(newPage);
//        }
//
//        //Update Header and Footer if any.
//        SiteTemplate siteTemplate = siteTemplateDao.findUniqueBy("site.id", site.getId());
//        if (siteTemplate != null) {
//            boolean isHeaderFooterChanged = false;
//            //Save header
//            if (StringUtils.isNotEmpty(preHeader) && !preHeader.equals(siteTemplate.getPreCmsHeader())) {
//                siteTemplate.setPreCmsHeader(preHeader);
//                messages.addInfo("Header content were saved");
//                isHeaderFooterChanged = true;
//            }
//
//            //Save footer
//            if (StringUtils.isNotEmpty(preFooter) && !preFooter.equals(siteTemplate.getPreCmsFooter())) {
//                siteTemplate.setPreCmsFooter(preFooter);
//                messages.addInfo("Footer content were saved");
//                isHeaderFooterChanged = true;
//            }
//
//            if (isHeaderFooterChanged) {
//                siteTemplateDao.merge(siteTemplate);
//            }
//            //Remove cache and reload site template.
//            cacheData.removeCommonCache(site.getDomain());
//            cacheData.removeCommonCache(site.getSubDomain());
//        }
        return messages;
    }

    /**
     * Create or update should be preContent column. We will move from preContent to content column when customers click publish
     * @return
     */
    @Override
    public Messages createOrUpdatePage(String preContent, String preHeader, String preFooter) {
        Messages messages = new Messages();
//        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//        if (page != null) {
//            if (!page.isEmptyId()) { // Update page
//                Page originalPage =  pageDao.getPageById(site, page.getId(), false);
//                boolean isChange = false;
//                if (originalPage != null) {
//                    if ((originalPage.getPreContent() == null && preContent != null) ||
//                            (originalPage.getPreContent() != null && preContent != null && !originalPage.getPreContent().equals(preContent))) {
//                        originalPage.setPreContent(preContent.trim());
//                        isChange = true;
//                    }
//
//                    if (page.getTitle() != null && originalPage.getTitle() != null && !page.getTitle().equals(originalPage.getTitle())) {
//                        originalPage.setTitle(page.getTitle());
//                        isChange = true;
//                    }
//
//                    if (page.getUri() != null && originalPage.getUri() != null && !page.getUri().equals(originalPage.getUri())) {
//                        originalPage.setUri(page.getUri());
//                        isChange = true;
//                    }
//                    if (page.getActive() != null && originalPage.getActive() != null && !page.getActive().equals(originalPage.getActive())) {
//                        originalPage.setActive(page.getActive());
//                        isChange = true;
//                    }
//
//                    if (isChange) {
//                        originalPage.setUpdatedDate(new Date());
//                        pageDao.merge(originalPage);
//                        messages.addInfo("Page content was updated");
//                    }
//                }
//            } else { // Insert new page
//                page.setSite(site);
//                if (StringUtils.isNotEmpty(preContent)) {
//                    page.setPreContent(preContent.trim());
//                }
//                page.setUpdatedDate(new Date());
//                page.setId(null);
//                Float maxSequence = pageDao.getMaxSequence(site.getId());
//                page.setSequence(maxSequence + 1f);
//                pageDao.persist(page);
//                messages.addInfo("Page content was created");
//            }
//
//        }
//
//        //Update Header and Footer if any.
//        SiteTemplate siteTemplate = siteTemplateDao.findUniqueBy("site.id", site.getId());
//        if (siteTemplate != null) {
//            boolean isHeaderFooterChanged = false;
//            //Save header
//            if (StringUtils.isNotEmpty(preHeader) && !preHeader.equals(siteTemplate.getPreCmsHeader())) {
//                siteTemplate.setPreCmsHeader(preHeader);
//                messages.addInfo("Header content were saved");
//                isHeaderFooterChanged = true;
//            }
//
//            //Save footer
//            if (StringUtils.isNotEmpty(preFooter) && !preFooter.equals(siteTemplate.getPreCmsFooter())) {
//                siteTemplate.setPreCmsFooter(preFooter);
//                messages.addInfo("Footer content were saved");
//                isHeaderFooterChanged = true;
//            }
//
//            if (isHeaderFooterChanged) {
//                siteTemplateDao.merge(siteTemplate);
//            }
//            //Remove cache and reload site template.
//            cacheData.removeCommonCache(site.getDomain());
//            cacheData.removeCommonCache(site.getSubDomain());
//
//        }
//        if (messages.getInfos() == null || messages.getInfos().size() == 0) {
//            messages.addInfo("No data changed");
//        }
        return messages;
    }

    @Override
    public Messages publishPage(Menu page) {
        Messages messages = new Messages();
//        String temp = "No new content was published";
//        boolean isSiteTemplatePublish = false;
//        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//        if (page != null) {
//            //If change something
//            if (page.getContent() == null ||
//                    (page.getContent() != null && !page.getContent().equals(page.getPreContent()))) {
//                page.setContent(page.getPreContent());
//                pageDao.merge(page);
//                temp = "The content was published";
//            }
//        }
//
//        //Update Header and Footer if any.
//        SiteTemplate siteTemplate = siteTemplateDao.findUniqueBy("site.id", site.getId());
//        if (siteTemplate != null) {
//            if (siteTemplate.getPreCmsHeader() != null && !siteTemplate.getPreCmsHeader().equals(siteTemplate.getCmsHeader())) {
//                siteTemplate.setCmsHeader(siteTemplate.getPreCmsHeader());
//                isSiteTemplatePublish = true;
//            }
//
//            if (siteTemplate.getPreCmsFooter() != null && !siteTemplate.getPreCmsFooter().equals(siteTemplate.getCmsFooter())) {
//                siteTemplate.setCmsFooter(siteTemplate.getPreCmsFooter());
//                isSiteTemplatePublish = true;
//            }
//
//            if (isSiteTemplatePublish) {
//                siteTemplateDao.merge(siteTemplate);
//                temp = "The content was published";
//            }
//        }
//        messages.addInfo(temp);
//        //Remove cache and reload site template.
//        cacheData.removeCommonCache(site.getDomain());
//        cacheData.removeCommonCache(site.getSubDomain());
        return messages;
    }


    @Override
    public Messages createOrUpdateNewsCategory(NewsCategory entity, Long parentId) {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        NewsCategory parentNewsCategory = null;
        if (parentId != null && parentId > 0) {
            parentNewsCategory = newsCategoryDao.findById(parentId);
        }
        if (entity.isEmptyId()) {
            Date date = new Date();
            //Create news category record
            entity.setCreatedDate(date);
            entity.setUpdatedDate(date);
            entity.setSite(site);
            entity.setUri(generateNewsCategoryUri(entity.getName(), null));
            entity.setParentNewsCategory(parentNewsCategory);
            Float maxSequence = newsCategoryDao.getMaxSequence(site.getId());
            entity.setSequence(maxSequence+1);
            newsCategoryDao.persist(entity);

            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        } else {
            //Update News Category
            NewsCategory originalNewsCategory = newsCategoryDao.findById(entity.getId());
            boolean isChange = false;
            if (entity.getName() != null && !entity.getName().equals(originalNewsCategory.getName())) {
                originalNewsCategory.setName(entity.getName());
                originalNewsCategory.setUri(generateNewsCategoryUri(entity.getName(), originalNewsCategory.getId()));
                isChange = true;
            }

            //Update Parent News category in 3 cases below. if both is null, don't need to update.
            NewsCategory originalParentNewsCategory = newsCategoryDao.getParentNewsCategory(site, originalNewsCategory.getId(), "N");
            if ((parentNewsCategory != null && originalParentNewsCategory != null && !parentNewsCategory.getId().equals(originalParentNewsCategory.getId())) ||
                    (parentNewsCategory == null && originalParentNewsCategory != null) ||
                    (parentNewsCategory != null && originalParentNewsCategory == null)) {
                originalNewsCategory.setParentNewsCategory(parentNewsCategory);
                isChange = true;
            }

            if (entity.getActive() != null && !entity.getActive().equals(originalNewsCategory.getActive())) {
                if ("Y".equals(entity.getActive())) {
                    originalNewsCategory.setActive(entity.getActive());
                } else {
                    originalNewsCategory.setActive(entity.getActive());
                }
                isChange = true;
            }

            if (isChange) {
                //update menu
                originalNewsCategory.setUpdatedDate(new Date());
                originalNewsCategory.setSite(site);
                this.newsCategoryDao.merge(originalNewsCategory);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            } else {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            }

        }
        return messages;
    }

    private String generateNewsCategoryUri(String newsCategoryName, Long newsCategoryId) {
        if (StringUtils.isNotEmpty(newsCategoryName)) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            String uriNonHTM = WebUtil.getURI(newsCategoryName);
            uriNonHTM = uriNonHTM.toLowerCase();
            String uri = uriNonHTM + ".html";
            int count = 1;
            if (newsCategoryId != null && newsCategoryId > 0) { // old news category
                NewsCategory newsCategory = newsCategoryDao.findById(newsCategoryId, site.getId());
                if (newsCategory.getUri() != null && !newsCategory.getUri().equals(uriNonHTM+".html")) {
                    while (isExistNewsCategoryURI(site, uriNonHTM+".html")) {
                        uriNonHTM = uriNonHTM + "-" + count;
                        uri = uriNonHTM + ".html";
                        count++;
                    }
                } else {
                    //keep the current uri
                }
            } else { // new news category
                while (isExistNewsCategoryURI(site, uriNonHTM+".html")) {
                    uriNonHTM = uriNonHTM + "-" + count;
                    uri = uriNonHTM + ".html";
                    count++;
                }
            }
            return uri;
        } else {
            return "";
        }

    }
    private boolean isExistNewsCategoryURI(Site site, String uri) {
        NewsCategory newsCategory = newsCategoryDao.getNewsCategory(site, uri, "N");
        return newsCategory != null;
    }

    @Override
    public Messages createOrUpdateNews(News entity, Map map) {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (entity.isEmptyId()) {
            Date date = new Date();
            //Create news record
            entity.setCreatedDate(date);
            entity.setUpdatedDate(date);
            entity.setUri(generateNewsUri(entity.getTitle(), null));
            entity.setSite(site);
            float maxSequence = newsDao.getMaxSequence(site.getId());
            entity.setSequence(maxSequence + 1f);

            //Create relationship between News and News Category
            Long [] categoryIds = (Long[]) map.get("newscategories");
            if (categoryIds != null && categoryIds.length > 0) {
                //Add new relationship
                for (Long id : categoryIds) {
                    NewsCategory newsCategory = newsCategoryDao.findById(id, site.getId());
                    entity.getNewsCategories().add(newsCategory);
                    newsCategory.getNewses().add(entity);
                }
            }

            newsDao.persist(entity);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        } else {
            //Update news
            News originalNews = newsDao.findById(entity.getId(), site.getId());
            boolean isChange = false;
            if (entity.getTitle() != null && !entity.getTitle().equals(originalNews.getTitle())) {
                originalNews.setTitle(entity.getTitle());
                originalNews.setUri(generateNewsUri(entity.getTitle(), originalNews.getId()));
                isChange = true;
            }
            if (entity.getThumbImg() != null && !entity.getThumbImg().equals(originalNews.getThumbImg())) {
                originalNews.setThumbImg(entity.getThumbImg());
                isChange = true;
            }
            if (entity.getPreShortDescription() != null && !entity.getPreShortDescription().equals(originalNews.getPreShortDescription())) {
                originalNews.setPreShortDescription(entity.getPreShortDescription());
                isChange = true;
            }
            if (entity.getPreContent() != null && !entity.getPreContent().equals(originalNews.getPreContent())) {
                originalNews.setPreContent(entity.getPreContent());
                isChange = true;
            }

            if (entity.getActive() != null && !entity.getActive().equals(originalNews.getActive())) {
                if ("Y".equals(entity.getActive())) {
                    originalNews.setActive(entity.getActive());
                } else {
                    originalNews.setActive(entity.getActive());
                }
                isChange = true;
            }

            // remove all relationship between News and NewsCategory
            News news = newsDao.findById(entity.getId(), site.getId());
            if (news != null) {
                isChange = true;
                newsDao.removeNewsNewsCategory(news.getId());
            }

            //Create again relationship between News and News Category
            Long [] categoryIds = (Long[]) map.get("newscategories");
            if (categoryIds != null && categoryIds.length > 0) {
                //Add new relationship
                originalNews.getNewsCategories().clear();
                for (Long id : categoryIds) {
                    NewsCategory newsCategory = newsCategoryDao.findById(id, site.getId());
                    originalNews.getNewsCategories().add(newsCategory);
                    newsCategory.getNewses().add(originalNews);
                }
                isChange = true;
            }

            if (isChange) {
                //update menu
                originalNews.setUpdatedDate(new Date());
                originalNews.setSite(site);
                this.newsDao.merge(originalNews);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            } else {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            }

        }
        return messages;
    }

    @Override
    public Messages copyNews(Long id) {
        Messages messages = new Messages();
        if (id > 0) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            News news = newsDao.findById(id, site.getId());
            News copyNews = new News();
            try {
                BeanUtils.copyProperties(copyNews, news);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            copyNews.setId(null);
            copyNews.setNewsCategories(null);
            copyNews.setTitle("Copy of " + news.getTitle());
            copyNews.setUri(generateNewsUri(news.getTitle(), null));
            copyNews.setActive("N");
            copyNews.setCreatedDate(new Date());
            newsDao.persist(copyNews);
            messages.addInfo("Copied news: <b>" + news.getTitle() + "</b>");
        } else {
            messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
        }
        return messages;
    }

        @Override
    public Messages createOrUpdateVideos(List<Video> videos, String categoryIds) {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (videos != null) {
            List <Category> categories = null;
            if (!StringUtils.isEmpty(categoryIds)) {
                categories = this.categoryDao.findObjectInBy(null, "id", categoryIds, null, null, null, site.getId());
            }

            for (Video video : videos) {
                if (video.isEmptyId()) {
                    Date date = new Date();
                    //Create video record
                    video.setCreatedDate(date);
                    video.setUpdatedDate(date);
                    video.setSite(site);
                    float maxSequence = videoDao.getMaxSequence(site.getId());
                    video.setSequence(maxSequence + 1f);

                    //Create relationship between Video and Category
                    if (categories != null && categories.size() > 0) {
                        //Add new relationship
                        for (Category category : categories) {
                            video.getCategories().add(category);
                            category.getVideos().add(video);
                        }
                    }

                    videoDao.persist(video);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));

                } else {
                    //Update Video
                    Video originalVideo = videoDao.findById(video.getId(), site.getId());
                    boolean isChange = false;
                    if (video.getName() != null && !video.getName().equals(originalVideo.getName())) {
                        originalVideo.setName(video.getName());
                        originalVideo.setUri(video.getUri());
                        isChange = true;
                    }
                    if (video.getDescription() != null && !video.getDescription().equals(originalVideo.getDescription())) {
                        originalVideo.setDescription(video.getDescription());
                        isChange = true;
                    }
                    if (video.getActive() != null && !video.getActive().equals(originalVideo.getActive())) {
                        if ("Y".equals(video.getActive())) {
                            originalVideo.setActive(video.getActive());
                        } else {
                            originalVideo.setActive(video.getActive());
                        }
                        isChange = true;
                    }

                    // remove all relationship between Video and Category
                    videoDao.removeVideoCategory(video.getId());

                    //Create again relationship between Video and Category
                    if (categories != null && categories.size() > 0) {
                        //Add new relationship
                        for (Category category : categories) {
                            video.getCategories().add(category);
                            category.getVideos().add(video);
                        }
                    }

                    if (isChange) {
                        //update menu
                        originalVideo.setUpdatedDate(new Date());
                        originalVideo.setSite(site);
                        this.videoDao.merge(originalVideo);
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                    } else {
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                    }

                }
            }
        }
        return messages;
    }

    @Override
    public Messages publishNews(News news) {
        Messages messages = new Messages();
        if (news != null) {
            //If change something
            if (news.getShortDescription() == null ||
                    (news.getShortDescription() != null && !news.getShortDescription().equals(news.getPreShortDescription()))) {
                news.setShortDescription(news.getPreShortDescription());
                messages.addInfo("The content was published");
            }
            //If change something
            if (news.getContent() == null ||
                    (news.getContent() != null && !news.getContent().equals(news.getPreContent()))) {
                news.setContent(news.getPreContent());
                if (messages.isEmpty()) {
                    messages.addInfo("The content was published");
                }
            }
            if (!messages.isEmpty()) {
                newsDao.merge(news);
                messages.getInfos().clear();
                messages.addInfo("The news was published");
            } else {
                messages.addInfo("No content is published");
            }
        }

        return messages;
    }

//    private String generatePageUri (String pageTitle, Long pageId) {
//        if (StringUtils.isNotEmpty(pageTitle)) {
//            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//            String uriNonHTM = WebUtil.getURI(pageTitle);
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
//        Page page = pageDao.getPageByURI(site, uri, false);
//        return page != null;
//    }

    @Override
    public Messages createOrUpdatePartContent(SiteMenuPartContent entity, Long rowId) {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        boolean isUpdate = false;
        if (entity.isEmptyId()) {
            Calendar calendar = Calendar.getInstance();
            //Create news category record
            entity.setCreatedDate(calendar.getTime());
            entity.setStartDate(calendar.getTime());
            calendar.add(Calendar.YEAR, 100); //TODO: Allow customer set the end date which want to show the content
            entity.setEndDate(calendar.getTime());
            Float maxSequence = siteMenuPartContentDao.getMaxSequenceBy("row.id", rowId);
            entity.setSequence(maxSequence+1);
            if (rowId != null && rowId > 0) {
                Row row = rowDao.findById(rowId);
                entity.setRow(row);
            }
            //change vide url to video embed type if need
            String videoUrl = WebUtil.getEmbedYoutubeVideo(entity.getVideoUrl());
            entity.setVideoUrl(videoUrl);
            siteMenuPartContentDao.persist(entity);

            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.create.successful", null, LocaleContextHolder.getLocale()));
        } else {
            //Update News Category
            SiteMenuPartContent originalPartContent = siteMenuPartContentDao.findById(entity.getId());

            if (entity.getHeader() != null && !entity.getHeader().equals(originalPartContent.getHeader())) {
                originalPartContent.setHeader(entity.getHeader());
                isUpdate = true;
            }
            if (entity.getTitle() != null && !entity.getTitle().equals(originalPartContent.getTitle())) {
                originalPartContent.setTitle(entity.getTitle());
                isUpdate = true;
            }
            if (entity.getContent() != null && !entity.getContent().equals(originalPartContent.getContent())) {
                originalPartContent.setContent(entity.getContent());
                isUpdate = true;
            }
            if (entity.getImgUrl() != null && !entity.getImgUrl().equals(originalPartContent.getImgUrl())) {
                originalPartContent.setImgUrl(entity.getImgUrl());
                isUpdate = true;
            }
            String videoUrl = WebUtil.getEmbedYoutubeVideo(entity.getVideoUrl());
            if (videoUrl != null && !videoUrl.equals(originalPartContent.getVideoUrl())) {
                originalPartContent.setVideoUrl(videoUrl);
                isUpdate = true;
            }
            if (entity.getLink() != null && !entity.getLink().equals(originalPartContent.getLink())) {
                originalPartContent.setLink(entity.getLink());
                isUpdate = true;
            }
            if (entity.getCrop() != null && !entity.getCrop().equals(originalPartContent.getCrop())) {
                originalPartContent.setCrop(entity.getCrop());
                isUpdate = true;
            }

            if (entity.getActive() != null && !entity.getActive().equals(originalPartContent.getActive())) {
                if ("Y".equals(entity.getActive())) {
                    originalPartContent.setActive(entity.getActive());
                } else {
                    originalPartContent.setActive(entity.getActive());
                }
                isUpdate = true;
            }

//            if (widgetTemplateId != null && widgetTemplateId > 0) {
//                WidgetTemplate originalWidgetTemplate = siteMenuPartContentDao.getWidgetTemplate(site, originalPartContent.getId());
//                WidgetTemplate widgetTemplate = widgetTemplateDao.findById(widgetTemplateId);
//                //if the content don't have widget format, or the old widget is different with the new one.
//                if (originalWidgetTemplate == null || !originalWidgetTemplate.getId().equals(widgetTemplateId)) {
//                    originalPartContent.setWidgetTemplate(widgetTemplate);
//                    isUpdate = true;
//                }
//            }

            if (!isUpdate) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            } else {
                //update menu
                this.siteMenuPartContentDao.merge(originalPartContent);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }

        }
        return messages;

    }

    @Override
    public Messages createOrUpdateRow(Row entity, Long menuId, Long widgetTemplateId) {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        boolean isUpdate = false;
        if (entity.isEmptyId()) {
            Calendar calendar = Calendar.getInstance();
            //Create news category record
            entity.setCreatedDate(calendar.getTime());
            entity.setUpdatedDate(calendar.getTime());
//            entity.setSite(site);
            if (menuId != null && menuId > 0) {
                Float maxSequence = rowDao.getMaxSequenceBy("menu.id", menuId);
                entity.setSequence(maxSequence+1);
                Menu menu = menuDao.findById(menuId, site.getId());
                entity.setMenu(menu);
            }
            if (widgetTemplateId != null && widgetTemplateId > 0) {
                WidgetTemplate widgetTemplate = widgetTemplateDao.findById(widgetTemplateId);
                entity.setWidgetTemplate(widgetTemplate);
            }
            rowDao.persist(entity);

            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.create.successful", null, LocaleContextHolder.getLocale()));
        } else {
            //Update News Category
            Row originalRow = rowDao.findById(entity.getId());
            if (originalRow != null) {
                if (entity.getTitle() != null && !entity.getTitle().equals(originalRow.getTitle())) {
                    originalRow.setTitle(entity.getTitle());
                    isUpdate = true;
                }
                if (entity.getBackground() != null && !entity.getBackground().equals(originalRow.getBackground())) {
                    originalRow.setBackground(entity.getBackground());
                    isUpdate = true;
                }

                if (entity.getActive() != null && !entity.getActive().equals(originalRow.getActive())) {
                    if ("Y".equals(entity.getActive())) {
                        originalRow.setActive(entity.getActive());
                    } else {
                        originalRow.setActive(entity.getActive());
                    }
                    isUpdate = true;
                }
                if (entity.getShowTitle() != null && !entity.getShowTitle().equals(originalRow.getShowTitle())) {
                    if ("Y".equals(entity.getShowTitle())) {
                        originalRow.setShowTitle(entity.getShowTitle());
                    } else {
                        originalRow.setShowTitle(entity.getShowTitle());
                    }
                    isUpdate = true;
                }

                if (widgetTemplateId != null && widgetTemplateId > 0) {
                    WidgetTemplate originalWidgetTemplate = siteMenuPartContentDao.getWidgetTemplate(originalRow.getId());
                    WidgetTemplate widgetTemplate = widgetTemplateDao.findById(widgetTemplateId);
                    //if the content don't have widget format, or the old widget is different with the new one.
                    if (originalWidgetTemplate == null || !originalWidgetTemplate.getId().equals(widgetTemplateId)) {
                        originalRow.setWidgetTemplate(widgetTemplate);
                        isUpdate = true;
                    }
                }
            }
            if (!isUpdate) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            } else {
                //update menu
//                originalRow.setSite(site);
                this.rowDao.merge(originalRow);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }

        }
        return messages;
    }
}
