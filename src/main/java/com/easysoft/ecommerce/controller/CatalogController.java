package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping()
public class CatalogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogController.class);
    private ServiceLocator serviceLocator;

    @Autowired
    public CatalogController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @RequestMapping("/catalog/{uri}/index.html")
    public String sectionPage(HttpServletRequest request, @PathVariable String uri, HttpServletResponse response) throws Exception {
        return "catalog/section";
    }

    @RequestMapping(value = {"/catalog/index.html"}, method = {RequestMethod.GET})
    public ModelAndView catalog(@Valid SearchForm form) throws Exception {
        return new ModelAndView("catalog/products", "command", form);
    }

    /*
     * HTTP ERROR 400 BAD_REQUEST
     * It happens when we have @PathVariable but we don't pass it into url
     */
    @RequestMapping(value = "/category/{parCatUri}.html")
    public ModelAndView parentCategory(@Valid SearchForm form, @PathVariable String parCatUri) throws Exception {
        return new ModelAndView("catalog/subcategory", "command", form);
    }

    @RequestMapping("/category/{parCatUri}/{catUri}.html")
    public ModelAndView category(@Valid SearchForm form, @PathVariable String parCatUri, @PathVariable String catUri) throws Exception {
        return new ModelAndView("catalog/subcategory", "command", form);
    }

    @RequestMapping(value = "/category/{parCatUri}/{catUri}/{subCatUri}.html")
    public ModelAndView subCategory(@Valid SearchForm form, @PathVariable String parCatUri, @PathVariable String catUri, @PathVariable String subCatUri) throws Exception {
        return new ModelAndView("catalog/subcategory", "command", form);
    }

//    @RequestMapping(value={, }, method = {RequestMethod.GET})
//    public String product(@PathVariable String catUri, @PathVariable String subCatUri, @PathVariable String prodUri, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if (!isProductAvailable(prodUri)) {
//            throw new Exception("Product '" + prodUri+"' is not available");
//        }
//        updateRecentlyView(request, response, prodUri);
//        return "catalog/product";
//    }
    @RequestMapping(value="/product/{prodUri}.html")
    public String getProduct(@PathVariable String prodUri, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!isProductAvailable(prodUri)) {
            throw new Exception("Product '" + prodUri+"' is not available");
        }
        updateRecentlyView(request, response, prodUri);
        return "catalog/product";
    }
    @RequestMapping(value="/product/{subCatUri}/{prodUri}.html")
    public String getProductL1(@PathVariable String subCatUri, @PathVariable String prodUri, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!isProductAvailable(prodUri)) {
            throw new Exception("Product '" + prodUri+"' is not available");
        }
        updateRecentlyView(request, response, prodUri);
        return "catalog/product";
    }
    @RequestMapping(value="/product/{catUri}/{subCatUri}/{prodUri}.html")
    public String getProductL2(@PathVariable String catUri, @PathVariable String subCatUri, @PathVariable String prodUri, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!isProductAvailable(prodUri)) {
            throw new Exception("Product '" + prodUri+"' is not available");
        }
        updateRecentlyView(request, response, prodUri);
        return "catalog/product";
    }
    @RequestMapping(value="/product/{parCatUri}/{catUri}/{subCatUri}/{prodUri}.html")
    public String getProductL3(@PathVariable String parCatUri, @PathVariable String catUri, @PathVariable String subCatUri, @PathVariable String prodUri, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!isProductAvailable(prodUri)) {
            throw new Exception("Product '" + prodUri+"' is not available");
        }
        updateRecentlyView(request, response, prodUri);
        return "catalog/product";
    }

    private boolean isProductAvailable (String prodUri) {
        String []ids = prodUri.split("-");
        if (ids.length > 1 && StringUtils.isNumeric(ids[ids.length - 1])) {
            Product product = serviceLocator.getProductDao().findById(Long.valueOf(ids[ids.length - 1]));
            return product != null;
        } else {
            return false;
        }

    } 
        
    private void updateRecentlyView (HttpServletRequest request, HttpServletResponse response, String prodUri) {
        SessionObject so = null;
        try {
            so = SessionUtil.load(request, response);
            String recentlyView = (String) so.get("RECENTLY_VIEW");
            String []ids = prodUri.split("-");
            if (ids.length > 1 && StringUtils.isNumeric(ids[ids.length - 1])) {
                if (StringUtils.isEmpty(recentlyView)) {
                    so.set("RECENTLY_VIEW", ids[ids.length - 1]);
                } else {
                    so.set("RECENTLY_VIEW", recentlyView + "," + ids[ids.length - 1]);
                }
            }
            //Just keep 10 recent review products.
            //TODO: Using LinkedHashSet
        } catch (Exception e) {
            // Do nothing
        }

    }
}
