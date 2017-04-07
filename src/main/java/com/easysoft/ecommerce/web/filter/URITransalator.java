package com.easysoft.ecommerce.web.filter;

import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.service.SEOService;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class URITransalator {
    private ServletConfig config;

    public void init(ServletConfig config) throws Exception {
        this.config = config;
    }

    public void destroy() {
        this.config = null;
    }

    public void outBoundNews(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String language = site.getSiteParamsMap().get("LANGUAGE");
        String target = "news";
        if ("vi_VN".equalsIgnoreCase(language)) {
            target = "tin-tuc";
        }
        request.setAttribute("target", target);
    }
    public void outBoundProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String language = site.getSiteParamsMap().get("LANGUAGE");
        String target = "product";
        if ("vi_VN".equalsIgnoreCase(language)) {
            target = "san-pham";
        }
        request.setAttribute("target", target);
    }
    public void outBoundCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String language = site.getSiteParamsMap().get("LANGUAGE");
        String target = "category";
        if ("vi_VN".equalsIgnoreCase(language)) {
            target = "danh-muc";
        }
        request.setAttribute("target", target);
    }
    public void outBoundContent(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String language = site.getSiteParamsMap().get("LANGUAGE");
        String target = "content";
        if ("vi_VN".equalsIgnoreCase(language)) {
            target = "noi-dung";
        }
        request.setAttribute("target", target);
    }

    public void outBoundContact(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String language = site.getSiteParamsMap().get("LANGUAGE");
        String target = "contact-us.html";
        if ("vi_VN".equalsIgnoreCase(language)) {
            target = "lien-he.html";
        }
        request.setAttribute("target", target);
    }
    public void outBoundQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String language = site.getSiteParamsMap().get("LANGUAGE");
        String target = "question-answer.html";
        if ("vi_VN".equalsIgnoreCase(language)) {
            target = "hoi-dap.html";
        }
        request.setAttribute("target", target);
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

