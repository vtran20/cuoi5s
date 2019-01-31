package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.NailCustomerAppointmentDao;
import com.easysoft.ecommerce.model.NailCustomerAppointment;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class NailCustomerAppointmentDaoImpl extends GenericDaoImpl<NailCustomerAppointment, Long> implements NailCustomerAppointmentDao {

    @Override
    public List<NailCustomerAppointment> getCustomerAppointmentsByDate(Date startDateObj, Date endDateObj, Long storeId) throws Exception {

        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT cs FROM " + getPersistentClass().getName() + " cs join cs.store s where cs.startTime between :startDate and :endDate and s.id = :storeId")
                .setParameter("startDate", startDateObj, new TimestampType())
                .setParameter("endDate", endDateObj, new TimestampType())
                .setParameter("storeId", storeId).list();
    }

    public NailCustomerAppointment getCustomerAppointment (Long customerId, Long customerAppointmentId) throws Exception {
        return (NailCustomerAppointment) getSessionFactory().getCurrentSession()
                .createQuery("SELECT cs FROM " + getPersistentClass().getName() + " cs where cs.nailCustomer.id = :customerId and cs.id = :customerAppointmentId")
                .setParameter("customerId", customerId)
                .setParameter("customerAppointmentId",customerAppointmentId)
                .uniqueResult();
    }
}