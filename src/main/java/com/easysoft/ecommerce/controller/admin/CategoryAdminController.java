package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.controller.admin.form.CommonForm;
import com.easysoft.ecommerce.dao.CatalogDao;
import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.model.Catalog;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.security.UserDetailsImpl;
import com.easysoft.ecommerce.service.CategoryService;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * We don't use this one for the new layout. For the new layout please refer CatalogAdminController.java
 *@deprecated
 */

@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private CategoryService service;
    private CatalogDao catalogDao;
    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired
    public CategoryAdminController(CategoryService service, CatalogDao catalogDao, CategoryDao categoryDao, ProductDao productDao) {
        this.service = service;
        this.catalogDao = catalogDao;
        this.categoryDao = categoryDao;
        this.productDao = productDao;

    }

    /***************************************************************************************************
     *  Implement catalog
     ***************************************************************************************************/

    /**
     * View catalog table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "catalog.html", method = RequestMethod.GET)
    public ModelAndView catalog() throws Exception {
        List<Catalog> catalogs = this.catalogDao.getAllCatalogsBySite(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite());
        return new ModelAndView("admin/categories/catalog_tab", "catalogs", catalogs);
    }

    @RequestMapping(value = "{catalogId}/editcatalog.html", method = RequestMethod.GET)
    public ModelAndView editCatalog(@PathVariable Long catalogId) throws Exception {
        Catalog catalog = this.catalogDao.findById(catalogId);
        return new ModelAndView("admin/categories/catalogform", "catalog", catalog);
    }

    /**
     * This will be call when save the new Catalog
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "insertcatalog.html", method = RequestMethod.PUT)
    public ModelAndView insertCatalog(@Valid Catalog entity, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            //TODO: show error messages
        }
        Map map = new HashMap();
        map.put("catalog", entity);
        map.put("messages", createOrUpdateCatalog(entity));
        return new ModelAndView("admin/categories/catalogform", map);
    }

    /**
     * This will be called when save to update the Catalog
     *
     * @param id
     * @param entity
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{id}/updatecatalog.html", method = RequestMethod.POST)
    public ModelAndView updateCatalog(@PathVariable Long id, @Valid Catalog entity, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            //TODO: show error messages
        }
        entity.setId(id);
        Map map = new HashMap();
        map.put("catalog", categoryDao.findById(id));
        map.put("messages", createOrUpdateCatalog(entity));
        return new ModelAndView("admin/categories/catalogform", map);
    }

    private Messages createOrUpdateCatalog(Catalog entity) throws Exception {
        Messages messages = new Messages();

        if (entity.getId() == null) {
            Date date = new Date();
            entity.setCreatedDate(date);
            catalogDao.persist(entity);
            messages.addInfo("New Catalog is created");
        } else {
            //Update catalog
            Catalog original = catalogDao.findById(entity.getId());
            if (!entity.getName().equals(original.getName())) {
                original.setName(entity.getName());
                messages.addInfo("Name is saved");
            }
            if (!entity.getUri().equals(original.getUri())) {
                original.setUri(entity.getUri());
                messages.addInfo("URI is saved");
            }
            if (entity.isActive() != original.isActive()) {
                if (entity.isActive()) {
                    original.setActive(entity.isActive());
                    messages.addInfo("Product is activated");
                } else {
                    original.setActive(entity.isActive());
                    messages.addInfo("Product is deactivated");
                }
            }
            if (!entity.getDescription().equals(original.getDescription())) {
                original.setDescription(entity.getDescription());
                messages.addInfo("Description is saved");
            }

            if (messages.isEmpty()) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            } else {
                catalogDao.merge(original);
            }
        }
        return messages;
    }


    @RequestMapping(value = "{catalogId}/categories.html", method = RequestMethod.GET)
    public ModelAndView listCategoriesOfCatalog(@PathVariable Long catalogId, HttpServletRequest request) throws Exception {
        List<Category> allCategories = categoryDao.getRootCategoriesByCatalog(catalogId);
//        Map map = new HashMap();
//        map.put("command", allCategories);
        return new ModelAndView("admin/categories/catalogcategories_tab", "catalogId", catalogId);
    }

    /***************************************************************************************************
     *  Implement Category
     ***************************************************************************************************/

    /**
     * View all categories by treeview
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "index.html", method = RequestMethod.GET)
    public ModelAndView index(@Valid CommonForm form) throws Exception {
        return new ModelAndView("admin/categories/index", "command", form);
    }

    /**
     * The method will be called when click on Create New Category
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "form.html", method = RequestMethod.GET)
    public ModelAndView displayForm(HttpServletRequest request) throws Exception {
        Category entity = new Category();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Site site = principal.getSite();
        if (!StringUtils.isEmpty(request.getParameter("parentCategory"))) {
            Long catId = Long.valueOf(request.getParameter("parentCategory"));
            Category parentCat = service.getCategory(catId);
            entity.setParentCategory(parentCat);
        }
        Map map = new HashMap();
        map.put("command", entity);
        return new ModelAndView("admin/categories/form", map);
    }

    @RequestMapping(value = "{id}/index.html", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable Long id, HttpServletRequest request) throws Exception {
        if ("inplace".equals(request.getParameter("action"))) {
            return new ModelAndView("admin/categories/categorytree_ajax", "command", categoryDao.getSubCategories(id));
        } else {
            Map map = new HashMap();
            map.put("command", categoryDao.findById(id));
            return new ModelAndView("admin/categories/form", map);
        }
    }

    @RequestMapping(value = "{id}/subcategories.html", method = RequestMethod.GET)
    public ModelAndView viewSubCategories(@PathVariable Long id, HttpServletRequest request) throws Exception {
        Map map = new HashMap();
        map.put("command", categoryDao.getSubCategories(id));
        map.put("currentCategory", service.getCategory(id));
        return new ModelAndView("admin/categories/subcategories_tab", map);
    }

    @RequestMapping(value = "{catid}/{subcatid}/removeSubCategory.html", method = RequestMethod.GET)
    public ModelAndView removeSubCategory(@PathVariable Long catid, @PathVariable Long subcatid, HttpServletRequest request) throws Exception {
        if (subcatid != null && subcatid > 0) {
            Category cat = categoryDao.findById(subcatid);
            cat.setParentCategory(null);
            categoryDao.merge(cat);
        }
        Map map = new HashMap();
//        map.put("command", service.getSubCategories(catid));
//        map.put("currentCategory", service.getCategory(catid));
        return new ModelAndView("admin/categories/subcategories_tab", map);
    }

    @RequestMapping(value = "{categoryId}/{productId}/removeproductcategory.html", method = RequestMethod.GET)
    public String removeProductCategory(@PathVariable Long categoryId, @PathVariable Long productId) throws Exception {
        if (categoryId != null && productId > 0) {
            categoryDao.removeProductCategory(categoryId, productId);
            return "admin/ok";
        } else {
            return "admin/ok";
        }
//        Map map = new HashMap();
//        map.put("categoryId", categoryId);
//        map.put("products", this.productDao.getProductBySubCategory(categoryId, 0, Integer.MAX_VALUE, false, null, false));
//        return new ModelAndView("admin/products/listproductsbycategory_tab", map);
    }

    @RequestMapping(value = "{categoryId}/listcategories.html", method = RequestMethod.GET)
    public ModelAndView listCategories(@PathVariable Long categoryId, HttpServletRequest request) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        //TODO: get correct listCategories
        List<Catalog> catalogs = catalogDao.getCatalogFromCategory(categoryId, false);
        if (catalogs != null && catalogs.size() > 0) {
            Catalog catalog = catalogs.get(0);
            List<Category> allCategories = categoryDao.getAllCategoriesExclude(categoryDao.getSubCategories(categoryId), catalog.getId());
            Map map = new HashMap();
            map.put("command", allCategories);
            map.put("currentCategory", service.getCategory(categoryId));
            return new ModelAndView("admin/categories/listcategories_popup", map);
        } else {
            throw new IllegalArgumentException("Cannot find catalog from category. This is data issue. " + categoryId);
        }
    }

    @RequestMapping(value = "{catid}/activateCategory.html", method = RequestMethod.GET)
    public String activateCategory(@PathVariable Long catid, HttpServletRequest request) throws Exception {
        String check = request.getParameter("flag");

        if (catid != null && catid.longValue() > 0) {
            Category cat = categoryDao.findById(catid);
            if (!StringUtils.isEmpty(check) && check.equalsIgnoreCase("true")) {
                cat.setActive("Y");
            } else {
                cat.setActive("N");
            }
            service.createOrUpdate(cat, 0l);
        }
        return "admin/ok";
    }
    @RequestMapping(value = "checkuri.html", method = RequestMethod.GET)
    public String checkURI(HttpServletRequest request) throws Exception {
        String uri = request.getParameter("uri");

        if (!StringUtils.isEmpty(uri)) {
            Category cat = categoryDao.findUniqueBy("uri", uri);
            if (cat == null) {
                return "admin/ok";
            } else {
                return "admin/fail";
            }
        }
        return "admin/fail";
    }

    @RequestMapping(value = "{catid}/{subcatid}/addSubCategory.html", method = RequestMethod.GET)
    public ModelAndView addSubCategory(@PathVariable Long catid, @PathVariable Long subcatid, HttpServletRequest request) throws Exception {
        if (catid != null && catid > 0 && subcatid != null && subcatid > 0) {
            categoryDao.addSubCategory(catid, subcatid);
        }
        //TODO: correct get listCategories
        List <Catalog> catalogs = catalogDao.getCatalogFromCategory(catid, false);
        if (catalogs != null && catalogs.size() > 0) {
            Catalog catalog = catalogs.get(0);
            List<Category> allCategories = categoryDao.getAllCategoriesExclude(categoryDao.getSubCategories(catid), catalog.getId());
            Map map = new HashMap();
            map.put("command", allCategories);
            map.put("currentCategory", service.getCategory(catid));
            return new ModelAndView("admin/categories/listcategories_popup", map);
        } else {
            throw new IllegalArgumentException("Cannot find catalog");
        }
    }

    /**
     * This will be call when save the new category
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "index.html", method = RequestMethod.PUT)
    public ModelAndView insert(@Valid Category entity, BindingResult result) throws Exception {

        Messages messages = new Messages();
        Map map = new HashMap();

        if (entity.getParentCategory() != null && entity.getParentCategory().getId() == null) {
            entity.setParentCategory(null);
        }
        if (result.hasErrors()) {
            //TODO: show error messages
        }
        //Check unique uri
        Category cat = categoryDao.findUniqueBy("uri", entity.getUri());
        if (cat != null) {
            messages.addError("The uri of this category existed. Please enter another one.");
            map.put("command", entity);
            map.put("messages", messages);
            return new ModelAndView("admin/categories/form", map);
        }

        Date date = new Date();
        entity.setCreatedDate(date);
        entity.setUpdatedDate(date);
        messages = service.createOrUpdate(entity, 0l);
        map.put("command", entity);
        map.put("messages", messages);
        return new ModelAndView("admin/categories/form", map);
    }

    /**
     * This will be called when save to update the category
     *
     * @param id
     * @param entity
     * @param result
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{id}/index.html", method = RequestMethod.POST)
    public ModelAndView update(@PathVariable Long id, @Valid Category entity, BindingResult result) throws Exception {
        Map map = new HashMap();
        if (result.hasErrors()) {
            //TODO: show error messages
        }
        entity.setId(id);

        //Check unique uri
        Category original = categoryDao.findById(id);
        if (!entity.getUri().equals(original.getUri())) {
            Category cat = categoryDao.findUniqueBy("uri", entity.getUri());
            if (cat != null) {
                Messages messages = new Messages();
                messages.addError("The uri of this category existed. Please enter another one.");
                map.put("command", entity);
                map.put("messages", messages);
                return new ModelAndView("admin/categories/form", map);
            }
        }

        map.put("command", categoryDao.findById(id));
        map.put("messages", service.createOrUpdate(entity, 0l));
        return new ModelAndView("admin/categories/form", map);
    }

    @RequestMapping(value = "{id}/index.html", method = RequestMethod.DELETE)
    public ModelAndView delete(@PathVariable Long id) throws Exception {
        service.remove(categoryDao.findById(id), ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite());
        return new ModelAndView("admin/categories/index");
    }

}
