package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.NailCustomer;
import com.easysoft.ecommerce.model.NailCustomerService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface NailCustomerDao extends GenericDao<NailCustomer, Long> {

    List<NailCustomer> findSuggestionCustomers(String phone, String email, Long storeId);
    List<NailCustomer> getCheckedInCustomersByDate(Date date, Long storeId) throws Exception;
}