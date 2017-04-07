package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.model.Keyword;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.util.QueryString;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class SearchController {

    private ServiceLocator serviceLocator;
    private ProductDao productDao;

    @Autowired
    public SearchController(ServiceLocator serviceLocator, ProductDao productDao) {
        this.serviceLocator = serviceLocator;
        this.productDao = productDao;
    }

    @RequestMapping(value="autocomplete.html", method={RequestMethod.GET})
    public void autocomplete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String keyword = request.getParameter("q");
        if (!StringUtils.isEmpty(keyword)) {
            response.getWriter().write(serviceLocator.getSearchService().getAutoCompleteKeywords(keyword, serviceLocator.getSystemContext().getSite(), 0, 10));
        } else {
            response.getWriter().write("");
        }

//        return new ModelAndView("autocomplete");
    }


    @RequestMapping("search.html")
    public ModelAndView search(@Valid SearchForm form, HttpServletRequest request) throws Exception {
        //return home if keyword is empty
        if (StringUtils.isEmpty(form.getKeyword())) return new ModelAndView("index");

        //remove special characters
        form.setKeyword(form.getKeyword().replace('*',' '));
        form.setKeyword(form.getKeyword().replace('\\',' '));
        form.setKeyword(form.getKeyword().replace('?',' '));
        form.setKeyword(form.getKeyword().replace("[",""));
        form.setKeyword(form.getKeyword().replace("]",""));
        form.setKeyword(form.getKeyword().replace("(",""));
        form.setKeyword(form.getKeyword().replace(")",""));
        form.setKeyword(form.getKeyword().replace("{",""));
        form.setKeyword(form.getKeyword().replace("}",""));

//        int from = form.getStartPos();
//        int to = form.getStartPos() + form.getMaxResult();
//        int page = form.getPage();
//        if (products.size() < from) {
//            from = 0;
//            to = Math.min(products.size(), Constants.MAX_ITEM_PER_PAGE);
//            page = 1;
//        } else if (products.size() < to){
//            to = products.size();
//        }
//        form.setPage(page);

        //Collect the keyword that user search. This will use for marketing in the future.
        if (!StringUtils.isEmpty(form.getKeyword())) {
            Keyword keyword = serviceLocator.getKeywordDao().getKeywordByGroup(form.getKeyword(), "product", serviceLocator.getSystemContext().getSite());
            if (keyword != null) {
                keyword.setCount(keyword.getCount()+1);
                serviceLocator.getKeywordDao().merge(keyword);
            } else {
                keyword = new Keyword();
                keyword.setCount(1);
                keyword.setKeyword(form.getKeyword());
                keyword.setGroupBy("product");
                keyword.setSite(serviceLocator.getSystemContext().getSite());
                serviceLocator.getKeywordDao().persist(keyword);
            }
        }
        return new ModelAndView("search", "command", form);
    }

    @RequestMapping("giam-gia.html")
    public ModelAndView discount(@Valid SearchForm form, HttpServletRequest request) throws Exception {
        return new ModelAndView("discount", "command", form);
    }

    @RequestMapping(value="itemcount.html", method={RequestMethod.GET})
    public void count(@Valid SearchForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map <String, String> refinements = WebUtil.getRefinementMap(form.getRefinement(), new QueryString(request.getQueryString()));

        Integer count = serviceLocator.getSearchService().count(form.getKeyword(), form.getCategoryId(),
            serviceLocator.getSystemContext().getSite(), refinements, true);
        response.getWriter().write(String.valueOf(count));
    }

    @RequestMapping(value="categoryitemcount.html", method={RequestMethod.GET})
    public void categoryItemCount(@Valid SearchForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map <String, String> refinements = WebUtil.getRefinementMap(form.getRefinement(), new QueryString(request.getQueryString()));
        List<Long> catIds = form.getCategoryId();
        String categoryIds = "";
        for (Long id: catIds) {
            if (StringUtils.isEmpty(categoryIds)) {
                categoryIds = id.toString();
            } else {
                categoryIds = categoryIds + " " + id.toString();
            }
        }
        Integer count = serviceLocator.getSearchService().countProductsBySubCategoryUsingLucene(categoryIds,
            serviceLocator.getSystemContext().getSite(), refinements, true);
        response.getWriter().write(String.valueOf(count));
    }

    private String buildProductIds (List <Product> prods) {
        StringBuffer prodIds = new StringBuffer();
        for (Product prod: prods) {
            if (prodIds.toString().equals("")) {
                prodIds.append(prod.getId());
            } else {
                prodIds.append(",").append(prod.getId());
            }
        }
        return prodIds.toString();
    }
}
