package com.easysoft.ecommerce.web.filter;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.service.SEOService;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;

public class KeywordTransalator {
    private ServletConfig config;

    public void init(ServletConfig config) throws Exception {
        this.config = config;
    }

    public void destroy() {
        this.config = null;
    }

    public void uri2id(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WebApplicationContext rootApplicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        String source = (String) request.getAttribute("source");

        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        SEOService seo = rootApplicationContext.getBean(SEOService.class);
        Class currentClass = null;
        String target = "";
        for (String str : StringUtils.split(source, '/')) {
            Object object = seo.uri2object(str, site != null ? site.getId() : null);
            if (object != null && object instanceof Category) {
                Category category = (Category) object;
                if (currentClass != Category.class) {
                    target += "/category";
                    currentClass = Category.class;
                }
                target += "/" + category.getId();
            } else if (object != null && object instanceof Product) {
                Product product = (Product) object;
                if (currentClass != Product.class) {
                    target += "/product";
                    currentClass = Product.class;
                }
                target += "/" + product.getId();
            } else {
                throw new IllegalArgumentException(str);
                //target = "not-found";
            }
        }
        request.setAttribute("target", target);
    }

    public void id2uri(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = (String) request.getAttribute("source");
        WebApplicationContext rootApplicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        SEOService seo = rootApplicationContext.getBean(SEOService.class);
        Class entityClass = null;
        String target = "";
        for (String str : StringUtils.split(source, '/')) {
            if (str.equals("category")) {
                entityClass = Category.class;
            } else if (str.equals("product")) {
                entityClass = Product.class;
            } else {
                if (!StringUtils.isEmpty(target)) {
                    target += '/';
                }
                target += seo.id2uri(entityClass, Long.parseLong(str), site != null ? site.getId() : null);
            }
        }
        request.setAttribute("target", target);
    }
}

