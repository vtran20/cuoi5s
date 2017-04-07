package com.easysoft.ecommerce.service.payment;

import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PaymentSupport implements Payment {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSupport.class);

        /**
     * Encode string
     *
     * @param messages input string
     * @return output string is encode.
     * @throws Exception
     */
    protected String getMD5Hash(String messages) throws Exception {
        if (messages == null) throw new NullPointerException();

        StringBuffer result = null;
        byte[] data = messages.getBytes();
        try {
//            Long siteId = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getId();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(data);
            byte[] msgDigest = md.digest();
            result = new StringBuffer();
            for (byte aMsgDigest : msgDigest) {
                String hex = Integer.toHexString(0xff & aMsgDigest);
                if (hex.length() == 1) result.append('0');
                result.append(hex);
            }
        }
        catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.toString());
        }
        return result.toString();
    }


    protected String generateCheckSum (String orderId, String totalPrice, String merchantId, String receiver, String secureCode) throws Exception {
        return getMD5Hash(orderId+totalPrice+merchantId+receiver+secureCode).toUpperCase();
    }

}
