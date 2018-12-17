package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.model.helper.ServiceStatus;
import com.easysoft.ecommerce.service.NailManagementService;
import com.easysoft.ecommerce.service.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
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

    @Autowired
    private NailCustomerAppointmentDao nailCustomerAppointmentDao;


    @Override
    public Map<String, Object> addNailCustomerService(Map inputData, Long storeId) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<NailCustomerService> customerServices = new ArrayList<NailCustomerService>();

        String email = inputData.get("email") != null ? (String) inputData.get("email") : "";
        String phone = inputData.get("phone") != null ? (String) inputData.get("phone") : "";

        Integer tempId = inputData.get("customerAppointmentId") != null ? (Integer) inputData.get("customerAppointmentId") : 0;
        Long customerAppointmentId = new Long(tempId.toString());
        NailCustomerAppointment appointment = serviceLocator.getNailCustomerAppointmentDao().findById(customerAppointmentId);

        Date serviceDate = new Date();
        String status = ServiceStatus.CHECKED_IN.toString();
        if (appointment != null) {
            status = appointment.getStatus();
            serviceDate = appointment.getServiceDate();
            //Update the appointment to CHECKED_IN status
            appointment.setStatus(ServiceStatus.CHECKED_IN.toString());
            serviceLocator.getNailCustomerAppointmentDao().merge(appointment);
        }
        NailCustomer customer = findCustomer(null, email, phone, storeId);
        if (customer == null){
            customer = new NailCustomer();
            customer.setFirstName(inputData.get("firstName") != null? (String) inputData.get("firstName") :"");
            customer.setLastName(inputData.get("lastName") != null? (String) inputData.get("lastName") :"");
            customer.setEmail(email);
            customer.setPhone(phone);

            NailStore nailStore = serviceLocator.getNailStoreDao().findById(storeId);
            customer.setStore(nailStore);
            customer.setActive("Y");
            serviceLocator.getNailCustomerDao().persist(customer);
        }

        List services = inputData.get("services") != null? (List) inputData.get("services") : null;
        if (services == null || services.size() == 0) {
            //add a empty customer service.
            NailCustomerService customerService = new NailCustomerService();
            customerService.setNailCustomer(customer);
            customerService.setStatus(status);
            customerService.setServiceDate(serviceDate);
            customerService.setCheckIn(new Date());
            serviceLocator.getNailCustomerServiceDao().persist(customerService);
            customerServices.add(customerService);
        } else {
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
                    customerService.setCheckIn(new Date());
                    serviceLocator.getNailCustomerServiceDao().persist(customerService);
                    customerServices.add(customerService);
                }
            }
        }
        result.put("customer", customer);
        result.put("customerServices", customerServices);

        return result;
    }

}
