package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.NailCustomerService;

import java.util.Date;
import java.util.List;

public interface NailCustomerServiceDao extends GenericDao<NailCustomerService, Long> {

    public List<NailCustomerService> getCustomerServicesByDate (Date startDate, Date endDate, Long storeId) throws Exception;
    public List<NailCustomerService> getCustomerServicesByDate(Date startDate, Date endDate, Long customerId, Long storeId) throws Exception;
    public NailCustomerService getCustomerService (Long customerId, Long customerServiceId) throws Exception;
}