package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.NailCustomerServiceDao;
import com.easysoft.ecommerce.model.NailCustomerService;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class NailCustomerServiceDaoImpl extends GenericDaoImpl<NailCustomerService, Long> implements NailCustomerServiceDao {

    @Override
    public List<NailCustomerService> getCustomerServicesByDate(Date date, Long storeId) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date endDate = calendar.getTime();

        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT cs FROM " + getPersistentClass().getName() + " cs join cs.nailCustomer c join c.store s where cs.serviceDate between :startDate and :endDate and s.id = :storeId ORDER BY cs.serviceDate asc")
                .setParameter("startDate", startDate, new TimestampType())
                .setParameter("endDate", endDate, new TimestampType())
                .setParameter("storeId", storeId).list();
    }

    @Override
    public List<NailCustomerService> getCustomerServicesByDate(Date date, Long customerId, Long storeId) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date endDate = calendar.getTime();

        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT cs FROM " + getPersistentClass().getName() + " cs join cs.nailCustomer c join c.store s where cs.serviceDate between :startDate and :endDate and c.id = :customerId and s.id = :storeId ORDER BY cs.serviceDate asc")
                .setParameter("startDate", startDate, new TimestampType())
                .setParameter("endDate", endDate, new TimestampType())
                .setParameter("customerId", customerId)
                .setParameter("storeId", storeId).list();
    }

    public NailCustomerService getCustomerService (Long customerId, Long customerServiceId) throws Exception {
        return (NailCustomerService) getSessionFactory().getCurrentSession()
                .createQuery("SELECT cs FROM " + getPersistentClass().getName() + " cs where cs.nailCustomer.id = :customerId and cs.id = :customerServiceId")
                .setParameter("customerId", customerId)
                .setParameter("customerServiceId",customerServiceId)
                .uniqueResult();
    }
}