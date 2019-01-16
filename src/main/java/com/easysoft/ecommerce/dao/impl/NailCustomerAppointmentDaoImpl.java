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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDateObj);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        calendar = Calendar.getInstance();
        calendar.setTime(endDateObj);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date endDate = calendar.getTime();

        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT cs FROM " + getPersistentClass().getName() + " cs join cs.store s where cs.startTime between :startDate and :endDate and s.id = :storeId")
                .setParameter("startDate", startDate, new TimestampType())
                .setParameter("endDate", endDate, new TimestampType())
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