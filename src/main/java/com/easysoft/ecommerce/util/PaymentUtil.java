package com.easysoft.ecommerce.util;

import com.easysoft.ecommerce.model.PaymentProvider;
import com.easysoft.ecommerce.model.PaymentProviderParam;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is an utility class that provide utility methods for entire web application.
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentUtil {

    protected static Map <Long, PaymentProvider> providers = null;
    protected static Map <Long, Map<String, String>> providerParams = null;

    public static PaymentProvider getProvider(Long id) {
        if (providers == null) {
            providers = new HashMap<Long, PaymentProvider>();
            List<PaymentProvider> paymentProviders = ServiceLocatorHolder.getServiceLocator().getPaymentProviderDao().findBy("active", "Y", null);
            for (PaymentProvider provider: paymentProviders) {
                providers.put(provider.getId(), provider);
            }
        }
        return providers.get(id);
    }

    public static Map <String, String> getProviderParams(Long providerId, Long siteId) {
        if (providerParams == null) {
            providerParams = new HashMap<Long, Map<String, String>>();
        }

        Map<String, String> result = providerParams.get(providerId);
        if (result != null) {
            return result;
        } else {
            List<PaymentProviderParam> paymentProviderParams = ServiceLocatorHolder.getServiceLocator().getPaymentProviderParamDao().findBy("paymentProviderId", providerId, siteId);
            Map<String, String> params = new HashMap<String, String>();
            for (PaymentProviderParam providerParam: paymentProviderParams) {
                params.put(providerParam.getKey(), providerParam.getValue());
            }
            providerParams.put(providerId, params);
            return params;
        }

    }


}
