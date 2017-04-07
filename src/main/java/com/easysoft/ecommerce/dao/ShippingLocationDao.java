package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.ShippingLocation;

public interface ShippingLocationDao extends GenericDao<ShippingLocation, Long> {

    ShippingLocation getShippingLocation (String from, String to, String shippingMethod);

}