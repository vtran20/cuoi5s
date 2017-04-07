package com.easysoft.ecommerce.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.service.SEOService;
import com.easysoft.ecommerce.service.SystemService;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {

    private CategoryDao categoryDao;
    private ProductDao productDao;
    private SEOService seoService;

    @Autowired
    public SystemServiceImpl(CategoryDao categoryDao, ProductDao productDao, SEOService seoService) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.seoService = seoService;
    }

    @Override
    public void updateSEOURIs() throws Exception {

        Map<Long, Set<String>> seoNames = new HashMap<Long, Set<String>>();
        /*
        List<Category> categories = categoryDao.findAll();
        for (Category category : categories) {
            Long siteId = (category.getSite() != null ? category.getSite().getId() : null);
            Set<String> siteSeoNames = seoNames.get(siteId);
            if (siteSeoNames == null) {
                siteSeoNames = new HashSet<String>();
                seoNames.put(siteId, siteSeoNames);
            }
            String uri = category.getUri();
            if (StringUtils.isBlank(uri) || siteSeoNames.contains(uri)) {
                String name = category.getName();
                uri = seoService.getSEOName(name);
                while (siteSeoNames.contains(uri)) {
                    uri += "-" + category.getId();
                }
                category.setUri(uri);
            }
            siteSeoNames.add(uri);
        }
        List<Product> products = productDao.findAll();
        for (Product product : products) {
            Long siteId = (product.getSite() != null ? product.getSite().getId() : null);
            Set<String> siteSeoNames = seoNames.get(siteId);
            if (siteSeoNames == null) {
                siteSeoNames = new HashSet<String>();
                seoNames.put(siteId, siteSeoNames);
            }
            String uri = product.getUri();
            if (StringUtils.isBlank(uri) || siteSeoNames.contains(uri)) {
                String name = product.getName();
                uri = seoService.getSEOName(name);
                while (siteSeoNames.contains(uri)) {
                    uri += "-" + product.getId();
                }
                product.setUri(uri);
            }
            siteSeoNames.add(uri);
        }
        */
    }
}
