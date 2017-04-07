package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.ShippingType;

import java.util.List;

public interface ShippingTypeDao extends GenericDao<ShippingType, Long> {

    List<ShippingType> getAllShippingTypes(String active);
}