package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.controller.admin.form.FileSystemForm;
import com.easysoft.ecommerce.dao.CatalogDao;
import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin/utility")
public class UtilityAdminController {

    private CategoryService service;
    private CatalogDao catalogDao;
    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired
    public UtilityAdminController(CategoryService service, CatalogDao catalogDao, CategoryDao categoryDao, ProductDao productDao) {
        this.service = service;
        this.catalogDao = catalogDao;
        this.categoryDao = categoryDao;
        this.productDao = productDao;

    }

    /***************************************************************************************************
     *  Implement Images Management
     ***************************************************************************************************/

    /**
     * View string param
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "stringparam.html", method = RequestMethod.GET)
    public ModelAndView listStringParam() throws Exception {
        return new ModelAndView("admin/utility/stringparam");
    }
    

}
