package com.easysoft.ecommerce.service.payment.impl;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.payment.PaymentSupport;
import com.easysoft.ecommerce.util.PaymentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Build url for submitting order to Bao Kim Payment Gateway
 * Get data return from Bao Kim and verify.
 *
 * URL checkout of Payment Provider
 * private String baokim_url = "http://sandbox.baokim.vn/payment/customize_payment/order";
 *
 * Merchant Id: This id is provided by Bao Kim
 * private String merchant_id = "126";
 *
 * Secure password: This password is provided by Bao Kim
 * private String secure_pass = "cda3b788a3333b42";
 * 
 * private static String algorithmName = "MD5";
 *
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaoKimPayment extends PaymentSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaoKimPayment.class);

    /**
     * Mehotd build url and submit data to Bao Kim and place a payment.
     * $order_id: Order id - SITE_CODE+ORDER_ID
     * $business: Email of the merchant account
     * $total_amount: Total ammount of order
     * $shipping_fee: Shipping fee.
     * $tax_fee: Tax fee
     * $order_description: Order description
     * $url_success: URL returned if the order is success.
     * $url_cancel: URL returned when the order is cancelled for some reason.
     * $url_detail: URL order detail.
     *
     * @return url of payment gateway.
     */
    @Override
    public String createRequestUrl(SessionObject sessionObject, Site site) throws Exception {
        //String order_id, String business, String total_amount, String shipping_fee, String tax_fee, String order_description, String url_success, String url_cancel, String url_detail
        Map <String, String>params = new HashMap<String, String>();
        Long providerId = sessionObject.getOrder().getPaymentMethod();
        params.put("merchant_id", PaymentUtil.getProviderParams(providerId, site.getId()).get("merchant_id"));
        params.put("order_id", site.getSiteCode()+sessionObject.getOrder().get("ORDER_ID"));
        params.put("business", PaymentUtil.getProviderParams(providerId, site.getId()).get("business_email"));
        params.put("total_amount", String.valueOf(sessionObject.getOrder().getTotalPrice()));
        if (sessionObject.getOrder().getShippingDiscountPrice() != null && sessionObject.getOrder().getShippingDiscountPrice() > 0) {
            params.put("shipping_fee", String.valueOf(sessionObject.getOrder().getShippingDiscountPrice()));
        } else {
            params.put("shipping_fee", String.valueOf(sessionObject.getOrder().getShippingPrice()));
        }
        params.put("tax_fee", String.valueOf(sessionObject.getOrder().getTaxPrice()));
        params.put("order_description", "Thanh toan cho website http://"+site.getDomain());
        params.put("url_success", "http://"+site.getDomain()+"/checkout/receipt.html");
        params.put("url_cancel", "http://"+site.getDomain());
        params.put("url_detail", "http://"+site.getDomain());

        //Sap xep mang thuoc tinh don hang theo key
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // Tao public key
        String str_combined = PaymentUtil.getProviderParams(providerId, site.getId()).get("secure_pass");
        for (String key : keys) {
            str_combined += params.get(key);
        }
        // Create checksum
        String checksum = getMD5Hash(str_combined).toUpperCase();

        // Check url_success whether have '?' or not, if not add this character to the string
        String redirect_url = PaymentUtil.getProviderParams(providerId, site.getId()).get("url");
        if (!redirect_url.contains("?")) {
            redirect_url += "?";
        } else if (redirect_url.substring(redirect_url.length() - 1, redirect_url.length()).equals("?") && !redirect_url.contains("&")) {
            // If url_success have '?' but not at the end, add & at the end
            redirect_url += "&";
        }

        // Create url
        String url_params = "";
        for (String key : params.keySet()) {
            String val = params.get(key);
            if (StringUtils.isEmpty(url_params)) {
                url_params = key + "=" + java.net.URLEncoder.encode(val, "UTF-8");
            } else {
                url_params += "&" + key + "=" + java.net.URLEncoder.encode(val, "UTF-8");
            }
        }
        url_params += "&checksum=" + checksum;

        return redirect_url + url_params;
    }

    /**
     * Check returned data.
     *
     * @param params contain params that return from payment getway
     * @return true if is correct
     */
    public boolean verifyResponseUrl(SessionObject so, Map params, Site site) throws Exception {
        // decode and check
//        Long providerId = so.getOrder().getPaymentMethod();

        // Tao public key
//        String securePass = PaymentUtil.getProviderParams(providerId).get("secure_pass");
//
//        String secureCode =params.get("merchant_id")
//                +" "+ URLEncoder.encode((String) params.get("url_success")).toLowerCase()
//                +" "+params.get("business")
//                +" "+params.get("order_description")
//                +" "+params.get("order_id")
//                +" "+params.get("total_amount")
//                +" "+securePass;
//
//        String checksum = getMD5Hash(secureCode);
//
//        String checksumFromUrl = String.valueOf(params.get("checksum"));
//
//        return checksumFromUrl != null && checksumFromUrl.equals(checksum);

        // Sort keys
        String[] keys = (String[]) params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // decode and check
        Long providerId = so.getOrder().getPaymentMethod();
        String str_combined = PaymentUtil.getProviderParams(providerId, site.getId()).get("secure_pass");
        String checksum = "";
        for (String key : keys) {
            if (!(key.equals("checksum") || key.equals("checksum")))
                str_combined += params.get(key);
            else
                checksum = params.get(key).toString();
        }

        String verify_checksum = "";
        try {
            verify_checksum = this.getMD5Hash(str_combined).toUpperCase();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return verify_checksum != null && verify_checksum.equalsIgnoreCase(checksum);
    }
}
