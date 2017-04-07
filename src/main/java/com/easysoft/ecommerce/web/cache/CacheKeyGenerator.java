package com.easysoft.ecommerce.web.cache;

import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is implemented for generating CacheKey. All CacheKey MUST be driven from here.
 *
 */
public class CacheKeyGenerator {
    /**
     * This method generate the cache key for the page.
     *
     * @param httpRequest
     * @param uri
     * @return
     */
    public String generateCacheKey(HttpServletRequest httpRequest, String uri, String queryString) {
        StringBuilder stringBuffer = new StringBuilder();
        if (StringUtils.isNotEmpty(uri)) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            stringBuffer.append(site.getId()).append("|");
            SiteTemplate siteTemplate = site.getSiteTemplate();
            stringBuffer.append(siteTemplate.getTemplate().getId()).append("|");
            stringBuffer.append(siteTemplate.getFullWide()).append("|");
            stringBuffer.append(siteTemplate.getSkinColor()).append("|");
            stringBuffer.append(siteTemplate.getTemplateColorCode()).append("|");
            stringBuffer.append(siteTemplate.getHeaderFix()).append("|");
            stringBuffer.append(siteTemplate.getHeaderType()).append("|");
            stringBuffer.append(siteTemplate.getFooterType()).append("|");
            stringBuffer.append(toStringAwareNull(uri));
            if(queryString != null) {
                stringBuffer.append("|").append(queryString);
            }

/*
        String domain = httpRequest.getServerName();
        String queryString = httpRequest.getQueryString();
        String themeName = ServiceLocatorHolder.getServiceLocator().getThemeName();
        stringBuffer.append(toStringAwareNull(queryString)).append("|");
        stringBuffer.append(toStringAwareNull(themeName)).append("|");
        int serverPort = httpRequest.getServerPort();
        String method = httpRequest.getMethod();
        String user = httpRequest.getRemoteUser();
        String locale = ServiceLocatorHolder.getServiceLocator().getLocale().toString();
        String timeZone = ServiceLocatorHolder.getServiceLocator().getTimeZone().getID();
        stringBuffer.append("serverPort:").append(serverPort).append("|");
        stringBuffer.append("method:").append(toStringAwareNull(method)).append("|");
        stringBuffer.append("user:").append(toStringAwareNull(user)).append("|");
        stringBuffer.append("locale:").append(toStringAwareNull(locale)).append("|");
        stringBuffer.append("timeZone:").append(toStringAwareNull(timeZone)).append("|");
        stringBuffer.append("themeName:").append(toStringAwareNull(themeName));
*/
        }

