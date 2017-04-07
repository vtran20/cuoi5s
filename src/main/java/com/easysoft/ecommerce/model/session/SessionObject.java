package com.easysoft.ecommerce.model.session;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Transient;

/**
 * SessionObjectImpl implements SessionObject. This class will be used for:
 *
 * 1. Store all information of user like billing, delivery address, order, and other ones.
 * 2. The object will be serialize to database under SessionObject table
 * 3. To identify this object, we store USER_SESSION_ID at client site under cookie and also,
 *    it will be store in httpsession for using easily.
 * 4. When each user access the website, we will load the usersession object. If this is the first time, create the new one.
 *    The object will be cached (using L2 cache) to improve performance.
 * 5. We will save this object to database manually if we need.
 * 6. In case, the users close browser for any reasons, we will clean the object on memory when the session time out.
 *
 * Note: synchronized when add, remove this object
 *
 * User: Vu Tran
 * Date: Aug 18, 2010
 * Time: 4:22:57 PM
 * To change this template use File | Settings | File Templates.
 */

//@XStreamAlias("Session")
public class SessionObject extends HashMap {

    private static final long serialVersionUID = 1L;

    public static final String USER_SESSION_ID_KEY = "USER_SESSION_ID";
    public static final String USER_SESSION_SECURE_COOKIE_KEY = "USER_SESSION_SECURE_COOKIE";
    public static final String USER_NAME_KEY = "USER_NAME";
    public static final String USER_ID_KEY = "USER_ID";
    public static final String ADDRESSES_KEY = "ADDRESSES";
    public static final String ORDER_KEY = "ORDER";
    public static final String CREDIT_CARD_KEY = "CREDIT_CARD";

    /**
     * Returns the globally unique identifier associated with this user session.
     * In general, this is also the id that will stored within a user's
     * "USER_SESSION_ID" cookie so that they can be identified on
     * subsequent visits to the site.
     *
     * @return The unique identifier of the user session.
     */
    public SessionObject(String sessionObjectId) {
        put(USER_SESSION_ID_KEY, sessionObjectId);
        put(ADDRESSES_KEY, new AddressesMap ());
        put(ORDER_KEY, new OrderMap());
        put(CREDIT_CARD_KEY, new CreditCardMap());
    }

    public String getId() {
        return (String) get(USER_SESSION_ID_KEY);
    }

    public AddressesMap getAddresses() {
        return (AddressesMap) get(ADDRESSES_KEY);
    }

    public void setAddresses(AddressesMap addresses) {
        put(ADDRESSES_KEY, addresses);
    }

    public OrderMap getOrder() {
        return (OrderMap) get(ORDER_KEY);
    }

    public void setOrder(OrderMap order) {
        if (order != null) {
            put(ORDER_KEY, order);
        } else {
            put(ORDER_KEY, new OrderMap());
        }
    }

    public CreditCardMap getCreditCard() {
        return (CreditCardMap) get(CREDIT_CARD_KEY);
    }

    public void setCreditCard(CreditCardMap creditCard) {
        put(CREDIT_CARD_KEY, creditCard);
    }

    public String getSecureCookie() {
        return (String) get(USER_SESSION_SECURE_COOKIE_KEY);
    }

    public void setSecureCookie(String secureCookie) {
        put(USER_SESSION_SECURE_COOKIE_KEY, secureCookie);
    }
    public String getUserName() {
        return (String) get(USER_NAME_KEY);
    }

    public void setUserName(String userName) {
        put(USER_NAME_KEY, userName);
    }

    public Long getUserId() {
        return (Long) get(USER_ID_KEY);
    }

    public void setUserId(Long userId) {
        put(USER_ID_KEY, userId);
    }

    public String getString(String key) {
        return (String) this.get(key);
    }

    public Long getLong(String key) {
        if (this.get(key) == null)
            return 0l;

        if (this.get(key) instanceof Long) {
            return (Long) this.get(key);
        } else if (this.get(key) instanceof String) {
            return Long.valueOf((String) this.get(key));
        } else {
            return new Long((String) this.get(key));
        }
    }

    public Integer getInteger(String key) {
        return (Integer) this.get(key);
    }

    public void set(String key, Object value) {
        this.put(key, value);
    }

    /**
     * ***********transient properties declare here***************
     */
    @Transient
    public Long getShippingFee () {
        Long shippingPrice = this.getOrder().getShippingPrice();
        Long shippingPriceDiscount = this.getOrder().getShippingDiscountPrice();
        /*Shipping price > 0, Check discount for shipping fee*/
        if (shippingPrice > 0) {
            if (shippingPriceDiscount > 0) {
                if (shippingPrice <= shippingPriceDiscount) {
                    return shippingPriceDiscount;
                } else {
                    return shippingPrice;
                }
            } else {
                return shippingPrice;
            }
        } else {
            return 0l;
        }
    }

    @Transient
    public Long getItemsInCart() {
        Long itemsInCart = 0l;
        List<ItemMap> items = this.getOrder().getItems();
        if (items != null) {
            for (Iterator<ItemMap> iterator = items.iterator(); iterator.hasNext();) {
                ItemMap item = iterator.next();
                if (!item.isChildItem()) {
                    if (item.getQuantity() != null) {
                        itemsInCart += item.getQuantity();
                    } else {
                        iterator.remove();//Remove item doesn't have quality.
                    }
                }
            }
        }
        return itemsInCart;
    }

}
