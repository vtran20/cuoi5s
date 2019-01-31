package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.NailEmployeeServiceDao;
import com.easysoft.ecommerce.model.NailCustomerService;
import com.easysoft.ecommerce.model.NailEmployeeService;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class NailEmployeeServiceDaoImpl extends GenericDaoImpl<NailEmployeeService, Long> implements NailEmployeeServiceDao {

    @Override
    public List<NailEmployeeService> getEmployeeServicesByDate(Date startDate, Date endDate, Long storeId) throws Exception  {
        return getSessionFactory().getCurrentSession()
                .createQuery("SELECT es FROM " + getPersistentClass().getName() + " es join es.nailCustomerService cs join cs.nailCustomer c join c.store s where cs.serviceDate between :startDate and :endDate and s.id = :storeId ORDER BY cs.serviceDate asc")
                .setParameter("startDate", startDate, new TimestampType())
                .setParameter("endDate", endDate, new TimestampType())
                .setParameter("storeId", storeId).list();
    }

    @Override
    public NailEmployeeService getEmployeeService(Long employeeId, Long employeeServiceId) throws Exception {
        return (NailEmployeeService) getSessionFactory().getCurrentSession()
                .createQuery("SELECT es FROM " + getPersistentClass().getName() + " es where es.nailEmployee.id = :employeeId and es.id = :employeeServiceId")
                .setParameter("employeeId", employeeId)
                .setParameter("employeeServiceId",employeeServiceId)
                .uniqueResult();
    }

    @Override
    public List<NailEmployeeService> getEmployeeServices(Long customerServiceId) throws Exception {
        return (List<NailEmployeeService>) getSessionFactory().getCurrentSession()
                .createQuery("SELECT es FROM " + getPersistentClass().getName() + " es where es.nailCustomerService.id = :customerServiceId")
                .setParameter("customerServiceId",customerServiceId)
                .uniqueResult();
    }

}