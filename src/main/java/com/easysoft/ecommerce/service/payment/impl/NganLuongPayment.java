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
import java.util.HashMap;
import java.util.Map;

/**
 * Build url for submitting order to Ngan Luong Payment Gateway
 * Get data return from Ngan Luong and verify.
 * <p/>
 * URL checkout of Payment Provider
 * private String nganluong_url = "http://sandbox.nganluong.vn/checkout.php";
 * <p/>
 * Merchant Id: This id is provided by Ngan Luong
 * private String merchant_id = "126";
 * <p/>
 * Secure password: This password is provided by Ngan Luong
 * private String secure_pass = "cda3b788a3333b42";
 * <p/>
 * private static String algorithmName = "MD5";
 * <p/>
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class NganLuongPayment extends PaymentSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(NganLuongPayment.class);


    /**
     * Mehotd build url and submit data to Ngan Luong and place a payment.
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
        Long providerId = sessionObject.getOrder().getPaymentMethod();
        Map<String, String> params = new HashMap<String, String> ();

        params.put("merchant_site_code", PaymentUtil.getProviderParams(providerId, site.getId()).get("merchant_id"));
        params.put("return_url", "http://" + site.getDomain() + "/checkout/receipt.html");
        params.put("receiver", PaymentUtil.getProviderParams(providerId, site.getId()).get("business_email"));
        params.put("transaction_info", "Thanh toan cho website http://" + site.getDomain());
        params.put("order_code", site.getSiteCode() + sessionObject.getOrder().get("ORDER_ID"));
        params.put("price", String.valueOf(sessionObject.getOrder().getTotalPrice()));

        // Tao public key
        String securePass = PaymentUtil.getProviderParams(providerId, site.getId()).get("secure_pass");

        String secure_code =params.get("merchant_site_code")
                    +" "+URLEncoder.encode(params.get("return_url")).toLowerCase()
                    +" "+params.get("receiver")
                    +" "+params.get("transaction_info")
                    +" "+params.get("order_code")
                    +" "+params.get("price")
                    +" "+securePass;

        // Create checksum
        String checksum = getMD5Hash(secure_code);
        params.put("secure_code",checksum);

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
                url_params = key + "=" + val;
            } else {
                url_params += "&" + key + "=" + val;
            }
        }

        return redirect_url + url_params;
    }

    /**
     * Check returned data.
     *
     * @param params contain params that return from payment gateway
     * @return true if is correct
     */
    public boolean verifyResponseUrl(SessionObject sessionObject, Map params, Site site) throws Exception {

//        // decode and check
        Long providerId = sessionObject.getOrder().getPaymentMethod();
        String securePass = PaymentUtil.getProviderParams(providerId, site.getId()).get("secure_pass");
        String secureCode =params.get("merchant_site_code")
                +" "+ URLEncoder.encode((String) params.get("return_url")).toLowerCase()
                +" "+params.get("receiver")
                +" "+params.get("transaction_info")
                +" "+params.get("order_code")
                +" "+params.get("price")
                +" "+securePass;
        String checksum = getMD5Hash(secureCode);

        String checksumFromUrl = String.valueOf(params.get("checksum"));

        return checksumFromUrl != null && checksumFromUrl.equals(checksum);
    }
}
