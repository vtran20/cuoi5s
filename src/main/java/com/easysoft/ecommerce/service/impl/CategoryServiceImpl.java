package com.easysoft.ecommerce.service.impl;

import java.util.*;

import com.easysoft.ecommerce.dao.ProductVariantDao;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.dao.SiteDao;
import com.easysoft.ecommerce.service.CategoryService;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductVariantDao productVariantDao;
    @Autowired
    private SiteDao siteDao;

    @Override
    public Category getCategory(Long categoryId) throws Exception {
        return this.categoryDao.findById(categoryId);
    }

    @Override
    public void addSubCategories (Long catId, Long subCatId) throws Exception {
        this.categoryDao.addSubCategory(catId, subCatId);
    }

//    @Override
//    public List<Category> getSubCategories(Long catId) throws Exception {
//        return this.categoryDao.findBy("parentCategory.id", catId);
//    }

    @Override
    public Messages addProductToCategory(Long catId, Product product) throws Exception {
        Messages messages = new Messages();
        Product prod = this.productDao.createProduct(product);
        this.categoryDao.addProductToCategory(catId, prod.getId());
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        return messages;
    }
    @Override
    public Messages addProductToCategory(Long[] catIds, Product product) throws Exception {
        Messages messages = new Messages();
        Product prod = this.productDao.createProduct(product);
        if (catIds != null && catIds.length > 0) {
            for (Long catId : catIds) {
                this.categoryDao.addProductToCategory(catId, prod.getId());
            }
        }
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        return messages;
    }

    @Override
    public Messages addProductToCategory(Long catId, Long prodId) throws Exception {
        Messages messages = new Messages();
        this.categoryDao.addProductToCategory(catId, prodId);
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        return messages;
    }

    @Override
    public List<Category> findAll() throws Exception {
        List<Category> items;

        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (site != null) {
            items = categoryDao.findBy("site.id", site.getId());
        } else {
            items = categoryDao.findAll();
        }

        return items;
    }

    public Messages createOrUpdate(Product entity, ProductVariant productVariant, Long[] categoryIds) throws Exception {
        Messages messages = new Messages();
        boolean isChanged = false;
        //Insert new product.
        if (entity.getId() == null) {
            Date date = new Date();
            entity.setPriceMin(productVariant.getPrice());
            entity.setDisplayPrice(String.valueOf(productVariant.getPrice()));
            entity.setUpdatedDate(date);
            entity.setCreatedDate(date);
            //uri will be uri + model to make sure it is unique
            String uri = WebUtil.getURI(entity.getName());
            if (!StringUtils.isEmpty(entity.getModel())) {
                uri = uri + "-" + entity.getModel();
            }
            entity.setUri(uri);
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            float maxSequence = productDao.getMaxSequence(site.getId());
            entity.setSequence(maxSequence+1f);
            entity.setSite(site);
            productDao.persist(entity);

            //for product variant
            productVariant.setUpdatedDate(date);
            productVariant.setCreatedDate(date);
            productVariant.setProduct(entity);
            productVariant.setDefault("Y");
            productVariant.setActive("Y");
            productVariant.setSequence(1);
            productVariantDao.persist(productVariant);

            //Create product to category
            if (categoryIds != null) {
                for (Long categoryId: categoryIds) {
                    categoryDao.addProductToCategory(categoryId, entity.getId());
                }
            }
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("product.create.successful", null, LocaleContextHolder.getLocale()));
        } else {
            //Update for product
            Product original = productDao.findById(entity.getId());
            if (entity.getName() != null && !entity.getName().equals(original.getName())) {
                original.setName(entity.getName());
                isChanged = true;
            }
            if (entity.getUri() != null && !entity.getUri().equals(original.getUri())) {
                original.setUri(entity.getUri());
                isChanged = true;
            }
            if (!entity.getActive().equals(original.getActive())) {
                if ("Y".equals(entity.getActive())) {
                    original.setActive(entity.getActive());
                    isChanged = true;
                } else {
                    original.setActive(entity.getActive());
                    isChanged = true;
                }
            }
            if (!entity.getNewProduct().equals(original.getNewProduct())) {
                if ("Y".equals(entity.getNewProduct())) {
                    original.setNewProduct(entity.getNewProduct());
                    isChanged = true;
                } else {
                    original.setNewProduct(entity.getNewProduct());
                    isChanged = true;
                }
            }
            if (entity.getModel() != null && !entity.getModel().equals(original.getModel())) {
                original.setModel(entity.getModel());
                isChanged = true;
            }
            if (entity.getImageUrl() != null && !entity.getImageUrl().equals(original.getImageUrl())) {
                original.setImageUrl(entity.getImageUrl());
                isChanged = true;
            }
            if (entity.getDescription() != null && !entity.getDescription().equals(original.getDescription())) {
                original.setDescription(entity.getDescription());
                isChanged = true;
            }
            if (entity.getKeyword() != null && !entity.getKeyword().equals(original.getKeyword())) {
                original.setKeyword(entity.getKeyword());
                isChanged = true;
            }
            if (entity.getMetaDescription() != null && !entity.getMetaDescription().equals(original.getMetaDescription())) {
                original.setMetaDescription(entity.getMetaDescription());
                isChanged = true;
            }
            if (entity.getMetaKeyword() != null && !entity.getMetaKeyword().equals(original.getMetaKeyword())) {
                original.setMetaKeyword(entity.getMetaKeyword());
                isChanged = true;
            }
            if (entity.getAttribute1() != null && !entity.getAttribute1().equals(original.getAttribute1())) {
                original.setAttribute1(entity.getAttribute1());
                isChanged = true;
            }
            if (entity.getAttribute2() != null && !entity.getAttribute2().equals(original.getAttribute2())) {
                original.setAttribute2(entity.getAttribute2());
                isChanged = true;
            }
            if (entity.getAttribute3() != null && !entity.getAttribute3().equals(original.getAttribute3())) {
                original.setAttribute3(entity.getAttribute3());
                isChanged = true;
            }
            if (entity.getAttribute4() != null && !entity.getAttribute4().equals(original.getAttribute4())) {
                original.setAttribute4(entity.getAttribute4());
                isChanged = true;
            }
            if (entity.getAttribute5() != null && !entity.getAttribute5().equals(original.getAttribute5())) {
                original.setAttribute5(entity.getAttribute5());
                isChanged = true;
            }
            if (isChanged) {
                original.setUpdatedDate(new Date());
                productDao.merge(original);
            }

            /*Don't update product variant here*/
            /*boolean isChanged2 = false;
            ProductVariant orgProductVariant = productVariantDao.getProductVariantDefault(entity.getId());
            if (orgProductVariant == null) {
                orgProductVariant = new ProductVariant();
                orgProductVariant.setDefault("Y");//if have not had product variant default. Create new one.
                orgProductVariant.setProduct(original);//set relationship to the product
                orgProductVariant.setActive("Y");
                productVariantDao.persist(orgProductVariant);
                isChanged2 = true;
            } else {
                if (productVariant.getPrice() != orgProductVariant.getPrice()) {
                    orgProductVariant.setPrice(productVariant.getPrice());
                    isChanged2 = true;
                }
                if (productVariant.getOriginalPrice() != orgProductVariant.getOriginalPrice()) {
                    orgProductVariant.setOriginalPrice(productVariant.getOriginalPrice());
                    isChanged2 = true;
                }
                if (productVariant.getInventory() != orgProductVariant.getInventory()) {
                    orgProductVariant.setInventory(productVariant.getInventory());
                    isChanged2 = true;
                }
                if (productVariant.getBarcode() != null && !productVariant.getBarcode().equals(orgProductVariant.getBarcode())) {
                    orgProductVariant.setBarcode(productVariant.getBarcode());
                    isChanged = true;
                }
                if (isChanged2) {
                    orgProductVariant.setUpdatedDate(new Date());
                    productVariantDao.merge(orgProductVariant);
                }
            }*/

            if (isChanged) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            } else {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            }
        }
        return messages;
    }

    @Override
    public Messages createOrUpdate(Category entity, Long parentCatId) throws Exception {
        Messages messages = new Messages();
        boolean isChanged = false;
        Category parentCategory = null;
        if (parentCatId != null && parentCatId > 0) {
            parentCategory = categoryDao.findById(parentCatId);
        }

        Date date = new Date();
        entity.setUpdatedDate(date);

        if (entity.getId() == null) {
            entity.setCreatedDate(date);
            entity.setUri(WebUtil.getURI(entity.getName()));
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            float maxSequence = categoryDao.getMaxSequence(site.getId());
            entity.setSequence(maxSequence+1f);
            entity.setParentCategory(parentCategory);
            entity.setSite(site);
            categoryDao.persist(entity);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("category.create.successful", null, LocaleContextHolder.getLocale()));
            return messages;
        } else {
            Category original = categoryDao.findById(entity.getId());
            if (entity.getName()!= null && !entity.getName().equals(original.getName())) {
                original.setName(entity.getName());
                original.setUri(WebUtil.getURI(entity.getName()));
                isChanged = true;
            }

            if (entity.getActive() != null && !entity.getActive().equals(original.getActive())) {
                if ("Y".equals(entity.getActive())) {
                    original.setActive(entity.getActive());
                } else {
                    original.setActive(entity.getActive());
                }
                isChanged = true;
            }

            if (entity.getDescription() != null && !entity.getDescription().equals(original.getDescription())) {
                original.setDescription(entity.getDescription());
                isChanged = true;
            }
            if (entity.getMetaDescription() != null && !entity.getMetaDescription().equals(original.getMetaDescription())) {
                original.setMetaDescription(entity.getMetaDescription());
                isChanged = true;
            }
            if (entity.getMetakeyword() != null && !entity.getMetakeyword().equals(original.getMetakeyword())) {
                original.setMetakeyword(entity.getMetakeyword());
                isChanged = true;
            }
            //Update Parent Menu in 3 cases below. if both is null, don't need to update.
            Category originalParentCategory = categoryDao.getParentCategory(original.getId());
            if ((parentCategory != null && originalParentCategory != null && !parentCategory.getId().equals(originalParentCategory.getId())) ||
                    (parentCategory == null && originalParentCategory != null) ||
                    (parentCategory != null && originalParentCategory == null)) {
                original.setParentCategory(parentCategory);
                isChanged = true;
            }

            if (isChanged) {
                original.setUpdatedDate(entity.getUpdatedDate());
                categoryDao.merge(original);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            } else {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            }

        }
        return messages;
    }

        @Override
    public Messages deleteProduct(Long productId) throws Exception {

        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        //Delete Product Variant
//        List<ProductVariant> variants = ServiceLocatorHolder.getServiceLocator().getProductVariantDao().findAll(productId, "N");
//        for (ProductVariant variant : variants) {
//            ServiceLocatorHolder.getServiceLocator().getProductVariantDao().remove(variant);
//        }
//        //Delete Product File
//        List<ProductFile> productFiles = ServiceLocatorHolder.getServiceLocator().getProductFileDao().findBy("product.id", productId);
//        for (ProductFile productFile : productFiles) {
//            ServiceLocatorHolder.getServiceLocator().getProductFileDao().remove(productFile);
//        }
//        //Delete Product category
//        ServiceLocatorHolder.getServiceLocator().getCategoryDao().removeProductCategory(productId, site);

        Product product = ServiceLocatorHolder.getServiceLocator().getProductDao().findById(productId, site.getId());
        if (product != null) {
            ServiceLocatorHolder.getServiceLocator().getProductDao().remove(product);
        }
        Messages messages = new Messages();
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
        return messages;
    }

    @Override
    public void remove(Category entity, Site site) throws Exception {
        //Delete Product Variant
        List<RefinementValue> refinementValues = ServiceLocatorHolder.getServiceLocator().getRefinementValueDao().findBy("category.id", entity.getId(), site.getId());
        for (RefinementValue refinementValue : refinementValues) {
            ServiceLocatorHolder.getServiceLocator().getRefinementValueDao().remove(refinementValue);
        }
        entity = categoryDao.findById(entity.getId());
        entity.setParentCategory(null);
        entity.setRefinementValues(null);
        categoryDao.remove(entity);
    }

    @Override
    public Messages addVariant (ProductVariant productVariant, Long productId) {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Product product = productDao.findById(productId, site.getId());
        boolean isChanged = false;
        if (productVariant.isEmptyId()) {
            productVariant.setUpdatedDate(new Date());
            productVariant.setProduct(product);
            productVariant.setDefault("N");
            productVariant.setActive("Y");
            productVariant.setSequence(1);
            StringParamValue sizeName = ServiceLocatorHolder.getServiceLocator().getStringParamValueDao().getStringParamValue("PRODUCT_SIZE", ServiceLocatorHolder.getServiceLocator().getLocale().toString(), productVariant.getSizeCode());
            if (sizeName != null) {
                productVariant.setSizeName(sizeName.getValue());
            }
            StringParamValue colorName = ServiceLocatorHolder.getServiceLocator().getStringParamValueDao().getStringParamValue("PRODUCT_COLOR", ServiceLocatorHolder.getServiceLocator().getLocale().toString(), productVariant.getColorCode());
            if (colorName != null) {
                productVariant.setColorName(colorName.getValue());
            }
//            if (!product.getProductVariant().contains(productVariant)) {
//                product.getProductVariant().add(productVariant);
//            }
            productVariantDao.persist(productVariant);
            isChanged = true;
        } else {
            ProductVariant orgProductVariant = productVariantDao.findById(productVariant.getId());
            if (productVariant.getPrice() != orgProductVariant.getPrice()) {
                orgProductVariant.setPrice(productVariant.getPrice());
                isChanged = true;
            }
            if (productVariant.getOriginalPrice() != orgProductVariant.getOriginalPrice()) {
                orgProductVariant.setOriginalPrice(productVariant.getOriginalPrice());
                isChanged = true;
            }
            if (productVariant.getInventory() != orgProductVariant.getInventory()) {
                orgProductVariant.setInventory(productVariant.getInventory());
                isChanged = true;
            }
            if (productVariant.getBarcode() != null && !productVariant.getBarcode().equals(orgProductVariant.getBarcode())) {
                orgProductVariant.setBarcode(productVariant.getBarcode());
                isChanged = true;
            }
            if (productVariant.getSku() != null && !productVariant.getSku().equals(orgProductVariant.getSku())) {
                orgProductVariant.setSku(productVariant.getSku());
                isChanged = true;
            }
            if (productVariant.getSizeCode() != null && !productVariant.getSizeCode().equals(orgProductVariant.getSizeCode())) {
                orgProductVariant.setSizeCode(productVariant.getSizeCode());
                StringParamValue value = ServiceLocatorHolder.getServiceLocator().getStringParamValueDao().getStringParamValue("PRODUCT_SIZE", ServiceLocatorHolder.getServiceLocator().getLocale().toString(), productVariant.getSizeCode());
                if (value != null) {
                    orgProductVariant.setSizeName(value.getValue());
                }
                isChanged = true;
            }
            if (productVariant.getColorCode() != null && !productVariant.getColorCode().equals(orgProductVariant.getColorCode())) {
                orgProductVariant.setColorCode(productVariant.getColorCode());
                StringParamValue value = ServiceLocatorHolder.getServiceLocator().getStringParamValueDao().getStringParamValue("PRODUCT_COLOR", ServiceLocatorHolder.getServiceLocator().getLocale().toString(), productVariant.getColorCode());
                if (value != null) {
                    orgProductVariant.setColorName(value.getValue());
                }
                isChanged = true;
            }
            if (isChanged) {
                orgProductVariant.setUpdatedDate(new Date());
                productVariantDao.merge(orgProductVariant);
            }
        }

        Messages messages = new Messages();
        if (isChanged) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        } else {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages;
    }
}
