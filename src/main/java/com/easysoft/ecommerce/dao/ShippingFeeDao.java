package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.ShippingFee;

import java.util.List;

public interface ShippingFeeDao extends GenericDao<ShippingFee, Long> {

    ShippingFee getShippingFee(Integer weight, String shippingTypeCode);

}