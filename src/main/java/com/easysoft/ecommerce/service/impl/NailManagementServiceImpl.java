package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.controller.exception.NailsException;
import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.model.helper.ServiceStatus;
import com.easysoft.ecommerce.service.NailManagementService;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import com.google.gson.Gson;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class NailManagementServiceImpl extends BaseServiceImpl implements NailManagementService  {

    private static final Logger LOGGER = LoggerFactory.getLogger(NailManagementServiceImpl.class);
    private static final int DEFAULT_SERVICE_DURATION = 60; //60 minutes
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
    public Map<String, Object> addNailCustomerService(Map inputData, Long storeId, Date currentDate) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<NailCustomerService> customerServices = new ArrayList<NailCustomerService>();

        String email = inputData.get("email") != null ? (String) inputData.get("email") : "";
        String phone = inputData.get("phone") != null ? (String) inputData.get("phone") : "";

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
            customer.setStatus(ServiceStatus.WAITING.toString());
            serviceLocator.getNailCustomerDao().persist(customer);
        } else {

        }
        //Set checkin to current date for both old and new customer.
        customer.setCheckIn(currentDate);

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
                    customerService.setServiceDate(currentDate);
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
    public Map<String, Object> makeAppointment(Map inputData, Long storeId) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Messages messages = new Messages();
        List<NailCustomerService> customerServices = new ArrayList<NailCustomerService>();
        List<NailEmployeeService> employeeServices = new ArrayList<NailEmployeeService>();

        String startDate = inputData.get("startDate") != null? (String) inputData.get("startDate") : null;
        Date serviceDate = null;
        Calendar appointmentCalendar = Calendar.getInstance();
        int appointmentDuration = 0;

        if (startDate != null) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            serviceDate = sdf.parse(startDate);
            appointmentCalendar.setTime(serviceDate);
            if (appointmentCalendar.before(Calendar.getInstance())) {
                messages.addError("Cannot make appointment in the past. Please select time for appointment");
                result.put("messages", messages);
                return result;
            }
        } else {
            messages.addError("Please select time for appointment");
            result.put("messages", messages);
            return result;
        }

        Long customerId = inputData.get("id") != null ?  new Long(inputData.get("id")+"") : null;
        String email = inputData.get("email") != null ? (String) inputData.get("email") : "";
        String phone = inputData.get("phone") != null ? (String) inputData.get("phone") : "";

        NailCustomer customer = findCustomer(customerId, email, phone, storeId);
        if (customer == null){
            customer = new NailCustomer();
            customer.setFirstName(inputData.get("firstName") != null ? (String) inputData.get("firstName") : "");
            customer.setLastName(inputData.get("lastName") != null ? (String) inputData.get("lastName") : "");
            customer.setEmail(email);
            customer.setPhone(phone);

            NailStore nailStore = serviceLocator.getNailStoreDao().findById(storeId);
            customer.setStore(nailStore);
            customer.setActive("Y");
            customer.setStatus(ServiceStatus.SCHEDULED.toString());
            serviceLocator.getNailCustomerDao().persist(customer);
        } else {
            customer.setStatus(ServiceStatus.SCHEDULED.toString());
            serviceLocator.getNailCustomerDao().merge(customer);
        }

        List <NailService> services = new ArrayList<NailService>();
        List serviceIds = inputData.get("services") != null? (List) inputData.get("services") : null;
        if (serviceIds != null && serviceIds.size() > 0) {
            for (Object obj : serviceIds) {
                if (obj instanceof String) {
                    String i = (String) obj;
                    Long serviceId = new Long(i);
                    NailService service = serviceLocator.getNailServiceDao().findById(serviceId);
                    services.add(service);
                    //calculate duration
                    if (service.getMinutes() <= 0) {
                        appointmentDuration += DEFAULT_SERVICE_DURATION; // assume this server will take 60 minutes.
                    } else {
                        appointmentDuration += service.getMinutes();
                    }
                }
            }
        } else {
            messages.addError("Please select services");
            result.put("messages", messages);
            return result;
        }

        //Add Appointment
        Long employeeId = inputData.get("employeeId") != null? new Long (inputData.get("employeeId")+"") : 0;
        NailEmployee employee = serviceLocator.getNailEmployeeDao().findByIdByStore(employeeId, storeId);
        NailCustomerAppointment customerAppointment = new NailCustomerAppointment();
        customerAppointment.setStartTime(serviceDate);
        appointmentCalendar.add(Calendar.MINUTE, appointmentDuration);
        customerAppointment.setEndTime(appointmentCalendar.getTime());
        if (inputData.get("note") != null) {
            customerAppointment.setCustomerNote(inputData.get("note")+"");
        }
        customerAppointment.setNailCustomer(customer);
        customerAppointment.setNailEmployee(employee);
        NailStore store = serviceLocator.getNailStoreDao().findById(storeId);
        customerAppointment.setStore(store);
        serviceLocator.getNailCustomerAppointmentDao().persist(customerAppointment);


        for (NailService service: services) {
            //Add customer services
            NailCustomerService customerService = new NailCustomerService();
            customerService.setNailService(service);
            customerService.setNailCustomer(customer);
            if (service != null) {
                customerService.setPrice(service.getPrice());
            }
            customerService.setServiceDate(serviceDate);
            customerService.setAppointment(customerAppointment);
            serviceLocator.getNailCustomerServiceDao().persist(customerService);
            customerServices.add(customerService);

            //Add employee services
            NailEmployeeService employeeService = new NailEmployeeService();
            employeeService.setNailEmployee(employee);
            employeeService.setNailCustomerService(customerService);
            employeeService.setServicePrice(customerService.getPrice());
            serviceLocator.getNailEmployeeServiceDao().persist(employeeService);
            employeeServices.add(employeeService);

        }
        if (WebUtil.isToday(customerAppointment.getStartTime())) {
            result.put("customer", customer);
            result.put("customerServices", customerServices);
            result.put("employeeServices", employeeServices);
            result.put("appointment", customerAppointment);
        } else {
            result.put("appointment", customerAppointment);
        }

        return result;
    }

    @Override
    public Messages makeAppointmentFromFrontEnd(Map inputData, Long storeId) throws Exception {
        Messages messages = new Messages();
        String startDate = inputData.get("selectedDate") != null? (String) inputData.get("selectedDate") : null;
        String startTime = inputData.get("selectedTime") != null? (String) inputData.get("selectedTime") : null;
        Date serviceDate = null;
        Calendar appointmentCalendar = Calendar.getInstance();
        int appointmentDuration = 0;

        if (startDate != null && startTime != null) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            serviceDate = sdf.parse(startDate + " " + startTime);
            appointmentCalendar.setTime(serviceDate);
            appointmentCalendar.set(Calendar.SECOND, 0);
            appointmentCalendar.set(Calendar.MILLISECOND, 0);
            if (appointmentCalendar.before(Calendar.getInstance())) {
                messages.addError("Cannot make appointment in the past. Please select time for appointment");
                return messages;
            }
        } else {
            messages.addError("Please select time for appointment");
            return messages;
        }

        String email = inputData.get("email") != null ? (String) inputData.get("email") : "";
        String phone = inputData.get("phone") != null ? (String) inputData.get("phone") : "";

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
            customer.setStatus(ServiceStatus.SCHEDULED.toString());
            serviceLocator.getNailCustomerDao().persist(customer);
        } else {
            customer.setStatus(ServiceStatus.SCHEDULED.toString());
            if (inputData.get("firstName") != null && !inputData.get("firstName").equals(customer.getFirstName())) customer.setFirstName((String) inputData.get("firstName"));
            if (inputData.get("lastName") != null && !inputData.get("lastName").equals(customer.getLastName())) customer.setLastName((String) inputData.get("lastName"));
            if (!email.equals(customer.getEmail())) customer.setEmail(email);
            if (!phone.equals(customer.getPhone())) customer.setPhone(phone);
            serviceLocator.getNailCustomerDao().merge(customer);
        }

        List <NailService> services = new ArrayList<NailService>();
        String[] serviceIds = inputData.get("selectedServiceId") != null? (String[]) inputData.get("selectedServiceId") : null;
        if (serviceIds != null && serviceIds.length > 0) {
            for (Object obj : serviceIds) {
                if (obj instanceof String) {
                    String i = (String) obj;
                    Long serviceId = new Long(i);
                    NailService service = serviceLocator.getNailServiceDao().findById(serviceId);
                    services.add(service);
                    //calculate duration
                    if (service.getMinutes() <= 0) {
                        appointmentDuration += DEFAULT_SERVICE_DURATION; // assume this server will take 60 minutes.
                    } else {
                        appointmentDuration += service.getMinutes();
                    }
                }
            }
        } else {
            messages.addError("Please select services");
            return messages;
        }

        //Add Appointment
        Long employeeId = inputData.get("selectedEmployeeId") != null? new Long (inputData.get("selectedEmployeeId")+"") : 0;
        NailEmployee employee = serviceLocator.getNailEmployeeDao().findByIdByStore(employeeId, storeId);
        NailCustomerAppointment customerAppointment = new NailCustomerAppointment();
        customerAppointment.setStartTime(serviceDate);
        appointmentCalendar.add(Calendar.MINUTE, appointmentDuration);
        customerAppointment.setEndTime(appointmentCalendar.getTime());
        if (inputData.get("message") != null) {
            customerAppointment.setCustomerNote(inputData.get("message")+"");
        }
        customerAppointment.setNailCustomer(customer);
        customerAppointment.setNailEmployee(employee);
        NailStore store = serviceLocator.getNailStoreDao().findById(storeId);
        customerAppointment.setStore(store);
        serviceLocator.getNailCustomerAppointmentDao().persist(customerAppointment);


        for (NailService service: services) {
            //Add customer services
            NailCustomerService customerService = new NailCustomerService();
            customerService.setNailService(service);
            customerService.setNailCustomer(customer);
            if (service != null) {
                customerService.setPrice(service.getPrice());
            }
            customerService.setServiceDate(serviceDate);
            customerService.setAppointment(customerAppointment);
            serviceLocator.getNailCustomerServiceDao().persist(customerService);

            //Add employee services
            if (employee != null) {
                NailEmployeeService employeeService = new NailEmployeeService();
                employeeService.setNailEmployee(employee);
                employeeService.setNailCustomerService(customerService);
                employeeService.setServicePrice(customerService.getPrice());
                serviceLocator.getNailEmployeeServiceDao().persist(employeeService);
            }
        }
        messages.addInfo("Your appointment has been booked at <b>" + startDate + " " + startTime + "</b>");
        return messages;
    }

    @Override
    public Map<String, Object> checkInAppointmentCustomer(Map inputData, Long storeId, Date currentDate) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        List<NailCustomerService> customerServices = new ArrayList<NailCustomerService>();

        Long customerId = inputData.get("id") != null ?  new Long(inputData.get("id")+"") : null;
        String email = inputData.get("email") != null ? (String) inputData.get("email") : "";
        String phone = inputData.get("phone") != null ? (String) inputData.get("phone") : "";

        NailCustomer customer = findCustomer(customerId, email, phone, storeId);
        if (customer != null){
            if (!customer.getStatus().equals(ServiceStatus.SCHEDULED.toString())) {
                customer.setStatus(ServiceStatus.WAITING.toString());
            }
            customer.setCheckIn(currentDate); //this will be used when reload the POS. Customer may be removed all customer services
            List csIds = inputData.get("customerServices") != null? (List) inputData.get("customerServices") : null;
            if (csIds == null || csIds.size() == 0) {
                throw new NailsException("Cannot find your appointment");
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
            throw new NailsException("Cannot find your appointment");
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
                throw new NailsException("CustomerService is not found");
            }
            serviceLocator.getNailCustomerServiceDao().remove(customerService);
        } catch (Exception e) {
            throw new NailsException("CustomerService is not found");
        }

    }

    @Override
    public Map submitCustomerPayment(Long id, Long storeId, Map inputData, Date currentDate) throws Exception {
        Map  result = new HashMap();
        Messages messages = new Messages();
        Map currentCheckout = (Map) inputData.get("currentCheckout");
        //Validate data
        List <Integer> employeeIds = (List<Integer>) inputData.get("employeeIds");
        //Many employees work on the same customer
        //Update employeeService
        List <Map> employeeServices = (List<Map>) inputData.get("employeeServices");
        if (employeeIds != null && employeeIds.size() > 1) {
            //Validate to make sure tip enter correctly.
            Integer totalTip = getTotalTips(employeeServices);
            Integer globalTipPrice = currentCheckout.get("tipPrice") != null ? (Integer) currentCheckout.get("tipPrice") : 0;
            if (!totalTip.equals(globalTipPrice)) {
                messages.addError("Tips of the employees don't match with total tip.");
                result.put("messages", messages);
                return result;
            }
        }

        //Insert Payment
        NailCustomer customer = this.serviceLocator.getNailCustomerDao().findByIdByStore(id, storeId);
        NailStore store = this.serviceLocator.getNailStoreDao().findById(storeId);
        NailCustomerPayment payment = new NailCustomerPayment();
        payment.setServicePrice(currentCheckout.get("servicePrice") != null? new Long(currentCheckout.get("servicePrice") + ""): 0);
        payment.setTaxPrice(currentCheckout.get("taxPrice") != null ? new Long(currentCheckout.get("taxPrice") + "") : 0);
        payment.setTipPrice(currentCheckout.get("tipPrice") != null ? new Long(currentCheckout.get("tipPrice") + "") : 0);
        payment.setTotalPrice(currentCheckout.get("totalPrice") != null ? new Long(currentCheckout.get("totalPrice") + "") : 0);
        payment.setCredit(currentCheckout.get("credit")+"");
        payment.setCash(currentCheckout.get("cash")+"");
        payment.setCheck(currentCheckout.get("check")+"");
        payment.setGiftcard(currentCheckout.get("giftcard")+"");
        payment.setNailCustomer(customer);
        payment.setStore(store);

        Gson gson = new Gson();
        String json = gson.toJson(inputData);
        payment.setServiceSession(json);
        payment.setCreatedDate(currentDate);
        this.serviceLocator.getNailCustomerPaymentDao().persist(payment);

        customer.setStatus(ServiceStatus.COMPLETED.toString());
        this.serviceLocator.getNailCustomerDao().merge(customer);

        if (employeeIds != null && employeeIds.size() > 1) {
            for (Map map : employeeServices) {
                Long employeeServiceId = new Long (map.get("id")+"");
                NailEmployeeService employeeService = serviceLocator.getNailEmployeeServiceDao().findById(employeeServiceId);
                employeeService.setServicePrice(map.get("servicePrice") != null ? new Long(map.get("servicePrice") + "") : 0);
                employeeService.setTipPrice(map.get("tipPrice") != null ? new Long(map.get("tipPrice") + "") : 0);
                employeeService.setNailCustomerPayment(payment);
                serviceLocator.getNailEmployeeServiceDao().merge(employeeService);
            }
        } else {
            boolean chargeTip = false;
            for (Map map : employeeServices) {
                Long employeeServiceId = new Long (map.get("id")+"");
                NailEmployeeService employeeService = serviceLocator.getNailEmployeeServiceDao().findById(employeeServiceId);
                employeeService.setServicePrice(map.get("servicePrice") != null ? new Long(map.get("servicePrice") + "") : 0);
                if (!chargeTip) {
                    employeeService.setTipPrice(currentCheckout.get("tipPrice") != null ? new Long(currentCheckout.get("tipPrice") + "") : 0);
                    chargeTip = true;
                }
                employeeService.setNailCustomerPayment(payment);
                serviceLocator.getNailEmployeeServiceDao().merge(employeeService);
            }

        }

        result.put("payment", payment);
        result.put("customer", customer);

        return result;
    }

    public static void main(String[] args) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = null;
        try {
            date = sdf.parse("2019-01-29T14:57:41-05:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(new DateTime(date).toLocalDate().equals(new LocalDate()));
        System.out.println(new DateTime(new Date()).toLocalDate().equals(new LocalDate()));


    }

    private Integer getTotalTips (List <Map>employeeServices) {
        Integer totalTip = 0;
        for (Map map : employeeServices) {
            totalTip += map.get("tipPrice") != null ? (Integer)map.get("tipPrice") : 0;
        }
        return totalTip;
    }
}
