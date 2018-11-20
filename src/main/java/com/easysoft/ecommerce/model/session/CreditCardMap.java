package com.easysoft.ecommerce.model.session;

import java.util.HashMap;

/**
 * SessionObjectImpl implements SessionObject. This class will be used for:
 *
 * 1. Store all information of user like billing, delivery address, order, and other ones.
 * 2. The object will be serialize to database under SessionObject table
 * 3. To identify this object, we store USER_SESSION_ID at client site under cookie and also,
 *    it will be store in httpsession for using easily.
 * 4. When each user access the website, we will load the usersession object. If this is the first time, create the new one.
 * 5. We will save this object to database manually if we need.
 * 6. In case, the users close browser for any reasons, we will clean the object on memory when the session time out.
 *
 * User: vtran
 * Date: Aug 18, 2010
 * Time: 4:23:36 PM
 * To change this template use File | Settings | File Templates.
 */
//@XStreamAlias("CreditCard")
public class CreditCardMap extends HashMap <String, String> {

    private static final long serialVersionUID = 1L;

    public String getString(String key) {
        return this.get(key);
    }

    public void set(String key, String value) {
        this.put(key, value);
    }

}
