package com.easysoft.ecommerce.service;

import java.util.Map;

public interface NailManagementService {
    public Map<String, Object> addNailCustomerService (Map inputData, Long storeId);

    public Map<String, Object> checkInAppointmentCustomer(Map inputData, Long storeId) throws Exception;

    public void deleteCustomerService(long customerId, long customerServiceId) throws Exception;

    public Map submitCustomerPayment(Long id, Long storeId, Map inputData) throws Exception;
}

