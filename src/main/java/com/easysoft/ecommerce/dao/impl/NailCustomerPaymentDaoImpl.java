package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.NailCustomerPaymentDao;
import com.easysoft.ecommerce.model.NailCustomerPayment;
import com.easysoft.ecommerce.model.NailCustomerService;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class NailCustomerPaymentDaoImpl extends GenericDaoImpl<NailCustomerPayment, Long> implements NailCustomerPaymentDao {
    @Override
    public List<NailCustomerPayment> getCustomerPaymentsByDate(Date startDate, Date endDate, Long storeId) throws Exception {
        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT cs FROM " + getPersistentClass().getName() + " cs where cs.createdDate between :startDate and :endDate and cs.store.id = :storeId")
                .setParameter("startDate", startDate, new TimestampType())
                .setParameter("endDate", endDate, new TimestampType())
                .setParameter("storeId", storeId).list();
    }

}
