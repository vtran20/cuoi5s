package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ShippingLocationDao;
import com.easysoft.ecommerce.model.ShippingLocation;
import org.springframework.stereotype.Repository;

@Repository
public class ShippingLocationDaoImpl extends GenericDaoImpl<ShippingLocation, Long> implements ShippingLocationDao {

    @SuppressWarnings("unchecked")
    @Override
    public ShippingLocation getShippingLocation(String from, String to, String shippingMethod) {
        return (ShippingLocation) getSessionFactory().getCurrentSession()
                .createQuery("SELECT c FROM ShippingLocation c where c.fromLocation = :from and c.toLocation = :to and c.shippingType.shippingTypeCode = :shippingMethod")
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("shippingMethod", shippingMethod)
                .uniqueResult();

    }
}