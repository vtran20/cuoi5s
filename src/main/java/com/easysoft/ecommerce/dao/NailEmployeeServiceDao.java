package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.NailCustomerService;
import com.easysoft.ecommerce.model.NailEmployeeService;
import org.hibernate.type.TimestampType;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface NailEmployeeServiceDao extends GenericDao<NailEmployeeService, Long> {

    public List<NailEmployeeService> getEmployeeServicesByDate(Date date, Long storeId) throws Exception;

    public NailEmployeeService getEmployeeService (Long employeeId, Long employeeServiceId) throws Exception ;

    public List<NailEmployeeService> getEmployeeServices(Long customerServiceId) throws Exception;

}