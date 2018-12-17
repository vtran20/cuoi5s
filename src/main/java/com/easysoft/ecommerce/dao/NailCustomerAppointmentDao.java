package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.NailCustomerAppointment;
import com.easysoft.ecommerce.model.NailCustomerService;

import java.util.Date;
import java.util.List;

public interface NailCustomerAppointmentDao extends GenericDao<NailCustomerAppointment, Long> {

    public List<NailCustomerAppointment> getCustomerAppointmentsByDate(Date date, Long storeId) throws Exception;
    public NailCustomerAppointment getCustomerAppointment(Long customerId, Long customerAppointmentId) throws Exception;
}