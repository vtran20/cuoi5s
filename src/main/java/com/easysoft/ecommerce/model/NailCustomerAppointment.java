package com.easysoft.ecommerce.model;


import com.easysoft.ecommerce.model.json.DateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table (name="nail_customer_appointment")
public class NailCustomerAppointment extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private Date startTime;
    private Date endTime;

    private String customerNote;

    private NailCustomer nailCustomer;
    private NailEmployee nailEmployee;
    private NailStore store;

    private List<NailCustomerService> nailCustomerServices;

    @ManyToOne
//    @JsonProperty("customer")
    @JsonIgnore
    public NailCustomer getNailCustomer() {
        return nailCustomer;
    }

    public void setNailCustomer(NailCustomer nailCustomer) {
        this.nailCustomer = nailCustomer;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    @JsonSerialize(using=DateSerializer.class)
    @JsonProperty("start")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonSerialize(using=DateSerializer.class)
    @JsonProperty("end")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @ManyToOne
    @JsonIgnore
    public NailEmployee getNailEmployee() {
        return nailEmployee;
    }

    public void setNailEmployee(NailEmployee nailEmployee) {
        this.nailEmployee = nailEmployee;
    }

    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    public NailStore getStore() {
        return store;
    }

    public void setStore(NailStore store) {
        this.store = store;
    }

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailCustomerService> getNailCustomerServices() {
        return nailCustomerServices;
    }

    public void setNailCustomerServices(List<NailCustomerService> nailCustomerServices) {
        this.nailCustomerServices = nailCustomerServices;
    }


    /////////Transient attribute///////////
    private Long employeeId;
    private Long customerId;

    @Transient
    @JsonProperty("resourceId")
    public Long getEmployeeId() {
        return employeeId!= null? employeeId : (nailEmployee != null && nailEmployee.getId() != null)? nailEmployee.getId() : 0;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Transient
    @JsonProperty("customerId")
    public Long getCustomerId() {
        return customerId!= null? customerId : (nailCustomer != null && nailCustomer.getId() != null)? nailCustomer.getId() : 0;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    private String customerName;

    @Transient
    @JsonProperty("title")
    public String getCustomerName() {
        return this.nailCustomer != null? this.nailCustomer.getFirstName() + " " + this.nailCustomer.getLastName() : "" + " - " + this.nailCustomer.getPhone();
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

