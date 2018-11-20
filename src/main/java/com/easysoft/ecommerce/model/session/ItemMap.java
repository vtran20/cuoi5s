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
//@XStreamAlias("Item")
public class ItemMap extends HashMap {

    private static final long serialVersionUID = 1L;

    public static final String ITEM_ID_KEY = "ITEM_ID";
    public static final String ITEM_VARIANT_ID_KEY = "ITEM_VARIANT_ID";
    public static final String NAME_KEY = "NAME";
    public static final String COLOR_KEY = "COLOR";
    public static final String SIZE_KEY = "SIZE";
    public static final String QUANTITY_KEY = "QUANTITY";
    /*price of item*/
    public static final String PRICE_KEY = "PRICE";
    /*discount price of variant*/
    public static final String PRICE_VARIANT_DISCOUNT_KEY = "PRICE_VARIANT_DISCOUNT";
    /*real price of item, we base on this price for calculating discount*/
    public static final String FINAL_PRICE_ITEM_KEY = "FINAL_PRICE_ITEM";
    /*Store discount price for each item base on promo code*/
    public static final String PRICE_ITEM_PROMO_DISCOUNT_KEY = "PRICE_ITEM_PROMO_DISCOUNT";
    public static final String MODEL_NUMBER_KEY = "MODEL_NUMBER";
    public static final String IMAGE_URL_KEY = "IMAGE_URL";
    public static final String ITEM_URI_KEY = "ITEM_URI";
    public static final String GIFT_BOX_KEY = "GIFT_BOX";
    public static final String WEIGHT_KEY = "WEIGHT";

    public Long getId() {
        return (Long) this.get(ITEM_ID_KEY);
    }

    public String getName() {
        return (String) this.get(NAME_KEY);
    }

    public Integer getQuantity() {
        return (Integer) this.get(QUANTITY_KEY);
    }

    public Long getPrice() {
        return getLong(PRICE_KEY);
    }

    public Long getPriceVariantDiscount() {
        return getLong(PRICE_VARIANT_DISCOUNT_KEY);
    }

    public Long getFinalPriceItem() {
        return getLong(FINAL_PRICE_ITEM_KEY);
    }

    public Long getPriceItemPromoDiscount() {
        return getLong(PRICE_ITEM_PROMO_DISCOUNT_KEY);
    }

    public String getModelNumber() {
        return (String) this.get(MODEL_NUMBER_KEY);
    }

    public String getImageUrl() {
        return (String) this.get(IMAGE_URL_KEY);
    }

    public String getItemUri() {
        return (String) this.get(ITEM_URI_KEY);
    }

    public String getGiftBox() {
        return (String) this.get(GIFT_BOX_KEY);
    }
    public Integer getWeight() {
        return (Integer) this.get(WEIGHT_KEY);
    }

    public void setId(Long value) {
        this.put(ITEM_ID_KEY, value);
    }

    public void setName(String value) {
        this.put(NAME_KEY, value);
    }

    public void setQuantity(Integer value) {
        this.put(QUANTITY_KEY, value);
    }

    public void setPrice(Long value) {
        this.put(PRICE_KEY, value);
    }

    public void setPriceDiscount(Long value) {
        this.put(PRICE_VARIANT_DISCOUNT_KEY, value);
    }

    public void setFinalPriceItem(Long value) {
        this.put(FINAL_PRICE_ITEM_KEY, value);
    }

    public void setPriceItemPromoDiscount(Long value) {
        this.put(PRICE_ITEM_PROMO_DISCOUNT_KEY, value);
    }

    public void setModelNumber(String value) {
        this.put(MODEL_NUMBER_KEY, value);
    }

    public void setImageUrl(String value) {
        this.put(IMAGE_URL_KEY, value);
    }

    public void setItemUri(String value) {
        this.put(ITEM_URI_KEY, value);
    }

    public void setGiftBox(String value) {
        this.put(GIFT_BOX_KEY, value);
    }
    public void setWeight(Integer value) {
        this.put(WEIGHT_KEY, value);
    }

    public Long getProductVariantId() {
        return (Long) this.get(ITEM_VARIANT_ID_KEY);
    }

    public void setProductVariantId(Long value) {
        this.put(ITEM_VARIANT_ID_KEY, value);
    }

    public String getColor() {
        return (String) this.get(COLOR_KEY);
    }

    public void setColor(String value) {
        this.put(COLOR_KEY, value);
    }

    public String getSize() {
        return (String) this.get(SIZE_KEY);
    }

    public void setSize(String value) {
        this.put(SIZE_KEY, value);
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
        if (this.get(key) == null)
            return 0;

        if (this.get(key) instanceof Integer) {
            return (Integer) this.get(key);
        } else if (this.get(key) instanceof String) {
            return Integer.valueOf((String) this.get(key));
        } else {
            return new Integer((String) this.get(key));
        }
    }

    public void set(String key, Object value) {
        this.put(key, value);
    }

    public boolean isChildItem() {
        return this.getLong("PARENT_PRODUCT_ID") > 0;
    }
}
