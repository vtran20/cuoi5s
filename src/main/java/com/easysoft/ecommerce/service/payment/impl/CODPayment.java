package com.easysoft.ecommerce.service.payment.impl;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.payment.PaymentSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * Transfer money via bank
 *
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CODPayment extends PaymentSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CODPayment.class);


    /**
     *
     * @return url of payment gateway.
     */
    @Override
    public String createRequestUrl(SessionObject sessionObject, Site site) throws Exception {
        return "/checkout/receipt.html";
    }

    /**
     * Check returned data.
     *
     * @param params contain params that return from payment gateway
     * @return true if is correct
     */
    public boolean verifyResponseUrl(SessionObject sessionObject, Map params, Site site) throws Exception {

        return false;
    }
}
