package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.controller.exception.NailsException;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.model.helper.ServiceStatus;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/nails")
public class NailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NailController.class);
    private ServiceLocator serviceLocator;
    private static String STORE_ID_COOKIE = "STORE_ID_COOKIE";
    @Autowired
    public NailController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @RequestMapping(value = {
            "/naildata.json",
            "/searchNailCustomer.json",
            "/services/",
            "/customers.json",
            "/customers/{id}.json",
            "/customers/{id}/checkIn.json",
            "/customers/{id}/checkout.json",
            "/employees.json",
            "/employees/{id}.json",
            "/services.json",
            "/services/{id}.json",
            "/customers/{id}/customerServices.json",
            "/customers/{id}/customerServices/{csId}.json",
            "/customers/customerServices.json",
            "/customers/makeAppointment.json",
            "/employees/{id}/employeeServices/{esId}.json",
            "/employees/{id}/employeeServices.json",
            "/employees/{id}/employeeServices/date/{date}.json",
            "/employees/{id}/addEmployeeToCustomer/{customerId}.json",
            "/services/{id}/addServiceToCustomer/{customerId}.json",
            "/errorLog.json",
            "/appointments.json",
            "/appointments/{id}.json",
    }, method = {RequestMethod.OPTIONS})
    public void catchAllOpt(final HttpServletResponse response)
            throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Max-Age", "120"); // in seconds
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods",
                "HEAD, GET, OPTIONS, POST, PUT, UPDATE, DELETE");
        response.addHeader("Access-Control-Allow-Headers",
                "origin, content-type, accept, x-requested-with");
    }

