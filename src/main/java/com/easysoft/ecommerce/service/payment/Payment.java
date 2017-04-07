package com.easysoft.ecommerce.service.payment;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.session.SessionObject;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Payment {
    String createRequestUrl (SessionObject sessionObject, Site site) throws Exception;

    /**
     * This method return from PaymentProvider to said that the order was paid.
     *
     * @param sessionObject
     * @param response
     * @param site
     * @return true: mean paid. false: This mean one of these status: new order
     * @throws Exception
     */
    boolean verifyResponseUrl (SessionObject sessionObject, Map response, Site site) throws Exception;
}
