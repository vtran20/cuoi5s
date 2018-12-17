package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.model.NailCustomer;
import com.easysoft.ecommerce.service.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by vutran on 12/16/2018.
 */
public class BaseServiceImpl {
    @Autowired
    protected ServiceLocator serviceLocator;

    protected NailCustomer findCustomer (Long id, String email, String phone, Long storeId) {
        NailCustomer customer = null;
        if (id != null && id > 0) {
            customer = serviceLocator.getNailCustomerDao().findByIdByStore(id, storeId);
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (customer == null && StringUtils.isNotEmpty(email) && emailValidator.isValid(email)) {
            customer = serviceLocator.getNailCustomerDao().findUniqueByStore("email", email, storeId);
        }
        if (customer == null && StringUtils.isNotEmpty(phone)){
            customer = serviceLocator.getNailCustomerDao().findUniqueByStore("phone", phone, storeId);
        }
        return customer;
    }

}
