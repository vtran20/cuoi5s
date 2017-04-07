package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ShippingFeeDao;
import com.easysoft.ecommerce.model.ShippingFee;
import org.springframework.stereotype.Repository;

@Repository
public class ShippingFeeDaoImpl extends GenericDaoImpl<ShippingFee, Long> implements ShippingFeeDao {

    @SuppressWarnings("unchecked")
    @Override
    public ShippingFee getShippingFee(Integer weight, String shippingTypeCode) {
        return (ShippingFee) getSessionFactory().getCurrentSession()
                .createQuery("SELECT c FROM ShippingFee c where c.weightFrom <= :weight and :weight <= c.weightTo and c.shippingType.shippingTypeCode = :shippingTypeCode")
                .setParameter("weight", weight)
                .setParameter("shippingTypeCode", shippingTypeCode)
                .uniqueResult();
    }

}