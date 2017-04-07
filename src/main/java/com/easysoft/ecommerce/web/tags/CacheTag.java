package com.easysoft.ecommerce.web.tags;

import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.web.cache.CacheKeyGenerator;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

public class CacheTag extends SimpleTagSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheTag.class);

    /**
     * if cache key is null, the request URI is used
     */
    private String key = null;

    /**
     * Set the key for this cache entry.
     *
     * @param key Set the key for this cache entry. System will base on this key to generate a unique one by site.
     */
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void doTag() throws JspException, IOException {
        String cachedContent = getFromCache();
        if (cachedContent == null) {
            // evaluate the tag body
            StringWriter sw = new StringWriter();
            getJspBody().invoke(sw);
            cachedContent = sw.toString();
            getJspContext().getOut().write(cachedContent);
            putIntoCache(cachedContent);
        } else {
            getJspContext().getOut().write(cachedContent);
        }
    }

    private void putIntoCache(String cachedContent) {
        String actualKey = generateEntryKey();
        Cache cache = getCacheService();
        Element element = new Element(actualKey, cachedContent);
        cache.put(element);
    }

    private String getFromCache() {
        String result;
        String actualKey = generateEntryKey();
        Cache cache = getCacheService();
        Element element = cache.get(actualKey);
        if (element != null) result = (String) element.getValue();
        else result = null;
        return result;
    }

    private Cache getCacheService() {
        return ServiceLocatorHolder.getServiceLocator().getCacheManager().getCache("CacheTag");
    }

    /**
     * Generates a cache entry key.
     * <p/>
     * If the string key is not specified, the HTTP request URI and QueryString is used.
     * Operating systems that have a filename limitation less than 255 or have
     * filenames that are case insensitive may have issues with key generation where
     * two distinct pages map to the same key.
     * <p/>
     * POST Requests (which have no distinguishing
     * query string) may also generate identical keys for what is actually different pages.
     * In these cases, specify an explicit key attribute for the CacheTag.
     *
     * @return The generated cache key
     */
    private String generateEntryKey() {

        /**
         * Used for generating cache entry keys.
         */
        StringBuilder cBuffer = new StringBuilder(100);

        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        cBuffer.append(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKey(request, key));

        return cBuffer.toString();
    }

}
