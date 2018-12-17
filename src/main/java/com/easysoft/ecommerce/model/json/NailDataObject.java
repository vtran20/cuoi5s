package com.easysoft.ecommerce.model.json;

import com.easysoft.ecommerce.model.*;

import java.util.*;

/**
 * Created by vutran on 11/20/2018.
 */
public class NailDataObject {
    private StoreJson storeInfo = new StoreJson();
    private Map<String, Object> services = new HashMap<String, Object>();
    private Map<String, Object> customers = new HashMap<String, Object>();
    private Map<String, Object> employees = new HashMap<String, Object>();
    private Map<String, Object> customerServices = new HashMap<String, Object>();
    private Map<String, Object> employeeServices = new HashMap<String, Object>();

    public StoreJson getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(NailStore nailStore) {
        if (nailStore == null) return;

        storeInfo.setId(nailStore.getId());
        storeInfo.setName(nailStore.getName());
        storeInfo.setActive(nailStore.getActive());
        storeInfo.setAddress_1(nailStore.getAddress_1());
        storeInfo.setAddress_2(nailStore.getAddress_2());
        storeInfo.setDistrict(nailStore.getDistrict());
        storeInfo.setCity(nailStore.getCity());
        storeInfo.setState(nailStore.getState());
        storeInfo.setZipCode(nailStore.getZipCode());
        storeInfo.setCountry(nailStore.getCountry());
        storeInfo.setPhone(nailStore.getPhone());
        storeInfo.setEmail(nailStore.getEmail());
    }

    public void setServices(List<NailService> services) {
        if (services == null) return;
        Map<String, NailServiceJson> nailServiceJsonMap = new HashMap<String, NailServiceJson>();
        Long[] allIds = new Long[services.size()];
        int index = 0;
        for (NailService service : services) {
            NailServiceJson nailServiceJson = new NailServiceJson();
            nailServiceJson.setId(service.getId());
            nailServiceJson.setActive(service.getActive());
            nailServiceJson.setName(service.getName());
            nailServiceJson.setPrice(service.getPrice());
            nailServiceJsonMap.put(service.getId().toString(), nailServiceJson);
            allIds[index++]= service.getId();
        }
        this.services.put("byId", nailServiceJsonMap);
        this.services.put("allIds", allIds);

    }

    public void setCustomerServices(List<NailCustomerService> customerServices) {
        if (customerServices == null) return;
        //for customers
        Map<String, NailCustomerJson> nailCustomerJsonMap = new HashMap<String, NailCustomerJson>();
        //for customerServices
        Map<String, NailCustomerServiceJson> nailCustomerServiceJsonMap = new HashMap<String, NailCustomerServiceJson>();

        int customerServiceIndex = 0;
        int customerIndex = 0;
        List<Long> allIdsCustomerServices = new ArrayList<Long>();
        List<Long> allIdsCustomers = new ArrayList<Long>();
        Map<Long, Set<Long>> servicesForCustomer = new HashMap<Long, Set<Long>>();
        Set<Long> sfc = null;
        for (NailCustomerService customerService : customerServices) {
            NailCustomerServiceJson nailCustomerServiceJson = new NailCustomerServiceJson();
            nailCustomerServiceJson.setId(customerService.getId());
            nailCustomerServiceJson.setServiceDate(customerService.getServiceDate());
            nailCustomerServiceJson.setStatus(customerService.getStatus());
            nailCustomerServiceJsonMap.put(customerService.getId().toString(), nailCustomerServiceJson);
            allIdsCustomerServices.add(customerService.getId());

            //adding customerService id to customers
            NailCustomer customer = customerService.getNailCustomer();
            if (servicesForCustomer.get(customer.getId()) == null) {
                sfc = new HashSet<Long>();
                servicesForCustomer.put(customer.getId(), sfc);
            } else {
                sfc = servicesForCustomer.get(customer.getId());
            }
            sfc.add(customerService.getId());

            //adding customers if it hasn't added
            if (nailCustomerJsonMap.get(customer.getId().toString()) == null) {
                NailCustomerJson nailCustomerJson = new NailCustomerJson();
                nailCustomerJson.setId(customer.getId());
                nailCustomerJson.setActive(customer.getActive());
                nailCustomerJson.setFirstName(customer.getFirstName());
                nailCustomerJson.setLastName(customer.getLastName());
                nailCustomerJson.setEmail(customer.getEmail());
                nailCustomerJson.setPhone(customer.getPhone());
                nailCustomerJson.setCustomerServices(sfc);
                nailCustomerJsonMap.put(customer.getId().toString(), nailCustomerJson);
                allIdsCustomers.add(customer.getId());
            }

        }


        this.customerServices.put("byId", nailCustomerServiceJsonMap);
        this.customerServices.put("allIds", allIdsCustomerServices);
        this.customers.put("byId", nailCustomerJsonMap);
        this.customers.put("allIds", allIdsCustomers);

    }

