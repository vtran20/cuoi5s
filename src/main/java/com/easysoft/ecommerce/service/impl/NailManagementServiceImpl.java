package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.model.helper.ServiceStatus;
import com.easysoft.ecommerce.service.NailManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class NailManagementServiceImpl extends BaseServiceImpl implements NailManagementService  {

    private static final Logger LOGGER = LoggerFactory.getLogger(NailManagementServiceImpl.class);

    @Autowired
    private NailStoreDao nailStoreDao;

    @Autowired
    private NailCustomerDao nailCustomerDao;

    @Autowired
    private NailServiceDao nailServiceDao;

    @Autowired
    private NailEmployeeDao nailEmployeeDao;

    @Autowired
    private NailEmployeeServiceDao nailEmployeeServiceDao;

    @Autowired
    private NailCustomerServiceDao nailCustomerServiceDao;

    @Override
    public Map<String, Object> addNailCustomerService(Map inputData, Long storeId) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<NailCustomerService> customerServices = new ArrayList<NailCustomerService>();

        String email = inputData.get("email") != null ? (String) inputData.get("email") : "";
        String phone = inputData.get("phone") != null ? (String) inputData.get("phone") : "";

        Date serviceDate = new Date();
        String status = ServiceStatus.WAITING.toString();
        NailCustomer customer = findCustomer(null, email, phone, storeId);
        if (customer == null){
            customer = new NailCustomer();
            customer.setFirstName(inputData.get("firstName") != null ? (String) inputData.get("firstName") : "");
            customer.setLastName(inputData.get("lastName") != null ? (String) inputData.get("lastName") : "");
            customer.setEmail(email);
            customer.setPhone(phone);

            NailStore nailStore = serviceLocator.getNailStoreDao().findById(storeId);
            customer.setStore(nailStore);
            customer.setActive("Y");
            serviceLocator.getNailCustomerDao().persist(customer);
        }
        //Set checkin to current date for both old and new customer.
        customer.setCheckIn(new Date());

        List services = inputData.get("services") != null? (List) inputData.get("services") : null;
        if (services != null && services.size() > 0) {
            for (Object obj : services) {
                if (obj instanceof String) {
                    String i = (String) obj;
                    Long serviceId = new Long(i);
                    NailService service = serviceLocator.getNailServiceDao().findById(serviceId);
                    NailCustomerService customerService = new NailCustomerService();
                    customerService.setNailService(service);
                    customerService.setNailCustomer(customer);
                    if (service != null) {
                        customerService.setPrice(service.getPrice());
                    }
                    customerService.setStatus(status);
                    customerService.setServiceDate(serviceDate);
                    serviceLocator.getNailCustomerServiceDao().persist(customerService);
                    customerServices.add(customerService);
                }
            }
        }
        result.put("customer", customer);
        result.put("customerServices", customerServices);

        return result;
    }

    @Override
    public Map<String, Object> checkInAppointmentCustomer(Map inputData, Long storeId) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        List<NailCustomerService> customerServices = new ArrayList<NailCustomerService>();

        Long customerId = inputData.get("id") != null ?  new Long(inputData.get("id")+"") : null;
        String email = inputData.get("email") != null ? (String) inputData.get("email") : "";
        String phone = inputData.get("phone") != null ? (String) inputData.get("phone") : "";

        NailCustomer customer = findCustomer(customerId, email, phone, storeId);
        if (customer != null){
            customer.setStatus(ServiceStatus.SCHEDULED.toString());
            customer.setCheckIn(new Date()); //this will be used when reload the POS. Customer may be removed all customer services
            List csIds = inputData.get("customerServices") != null? (List) inputData.get("customerServices") : null;
            if (csIds == null || csIds.size() == 0) {
                throw new Exception("Cannot find your appointment");
            } else {
                for (Object obj : csIds) {
                    if (obj instanceof Integer) {
                        Integer i = (Integer) obj;
                        Long csId = new Long(i+"");
                        NailCustomerService customerService = serviceLocator.getNailCustomerServiceDao().findById(csId);
                        if (customerService != null) {
                            serviceLocator.getNailCustomerServiceDao().merge(customerService);
                            customerServices.add(customerService);
                        }
                    }
                }
            }
            result.put("customer", customer);
            result.put("customerServices", customerServices);

        } else {
            throw new Exception("Cannot find your appointment");
        }


        return result;
    }

    @Override
    public void deleteCustomerService(long customerId, long customerServiceId) throws Exception {
        NailCustomerService customerService = null;
        try {
            List<NailEmployeeService> employeeServices = serviceLocator.getNailEmployeeServiceDao().getEmployeeServices(customerServiceId);
            if (employeeServices != null) {
                for (NailEmployeeService es: employeeServices) {
                    serviceLocator.getNailEmployeeServiceDao().remove(es);
                }
            }
            customerService = serviceLocator.getNailCustomerServiceDao().getCustomerService(customerId, customerServiceId);
            if (customerService == null) {
                System.out.println("Unable to delete. NailCustomerService with id " + customerId + " not found");
                throw new Exception("CustomerService is not found");
            }
            serviceLocator.getNailCustomerServiceDao().remove(customerService);
        } catch (Exception e) {
            throw new Exception("CustomerService is not found");
        }

    }

}
