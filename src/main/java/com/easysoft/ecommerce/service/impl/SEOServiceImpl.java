package com.easysoft.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.service.SEOService;

@Service
public class SEOServiceImpl implements SEOService {

    private CategoryDao categoryDao;
    private ProductDao productDao;
    private MessageSource messageSource;

    @Autowired
    public SEOServiceImpl(CategoryDao categoryDao, ProductDao productDao, MessageSource messageSource) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.messageSource = messageSource;
    }

    public String id2uri(Class<?> entityClass, Long id, Long siteId) throws Exception {
        String result;

        if (entityClass == Category.class) {
            Category category = categoryDao.findById(id);
            result = category.getUri();
        } else if (entityClass == Product.class) {
            Product product = productDao.findById(id);
            result = product.getUri();
        } else {
            throw new IllegalArgumentException();
        }

        return result;

    }

    public Object uri2object(String uri, Long siteId) throws Exception {
        Object result;
        List<Object> entities = new ArrayList<Object>();
        List<Category> categories = categoryDao.findBy("uri", uri);
        for (Category category : categories) {
            entities.add(category);
        }
        List<Product> products = productDao.findBy("uri", uri);
        for (Product product : products) {
            entities.add(product);
        }
        if (entities.isEmpty()) {
            result = null;
        } else {
            result = entities.get(0);
        }
        return result;
    }

    @Override
    public String getSEOName(String name) {
        String unsafe = messageSource.getMessage("url_unsafechars", null, LocaleContextHolder.getLocale());
        String safe = messageSource.getMessage("url_safechars", null, LocaleContextHolder.getLocale());
        String result = "";
        for (int i = 0; i < name.length(); i ++) {
            char c = name.charAt(i);
            int index = unsafe.indexOf(c);
            if (index != -1) c = safe.charAt(index);
            else if (c == ' ') c = '-';
            else if (c == '/') c = '-';
            else if (c == '.') c = '-'; // urlrewrite.xml cannot handle uris with dots, so replace it by -

            result += c;
        }

        // remove duplicated - chars
        Pattern pattern = Pattern.compile("\\-+");
        Matcher matcher = pattern.matcher(result);
        result = matcher.replaceAll("-");

        //use lowercase
        result = result.toLowerCase(LocaleContextHolder.getLocale());

        return result;
    }
}