//    @ExceptionHandler(NailsException.class )
//    public @ResponseBody ResponseEntity handleException(NailsException e) {
//        Map map = new HashMap();
//        map.put("error", e.getMessage());
//        return new ResponseEntity(map,HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler({Exception.class} )
//    public ResponseEntity handleException(Exception e) {
//        return new ResponseEntity(e.getMessage() + ":" + e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @Deprecated
//    @RequestMapping(value = {"/naildata.json"}, method = RequestMethod.GET)
//    public
//    @ResponseBody
//    NailDataObject getNailsData(HttpServletRequest request) throws Exception {
//        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//        NailDataObject dataObject = new NailDataObject ();
//        List stores = this.serviceLocator.getNailStoreDao().findAll(site.getId());
//        NailStore nailStore = null;
//        if (stores != null && stores.size() > 0) {
//            nailStore = (NailStore) stores.get(0);
//            dataObject.setStoreInfo(nailStore);
//            List<NailCustomerService> nailCustomerServices = this.serviceLocator.getNailCustomerServiceDao().getCustomerServicesByDate(new Date(), nailStore.getId());
//            if (nailCustomerServices != null && nailCustomerServices.size() > 0) {
//                //Don't need to do this if doesn't have any customer service on that day
//                dataObject.setCustomerServices(nailCustomerServices);
//            }
//            dataObject.setEmployeeServices(this.serviceLocator.getNailEmployeeDao().findBy("store.id", nailStore.getId()), this.serviceLocator.getNailEmployeeServiceDao().getEmployeeServicesByDate(new Date(), nailStore.getId()));
//
//            dataObject.setServices(this.serviceLocator.getNailServiceDao().findBy("store.id", nailStore.getId()));
//        }
//        return dataObject;
//    }

    @RequestMapping(value = {"/initialData.json"}, method = RequestMethod.GET)
    public
    @ResponseBody
    Map initialNailsData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Map dataObject = new HashMap ();
        NailStore nailStore = getCurrentStore(request, response);
        List stores = this.serviceLocator.getNailStoreDao().findAll(site.getId());

        if (nailStore != null) {
//            dataObject.put("selectedStoreId",nailStore.getId());
            dataObject.put("stores",stores);
            dataObject.put("employees",this.serviceLocator.getNailEmployeeDao().findBy("store.id", nailStore.getId()));
            dataObject.put("services",this.serviceLocator.getNailServiceDao().findBy("store.id", nailStore.getId()));
            dataObject.put("payments",this.serviceLocator.getNailCustomerPaymentDao().getCustomerPaymentsByDate(new Date(), nailStore.getId()));
            dataObject.put("appointments",this.serviceLocator.getNailCustomerAppointmentDao().getCustomerAppointmentsByDate(new Date(), new Date(), nailStore.getId()));

            List<NailCustomerService> nailCustomerServices = this.serviceLocator.getNailCustomerServiceDao().getCustomerServicesByDate(new Date(), nailStore.getId());
            dataObject.put("customerServices",nailCustomerServices);
            List<NailCustomer> customers = new ArrayList<NailCustomer>();
            for (NailCustomerService customerService : nailCustomerServices) {
                NailCustomer customer = customerService.getNailCustomer();
                if (customer != null) {
                    //set customer id to customer service.
                    customerService.setCustomerId(customer.getId());
                    customers.add(customer);
                }
                NailService service = customerService.getNailService();
                if (service != null) {
                    //set customer id to customer service.
                    customerService.setServiceId(service.getId());
                }
            }
            List<NailCustomer> checkedInCustomers = serviceLocator.getNailCustomerDao().getCheckedInCustomersByDate(new Date(), nailStore.getId());
            for (NailCustomer c : checkedInCustomers) {
                if (!customers.contains(c)) {
                    customers.add(c);
                }
            }
            dataObject.put("customers", customers);

            List<NailEmployeeService> nailEmployeeServices = this.serviceLocator.getNailEmployeeServiceDao().getEmployeeServicesByDate(new Date(), nailStore.getId());
            dataObject.put("employeeServices", nailEmployeeServices);
            for (NailEmployeeService employeeService : nailEmployeeServices) {
                //adding customerService id to customers
                NailEmployee employee = employeeService.getNailEmployee();
                if (employee != null) {
                    employeeService.setEmployeeId(employee.getId());
                }
                NailCustomerService customerService = employeeService.getNailCustomerService();
                if (customerService != null) {
                    employeeService.setCustomerServiceId(customerService.getId());
                }
            }
        } else {
            dataObject.put("stores",stores);
        }
        return dataObject;
    }
    @RequestMapping(value = {"/searchNailCustomer.json"}, method = RequestMethod.GET)
    public
    @ResponseBody
    List<NailCustomer> searchCustomers(@RequestParam(required = false, value = "") final String phone, @RequestParam(required = false, value = "") final String email) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        List stores = this.serviceLocator.getNailStoreDao().findAll(site.getId());
        NailStore nailStore = null;
        List<NailCustomer> nailCustomers = null;
        if (stores != null && stores.size() > 0) {
            nailStore = (NailStore) stores.get(0);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("store.id", nailStore.getId());
            if (!StringUtils.isEmpty(phone)) {
                map.put("phone", phone);
            }
            if (!StringUtils.isEmpty(email)) {
                map.put("email", email);
            }

            nailCustomers = this.serviceLocator.getNailCustomerDao().findSuggestionCustomers(phone, email, nailStore.getId(), 5);
        }
        return nailCustomers;
    }

    /************************Customers**********************/
    //-------------------Retrieve All Customers--------------------------------------------------------

    @RequestMapping(value = "/customers/", method = RequestMethod.GET)
    public ResponseEntity listAllNailCustomers(@RequestParam(required = false, value = "") final Long storeId) {
        List<NailCustomer> customers = serviceLocator.getNailCustomerDao().findAllByStore(storeId);
        if(customers.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity(customers, HttpStatus.OK);
    }


    //-------------------Retrieve Single NailCustomer--------------------------------------------------------

    @RequestMapping(value = {"/customers/{id}", "/customers/{id}.json"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getNailCustomer(@PathVariable("id") long id, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Fetching NailCustomer with id " + id);
        NailCustomer customer = serviceLocator.getNailCustomerDao().findByIdByStore(id, storeId);
        if (customer == null) {
            System.out.println("NailCustomer with id " + id + " not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(customer, HttpStatus.OK);
    }



    //-------------------Create a NailCustomer--------------------------------------------------------

    @RequestMapping(value = {"/customers/","/customers.json"}, method = RequestMethod.POST)
    public ResponseEntity createNailCustomer(@RequestBody NailCustomer customer,  UriComponentsBuilder ucBuilder, @RequestParam(required = false, value = "") final Long storeId) {
        //TODO: will check this later
//        if (customerService.isNailCustomerExist(customer)) {
//            System.out.println("A NailCustomer with name " + customer.getName() + " already exist");
//            return new ResponseEntity<NailCustomer>(HttpStatus.CONFLICT);
//        }
        NailCustomer existing = findCustomer(customer.getId(), customer.getEmail(), customer.getPhone(), storeId);
        if (existing != null){
            customer = existing;
        } else {
            NailStore nailStore = serviceLocator.getNailStoreDao().findById(storeId);
            customer.setStore(nailStore);
            customer.setActive("Y");
            serviceLocator.getNailCustomerDao().persist(customer);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri());
        }

        return new ResponseEntity(customer, HttpStatus.CREATED);
    }


    //------------------- Update a NailCustomer --------------------------------------------------------

    @RequestMapping(value = {"/customers/{id}", "/customers/{id}.json"}, method = RequestMethod.PUT)
    public ResponseEntity updateNailCustomer(@PathVariable("id") long id, @RequestBody NailCustomer customer, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Updating NailCustomer " + id);

        NailCustomer currentNailCustomer = serviceLocator.getNailCustomerDao().findByIdByStore(id, storeId);

        if (currentNailCustomer==null) {
            System.out.println("NailCustomer with id " + id + " not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (customer.getFirstName() != null && !customer.getFirstName().equals(currentNailCustomer.getFirstName())) {
            currentNailCustomer.setFirstName(customer.getFirstName());
        }
        if (customer.getLastName() != null && !customer.getLastName().equals(currentNailCustomer.getLastName())) {
            currentNailCustomer.setLastName(customer.getLastName());
        }
        if (customer.getPhone() != null && !customer.getPhone().equals(currentNailCustomer.getPhone())) {
            currentNailCustomer.setPhone(customer.getPhone());
        }
        if (customer.getEmail() != null && !customer.getEmail().equals(currentNailCustomer.getEmail())) {
            currentNailCustomer.setEmail(customer.getEmail());
        }
        if (customer.getActive() != null && !customer.getActive().equals(currentNailCustomer.getActive())) {
            currentNailCustomer.setActive(customer.getActive());
        }
        if (customer.getStatus() != null && !customer.getStatus().equals(currentNailCustomer.getStatus())) {
            currentNailCustomer.setStatus(customer.getStatus());
        }

        serviceLocator.getNailCustomerDao().merge(currentNailCustomer);
        return new ResponseEntity<NailCustomer>(currentNailCustomer, HttpStatus.OK);
    }

    @RequestMapping(value = {"/customers/{id}/checkIn.json"}, method = RequestMethod.PUT)
    public ResponseEntity<NailCustomer> checkInCustomer(@PathVariable("id") Long id, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Updating NailCustomer " + id);

        NailCustomer currentNailCustomer = serviceLocator.getNailCustomerDao().findByIdByStore(id, storeId);

        if (currentNailCustomer==null) {
            System.out.println("NailCustomer with id " + id + " not found");
            return new ResponseEntity<NailCustomer>(HttpStatus.NOT_FOUND);
        }

        currentNailCustomer.setCheckIn(new Date());
        currentNailCustomer.setStatus(ServiceStatus.WAITING.toString());
        serviceLocator.getNailCustomerDao().merge(currentNailCustomer);
        return new ResponseEntity<NailCustomer>(currentNailCustomer, HttpStatus.OK);
    }

    //------------------- Delete a NailCustomer --------------------------------------------------------

    @RequestMapping(value = {"/customers/{id}", "/customers/{id}.json"}, method = RequestMethod.DELETE)
    public ResponseEntity<NailCustomer> deleteNailCustomer(@PathVariable("id") long id, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Fetching & Deleting NailCustomer with id " + id);

        NailCustomer customer = serviceLocator.getNailCustomerDao().findByIdByStore(id, storeId);
        if (customer == null) {
            System.out.println("Unable to delete. NailCustomer with id " + id + " not found");
            return new ResponseEntity<NailCustomer>(HttpStatus.NOT_FOUND);
        }

        serviceLocator.getNailCustomerDao().remove(customer);
        return new ResponseEntity<NailCustomer>(HttpStatus.OK);
    }


    //------------------- Delete All NailCustomers --------------------------------------------------------

//    @RequestMapping(value = {"/customers/","/customers.json"}, method = RequestMethod.DELETE)
//    public ResponseEntity<NailCustomer> deleteAllNailCustomers(@RequestParam(required = false, value = "") final Long storeId) {
//        System.out.println("Deleting All NailCustomers");
//
//        List <NailCustomer>customers = serviceLocator.getNailCustomerDao().findAllByStore(storeId);
//        if (customers != null && customers.size() > 0) {
//            for (NailCustomer customer : customers) {
//                serviceLocator.getNailCustomerDao().remove(customer);
//            }
//        }
//        return new ResponseEntity<NailCustomer>(HttpStatus.OK);
//    }

    /************************Employees**********************/
    //-------------------Retrieve All Employees--------------------------------------------------------

    @RequestMapping(value = "/employees/", method = RequestMethod.GET)
    public ResponseEntity<List<NailEmployee>> listAllNailEmployees(@RequestParam(required = false, value = "") final Long storeId) {
        List<NailEmployee> employees = serviceLocator.getNailEmployeeDao().findAllByStore(storeId);
        if(employees.isEmpty()){
            return new ResponseEntity<List<NailEmployee>>(HttpStatus.NOT_FOUND);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<NailEmployee>>(employees, HttpStatus.OK);
    }


    //-------------------Retrieve Single NailEmployee--------------------------------------------------------

    @RequestMapping(value = {"/employees/{id}", "/employees/{id}.json"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NailEmployee> getNailEmployee(@PathVariable("id") long id, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Fetching NailEmployee with id " + id);
        NailEmployee employee = serviceLocator.getNailEmployeeDao().findByIdByStore(id, storeId);
        if (employee == null) {
            System.out.println("NailEmployee with id " + id + " not found");
            return new ResponseEntity<NailEmployee>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<NailEmployee>(employee, HttpStatus.OK);
    }



    //-------------------Create a NailEmployee--------------------------------------------------------

    @RequestMapping(value = {"/employees/","/employees.json"}, method = RequestMethod.POST)
    public ResponseEntity createNailEmployee(@RequestBody NailEmployee employee,  UriComponentsBuilder ucBuilder, @RequestParam(required = false, value = "") final Long storeId) {
        Map result = new HashMap();
        Messages messages = new Messages();
        if (employee.isEmptyId()) {
            System.out.println("A NailEmployee with name " + employee.getName() + " already exist");
            NailStore nailStore = serviceLocator.getNailStoreDao().findById(storeId);
            employee.setStore(nailStore);
            employee.setActive("Y");
            serviceLocator.getNailEmployeeDao().persist(employee);
            messages.addInfo("The employee has been created.");
            result.put("messages", messages);
            result.put("employee", employee);
            return new ResponseEntity(result, HttpStatus.CREATED);
        } else {
            messages.addError("Cannot create a new Employee.");
            result.put("messages", messages);
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }

    //------------------- Update a NailEmployee --------------------------------------------------------

    @RequestMapping(value = {"/employees/{id}", "/employees/{id}.json"}, method = RequestMethod.PUT)
    public ResponseEntity updateNailEmployee(@PathVariable("id") long id, @RequestBody NailEmployee employee, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Updating NailEmployee " + id);
        Map result = new HashMap();
        Messages messages = new Messages();
        if (!employee.isEmptyId()) {
            System.out.println("A NailEmployee with name " + employee.getName() + " already exist");
            NailEmployee nailEmployee = serviceLocator.getNailEmployeeDao().findByIdByStore(employee.getId(), storeId);
            boolean isUpdate = false;
            if (nailEmployee != null) {
                if (employee.getFirstName() != null && !employee.getFirstName().equals(nailEmployee.getFirstName())) {
                    nailEmployee.setFirstName(employee.getFirstName());
                    isUpdate = true;
                }
                if (employee.getLastName() != null && !employee.getLastName().equals(nailEmployee.getLastName())) {
                    nailEmployee.setLastName(employee.getLastName());
                    isUpdate = true;
                }
                if (employee.getPhone() != null && !employee.getPhone().equals(nailEmployee.getPhone())) {
                    nailEmployee.setPhone(employee.getPhone());
                    isUpdate = true;
                }
                if (employee.getEmail() != null && !employee.getEmail().equals(nailEmployee.getEmail())) {
                    nailEmployee.setEmail(employee.getEmail());
                    isUpdate = true;
                }
                if (isUpdate) {
                    serviceLocator.getNailEmployeeDao().merge(nailEmployee);
                    messages.addInfo("The employee has been updated.");
                    result.put("employee", nailEmployee);
                } else {
                    messages.addInfo("Nothing change.");
                }
            } else {
                messages.addError("Cannot find this employee data. It may have been deleted.");
            }
        } else {
            messages.addError("Cannot find this employee data. It may have been deleted.");
        }
        result.put("messages", messages);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    //------------------- Delete a NailEmployee --------------------------------------------------------

    @RequestMapping(value = {"/employees/{id}", "/employees/{id}.json"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteNailEmployee(@PathVariable("id") long id, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Fetching & Deleting NailEmployee with id " + id);

        NailEmployee employee = serviceLocator.getNailEmployeeDao().findByIdByStore(id, storeId);
        if (employee == null) {
            System.out.println("Unable to delete. NailEmployee with id " + id + " not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        serviceLocator.getNailEmployeeDao().remove(employee);
        Messages messages = new Messages();
        messages.addInfo("The employee has been deleted.");
        return new ResponseEntity(messages, HttpStatus.OK);
    }

    /************************Services**********************/
    //-------------------Retrieve All Services--------------------------------------------------------

    @RequestMapping(value = "/services/", method = RequestMethod.GET)
    public ResponseEntity<List<NailService>> listAllNailServices(@RequestParam(required = false, value = "") final Long storeId) {
        List<NailService> services = serviceLocator.getNailServiceDao().findAllByStore(storeId);
        if(services.isEmpty()){
            return new ResponseEntity<List<NailService>>(HttpStatus.NOT_FOUND);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<NailService>>(services, HttpStatus.OK);
    }


    //-------------------Retrieve Single NailService--------------------------------------------------------

    @RequestMapping(value = {"/services/{id}", "/services/{id}.json"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NailService> getNailService(@PathVariable("id") long id, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Fetching NailService with id " + id);
        NailService service = serviceLocator.getNailServiceDao().findByIdByStore(id, storeId);
        if (service == null) {
            System.out.println("NailService with id " + id + " not found");
            return new ResponseEntity<NailService>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<NailService>(service, HttpStatus.OK);
    }



    //-------------------Create a NailService--------------------------------------------------------

    @RequestMapping(value = {"/services/","/services.json"}, method = RequestMethod.POST)
    public ResponseEntity createNailService(@RequestBody NailService service,  UriComponentsBuilder ucBuilder, @RequestParam(required = false, value = "") final Long storeId) throws Exception {
        Map result = new HashMap();
        Messages messages = new Messages();
        if (service.isEmptyId()) {
            System.out.println("A NailService with name " + service.getName() + " already exist");
            NailStore nailStore = serviceLocator.getNailStoreDao().findById(storeId);
            service.setStore(nailStore);
            service.setActive("Y");
            serviceLocator.getNailServiceDao().persist(service);
            messages.addInfo("The service has been created.");
            result.put("messages", messages);
            result.put("service", service);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/services/{id}").buildAndExpand(service.getId()).toUri());
            return new ResponseEntity(result, HttpStatus.CREATED);
        } else {
            messages.addError("Cannot create a new Service.");
            result.put("messages", messages);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/services/{id}").buildAndExpand(service.getId()).toUri());
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }


    //------------------- Update a NailService --------------------------------------------------------

    @RequestMapping(value = {"/services/{id}", "/services/{id}.json"}, method = RequestMethod.PUT)
    public ResponseEntity updateNailService(@PathVariable("id") long id, @RequestBody NailService service, @RequestParam(required = false, value = "") final Long storeId) throws Exception {
        System.out.println("Updating NailService " + id);
        Map result = new HashMap();
        Messages messages = new Messages();
        if (!service.isEmptyId()) {
            System.out.println("A NailService with name " + service.getName() + " already exist");
            NailService nailService = serviceLocator.getNailServiceDao().findByIdByStore(service.getId(), storeId);
            boolean isUpdate = false;
            if (nailService != null) {
                if (service.getName() != null && !service.getName().equals(nailService.getName())) {
                    nailService.setName(service.getName());
                    isUpdate = true;
                }
                if (service.getPrice() != nailService.getPrice()) {
                    nailService.setPrice(service.getPrice());
                    isUpdate = true;
                }
                if (service.getMinutes() != nailService.getMinutes()) {
                    nailService.setMinutes(service.getMinutes());
                    isUpdate = true;
                }
                if (isUpdate) {
                    serviceLocator.getNailServiceDao().merge(nailService);
                    messages.addInfo("The service has been updated.");
                    result.put("service", nailService);
                } else {
                    messages.addInfo("Nothing change.");
                }
            } else {
                messages.addError("Cannot find this service data. It may have been deleted.");
            }
        } else {
            messages.addError("Cannot find this service data. It may have been deleted.");
        }
        result.put("messages", messages);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    //------------------- Delete a NailService --------------------------------------------------------

    @RequestMapping(value = {"/services/{id}", "/services/{id}.json"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteNailService(@PathVariable("id") long id, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Fetching & Deleting NailService with id " + id);

        NailService service = serviceLocator.getNailServiceDao().findByIdByStore(id, storeId);
        if (service == null) {
            System.out.println("Unable to delete. NailService with id " + id + " not found");
            return new ResponseEntity<NailService>(HttpStatus.NOT_FOUND);
        }

        serviceLocator.getNailServiceDao().remove(service);
        Messages messages = new Messages();
        messages.addInfo("The service has been deleted.");
        return new ResponseEntity(messages, HttpStatus.OK);
    }

    /************************CustomerServices**********************/
    //-------------------Retrieve All CustomerServices--------------------------------------------------------

    @RequestMapping(value = {"/customers/{id}/customerServices/date/{date}","/customers/{id}/customerServices/date/{date}.json"}, method = RequestMethod.GET)
    public ResponseEntity<List<NailCustomerService>> listAllNailCustomerServicesByCustomer(
            @PathVariable("id") long id,
            @PathVariable("date") String date //date will have format YYYYMMDD
            ) {
        //TODO: Will implement this later
        List<NailCustomerService> customerServices = serviceLocator.getNailCustomerServiceDao().findAllByStore(id);
        if(customerServices.isEmpty()){
            return new ResponseEntity<List<NailCustomerService>>(HttpStatus.NOT_FOUND);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<NailCustomerService>>(customerServices, HttpStatus.OK);
    }


    //-------------------Retrieve Single NailCustomerService--------------------------------------------------------

    @RequestMapping(value = {"/customers/{id}/customerServices/{csId}", "/customers/{id}/customerServices/{csId}.json"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NailCustomerService> getNailCustomerService(
            @PathVariable("id") long id,
            @PathVariable("csId") long csId
    ) {
        //TODO: Will implement this later
        System.out.println("Fetching NailCustomerService with id " + id);
        NailCustomerService customerService = serviceLocator.getNailCustomerServiceDao().findByIdByStore(id, csId);
        if (customerService == null) {
            System.out.println("NailCustomerService with id " + id + " not found");
            return new ResponseEntity<NailCustomerService>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<NailCustomerService>(customerService, HttpStatus.OK);
    }



    //-------------------Create a NailCustomerService--------------------------------------------------------

    @RequestMapping(value = "/customers/{id}/customerServices.json", method = RequestMethod.POST)
    public ResponseEntity<NailCustomerService> addNailCustomerService(
            @RequestBody NailCustomerService customerService,
            UriComponentsBuilder ucBuilder,
            @PathVariable("id") Long id,
            @RequestParam(required = false, value = "") final Long serviceId
    ) {
        NailCustomer customer = serviceLocator.getNailCustomerDao().findById(id);
        NailService service = null;
        if (serviceId != null && serviceId > 0) {
            service = serviceLocator.getNailServiceDao().findById(serviceId);
        }
        customerService.setNailCustomer(customer);
        customerService.setNailService(service);
        if (service != null) {
            customerService.setPrice(service.getPrice());
        }
        serviceLocator.getNailCustomerServiceDao().persist(customerService);
        return new ResponseEntity<NailCustomerService>(customerService, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/customers/customerServices.json", method = RequestMethod.POST)
    public ResponseEntity<Map> addNailCustomerService(
            @RequestBody Map inputData,
            @RequestParam(required = false, value = "") final Long storeId,
            @RequestParam(required = false, value = "") final String appointment
    ) throws Exception {

        Map<String, Object> result = null;
        if ("Y".equals(appointment)) {
            result = serviceLocator.getNailManagementService().checkInAppointmentCustomer(inputData, storeId);
        } else {
            //TODO: check and make sure this method is update customer status correctly.
            result = serviceLocator.getNailManagementService().addNailCustomerService(inputData, storeId);
        }

        return new ResponseEntity<Map>(result, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/customers/makeAppointment.json", method = RequestMethod.POST)
    public ResponseEntity<Map> makeAppointment(
            @RequestBody Map inputData,
            @RequestParam(required = false, value = "") final Long storeId
    ) throws Exception {

        Map<String, Object> result = null;
        result = serviceLocator.getNailManagementService().makeAppointment(inputData, storeId);

        return new ResponseEntity<Map>(result, HttpStatus.CREATED);
    }

    //------------------- Update a NailCustomerService --------------------------------------------------------

    @RequestMapping(value = {"/customers/{id}/customerServices/{csId}.json"}, method = RequestMethod.PUT)
    public ResponseEntity<NailCustomerService> updateNailCustomerService(
            @PathVariable("id") long id,
            @PathVariable("csId") long csId,
            @RequestBody NailCustomerService customerService,
            @RequestParam(required = false, value = "") final Long serviceId
    ) {
        System.out.println("Updating NailCustomerService " + id);

        NailCustomerService currentNailCustomerService = null;
        boolean hasChange = false;
        try {
            currentNailCustomerService = serviceLocator.getNailCustomerServiceDao().getCustomerService(id, csId);
            if (currentNailCustomerService==null) {
                System.out.println("NailCustomerService with id " + id + " not found");
                return new ResponseEntity<NailCustomerService>(HttpStatus.NOT_FOUND);
            }

            if (customerService.getServiceDate() != null && !customerService.getServiceDate().equals(currentNailCustomerService.getServiceDate())) {
                currentNailCustomerService.setServiceDate(customerService.getServiceDate());
                hasChange = true;
            }
            if (customerService.getPrice() != null && !customerService.getPrice().equals(currentNailCustomerService.getPrice())) {
                currentNailCustomerService.setPrice(customerService.getPrice());
                hasChange = true;
            }

            if (serviceId != null && serviceId > 0) {
                NailService service = serviceLocator.getNailServiceDao().findById(serviceId);
                if (service != null) {
                    currentNailCustomerService.setNailService(service);
                    if (currentNailCustomerService.getPrice() == null || currentNailCustomerService.getPrice() <= 0) {
                        currentNailCustomerService.setPrice(service.getPrice());
                    }
                    hasChange = true;
                }
            }
            if (hasChange) {
                serviceLocator.getNailCustomerServiceDao().merge(currentNailCustomerService);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<NailCustomerService>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<NailCustomerService>(currentNailCustomerService, HttpStatus.OK);
    }

    @RequestMapping(value = {"/customers/{id}/customerServices.json"}, method = RequestMethod.PUT)
    public ResponseEntity<List<NailCustomerService>> updateNailCustomerServices(
            @PathVariable("id") long id,
            @RequestBody List<NailCustomerService> customerServices
    ) {
        System.out.println("Updating NailCustomerService " + id);

        NailCustomerService currentNailCustomerService = null;
        try {
            boolean hasChange = false;
            for (NailCustomerService customerService: customerServices) {
                currentNailCustomerService = serviceLocator.getNailCustomerServiceDao().getCustomerService(id, customerService.getId());
                if (currentNailCustomerService != null) {
                    if (customerService.getServiceDate() != null && !customerService.getServiceDate().equals(currentNailCustomerService.getServiceDate())) {
                        currentNailCustomerService.setServiceDate(customerService.getServiceDate());
                        hasChange = true;
                    }
                    if (customerService.getPrice() != null && !customerService.getPrice().equals(currentNailCustomerService.getPrice())) {
                        currentNailCustomerService.setPrice(customerService.getPrice());
                        hasChange = true;
                    }
                    if (hasChange) {
                        serviceLocator.getNailCustomerServiceDao().merge(currentNailCustomerService);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<NailCustomerService>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<NailCustomerService>>(customerServices, HttpStatus.OK);
    }

    //------------------- Delete a NailCustomerService --------------------------------------------------------

    @RequestMapping(value = {"/customers/{id}/customerServices/{csId}.json"}, method = RequestMethod.DELETE)
    public ResponseEntity<NailCustomerService> deleteNailCustomerService(
            @PathVariable("id") long id,
            @PathVariable("csId") long csId
    ) throws Exception {
        System.out.println("Fetching & Deleting NailCustomerService with id " + id);

        NailCustomerService customerService = null;
        try {
            customerService = serviceLocator.getNailCustomerServiceDao().getCustomerService(id, csId);
            if (customerService == null) {
                System.out.println("Unable to delete. NailCustomerService with id " + id + " not found");
                return new ResponseEntity<NailCustomerService>(HttpStatus.NOT_FOUND);
            }
            serviceLocator.getNailCustomerServiceDao().remove(customerService);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<NailCustomerService>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<NailCustomerService>(HttpStatus.OK);
    }


    /************************EmployeeServices**********************/
    //-------------------Retrieve All EmployeeServices--------------------------------------------------------

    @RequestMapping(value = {"/employees/{id}/employeeServices/date/{date}","/employees/{id}/employeeServices/date/{date}.json"}, method = RequestMethod.GET)
    public ResponseEntity<List<NailEmployeeService>> listAllNailEmployeeServicesByEmployee(
            @PathVariable("id") long id,
            @PathVariable("date") String date //date will have format YYYYMMDD
    ) {
        //TODO: Will implement this later
        List<NailEmployeeService> employeeServices = serviceLocator.getNailEmployeeServiceDao().findAllByStore(id);
        if(employeeServices.isEmpty()){
            return new ResponseEntity<List<NailEmployeeService>>(HttpStatus.NOT_FOUND);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<NailEmployeeService>>(employeeServices, HttpStatus.OK);
    }


    //-------------------Retrieve Single NailEmployeeService--------------------------------------------------------

    @RequestMapping(value = {"/employees/{id}/employeeServices/{esId}", "/employees/{id}/employeeServices/{esId}.json"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NailEmployeeService> getNailEmployeeService(
            @PathVariable("id") long id,
            @PathVariable("esId") long esId
    ) {
        //TODO: Will implement this later
        System.out.println("Fetching NailEmployeeService with id " + id);
        NailEmployeeService employeeService = serviceLocator.getNailEmployeeServiceDao().findByIdByStore(id, esId);
        if (employeeService == null) {
            System.out.println("NailEmployeeService with id " + id + " not found");
            return new ResponseEntity<NailEmployeeService>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<NailEmployeeService>(employeeService, HttpStatus.OK);
    }



    //-------------------Create a NailEmployeeService--------------------------------------------------------

    @RequestMapping(value = "/employees/{id}/employeeServices.json", method = RequestMethod.POST)
    public ResponseEntity<NailEmployeeService> addNailEmployeeService(
            @RequestBody NailEmployeeService employeeService,
            UriComponentsBuilder ucBuilder,
            @PathVariable("id") Long id,
            @RequestParam(required = false, value = "") final Long customerServiceId
    ) {
        NailEmployee employee = serviceLocator.getNailEmployeeDao().findById(id);
        NailCustomerService customerService = null;
        if (customerServiceId != null && customerServiceId > 0) {
            customerService = serviceLocator.getNailCustomerServiceDao().findById(customerServiceId);
        }
        employeeService.setNailEmployee(employee);
        employeeService.setNailCustomerService(customerService);
        serviceLocator.getNailEmployeeServiceDao().persist(employeeService);
        return new ResponseEntity<NailEmployeeService>(employeeService, HttpStatus.CREATED);
    }


    //------------------- Update a NailEmployeeService --------------------------------------------------------

    @RequestMapping(value = {"/employees/{id}/employeeServices/{esId}.json"}, method = RequestMethod.PUT)
    public ResponseEntity<NailEmployeeService> updateNailEmployeeService(
            @PathVariable("id") long id,
            @PathVariable("esId") long esId,
            @RequestBody NailEmployeeService employeeService,
            @RequestParam(required = false, value = "") final Long customerServiceId
    ) {
        System.out.println("Updating NailEmployeeService " + id);

        NailEmployeeService currentNailEmployeeService = null;
        try {
            currentNailEmployeeService = serviceLocator.getNailEmployeeServiceDao().findById(esId);
            if (currentNailEmployeeService==null) {
                System.out.println("NailEmployeeService with id " + id + " not found");
                return new ResponseEntity<NailEmployeeService>(HttpStatus.NOT_FOUND);
            }

            if (employeeService.getServicePrice() != currentNailEmployeeService.getServicePrice()) {
                currentNailEmployeeService.setServicePrice(employeeService.getServicePrice());
            }
            if (employeeService.getTipPrice() != currentNailEmployeeService.getTipPrice()) {
                currentNailEmployeeService.setTipPrice(employeeService.getTipPrice());
            }

            serviceLocator.getNailEmployeeServiceDao().merge(currentNailEmployeeService);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<NailEmployeeService>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<NailEmployeeService>(currentNailEmployeeService, HttpStatus.OK);
    }

    //------------------- Delete a NailEmployeeService --------------------------------------------------------

    @RequestMapping(value = {"/employees/{id}/employeeServices/{esId}.json"}, method = RequestMethod.DELETE)
    public ResponseEntity<NailEmployeeService> deleteNailEmployeeService(
            @PathVariable("id") long id,
            @PathVariable("esId") long esId
    ) {
        System.out.println("Fetching & Deleting NailEmployeeService with id " + id);

        NailEmployeeService employeeService = null;
        try {
            employeeService = serviceLocator.getNailEmployeeServiceDao().getEmployeeService(id, esId);
            if (employeeService == null) {
                System.out.println("Unable to delete. NailEmployeeService with id " + id + " not found");
                return new ResponseEntity<NailEmployeeService>(HttpStatus.NOT_FOUND);
            }
            serviceLocator.getNailEmployeeServiceDao().remove(employeeService);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<NailEmployeeService>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<NailEmployeeService>(HttpStatus.OK);
    }

    /************************Appointments**********************/
    //-------------------Retrieve All Appointments--------------------------------------------------------

    @RequestMapping(value = "/appointments.json", method = RequestMethod.GET)
    public ResponseEntity listAllNailAppointmentsByDate(
            @RequestParam(required = false, value = "") final Long storeId,
            @RequestParam(required = false, value = "") final String startDate,
            @RequestParam(required = false, value = "") final String endDate
    ) throws Exception {
        Date startDateObj;
        Date endDateObj;
        if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startDateObj = sdf.parse(startDate);
                endDateObj = sdf.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
                Messages messages = new Messages();
                messages.addError("Cannot parse the date. Please recheck");
                return new ResponseEntity(messages, HttpStatus.OK);
            }
        } else {
            startDateObj = new Date();
            endDateObj = new Date();
        }
        List<NailCustomerAppointment> appointments = serviceLocator.getNailCustomerAppointmentDao().getCustomerAppointmentsByDate(startDateObj, endDateObj, storeId);
        return new ResponseEntity(appointments, HttpStatus.OK);
    }


    //-------------------Retrieve Single NailCustomerAppointment--------------------------------------------------------

    @RequestMapping(value = {"/appointments/{id}", "/appointments/{id}.json"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NailCustomerAppointment> getNailCustomerAppointment(
            @PathVariable("id") long id,
            @RequestParam(required = false, value = "") final Long storeId,
            @RequestParam(required = false, value = "") final String date
    ) {
        System.out.println("Fetching NailCustomerAppointment with id " + id);
        NailCustomerAppointment appointment = serviceLocator.getNailCustomerAppointmentDao().findByIdByStore(id, storeId);
        if (appointment == null) {
            System.out.println("NailCustomerAppointment with id " + id + " not found");
            return new ResponseEntity<NailCustomerAppointment>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<NailCustomerAppointment>(appointment, HttpStatus.OK);
    }



    //-------------------Create a NailCustomerAppointment--------------------------------------------------------

    @RequestMapping(value = {"/appointments/","/appointments.json"}, method = RequestMethod.POST)
    public ResponseEntity<NailCustomerAppointment> createNailCustomerAppointment(@RequestBody Map appointment,  UriComponentsBuilder ucBuilder, @RequestParam(required = false, value = "") final Long storeId) throws Exception {
        NailCustomerAppointment customerAppointment = new NailCustomerAppointment();
        NailStore nailStore = serviceLocator.getNailStoreDao().findById(storeId);
        customerAppointment.setStore(nailStore);

        NailCustomer nailCustomer = null;
        if (appointment.get("customerId") != null && StringUtils.isNumeric(appointment.get("customerId")+"")) {
            nailCustomer = serviceLocator.getNailCustomerDao().findByIdByStore(new Long(appointment.get("customerId") + ""), storeId);
        }
        if (nailCustomer != null) {
            customerAppointment.setNailCustomer(nailCustomer);
        } else {
            throw new NailsException("Cannot find customer");
        }

        NailEmployee nailEmployee = null;
        if (appointment.get("employeeId") != null && StringUtils.isNumeric(appointment.get("employeeId")+"")) {
            nailEmployee = serviceLocator.getNailEmployeeDao().findByIdByStore(new Long(appointment.get("employeeId") + ""), storeId);
        }
        if (nailEmployee != null) {
            customerAppointment.setNailEmployee(nailEmployee);
        } else {
            throw new NailsException("Cannot find employee");
        }

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = sdf.parse(appointment.get("startDate")+"");
        Date endDate = sdf.parse(appointment.get("endDate")+"");
        customerAppointment.setStartTime(startDate);
        customerAppointment.setEndTime(endDate);
        if (appointment.get("note") != null) {
            customerAppointment.setCustomerNote(appointment.get("note")+"");
        }
        serviceLocator.getNailCustomerAppointmentDao().persist(customerAppointment);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/appointments/{id}").buildAndExpand(appointment.getId()).toUri());

        return new ResponseEntity<NailCustomerAppointment>(customerAppointment, HttpStatus.CREATED);
    }


    //------------------- Update a NailCustomerAppointment --------------------------------------------------------

    @RequestMapping(value = {"/appointments/{id}", "/appointments/{id}.json"}, method = RequestMethod.PUT)
    public ResponseEntity<NailCustomerAppointment> updateNailCustomerAppointment(@PathVariable("id") long id, @RequestBody NailCustomerAppointment appointment, @RequestParam(required = false, value = "") final Long storeId) {
        System.out.println("Updating NailCustomerAppointment " + id);

        NailCustomerAppointment currentNailCustomerAppointment = serviceLocator.getNailCustomerAppointmentDao().findByIdByStore(id, storeId);

        if (currentNailCustomerAppointment==null) {
            System.out.println("NailCustomerAppointment with id " + id + " not found");
            return new ResponseEntity<NailCustomerAppointment>(HttpStatus.NOT_FOUND);
        }
//TODO: need update
//        if (appointment.getFirstName() != null && !appointment.getFirstName().equals(currentNailCustomerAppointment.getFirstName())) {
//            currentNailCustomerAppointment.setFirstName(appointment.getFirstName());
//        }
//        if (appointment.getLastName() != null && !appointment.getLastName().equals(currentNailCustomerAppointment.getLastName())) {
//            currentNailCustomerAppointment.setLastName(appointment.getLastName());
//        }
//        if (appointment.getPhone() != null && !appointment.getPhone().equals(currentNailCustomerAppointment.getPhone())) {
//            currentNailCustomerAppointment.setPhone(appointment.getPhone());
//        }
//        if (appointment.getEmail() != null && !appointment.getEmail().equals(currentNailCustomerAppointment.getEmail())) {
//            currentNailCustomerAppointment.setEmail(appointment.getEmail());
//        }
//        if (appointment.getActive() != null && !appointment.getActive().equals(currentNailCustomerAppointment.getActive())) {
//            currentNailCustomerAppointment.setActive(appointment.getActive());
//        }
//        if (appointment.getStatus() != null && !appointment.getStatus().equals(currentNailCustomerAppointment.getStatus())) {
//            currentNailCustomerAppointment.setStatus(appointment.getStatus());
//        }

        serviceLocator.getNailCustomerAppointmentDao().merge(currentNailCustomerAppointment);
        return new ResponseEntity<NailCustomerAppointment>(currentNailCustomerAppointment, HttpStatus.OK);
    }

    //------------------- Delete a NailCustomerAppointment --------------------------------------------------------

    @RequestMapping(value = {"/appointments/{id}", "/appointments/{id}.json"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteNailCustomerAppointment(@PathVariable("id") long id, @RequestParam(required = false, value = "") final Long storeId) throws Exception {
        System.out.println("Fetching & Deleting NailCustomerAppointment with id " + id);

        NailCustomerAppointment appointment = serviceLocator.getNailCustomerAppointmentDao().findByIdByStore(id, storeId);
        if (appointment == null) {
            System.out.println("Unable to delete. NailCustomerAppointment with id " + id + " not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            // Only delete customer services and employee services if it is in the future.
            // We don't delete appointment in the past because it relates to pricing...
            if (appointment.getStartTime().after(new Date())) {
                NailCustomer customer = serviceLocator.getNailCustomerDao().findByIdByStore(appointment.getCustomerId(), storeId);
                if (customer != null) {
                    customer.setCheckIn(null);
                    customer.setStatus(null);
                }
                serviceLocator.getNailCustomerAppointmentDao().remove(appointment);
            } else {
                throw new NailsException("Cannot delete the old appointment");
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * Get the employee from the store
     * Get the current customer
     * Get all current customer service
     *    Assign employee to each customer service
     *
     * @param id
     * @param customerId
     * @return Map: {
     *     employeeServices
     * }
     */
    @RequestMapping(value = "/employees/{id}/addEmployeeToCustomer/{customerId}.json", method = RequestMethod.POST)
    public ResponseEntity<List<NailEmployeeService>> addNailEmployeeToCustomer(
            @PathVariable("id") Long id,
            @PathVariable("customerId") Long customerId,
            @RequestParam(required = false, value = "") final Long customerServiceId,
            @RequestParam(required = false, value = "") final Long storeId
    ) {
        NailEmployee employee = (NailEmployee) this.serviceLocator.getNailEmployeeDao().findByIdByStore(id, storeId);
        List<NailCustomerService> customerServices = null;
        try {
            if (customerServiceId != null && customerServiceId > 0) {
                customerServices = new ArrayList<NailCustomerService>();
                NailCustomerService customerService = this.serviceLocator.getNailCustomerServiceDao().getCustomerService(customerId, customerServiceId);
                if (customerService != null) {
                    customerServices.add(customerService);
                }
            } else {
                customerServices = this.serviceLocator.getNailCustomerServiceDao().getCustomerServicesByDate(new Date(), customerId, storeId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<NailEmployeeService>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<NailEmployeeService> employeeServices = new ArrayList<NailEmployeeService>();
        for (NailCustomerService cs : customerServices) {
            NailEmployeeService employeeService = new NailEmployeeService();
            employeeService.setNailEmployee(employee);
            employeeService.setNailCustomerService(cs);
            employeeService.setServicePrice(cs.getPrice());
            serviceLocator.getNailEmployeeServiceDao().persist(employeeService);
            employeeServices.add(employeeService);
        }
        return new ResponseEntity<List<NailEmployeeService>>(employeeServices, HttpStatus.CREATED);
    }



    /**
     *
     * Get the service from the store
     * Get the current customer
     * Create a customer service from these information
     *
     * @param id
     * @param customerId
     * @return Map: {
     *     employeeServices
     * }
     */
    @RequestMapping(value = "/services/{id}/addServiceToCustomer/{customerId}.json", method = RequestMethod.POST)
    public ResponseEntity<NailCustomerService> addNailServiceToCustomer(
            @PathVariable("id") Long id,
            @PathVariable("customerId") Long customerId,
            @RequestParam(required = false, value = "") final Long storeId
    ) {
        NailCustomer customer = this.serviceLocator.getNailCustomerDao().findByIdByStore(customerId, storeId);
        NailService service = this.serviceLocator.getNailServiceDao().findByIdByStore(id, storeId);
        NailCustomerService customerService = new NailCustomerService();
        if (service != null && customer != null) {
            customerService.setNailCustomer(customer);
            customerService.setNailService(service);
            customerService.setPrice(service.getPrice());
            customerService.setServiceDate(new Date());
            this.serviceLocator.getNailCustomerServiceDao().persist(customerService);
        }

        return new ResponseEntity<NailCustomerService>(customerService, HttpStatus.CREATED);
    }

    /**
     * Create a customer payment
     *
     *
     *
     * @param id
     * @return Map: {
     *     employeeServices
     * }
     */
    @RequestMapping(value = "/customers/{id}/checkout.json", method = RequestMethod.POST)
    public ResponseEntity<Map> createCustomerPayment(
            @PathVariable("id") Long id,
            @RequestBody Map inputData,
//            @RequestBody NailCustomerPayment payment,
            @RequestParam(required = false, value = "") final Long storeId
    ) throws Exception {
        Map result = null;
        try {
            result = serviceLocator.getNailManagementService().submitCustomerPayment(id, storeId, inputData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NailsException ("We're having an issue with saving payment");
        }
        return new ResponseEntity<Map>(result, HttpStatus.CREATED);
    }


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

    protected NailStore getCurrentStore(HttpServletRequest request, HttpServletResponse response) throws Exception {
        NailStore nailStore = null;
        Long storeId = null;
        if (request.getParameter("storeId") != null && StringUtils.isNumeric(request.getParameter("storeId"))) {
            storeId = new Long(request.getParameter("storeId"));
        } else {
            Cookie storeCookieId = SessionUtil.findCookie(request, STORE_ID_COOKIE);
            if (storeCookieId != null && StringUtils.isNumeric(storeCookieId.getValue())) {
                storeId = new Long(storeCookieId.getValue());
            }
        }

        if (storeId != null) {
            nailStore = (NailStore) ServiceLocatorHolder.getServiceLocator().getCacheData().getCommonCache("STORE_" + storeId);
            if (nailStore == null) {
                nailStore = serviceLocator.getNailStoreDao().findById(storeId);
            }
        } else {
            List stores = this.serviceLocator.getNailStoreDao().findAll(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getId());
            if (stores != null) {
                if (stores.size() == 1) {
                    nailStore = (NailStore) stores.get(0);
                    ServiceLocatorHolder.getServiceLocator().getCacheData().addCommonCache("STORE_" + nailStore.getId(), nailStore);
                    SessionUtil.createCookie(request, response, STORE_ID_COOKIE, nailStore.getId()+"", 10*365*24*60*60);
                }
            } else {
                throw new NailsException ("No stores available");
            }

        }

        return nailStore;
    }

    //-------------------Create a NailCustomer--------------------------------------------------------

    @RequestMapping(value = {"/errorLog.json"}, method = RequestMethod.POST)
    public ResponseEntity createErrorLog(@RequestBody Map inputData, @RequestParam(required = false, value = "") final Long storeId) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setRootCause(inputData.get("rootCause") + "");
        Gson gson = new Gson();
        String json = gson.toJson(inputData.get("data"));
        errorLog.setDataSession(json);
        errorLog.setStoreId(storeId);
        serviceLocator.getErrorLogDao().persist(errorLog);
        return new ResponseEntity(errorLog, HttpStatus.CREATED);
    }

}
