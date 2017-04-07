package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ShippingTypeDao;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.ShippingType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShippingTypeDaoImpl extends GenericDaoImpl<ShippingType, Long> implements ShippingTypeDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<ShippingType> getAllShippingTypes(String active) {
        if ("Y".equals(active)) {
            return getSessionFactory().getCurrentSession()
                        .createQuery("SELECT c FROM ShippingType c where c.active = :active ORDER BY c.sequence")
                        .setParameter("active", active).setCacheable(true).list();
        } else {
            return getSessionFactory().getCurrentSession()
                        .createQuery("SELECT c FROM ShippingType ORDER BY c.sequence")
                        .setCacheable(true).list();
        }
    }

}