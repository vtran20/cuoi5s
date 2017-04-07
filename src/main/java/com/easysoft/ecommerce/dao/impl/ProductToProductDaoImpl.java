package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.dao.ProductToProductDao;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductToProduct;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.search.FullTextSession;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class ProductToProductDaoImpl extends GenericDaoImpl<ProductToProduct, Long> implements ProductToProductDao {
}