    public void setEmployeeServices(List<NailEmployee> employees, List<NailEmployeeService> employeeServices) {
        //for employees
        if (employees == null) return;
        Map<String, NailEmployeeJson> nailEmployeeJsonMap = new HashMap<String, NailEmployeeJson>();
        List <Long>allIds = new ArrayList<Long>();
        for (NailEmployee employee : employees) {
            NailEmployeeJson nailEmployeeJson = new NailEmployeeJson();
            nailEmployeeJson.setId(employee.getId());
            nailEmployeeJson.setActive(employee.getActive());
            nailEmployeeJson.setFirstName(employee.getFirstName());
            nailEmployeeJson.setLastName(employee.getLastName());
            nailEmployeeJson.setEmail(employee.getEmail());
            nailEmployeeJson.setPhone(employee.getPhone());
            nailEmployeeJson.setCheckIn(employee.getCheckIn());
            nailEmployeeJson.setEmployeeServices(new HashSet<Long>());
            nailEmployeeJsonMap.put(employee.getId().toString(), nailEmployeeJson);
            allIds.add(employee.getId());
        }
        this.employees.put("byId", nailEmployeeJsonMap);
        this.employees.put("allIds", allIds);

        //for employeeServices
        if (employeeServices == null) return;
        Map<String, NailEmployeeServiceJson> nailEmployeeServiceJsonMap = new HashMap<String, NailEmployeeServiceJson>();
        allIds = new ArrayList<Long>();
        for (NailEmployeeService employeeService : employeeServices) {
            NailEmployeeServiceJson nailEmployeeServiceJson = new NailEmployeeServiceJson();
            nailEmployeeServiceJson.setId(employeeService.getId());
            nailEmployeeServiceJson.setCreditPay(employeeService.getCreditPay());
            nailEmployeeServiceJson.setCashPay(employeeService.getCashPay());
            nailEmployeeServiceJson.setGiftPay(employeeService.getGiftPay());
            nailEmployeeServiceJson.setCheckPay(employeeService.getCheckPay());
            nailEmployeeServiceJson.setTipPay(employeeService.getTipPay());
            nailEmployeeServiceJsonMap.put(employeeService.getId().toString(), nailEmployeeServiceJson);
            allIds.add(employeeService.getId());

            //adding customerService id to customers
            NailEmployee employee = employeeService.getNailEmployee();
            nailEmployeeJsonMap.get(employee.getId().toString()).addEmployeeServices(employeeService.getId());
        }
        this.employeeServices.put("byId", nailEmployeeServiceJsonMap);
        this.employeeServices.put("allIds", allIds);

    }

    public void setStoreJson(StoreJson storeJson) {
        this.storeInfo = storeJson;
    }

    public Map<String, Object> getServices() {
        return services;
    }

    public void setServices(Map<String, Object> services) {
        this.services = services;
    }

    public Map<String, Object> getCustomers() {
        return customers;
    }

    public void setCustomers(Map<String, Object> customers) {
        this.customers = customers;
    }

    public Map<String, Object> getEmployees() {
        return employees;
    }

    public void setEmployees(Map<String, Object> employees) {
        this.employees = employees;
    }

    public Map<String, Object> getCustomerServices() {
        return customerServices;
    }

    public void setCustomerServices(Map<String, Object> customerServices) {
        this.customerServices = customerServices;
    }

    public Map<String, Object> getEmployeeServices() {
        return employeeServices;
    }

    public void setEmployeeServices(Map<String, Object> employeeServices) {
        this.employeeServices = employeeServices;
    }
}

class BaseInfo {
    private long id;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
class StoreJson extends BaseInfo {
    private String name;
    private String active;
    private String address_1;
    private String address_2;
    private String district;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phone;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
class NailServiceJson extends BaseInfo {
    private String name;
    private long price;
    private String active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
class NailCustomerJson extends BaseInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String active;
    private Set<Long> customerServices;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Set<Long> getCustomerServices() {
        return customerServices;
    }

    public void setCustomerServices(Set<Long> customerServices) {
        this.customerServices = customerServices;
    }
    public void addCustomerServices(Long customerServiceId) {
        if (this.customerServices == null) {
            this.customerServices = new HashSet<Long>();
        }
        this.customerServices.add(customerServiceId);
    }
}
class NailEmployeeJson extends BaseInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String active;
    private Date checkIn;
    private Set<Long> employeeServices;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Set<Long> getEmployeeServices() {
        return employeeServices;
    }

    public void setEmployeeServices(Set<Long> employeeServices) {
        this.employeeServices = employeeServices;
    }
    public void addEmployeeServices(Long employeeServiceId) {
        if (this.employeeServices == null) {
            this.employeeServices = new HashSet<Long>();
        }
        this.employeeServices.add(employeeServiceId);
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }
}
class NailCustomerServiceJson extends BaseInfo {
    private Date serviceDate;
    private String customerNote;
    private String status;

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
class NailEmployeeServiceJson extends BaseInfo {
    private long creditPay;
    private long cashPay;
    private long giftPay;
    private long checkPay;
    private long tipPay;

    public long getCreditPay() {
        return creditPay;
    }

    public void setCreditPay(long creditPay) {
        this.creditPay = creditPay;
    }

    public long getCashPay() {
        return cashPay;
    }

    public void setCashPay(long cashPay) {
        this.cashPay = cashPay;
    }

    public long getGiftPay() {
        return giftPay;
    }

    public void setGiftPay(long giftPay) {
        this.giftPay = giftPay;
    }

    public long getCheckPay() {
        return checkPay;
    }

    public void setCheckPay(long checkPay) {
        this.checkPay = checkPay;
    }

    public long getTipPay() {
        return tipPay;
    }

    public void setTipPay(long tipPay) {
        this.tipPay = tipPay;
    }
}

