package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.NailEmployeeServiceDao;
import com.easysoft.ecommerce.model.NailEmployeeService;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class NailEmployeeServiceDaoImpl extends GenericDaoImpl<NailEmployeeService, Long> implements NailEmployeeServiceDao {

    @Override
    public List<NailEmployeeService> getEmployeeServicesByDate(Date date, Long storeId) throws Exception  {
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
                .createQuery("SELECT es FROM " + getPersistentClass().getName() + " es join es.nailCustomerService cs join cs.nailCustomer c join c.store s where cs.serviceDate between :startDate and :endDate and s.id = :storeId ORDER BY cs.serviceDate asc")
                .setParameter("startDate", startDate, new TimestampType())
                .setParameter("endDate", endDate, new TimestampType())
                .setParameter("storeId", storeId).list();
    }

}