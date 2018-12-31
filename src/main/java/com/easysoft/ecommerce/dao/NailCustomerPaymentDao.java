package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.NailCustomerPayment;
import com.easysoft.ecommerce.model.NailCustomerService;

import java.util.Date;
import java.util.List;

public interface NailCustomerPaymentDao extends GenericDao<NailCustomerPayment, Long> {
    public List<NailCustomerPayment> getCustomerPaymentsByDate (Date date, Long storeId) throws Exception;

}