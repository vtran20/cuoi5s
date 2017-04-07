package com.easysoft.ecommerce.service;

import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.util.Messages;

import java.util.*;

public interface SiteService {

    Messages createOrUpdate(Site site, HashMap <String, String> siteParams) throws Exception;

    Locale getSiteLocale(Site site);

    TimeZone getSiteTimeZone(Site site);

    String getSiteTheme(Site site);

    void remove(Site site);

    Messages createSite(Map data, Site site);
    Messages createPartnerSite(Map data, Site site);
    void copyProduct2Partners (List<Product> currentProducts, Site partnerSite);
    void copyPaymentMethod (Site site, Site thisSite);
    void copyShippingMethod (Site site, Site thisSite);
    Messages deleteSite(Map data, Site site);

    Messages createOrUpdateFreeShippingLocal(Promotion promotion, Site site);

    Messages createOrUpdateFreeShippingTotalPrice(Promotion promotion, Site site);
}