        return stringBuffer.toString();
    }
    public String generateCacheKey(HttpServletRequest httpRequest, String uri) {
        return generateCacheKey(httpRequest, uri, null);
    }
    public String generateCacheKeyFromMenu(HttpServletRequest httpRequest, Long menuId) {
        if (menuId != null) {
            Menu menu = ServiceLocatorHolder.getServiceLocator().getMenuDao().findById(menuId);
            if (menu != null) {
                if ("Y".equals(menu.getHomePage())) {
                    return generateCacheKey(httpRequest, "/index.html");
                } else {
                    if ("Y".equals(menu.getMenuTemplate())) {
                        return generateCacheKey(httpRequest, "/"+menu.getUri());
                    } else if ("E".equals(menu.getMenuTemplate())) {
                        return generateCacheKey(httpRequest, menu.getUri());
                    } else {
                        return generateCacheKey(httpRequest, "/content/"+menu.getUri());
                    }
                }
            } else {
                return generateCacheKey(httpRequest, "");
            }
        }
        return null;
    }

    public String generateCacheKeyFromRow(HttpServletRequest httpRequest, Long rowId) {
        if (rowId != null) {
            Menu menu = ServiceLocatorHolder.getServiceLocator().getMenuDao().getMenuFromRow(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite(), rowId, "Y");
            if (menu != null) {
                if ("Y".equals(menu.getHomePage())) {
                    return generateCacheKey(httpRequest, "/index.html");
                } else {
                    if ("Y".equals(menu.getMenuTemplate())) {
                        return generateCacheKey(httpRequest, "/"+menu.getUri());
                    } else if ("E".equals(menu.getMenuTemplate())) {
                        return generateCacheKey(httpRequest, menu.getUri());
                    } else {
                        return generateCacheKey(httpRequest, "/content/"+menu.getUri());
                    }
                }
            } else {
                return generateCacheKey(httpRequest, "");
            }
        }
        return null;
    }

    public String generateCacheKeyFromPartContent(HttpServletRequest httpRequest, Long partContentId) {
        if (partContentId != null) {
            Menu menu = ServiceLocatorHolder.getServiceLocator().getMenuDao().getMenuFromPartContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite(), partContentId, "Y");
            if (menu != null) {
                if ("Y".equals(menu.getHomePage())) {
                    return generateCacheKey(httpRequest, "/index.html");
                } else {
                    if ("Y".equals(menu.getMenuTemplate())) {
                        return generateCacheKey(httpRequest, "/"+menu.getUri());
                    } else if ("E".equals(menu.getMenuTemplate())) {
                        return generateCacheKey(httpRequest, menu.getUri());
                    } else {
                        return generateCacheKey(httpRequest, "/content/"+menu.getUri());
                    }
                }
            } else {
                return generateCacheKey(httpRequest, "");
            }
        }
        return null;
    }

    public String generateMenuCacheKey() {
        return generateCacheKey(null, "menu_cuoi5s");
    }
    public String generateHeaderCacheKey () {
        return generateCacheKey(null, "header_cuoi5s");
    }
    public String generateFooterCacheKey () {
        return generateCacheKey(null, "footer_cuoi5s");
    }

    public String generateCacheKeyFromGallery(HttpServletRequest httpRequest) {
        return generateCacheKey(httpRequest, "/page/gallery.html");
    }
    public String generateCacheKeyFromAlbum(HttpServletRequest httpRequest, Long albumId) {
        if (albumId != null) {
            Album album = ServiceLocatorHolder.getServiceLocator().getAlbumDao().findById(albumId, ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getId());
            if (album != null) {
                return generateCacheKey(httpRequest, "/page/album.html|albumId="+album.getId());
            } else {
                return generateCacheKey(httpRequest, "");
            }
        }
        return null;
    }
    public String generateCacheKeyFromAlbumImage(HttpServletRequest httpRequest, Long albumImageId) {
        if (albumImageId != null) {
            Album album = ServiceLocatorHolder.getServiceLocator().getAlbumDao().getAlbumFromImage(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite(), albumImageId, "Y");
            if (album != null) {
                return generateCacheKey(httpRequest, "/page/album.html|albumId="+album.getId());
            } else {
                return generateCacheKey(httpRequest, "");
            }
        }
        return null;
    }

    /**
     * Generate news index key.
     *
     * @param httpRequest
     * @return
     */
    public String generateNewsIndexCacheKey(HttpServletRequest httpRequest) {
        return generateCacheKey(httpRequest, "/news/index.html","");
    }

    public String generateNewsCategoryCacheKeyFromCategoryNews(HttpServletRequest httpRequest, Long categoryNewsId) {
        if (categoryNewsId != null) {
            NewsCategory newsCategory = ServiceLocatorHolder.getServiceLocator().getNewsCategoryDao().findById(categoryNewsId, ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getId());
            if (newsCategory != null) {
                return generateCacheKey(httpRequest, "/news/c/" + newsCategory.getUri(), "");
            } else {
                return null;
            }
        }
        return null;
    }
    public List<String> generateNewsCategoryCacheKeyFromNews(HttpServletRequest httpRequest, Long newsId) {
        if (newsId != null) {
            List<NewsCategory> newsCategories = ServiceLocatorHolder.getServiceLocator().getNewsDao().getNewsCategories(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite(),newsId, "Y");
            List<String> cacheKeys = new ArrayList<String>();
            if (newsCategories != null) {
                for (NewsCategory newsCategory: newsCategories) {
                    cacheKeys.add(generateCacheKey(httpRequest, "/news/c/" + newsCategory.getUri(), ""));
                }
            } else {
                return null;
            }
            return cacheKeys;
        }
        return null;
    }

    public String generateNewsCacheKeyFromNews(HttpServletRequest httpRequest, Long newsId) {
        if (newsId != null) {
            News news = ServiceLocatorHolder.getServiceLocator().getNewsDao().findById(newsId, ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getId());
            if (news != null) {
                return generateCacheKey(httpRequest, "/news/" + news.getUri());
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * Generate cache key for catalog.
     *
     * @param httpRequest
     * @return
     */
    //    /danh-muc/nuoc-hoa-65/nuoc-hoa-nu-66.html
    public List<String> generateCategoryCacheKeyFromProduct(HttpServletRequest httpRequest, Long productId) {
        List<String> cacheKeys = new ArrayList<String>();
        if (productId != null) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            Product product = ServiceLocatorHolder.getServiceLocator().getProductDao().findById(productId, site.getId());
            if (product != null) {
                String shortProductUrl = "/product/"+product.getUri()+"-"+product.getId()+".html";
                cacheKeys.add(generateCacheKey(null, shortProductUrl, ""));

                Category category = ServiceLocatorHolder.getServiceLocator().getCategoryDao().getSubCategory(productId);
                String uri = "";
                while (category != null) {
                    if (StringUtils.isEmpty(uri)) {
                        uri = "/" + category.getUri() + "-" + category.getId();
                    } else {
                        uri = "/" + category.getUri() + "-" + category.getId() + uri;
                    }
                    category = category.getParentCategory();//ServiceLocatorHolder.getServiceLocator().getCategoryDao().getp
                }
                if (StringUtils.isNotEmpty(uri)) {
                    //Generate category url key
                    cacheKeys.add(generateCacheKey(httpRequest, "/category" + uri + ".html", ""));
                    //Generate full product url key
                    cacheKeys.add(generateCacheKey(httpRequest, "/product" + uri + "/" + product.getUri() + "-" + product.getId() + ".html", ""));
                }
            }
        }
        return cacheKeys;
    }

    /**
     * Generate cache key for contact us.
     *
     * @param httpRequest
     * @return
     */
    public String generateContactUsCacheKey(HttpServletRequest httpRequest) {
        return generateCacheKey(httpRequest, "/contact-us.html");
    }


    /****************************************Generate Key Cache for CommonCache*********************************************/
    public List<String> generateSiteCacheKey () {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        List<String> cacheKeys = new ArrayList<String>();
        cacheKeys.add(site.getDomain());
        cacheKeys.add(site.getSubDomain());
        return cacheKeys;
    }

    private static String toStringAwareNull(String str) {
        if (str == null)
            str = "";
        return str;
    }
}
