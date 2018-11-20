package com.easysoft.ecommerce.model.session;

import java.util.HashMap;

/**
 * AddressesMap will contain shipping address and billing address.
 *
 * User: Vu Tran
 * Date: Aug 18, 2010
 * Time: 4:23:36 PM
 * To change this template use File | Settings | File Templates.
 */
//@XStreamAlias("Addresses")
public class AddressesMap extends HashMap <String, AddressMap> {

    private static final long serialVersionUID = 1L;

    public static final String BILLING_ADDRESS_KEY = "BILLING_ADDRESS";
    public static final String SHIPPING_ADDRESS_KEY = "SHIPPING_ADDRESS";

    public AddressesMap () {
        this.put(BILLING_ADDRESS_KEY, new AddressMap());
        this.put(SHIPPING_ADDRESS_KEY, new AddressMap());
    }

    public AddressMap getBillingAddress() {
        return get(BILLING_ADDRESS_KEY);
    }

    public void setBillingAddress(AddressMap billingAddress) {
        this.put(BILLING_ADDRESS_KEY, billingAddress);
    }

    public AddressMap getShippingAddress() {
        return get(SHIPPING_ADDRESS_KEY);
    }

    public void setShippingAddress(AddressMap shippingAddress) {
        this.put(SHIPPING_ADDRESS_KEY, shippingAddress);
    }

}
