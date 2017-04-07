package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.controller.admin.form.CommonForm;
import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductToProduct;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * We don't use this one for the new layout. For the new layout please refer CatalogAdminController.java
 *@deprecated
 */

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    //    private ProductService service;
    private SiteDao siteDao;
    private ProductDao productDao;
    private ProductVariantDao productVariantDao;
    private RelatedProductDao relatedProductDao;
    private CategoryDao categoryDao;
    

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get("DATE_FORMAT"));
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @Autowired
    public ProductAdminController(SiteDao siteDao, ProductVariantDao productVariantdao, ProductDao productDao, RelatedProductDao relatedProductDao, CategoryDao categoryDao) {
//        this.service = service;
        this.productDao = productDao;
        this.productVariantDao = productVariantdao;
        this.siteDao = siteDao;
        this.relatedProductDao = relatedProductDao;
        this.categoryDao = categoryDao;
    }

    @RequestMapping(value = "index.html", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        return new ModelAndView("admin/products/index");
    }

    @RequestMapping(value = "form.html", method = RequestMethod.GET)
    public ModelAndView displayForm(@Valid Product entity, HttpServletRequest request) throws Exception {

        Product product = new Product();
        ProductVariant productVariant = new ProductVariant();
        Map map = new HashMap();
        map.put("product", product);
        map.put("productVariant", productVariant);
        map.put("categoryId", request.getParameter("categoryId"));
        return new ModelAndView("admin/products/form", map);
    }

    @RequestMapping(value = "index.html", method = RequestMethod.PUT)
    public ModelAndView insertProduct(@Valid Product entity, @Valid ProductVariant variant, BindingResult result, HttpServletRequest request) throws Exception {

        Long categoryid = 0l;
        if (!StringUtils.isEmpty(request.getParameter("categoryId"))) {
            categoryid = Long.valueOf(request.getParameter("categoryId"));
        }

        Map map = new HashMap();
        map.put("product", entity);
        map.put("productVariant", variant);
        map.put("messages", createOrUpdate(entity, variant, categoryid));

        return new ModelAndView("admin/products/form", map);
    }

    @RequestMapping(value = "{id}/index.html", method = RequestMethod.POST)
    public ModelAndView updateProduct(@PathVariable Long id, @Valid Product entity, @Valid ProductVariant variant, BindingResult result, HttpServletRequest request) throws Exception {
        //update Product
        entity.setId(id);

        Map map = new HashMap();
        map.put("product", entity);
        map.put("productVariant", variant);
        map.put("messages", createOrUpdate(entity, variant, null));

        return new ModelAndView("admin/products/form", map);
    }

    @RequestMapping(value = "{id}/index.html", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable Long id) throws Exception {
        Product product = productDao.findById(id);
        Map map = new HashMap();
        map.put("product", product);
        map.put("productVariant", productVariantDao.getProductVariantDefault(id));

        return new ModelAndView("admin/products/form", map);
    }

    @RequestMapping(value = "{id}/delete.html", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable Long id) throws Exception {

        Product product = productDao.getProduct(id);

        //remove product variant
        List<ProductVariant> list = this.productVariantDao.findAll(id);
        if (list != null && !list.isEmpty()) {
            for (ProductVariant var : list) {
                this.productVariantDao.remove(var);
            }
        }

        //remove related product
        List<ProductToProduct> p2p = this.relatedProductDao.findBy("product",product);
        if (p2p != null && !p2p.isEmpty()) {
            for (ProductToProduct var : p2p) {
                this.relatedProductDao.remove(var);
            }
        }
        //remove related product
        p2p = this.relatedProductDao.findBy("relateProduct",product);
        if (p2p != null && !p2p.isEmpty()) {
            for (ProductToProduct var : p2p) {
                this.relatedProductDao.remove(var);
            }
        }

        //Remove category_to_product
        List<Category> categories = this.categoryDao.getSubCategories(product.getId());
        for (Category category: categories) {
            categoryDao.removeProductCategory(category.getId(), product.getId());
        }

        //remove product
        this.productDao.remove(product);
        return new ModelAndView("admin/ok");
    }

    @RequestMapping(value = "{prodId}/activateproduct.html", method = RequestMethod.GET)
    public String activateCategory(@PathVariable Long prodId, HttpServletRequest request) throws Exception {
        String check = request.getParameter("flag");

        if (prodId != null && prodId > 0) {
            Product product = productDao.findById(prodId);

            if (!StringUtils.isEmpty(check) && check.equalsIgnoreCase("true")) {
                product.setActive("Y");
            } else {
                product.setActive("N");
            }
            this.productDao.merge(product);
        }
        return "admin/ok";
    }

    private Messages createOrUpdate(Product entity, ProductVariant productVariant, Long categoryId) throws Exception {
        Messages messages = new Messages();

        if (entity.getId() == null) {
            Date date = new Date();
            entity.setPriceMin(productVariant.getPrice());
            entity.setDisplayPrice(String.valueOf(productVariant.getPrice()));
            entity.setUpdatedDate(date);
            entity.setCreatedDate(date);
            //uri will be uri + model to make sure it is unique
            entity.setUri(entity.getUri()+"-" +entity.getModel());
            productDao.persist(entity);
            //for product variant
            if (productVariant != null) {
                productVariant.setUpdatedDate(date);
                productVariant.setCreatedDate(date);
                productVariant.setProduct(entity);
                productVariant.setDefault("Y");
                productVariant.setSequence(1);
                productVariantDao.persist(productVariant);
            }
            //Create product to category
            if (categoryId > 0) {
                categoryDao.addProductToCategory(categoryId, entity.getId());
            }
            messages.addInfo("New product is created");
        } else {
            Messages messages1 = new Messages();
            //Update for product
            Product original = productDao.findById(entity.getId());
            if (entity.getName() != null && !entity.getName().equals(original.getName())) {
                original.setName(entity.getName());
                messages1.addInfo("Name is saved");
            }
            if (entity.getUri() != null && !entity.getUri().equals(original.getUri())) {
                original.setUri(entity.getUri());
                messages1.addInfo("URI is saved");
            }
            if (entity.getActive().equals(original.getActive())) {
                if ("Y".equals(entity.getActive())) {
                    original.setActive(entity.getActive());
                    messages1.addInfo("Product is activated");
                } else {
                    original.setActive(entity.getActive());
                    messages1.addInfo("Product is deactivated");
                }
            }
            if (entity.getModel() != null && !entity.getModel().equals(original.getModel())) {
                original.setModel(entity.getModel());
                messages1.addInfo("Model is saved");
            }
            if (entity.getImageUrl() != null && !entity.getImageUrl().equals(original.getImageUrl())) {
                original.setImageUrl(entity.getImageUrl());
                messages1.addInfo("Image Url is saved");
            }
            if (entity.getDescription() != null && !entity.getDescription().equals(original.getDescription())) {
                original.setDescription(entity.getDescription());
                messages1.addInfo("Description is saved");
            }
            if (entity.getKeyword() != null && !entity.getKeyword().equals(original.getKeyword())) {
                original.setKeyword(entity.getKeyword());
                messages1.addInfo("Keyword is saved");
            }
            if (entity.getMetaDescription() != null && !entity.getMetaDescription().equals(original.getMetaDescription())) {
                original.setMetaDescription(entity.getMetaDescription());
                messages1.addInfo("Meta description is saved");
            }
            if (entity.getMetaKeyword() != null && !entity.getMetaKeyword().equals(original.getMetaKeyword())) {
                original.setMetaKeyword(entity.getMetaKeyword());
                messages1.addInfo("Meta keyword is saved");
            }
//            if (entity.getVariantGroup() != null && !entity.getVariantGroup().equals(original.getVariantGroup())) {
//                original.setVariantGroup(entity.getVariantGroup());
//                messages1.add("Variant Group is saved");
//            }
            if (!messages1.isEmpty()) {
                original.setUpdatedDate(new Date());
                productDao.merge(original);
            }


            //Update for product variant
            Messages messages2 = new Messages();
            ProductVariant orgProductVariant = productVariantDao.getProductVariantDefault(entity.getId());
            if (orgProductVariant == null) {
                orgProductVariant = new ProductVariant();
                orgProductVariant.setDefault("Y");//if have not had product variant default. Create new one.
                orgProductVariant.setProduct(original);//set relationship to the product
                orgProductVariant.setActive("Y");
            }
            if (productVariant.getPrice() != orgProductVariant.getPrice()) {
                orgProductVariant.setPrice(productVariant.getPrice());
                messages2.addInfo("Price is saved");
            }
            if (productVariant.getOriginalPrice() != orgProductVariant.getOriginalPrice()) {
                orgProductVariant.setOriginalPrice(productVariant.getOriginalPrice());
                messages2.addInfo("Original Price is saved");
            }
            if (productVariant.getInventory() != orgProductVariant.getInventory()) {
                orgProductVariant.setInventory(productVariant.getInventory());
                messages2.addInfo("Inventory is saved");
            }
            if (productVariant.getBarcode() != null && !productVariant.getBarcode().equals(orgProductVariant.getBarcode())) {
                orgProductVariant.setBarcode(productVariant.getBarcode());
                messages2.addInfo("Barcode is saved");
            }
            if (!messages2.isEmpty()) {
                orgProductVariant.setUpdatedDate(new Date());
                productVariantDao.merge(orgProductVariant);
            }

            if (messages1.isEmpty() && messages2.isEmpty()) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            } else {
                messages.addAll(messages1);
                messages.addAll(messages2);
            }
        }
        return messages;
    }

    @RequestMapping(value = "{productId}/productvariantlist.html", method = RequestMethod.GET)
    public ModelAndView listProductVariants(@PathVariable Long productId, HttpServletRequest request) throws Exception {
        Product product = this.productDao.getProduct(productId);
        Map map = new HashMap();
        map.put("product", product);
        map.put("productVariants", this.productVariantDao.findAll(productId, "N"));

        return new ModelAndView("admin/products/listvariants_tab", map);
    }

    @RequestMapping(value = "{varid}/activatevariant.html", method = RequestMethod.GET)
    public String activeProductVariant(@PathVariable Long varid, HttpServletRequest request) throws Exception {
        String check = request.getParameter("flag");

        if (varid != null && varid.longValue() > 0) {
            ProductVariant variant = productVariantDao.findById(varid);

            if (!StringUtils.isEmpty(check) && check.equalsIgnoreCase("true")) {
                variant.setActive("Y");
            } else {
                variant.setActive("N");
            }
            this.productVariantDao.merge(variant);
        }
        return "admin/ok";
    }

    @RequestMapping(value = "{productId}/{variantId}/defaultvariant.html", method = RequestMethod.GET)
    public String setDefaultVariant(@PathVariable Long productId, @PathVariable Long variantId, HttpServletRequest request) throws Exception {
        String check = request.getParameter("flag");

        if (productId != null && variantId != null) {
            ProductVariant variant = productVariantDao.getProductVariantDefault(productId);
            if (variant != null) {
                variant.setDefault("N");
                this.productVariantDao.merge(variant);
            }

            //set default
            variant = productVariantDao.findById(variantId);
            variant.setDefault("Y");
            this.productVariantDao.merge(variant);

            return "admin/ok";

        }
        return "admin/fail";
    }

    @RequestMapping(value = "{id}/deletevariant.html", method = RequestMethod.GET)
    public ModelAndView deleteVariant(@PathVariable Long id) throws Exception {
        ProductVariant variant = productVariantDao.findById(id);
        //remove product
        this.productVariantDao.remove(variant);
        return new ModelAndView("admin/ok");
    }

    @RequestMapping(value = "{id}/viewvariant.html", method = RequestMethod.GET)
    public ModelAndView viewVariant(@PathVariable Long id) throws Exception {
        return new ModelAndView("admin/products/viewvariant_popup", "variant", productVariantDao.findById(id));
    }

    @RequestMapping(value = "{id}/updatevariant.html", method = RequestMethod.POST)
    public ModelAndView updateVariant(@PathVariable Long id, @Valid ProductVariant variant, BindingResult result, HttpServletRequest request) throws Exception {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.getModel().putAll(result.getModel());
            return mav;
        }
        //update Product
        variant.setId(id);

        Map map = new HashMap();
        map.put("variant", variant);
        map.put("messages", createOrUpdateVariant(variant));

        return new ModelAndView("admin/products/viewvariant_popup", map);
    }

    @RequestMapping(value = "{productId}/newvariant.html", method = RequestMethod.GET)
    public ModelAndView newVariant(@PathVariable Long productId) throws Exception {
        Map map = new HashMap();
        map.put("productId", productId);
        map.put("variant", new ProductVariant());
        return new ModelAndView("admin/products/viewvariant_popup", map);
    }

    @RequestMapping(value = "{productId}/insertvariant.html", method = RequestMethod.PUT)
    public ModelAndView insertVariant(@PathVariable Long productId, @Valid ProductVariant variant, BindingResult result, HttpServletRequest request) throws Exception {
        Product product = this.productDao.findById(productId);
        variant.setProduct(product);

        Map map = new HashMap();
        map.put("variant", variant);
        map.put("messages", createOrUpdateVariant(variant));

        return new ModelAndView("admin/products/viewvariant_popup", map);
    }

    private Messages createOrUpdateVariant(ProductVariant variant) {
        //Update for product variant
        Messages messages = new Messages();

        if (variant.getId() == null) {
            Date date = new Date();
            variant.setUpdatedDate(date);
            variant.setCreatedDate(date);
            productVariantDao.persist(variant);
            messages.addInfo("New product variant is created");
        } else {
            ProductVariant orgProductVariant = productVariantDao.findById(variant.getId());

            if (variant.getActive().equals(orgProductVariant.getActive())) {
                if ("Y".equals(variant.getActive())) {
                    orgProductVariant.setActive(variant.getActive());
                    messages.addInfo("Product variant is activated");
                } else {
                    orgProductVariant.setActive(variant.getActive());
                    messages.addInfo("Product variant is deactivated");
                }
            }
            if (variant.getPrice() != orgProductVariant.getPrice()) {
                orgProductVariant.setPrice(variant.getPrice());
                messages.addInfo("Price is saved");
            }
            if (variant.getOriginalPrice() != orgProductVariant.getOriginalPrice()) {
                orgProductVariant.setOriginalPrice(variant.getOriginalPrice());
                messages.addInfo("Original Price is saved");
            }
            if (variant.getInventory() != orgProductVariant.getInventory()) {
                orgProductVariant.setInventory(variant.getInventory());
                messages.addInfo("Inventory is saved");
            }
            if (!variant.getBarcode().equals(orgProductVariant.getBarcode())) {
                orgProductVariant.setBarcode(variant.getBarcode());
                messages.addInfo("Barcode is saved");
            }
            if (!variant.getColorCode().equals(orgProductVariant.getColorCode())) {
                orgProductVariant.setColorCode(variant.getColorCode());
                messages.addInfo("Color Code is saved");
            }
            if (!variant.getColorName().equals(orgProductVariant.getColorName())) {
                orgProductVariant.setColorName(variant.getColorName());
                messages.addInfo("Color Name is saved");
            }
            if (!variant.getColorCodeRBG().equals(orgProductVariant.getColorCodeRBG())) {
                orgProductVariant.setColorCodeRBG(variant.getColorCodeRBG());
                messages.addInfo("Color Code RBG is saved");
            }
            if (!variant.getSizeCode().equals(orgProductVariant.getSizeCode())) {
                orgProductVariant.setSizeCode(variant.getSizeCode());
                messages.addInfo("Size Code is saved");
            }
            if (!variant.getSizeName().equals(orgProductVariant.getSizeName())) {
                orgProductVariant.setSizeName(variant.getSizeName());
                messages.addInfo("Size Name is saved");
            }
            if (variant.getPricePromo() != orgProductVariant.getPricePromo()) {
                orgProductVariant.setPricePromo(variant.getPricePromo());
                messages.addInfo("Price Promo is saved");
            }
            if ((variant.getPromoStartDate() != null && orgProductVariant.getPromoStartDate() == null) ||
                    (variant.getPromoStartDate() == null && orgProductVariant.getPromoStartDate() != null) ||
                    (variant.getPromoStartDate() != null && orgProductVariant.getPromoStartDate() != null && !variant.getPromoStartDate().equals(orgProductVariant.getPromoStartDate()))) {
                orgProductVariant.setPromoStartDate(variant.getPromoStartDate());
                messages.addInfo("Start Promo Date is saved");
            }
            if ((variant.getPromoEndDate() != null && orgProductVariant.getPromoEndDate() == null) ||
                    (variant.getPromoEndDate() == null && orgProductVariant.getPromoEndDate() != null) ||
                    (variant.getPromoEndDate() != null && orgProductVariant.getPromoEndDate() != null && !variant.getPromoEndDate().equals(orgProductVariant.getPromoEndDate()))) {
                orgProductVariant.setPromoEndDate(variant.getPromoEndDate());
                messages.addInfo("End Promo Date is saved");
            }

            if (!messages.isEmpty()) {
                orgProductVariant.setUpdatedDate(new Date());
                productVariantDao.merge(orgProductVariant);
            }
        }
        return messages;
    }

    @RequestMapping(value = "{productId}/listrelatedproduct.html", method = RequestMethod.GET)
    public ModelAndView listRelatedProducts(@PathVariable Long productId, HttpServletRequest request) throws Exception {
        Product product = this.productDao.getProduct(productId);

        List<Product> relatedProducts = null;
        String relationType = request.getParameter("relationType");
        if (StringUtils.isEmpty(relationType) || "0".equals(relationType)) {
            relatedProducts = this.productDao.getRelatedProduct(productId, 0, "N");
        } else {
            relatedProducts = this.productDao.getRelatedProduct(productId, Integer.valueOf(relationType).intValue(), "N");
        }
        Map map = new HashMap();
        map.put("product", product);
        map.put("relatedProducts", relatedProducts);

        return new ModelAndView("admin/products/listrelatedproducts_tab", map);
    }

    @RequestMapping(value = "{id}/removerelatedproduct", method = RequestMethod.GET)
    public ModelAndView removeRelatedProduct(@PathVariable Long id) throws Exception {

        ProductToProduct relatedProduct = relatedProductDao.findById(id);
        this.relatedProductDao.remove(relatedProduct);
        return new ModelAndView("admin/ok");
    }

    @RequestMapping(value = "listproducts_popup.html", method = RequestMethod.GET)
    public ModelAndView listProducts(@Valid CommonForm common) throws Exception {

        return new ModelAndView("admin/products/listproducts_popup", "common", common);
    }

    @RequestMapping(value = "{productId}/{relatedProductId}/addrelatedproduct.html", method = RequestMethod.GET)
    public ModelAndView addRelatedProduct(@PathVariable Long productId, @PathVariable Long relatedProductId) throws Exception {

        ProductToProduct related = this.productDao.getRelated(productId, relatedProductId);
        if (related == null) {
            Product product = this.productDao.findById(productId);
            Product relatedProduct = this.productDao.findById(relatedProductId);
            related = new ProductToProduct();
            related.setProduct(product);
            related.setRelateProduct(relatedProduct);
            related.setRelationType(1);
            this.relatedProductDao.persist(related);
            return new ModelAndView("admin/ok");
        } else {
            return new ModelAndView("admin/fail");
        }
    }

    @RequestMapping(value = "{categoryId}/{productId}/addproducttocategory.html", method = RequestMethod.GET)
    public ModelAndView addProductToCategory(@PathVariable Long categoryId, @PathVariable Long productId) throws Exception {

        boolean isExist = this.productDao.isProductExistedInCategory(categoryId, productId);
        if (!isExist) {
            this.categoryDao.addProductToCategory(categoryId, productId);
            return new ModelAndView("admin/ok");
        } else {
            return new ModelAndView("admin/fail");
        }
    }


    @RequestMapping(value = "{categoryId}/products.html", method = RequestMethod.GET)
    public ModelAndView listProductsOfCategory(@PathVariable Long categoryId) throws Exception {
        Map map = new HashMap();
        map.put("categoryId", categoryId);
        map.put("products", this.productDao.getProductBySubCategory(categoryId, 0, Integer.MAX_VALUE, "N", null, false));
        return new ModelAndView("admin/products/listproductsbycategory_tab", map);
    }

}
