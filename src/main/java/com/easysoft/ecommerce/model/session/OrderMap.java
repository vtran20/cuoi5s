package com.easysoft.ecommerce.model.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * 7. This discount is used if the client has promo code for discounting. Ex: 10%, 20% for orders > 1.000.000 VND
 *
 * User: vtran
 * Date: Aug 18, 2010
 * Time: 4:23:36 PM
 * To change this template use File | Settings | File Templates.
 */
//@XStreamAlias("Order")
public class OrderMap extends HashMap {

    private static final long serialVersionUID = 1L;

    public static final String ITEMS_KEY = "ITEMS";
    /*This is a total price of items only*/
    public static final String SUB_TOTAL_PRICE_KEY = "SUB_TOTAL_PRICE";
    public static final String ORIGINAL_SUB_TOTAL_KEY = "ORIGINAL_SUB_TOTAL";
    public static final String SUB_TOTAL_PRICE_DISCOUNT_KEY = "SUB_TOTAL_PRICE_DISCOUNT";
    /*This is a total price of items, shipping, tax...*/
    public static final String TOTAL_PRICE_KEY = "TOTAL_PRICE";
    public static final String TAX_PRICE_KEY = "TAX_PRICE";
    public static final String SHIPPING_PRICE_KEY = "SHIPPING_PRICE";
    public static final String SHIPPING_PRICE_DISCOUNT_KEY = "SHIPPING_PRICE_DISCOUNT";
    public static final String SHIPPING_METHOD_ID_KEY = "SHIPPING_METHOD_ID";
    public static final String PAYMENT_METHOD_ID_KEY = "PAYMENT_METHOD_ID";
    public static final String PROMO_CODE_KEY = "PROMO_CODE";
    public static final String DEFAULT_PROMO_CODE_KEY = "DEFAULT_PROMO_CODE";

    public void addItem (ItemMap item) {
        this.getItems().add(item);
    }

    public void removeItem (int index) {
        if (this.getItems() != null && this.getItems().size() > index) {
            this.getItems().remove(index);
        }
    }

    public List<ItemMap> getItems() {
        if (this.get(ITEMS_KEY) == null) {
            List <ItemMap>items = new ArrayList<ItemMap>();
            this.put(ITEMS_KEY, items);
        }
        return (List<ItemMap>) this.get(ITEMS_KEY);
    }

    public void setItems(List<ItemMap> items) {
        this.put(ITEMS_KEY, items);
    }

    public Long getSubPriceTotal() {
        return getLong(SUB_TOTAL_PRICE_KEY);
    }

    public void setSubPriceTotal(Long value) {
        this.put(SUB_TOTAL_PRICE_KEY, value);
    }

    public Long getSubPriceDiscountTotal() {
        return getLong(SUB_TOTAL_PRICE_DISCOUNT_KEY);
    }

    public void setSubPriceDiscountTotal(Long value) {
        this.put(SUB_TOTAL_PRICE_DISCOUNT_KEY, value);
    }

    public Long getTotalPrice() {
        return getLong(TOTAL_PRICE_KEY);
    }

    public void setTotalPrice(Long value) {
        this.put(TOTAL_PRICE_KEY, value);
    }
    public Long getOriginalSubTotal() {
        return getLong(ORIGINAL_SUB_TOTAL_KEY);
    }

    public void setOriginalSubTotal(Long value) {
        this.put(ORIGINAL_SUB_TOTAL_KEY, value);
    }

    public Long getTaxPrice() {
        return getLong(TAX_PRICE_KEY);
    }

    public void setTaxPrice(Long value) {
        this.put(TAX_PRICE_KEY, value);
    }

    public Long getShippingPrice() {
        return getLong(SHIPPING_PRICE_KEY);
    }

    public void setShippingPrice(Long value) {
        this.put(SHIPPING_PRICE_KEY, value);
    }

    public Long getShippingDiscountPrice() {
        return getLong(SHIPPING_PRICE_DISCOUNT_KEY);
    }

    public void setShippingDiscountPrice(Long value) {
        this.put(SHIPPING_PRICE_DISCOUNT_KEY, value);
    }

    public Long getShippingMethod() {
        return this.getLong(SHIPPING_METHOD_ID_KEY);
    }

    public void setShippingMethod(Long value) {
        this.put(SHIPPING_METHOD_ID_KEY, value);
    }

    public Long getPaymentMethod() {
        return getLong(PAYMENT_METHOD_ID_KEY);
    }

    public void setPaymentMethod(Long value) {
        this.put(PAYMENT_METHOD_ID_KEY, value);
    }

    public String getPromoCode() {
        return (String) this.get(PROMO_CODE_KEY);
    }

    public void setPromoCode(String value) {
        this.put(PROMO_CODE_KEY, value);
    }
    public String getDefaultPromoCode() {
        return (String) this.get(DEFAULT_PROMO_CODE_KEY);
    }

    public void setDefaultPromoCode(String value) {
        this.put(DEFAULT_PROMO_CODE_KEY, value);
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

    public void set(String key, Object value) {
        this.put(key, value);
    }

    public List<ItemMap> getParentItems() {
        List<ItemMap> items = (List<ItemMap>) this.get(ITEMS_KEY);
        List<ItemMap> result = new ArrayList<ItemMap>();

        for (ItemMap item : items) {
            if (!item.isChildItem()) {
                result.add(item);
            }
        }
        return result;
    }
    public List<ItemMap> getChildrentItems (Long parentProductId, Integer relatedTypeId, Long siteId) {
        List<ItemMap> result = new ArrayList<ItemMap>();
        if (siteId > 0) {
            List<ItemMap> items = (List<ItemMap>) this.get(ITEMS_KEY);
            for (ItemMap item: items) {
                if (item.getLong("SITE_ID").equals(siteId) && relatedTypeId!= null && relatedTypeId.equals(item.getInteger("RELATED_TYPE_ID")) && parentProductId != null && parentProductId.equals(item.getLong("PARENT_PRODUCT_ID"))) {
                    result.add(item);
                }
            }
        } else {
            //TODO: support for customer sites later
        }
        return result;
    }

}
