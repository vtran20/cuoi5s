package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.CategoryService;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.Money;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class will be used for the new layout and support for multi-sites.
 * produces="application/x-www-form-urlencoded; charset=UTF-8" -> Fixing issue message return support UTF-8
 */
@Controller
@RequestMapping(value="/admin/catalog")
public class CatalogAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogAdminController.class);

    @Autowired
    private ServiceLocator serviceLocator;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private CatalogDao catalogDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductFileDao productFileDao;
    @Autowired
    private ProductVariantDao productVariantDao;
    @Autowired
    private RelatedProductDao relatedProductDao;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get("DATE_FORMAT"));
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping(value = "product/{id}.json", method = RequestMethod.GET)
    public
    @ResponseBody
    Product json(@PathVariable Long id) throws Exception {
        return productDao.findById(id);
    }

    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/catalog/" + action;
    }

    /**
     * ************************************************************************************************
     * Implement product
     * *************************************************************************************************
     */

    @RequestMapping(value = {"product_insert.html", "product_update.html", "product_create.html"}, method = RequestMethod.GET)
    public String productForm() throws Exception {
        return "admin/catalog/product";
    }

    @RequestMapping(value = {"products.html"}, method = RequestMethod.GET)
    public String getProducts() throws Exception {
        return "admin/catalog/products";
    }

    /**
     * This method is called when insert a new Product
     */
    @RequestMapping(value = {"product_insert.html","product_update.html"}, method = RequestMethod.POST)
    @CSRFProtection
    public @ResponseBody Map insertProduct(@Valid Product entity, @Valid ProductVariant variant, @Valid Long[] categoryId, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        Map <String, Object> result = new HashMap<String, Object>();
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCategoryCacheKeyFromProduct(null, entity.getId()));
        result.put("messages", categoryService.createOrUpdate(entity, variant, categoryId).toString());
        result.put("id", entity.getId());
        //update price and rebuild index
        serviceLocator.getProductDao().updateProductPrice(entity.getId());
        return result;
    }
    /**
     * This method is called when insert a new Product
     */
    @RequestMapping(value = {"deleteproduct.html"}, method = RequestMethod.GET, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteProduct(@Valid Long id) throws Exception {
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCategoryCacheKeyFromProduct(null, id));
        return categoryService.deleteProduct(id).toString();
    }
    /**
     * This method is called when delete product variant
     */
    //TODO: need to check current site and see if variant.getProduct() throw exception.
    @RequestMapping(value = {"deleteproductvariant.html"}, method = RequestMethod.GET, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteProductVariant(@Valid Long id) throws Exception {
        ProductVariant variant = productVariantDao.findById(id);
        if (variant != null) {
            Long productId = variant.getProduct().getId();
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCategoryCacheKeyFromProduct(null, productId));
            productVariantDao.remove(variant);
            //update price and rebuild index
            serviceLocator.getProductDao().updateProductPrice(productId);
        }
        Messages messages = new Messages();
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
        return messages.toString();
    }

    /**
     * This method is called when update or create new productVariant
     */
    @RequestMapping(value = {"productvariant_update.html"}, method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String updateProductVariant(@Valid Long productId, @Valid String productPrice, @Valid ProductVariant productVariant, BindingResult bindingResult) throws Exception {
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCategoryCacheKeyFromProduct(null, productId));
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String currency = site.getSiteParamsMap().get("CURRENCY");
        productVariant.setPrice(Money.moneyToDB(productPrice, currency));
        Messages message =  categoryService.addVariant(productVariant, productId);
        //Update and rebuild index for the product
        serviceLocator.getProductDao().updateProductPrice(productId);
        return message.toString();
    }
    /**
     * This method is called when update or create new productVariant
     */
    @RequestMapping(value = {"getproductvariant.html"}, method = RequestMethod.GET)
    public @ResponseBody ProductVariant getProductVariant(@Valid Long id) throws Exception {
        return productVariantDao.findById(id);
    }

    /**
     * This method is called when insert images to a product
     * Note: Adding headers="Accept=*\/*\" for fixing HTTP Error 406 Not acceptable
     */
    @RequestMapping(value = "insert_product_image.html", headers="Accept=*/*", method = RequestMethod.GET)
    @CSRFProtection
    public @ResponseBody Map ajaxInsertImage(
            @RequestParam(value = "productId", required = true) Long productId,
            @RequestParam(value = "uri", required = true) String uri,
            @RequestParam(value = "imageName", required = false, defaultValue = "") String imageName
    ) throws Exception {
        ProductFile productImage;
        String imageServer = serviceLocator.getSystemContext().getGlobalConfig("image.server");
        Map <String, Object>map = new HashMap<String, Object>();
        if (productId != null && productId > 0) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            Product product = productDao.findById(productId, site.getId());
            if (!StringUtils.isEmpty(uri) && product != null) {
                if (!StringUtils.isEmpty(uri)) {
                    productImage = new ProductFile();
                    productImage.setUri(uri);
                    productImage.setFileType("PRODUCT_FILE_IMAGE");
                    productImage.setFileName(imageName);
                    productImage.setProduct(product);
                    productFileDao.persist(productImage);
                    map.put("success", false);
                    map.put("id", productImage.getId());
                    map.put("uri", productImage.getUri());
                    map.put("delete_url", imageServer + "/images/remove.json?name=" + productImage.getUri() + "&key=" + URLEncoder.encode(WebUtil.encrypt(productImage.getUri()), "UTF-8") + "&path=" + site.getSiteCode());
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCategoryCacheKeyFromProduct(null, productId));
                }
            } else {
                map.put("success", false);
            }
        } else {
            map.put("success", false);
        }
        return map;
    }
    /**
     * This method is called when insert images to a product
     * Note: Adding headers="Accept=*\/*\" for fixing HTTP Error 406 Not acceptable
     */
    @RequestMapping(value = "update_product_image.html", headers="Accept=*/*", method = RequestMethod.GET)
    @CSRFProtection
    public @ResponseBody String ajaxUpdateImage(
            @RequestParam(value = "productId", required = true) Long productId,
            @RequestParam(value = "imageId", required = true) Long imageId,
            @RequestParam(value = "crop", required = true) String crop
    ) throws Exception {
        ProductFile productImage = productFileDao.getProductImage(imageId, productId);
        if (productImage != null && crop != null && !crop.equals(productImage.getCrop())) {
            productImage.setCrop(crop);
            productFileDao.merge(productImage);
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCategoryCacheKeyFromProduct(null, productId));
        }
        return "success";
    }
    /**
     * This method is called when set an image is default of a product
     */
    @RequestMapping(value = "set_default_image.html", method = RequestMethod.GET)
    @CSRFProtection
    public @ResponseBody String setDefaultImage(
            @RequestParam(value = "productId", required = true) Long productId,
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "uri", required = true) String uri,
            @RequestParam(value = "imageName", required = false, defaultValue = "") String imageName,
            ProductFile entity
    ) throws Exception {
        Messages messages = new Messages();
        //reset N all images
        productFileDao.resetDefaultProductImage(productId);
        ProductFile originalProductFile =  productFileDao.findById(entity.getId());
        if (originalProductFile != null) {
            //set default the current image
            originalProductFile.setDefault("Y");
            productFileDao.merge(originalProductFile);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCategoryCacheKeyFromProduct(null, productId));
            return messages.toString();
        }
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        return messages.toString();
    }

    /**
     * This will be called when delete an image of a product
     */
    //TODO: make sure it deletes product belong the current site.
    @RequestMapping(value = "deleteproductimage.html", method = RequestMethod.GET)
    @CSRFProtection
    public @ResponseBody String deleteImage(@Valid Long id) {
        try {
            if (id > 0) {
                ProductFile productFile = productFileDao.findById(id);
                if (productFile != null) {
                    productFileDao.remove(productFile);
                }
                return "ok";
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "fail";
        }
        return "fail";
    }

    /**
     * ************************************************************************************************
     * Implement Category
     * *************************************************************************************************
     */

    @RequestMapping(value = {"category_insert.html", "category_update.html", "category_create.html"}, method = RequestMethod.GET)
    public String categoryForm() throws Exception {
        return "admin/catalog/index";
    }

    @RequestMapping(value = "{categoryId}/index.html", method = RequestMethod.GET)
    public ModelAndView editForm(@PathVariable Long categoryId) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Category category = categoryDao.findById(categoryId, site.getId());
        if (category != null) {
            Map map = new HashMap();
            map.put("category", categoryDao.findById(categoryId, site.getId()));

            return new ModelAndView("admin/catalog/form", map);
        } else {
            return new ModelAndView("admin/catalog/form");
        }
    }

    /**
     * This will be called when save to insert/update a category
     */
    @RequestMapping(value = {"category_update.html","category_insert.html"}, method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String update(@Valid Category entity, @Valid Long parentId, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            //TODO: show error messages
        }
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateMenuCacheKey());
        return categoryService.createOrUpdate(entity, parentId).toString();
    }

    /**
     * This will be called when delete a category
     */
    @RequestMapping(value = "category_delete.html", method = RequestMethod.GET, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String delete(@Valid Long id) {
        Messages messages = new Messages();
        try {
            if (id > 0) {
                Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
                Category category = categoryDao.findById(id, site.getId());
                if (category != null) {
                    //Do not remove if the category has subcategories in it.
                    List subcategories = categoryDao.findBy("parentCategory.id", category.getId(), site.getId());
                    if (subcategories != null && subcategories.size() > 0) {
                        messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("category.delete.error.1", null, LocaleContextHolder.getLocale()));
                        return messages.toString();
                    }
                    //Do not remove if the category has product associate with it
                    Long count = productDao.countProductBySubCategory(category.getId(), "N");
                    if (count > 0) {
                        messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("category.delete.error.2", null, LocaleContextHolder.getLocale()));
                        return messages.toString();
                    }
                    //We don't support this relationship right now, so set null so we can delete it.
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateMenuCacheKey());
                    categoryService.remove(category, site);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                    return messages.toString();
                } else {
                    messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            messages.addError("ERROR: Cannot delete this category. Please make sure that all sub new categories and products of this one were deleted");
        }
        return messages.toString();
    }

    @RequestMapping(value = "category_reorder.html", method = RequestMethod.GET, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String reOrder(HttpServletRequest request, @Valid Integer currentItem, @Valid String orderList) throws Exception {
        Messages messages = new Messages();

        ObjectMapper objectMapper = new ObjectMapper();
        List<HashMap> arrayLists = null;//new ArrayList<HashMap>();
        arrayLists = objectMapper.readValue(orderList, ArrayList.class);
        Map before = null;
        Map current = null;
        Map after = null;
        Map parentLevel = null;
        boolean done = false;
        if (arrayLists == null) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            return messages.toString();
        }
        for (int i = 0; i<arrayLists.size(); i++) {
            if (!done) {
                Map itemLevel1 = arrayLists.get(i);
                Integer idLevel1 = (Integer) itemLevel1.get("id");
                if (idLevel1 != null && idLevel1.equals(currentItem)) {
                    //item before
                    if (i - 1 >= 0) before = arrayLists.get(i-1);
                    //current item
                    current = itemLevel1;
                    //item after that
                    if (i+1 < arrayLists.size()) after = arrayLists.get(i+1);
                    //parent item is always null
                    done = true;
                }
                //Check and see if this menu has children
                if (itemLevel1.get("children") != null) {
                    List<HashMap> children1 = (List<HashMap>) itemLevel1.get("children");
                    for (int j = 0; j<children1.size(); j++) {
                        if (!done) {
                            Map itemLevel2 = children1.get(j);
                            Integer idLevel2 = (Integer) itemLevel2.get("id");
                            if (idLevel2 != null && idLevel2.equals(currentItem)) {
                                //item before
                                if (j - 1 >= 0) before = children1.get(j-1);
                                //current item
                                current = itemLevel2;
                                //item after that
                                if (j+1 < children1.size()) after = children1.get(j+1);
                                //parent item
                                parentLevel = itemLevel1;
                                done = true;
                            }
                            //Check and see if this menu has children
                            if (itemLevel2.get("children") != null) {
                                List<HashMap> children2 = (List<HashMap>) itemLevel2.get("children");
                                for (int k = 0; k<children2.size(); k++) {
                                    if (!done) {
                                        Map itemLevel3 = children2.get(k);
                                        Integer idLevel3 = (Integer) itemLevel3.get("id");
                                        if (idLevel3 != null && idLevel3.equals(currentItem)) {
                                            //item before
                                            if (k - 1 >= 0) before = children2.get(k-1);
                                            //current item
                                            current = itemLevel3;
                                            //item after that
                                            if (k+1 < children2.size()) after = children2.get(k+1);
                                            //parent item
                                            parentLevel = itemLevel2;
                                            done = true;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        } else {
                            break;
                        }
                    }

                }
            } else {
                break;
            }
        }

        Category categoryBefore = null;
        Category categoryCurrent = null;
        Category categoryAfter = null;
        Category categoryParent = null;

        if (current != null) categoryCurrent = categoryDao.findById(Long.valueOf((Integer) current.get("id")));
        if (categoryCurrent == null) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            return messages.toString();
        }
        if (before != null) categoryBefore = categoryDao.findById(Long.valueOf((Integer) before.get("id")));
        if (after != null) categoryAfter = categoryDao.findById(Long.valueOf((Integer) after.get("id")));
        if (parentLevel != null) categoryParent = categoryDao.findById(Long.valueOf((Integer) parentLevel.get("id")));

        float sequence = categoryCurrent.getSequence();
        if (categoryBefore != null) {
            if (categoryAfter != null) {
                sequence = (categoryBefore.getSequence() + categoryAfter.getSequence()) / 2;
            } else {
                sequence = categoryBefore.getSequence() + 0.5f; // add 0.5 in the case it is the last menu in the same level.
            }
        } else {
            if (categoryAfter != null) {
                sequence = categoryAfter.getSequence() - 0.5f;
            } else {
                //Do nothing
            }
        }

        boolean isChange = false;
        if ((categoryParent != null && categoryCurrent.getParentCategory() == null) || //move menu to be chile
                (categoryParent == null && categoryCurrent.getParentCategory() != null) || //move menu to be parent
                (categoryParent != null && categoryCurrent.getParentCategory() != null && !categoryParent.getId().equals(categoryCurrent.getParentCategory().getId()))) { // menu has a different parent
            categoryCurrent.setParentCategory(categoryParent);
            isChange = true;
        }
        if (sequence != categoryCurrent.getSequence()) {
            categoryCurrent.setSequence(sequence);
            isChange = true;
        }

        if (isChange) {
            categoryDao.merge(categoryCurrent);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        } else {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateMenuCacheKey());
        return messages.toString();
    }

    @RequestMapping(value = "search_product.html", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody Map searchProduct(HttpServletRequest request) throws Exception {
        Map result = new HashMap();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Map input = new HashMap();
        input.put("keyword", request.getParameter("keyword"));
        List products = productDao.searchProduct(input, site);
        result.put("draw", 1);
        result.put("recordsTotal", products.size());
        result.put("recordsFiltered", products.size());
        result.put("data", products);
        return result;
    }
    @RequestMapping(value = "get_product_from_category.html", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody Map getProductFromCategory(
            @RequestParam(value = "catId", required = true) Long catId
    ) throws Exception {
        Map result = new HashMap();
        List products = productDao.getProductBySubCategory(catId, 0, 100, "N", null, true);
        result.put("draw", 1);
        result.put("recordsTotal", products != null? products.size():0);
        result.put("recordsFiltered", products != null? products.size():0);
        result.put("data", products);
        return result;
    }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//    /**
//     * View all categories by treeview
//     *
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "index.html", method = RequestMethod.GET)
//    public ModelAndView index(@Valid CommonForm form) throws Exception {
//        return new ModelAndView("admin/categories/index", "command", form);
//    }
//
//    /**
//     * The method will be called when click on Create New Category
//     *
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "form.html", method = RequestMethod.GET)
//    public ModelAndView displayForm(HttpServletRequest request) throws Exception {
//        Category entity = new Category();
//        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Site site = principal.getSite();
//        if (!StringUtils.isEmpty(request.getParameter("parentCategory"))) {
//            Long catId = Long.valueOf(request.getParameter("parentCategory"));
//            Category parentCat = categoryService.getCategory(catId);
//            entity.setParentCategory(parentCat);
//        }
//        Map map = new HashMap();
//        map.put("command", entity);
//        return new ModelAndView("admin/categories/form", map);
//    }
//
//    @RequestMapping(value = "{id}/index.html", method = RequestMethod.GET)
//    public ModelAndView view(@PathVariable Long id, HttpServletRequest request) throws Exception {
//        if ("inplace".equals(request.getParameter("action"))) {
//            return new ModelAndView("admin/categories/categorytree_ajax", "command", categoryService.getSubCategories(id));
//        } else {
//            Map map = new HashMap();
//            map.put("command", categoryDao.findById(id));
//            return new ModelAndView("admin/categories/form", map);
//        }
//    }
//
//    @RequestMapping(value = "{id}/subcategories.html", method = RequestMethod.GET)
//    public ModelAndView viewSubCategories(@PathVariable Long id, HttpServletRequest request) throws Exception {
//        Map map = new HashMap();
//        map.put("command", categoryService.getSubCategories(id));
//        map.put("currentCategory", categoryService.getCategory(id));
//        return new ModelAndView("admin/categories/subcategories_tab", map);
//    }
//
//    @RequestMapping(value = "{catid}/{subcatid}/removeSubCategory.html", method = RequestMethod.GET)
//    public ModelAndView removeSubCategory(@PathVariable Long catid, @PathVariable Long subcatid, HttpServletRequest request) throws Exception {
//        if (subcatid != null && subcatid > 0) {
//            Category cat = categoryDao.findById(subcatid);
//            cat.setParentCategory(null);
//            categoryDao.merge(cat);
//        }
//        Map map = new HashMap();
////        map.put("command", categoryService.getSubCategories(catid));
////        map.put("currentCategory", categoryService.getCategory(catid));
//        return new ModelAndView("admin/categories/subcategories_tab", map);
//    }
//
//    @RequestMapping(value = "{categoryId}/{productId}/removeproductcategory.html", method = RequestMethod.GET)
//    public String removeProductCategory(@PathVariable Long categoryId, @PathVariable Long productId) throws Exception {
//        if (categoryId != null && productId > 0) {
//            categoryDao.removeProductCategory(categoryId, productId);
//            return "admin/ok";
//        } else {
//            return "admin/ok";
//        }
////        Map map = new HashMap();
////        map.put("categoryId", categoryId);
////        map.put("products", this.productDao.getProductBySubCategory(categoryId, 0, Integer.MAX_VALUE, false, null, false));
////        return new ModelAndView("admin/products/listproductsbycategory_tab", map);
//    }
//
//    @RequestMapping(value = "{categoryId}/listcategories.html", method = RequestMethod.GET)
//    public ModelAndView listCategories(@PathVariable Long categoryId, HttpServletRequest request) throws Exception {
//        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//        //TODO: get correct listCategories
//        List<Catalog> catalogs = catalogDao.getCatalogFromCategory(categoryId, false);
//        if (catalogs != null && catalogs.size() > 0) {
//            Catalog catalog = catalogs.get(0);
//            List<Category> allCategories = categoryDao.getAllCategoriesExclude(categoryService.getSubCategories(categoryId), catalog.getId());
//            Map map = new HashMap();
//            map.put("command", allCategories);
//            map.put("currentCategory", categoryService.getCategory(categoryId));
//            return new ModelAndView("admin/categories/listcategories_popup", map);
//        } else {
//            throw new IllegalArgumentException("Cannot find catalog from category. This is data issue. " + categoryId);
//        }
//    }
//
    @RequestMapping(value = "{catid}/activate_category.html", method = RequestMethod.GET)
    public @ResponseBody String activateCategory(@PathVariable Long catid, HttpServletRequest request) throws Exception {
        String check = request.getParameter("flag");

        if (catid != null && catid > 0) {
            Category cat = categoryDao.findById(catid);
            if (!StringUtils.isEmpty(check) && check.equalsIgnoreCase("true")) {
                cat.setActive("Y");
            } else {
                cat.setActive("N");
            }
            categoryDao.merge(cat);
        }
        return "ok";
    }

    @RequestMapping(value = {"assign_product_to_category.html"}, method = RequestMethod.GET)
    public @ResponseBody String assignProduct2Category(
            @RequestParam(value = "prodId", required = true) Long prodId,
            @RequestParam(value = "catId", required = true) Long catId,
            @RequestParam(value = "flag", required = true) String flag
    ) throws Exception {
        if (!StringUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")) {
            categoryDao.addProductToCategory(catId, prodId);
        } else {
            categoryDao.removeProductCategory(catId, prodId);
        }
        //Update and rebuild index for the product
        serviceLocator.getProductDao().updateProductPrice(prodId);
        return "ok";
    }
//
//    @RequestMapping(value = "checkuri.html", method = RequestMethod.GET)
//    public String checkURI(HttpServletRequest request) throws Exception {
//        String uri = request.getParameter("uri");
//
//        if (!StringUtils.isEmpty(uri)) {
//            Category cat = categoryDao.findUniqueBy("uri", uri);
//            if (cat == null) {
//                return "admin/ok";
//            } else {
//                return "admin/fail";
//            }
//        }
//        return "admin/fail";
//    }
//
//    @RequestMapping(value = "{catid}/{subcatid}/addSubCategory.html", method = RequestMethod.GET)
//    public ModelAndView addSubCategory(@PathVariable Long catid, @PathVariable Long subcatid, HttpServletRequest request) throws Exception {
//        if (catid != null && catid > 0 && subcatid != null && subcatid > 0) {
//            categoryDao.addSubCategory(catid, subcatid);
//        }
//        //TODO: correct get listCategories
//        List<Catalog> catalogs = catalogDao.getCatalogFromCategory(catid, false);
//        if (catalogs != null && catalogs.size() > 0) {
//            Catalog catalog = catalogs.get(0);
//            List<Category> allCategories = categoryDao.getAllCategoriesExclude(categoryService.getSubCategories(catid), catalog.getId());
//            Map map = new HashMap();
//            map.put("command", allCategories);
//            map.put("currentCategory", categoryService.getCategory(catid));
//            return new ModelAndView("admin/categories/listcategories_popup", map);
//        } else {
//            throw new IllegalArgumentException("Cannot find catalog");
//        }
//    }
//
//    /**
//     * This will be call when save the new category
//     *
//     * @param entity
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "index.html", method = RequestMethod.PUT)
//    public ModelAndView insert(@Valid Category entity, BindingResult result) throws Exception {
//
//        Messages messages = new Messages();
//        Map map = new HashMap();
//
//        if (entity.getParentCategory() != null && entity.getParentCategory().getId() == null) {
//            entity.setParentCategory(null);
//        }
//        if (result.hasErrors()) {
//            //TODO: show error messages
//        }
//        //Check unique uri
//        Category cat = categoryDao.findUniqueBy("uri", entity.getUri());
//        if (cat != null) {
//            messages.addError("The uri of this category existed. Please enter another one.");
//            map.put("command", entity);
//            map.put("messages", messages);
//            return new ModelAndView("admin/categories/form", map);
//        }
//
//        Date date = new Date();
//        entity.setCreatedDate(date);
//        entity.setUpdatedDate(date);
//        messages = categoryService.createOrUpdate(entity);
//        map.put("command", entity);
//        map.put("messages", messages);
//        return new ModelAndView("admin/categories/form", map);
//    }
//
//    /**
//     * This will be called when save to update the category
//     *
//     * @param id
//     * @param entity
//     * @param result
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "{id}/index.html", method = RequestMethod.POST)
//    public ModelAndView update(@PathVariable Long id, @Valid Category entity, BindingResult result) throws Exception {
//        Map map = new HashMap();
//        if (result.hasErrors()) {
//            //TODO: show error messages
//        }
//        entity.setId(id);
//
//        //Check unique uri
//        Category original = categoryDao.findById(id);
//        if (!entity.getUri().equals(original.getUri())) {
//            Category cat = categoryDao.findUniqueBy("uri", entity.getUri());
//            if (cat != null) {
//                Messages messages = new Messages();
//                messages.addError("The uri of this category existed. Please enter another one.");
//                map.put("command", entity);
//                map.put("messages", messages);
//                return new ModelAndView("admin/categories/form", map);
//            }
//        }
//
//        map.put("command", categoryDao.findById(id));
//        map.put("messages", categoryService.createOrUpdate(entity));
//        return new ModelAndView("admin/categories/form", map);
//    }
//
//    @RequestMapping(value = "{id}/index.html", method = RequestMethod.DELETE)
//    public ModelAndView delete(@PathVariable Long id) throws Exception {
//        categoryService.remove(categoryDao.findById(id));
//        return new ModelAndView("admin/categories/index");
//    }
//
//    /***************************************************************************************************
//     *  Implement catalog
//     ***************************************************************************************************/
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    /**
//     * View catalog table
//     *
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "catalog.html", method = RequestMethod.GET)
//    public ModelAndView catalog() throws Exception {
//        List<Catalog> catalogs = this.catalogDao.getAllCatalogsBySite(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite());
//        return new ModelAndView("admin/categories/catalog_tab", "catalogs", catalogs);
//    }
//
//    @RequestMapping(value = "{catalogId}/editcatalog.html", method = RequestMethod.GET)
//    public ModelAndView editCatalog(@PathVariable Long catalogId) throws Exception {
//        Catalog catalog = this.catalogDao.findById(catalogId);
//        return new ModelAndView("admin/categories/catalogform", "catalog", catalog);
//    }
//
//    /**
//     * This will be call when save the new Catalog
//     *
//     * @param entity
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "insertcatalog.html", method = RequestMethod.PUT)
//    public ModelAndView insertCatalog(@Valid Catalog entity, BindingResult result) throws Exception {
//
//        if (result.hasErrors()) {
//            //TODO: show error messages
//        }
//        Map map = new HashMap();
//        map.put("catalog", entity);
//        map.put("messages", createOrUpdateCatalog(entity));
//        return new ModelAndView("admin/categories/catalogform", map);
//    }
//
//    /**
//     * This will be called when save to update the Catalog
//     *
//     * @param id
//     * @param entity
//     * @param result
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "{id}/updatecatalog.html", method = RequestMethod.POST)
//    public ModelAndView updateCatalog(@PathVariable Long id, @Valid Catalog entity, BindingResult result) throws Exception {
//        if (result.hasErrors()) {
//            //TODO: show error messages
//        }
//        entity.setId(id);
//        Map map = new HashMap();
//        map.put("catalog", categoryDao.findById(id));
//        map.put("messages", createOrUpdateCatalog(entity));
//        return new ModelAndView("admin/categories/catalogform", map);
//    }
//
//    private Messages createOrUpdateCatalog(Catalog entity) throws Exception {
//        Messages messages = new Messages();
//
//        if (entity.getId() == null) {
//            Date date = new Date();
//            entity.setCreatedDate(date);
//            catalogDao.persist(entity);
//            messages.addInfo("New Catalog is created");
//        } else {
//            //Update catalog
//            Catalog original = catalogDao.findById(entity.getId());
//            if (!entity.getName().equals(original.getName())) {
//                original.setName(entity.getName());
//                messages.addInfo("Name is saved");
//            }
//            if (!entity.getUri().equals(original.getUri())) {
//                original.setUri(entity.getUri());
//                messages.addInfo("URI is saved");
//            }
//            if (entity.getActive() != original.getActive()) {
//                if (entity.getActive()) {
//                    original.setActive(entity.getActive());
//                    messages.addInfo("Product is activated");
//                } else {
//                    original.setActive(entity.getActive());
//                    messages.addInfo("Product is deactivated");
//                }
//            }
//            if (!entity.getDescription().equals(original.getDescription())) {
//                original.setDescription(entity.getDescription());
//                messages.addInfo("Description is saved");
//            }
//
//            if (messages.isEmpty()) {
//                messages.addInfo("No data changed");
//            } else {
//                catalogDao.merge(original);
//            }
//        }
//        return messages;
//    }
//
//
//    @RequestMapping(value = "{catalogId}/categories.html", method = RequestMethod.GET)
//    public ModelAndView listCategoriesOfCatalog(@PathVariable Long catalogId, HttpServletRequest request) throws Exception {
//        List<Category> allCategories = categoryDao.getRootCategoriesByCatalog(catalogId);
////        Map map = new HashMap();
////        map.put("command", allCategories);
//        return new ModelAndView("admin/categories/catalogcategories_tab", "catalogId", catalogId);
//    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public SiteDao getSiteDao() {
        return siteDao;
    }

    public CatalogDao getCatalogDao() {
        return catalogDao;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public ProductVariantDao getProductVariantDao() {
        return productVariantDao;
    }

    public RelatedProductDao getRelatedProductDao() {
        return relatedProductDao;
    }
}
