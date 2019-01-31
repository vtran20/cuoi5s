package com.easysoft.ecommerce.service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public interface NailManagementService {
    public Map<String, Object> addNailCustomerService (Map inputData, Long storeId, Date currentDate);
    public Map<String, Object> makeAppointment (Map inputData, Long storeId) throws Exception;

    public Map<String, Object> checkInAppointmentCustomer(Map inputData, Long storeId, Date currentDate) throws Exception;

    public void deleteCustomerService(long customerId, long customerServiceId) throws Exception;

    public Map submitCustomerPayment(Long id, Long storeId, Map inputData, Date currentDate) throws Exception;
}

