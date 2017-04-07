package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.MenuTemplate;
import com.easysoft.ecommerce.model.PaymentProvider;
import com.easysoft.ecommerce.model.Site;

import java.util.List;


public interface PaymentProviderDao extends GenericDao<PaymentProvider, Long> {
    List<PaymentProvider> findAllPaymentProviders (String active, String orderBy);
    List<PaymentProvider> getPaymentProvidersNotUsed(Site site);
    PaymentProvider getPaymentProviderSelected(Long paymentMethodSiteId);
    PaymentProvider getPaymentProviderForSite(Long paymentId, Long siteId);
}