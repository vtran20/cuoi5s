package com.easysoft.ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table (name="nail_customer_service")
public class NailCustomerService extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private Date serviceDate;

    private String customerNote;
    private long price;

    private NailCustomer nailCustomer;
    private NailCustomerAppointment appointment;
    private NailService nailService;
    private List<NailEmployeeService> nailEmployeeServices;

    @ManyToOne
    @JsonIgnore
    public NailCustomer getNailCustomer() {
        return nailCustomer;
    }

    public void setNailCustomer(NailCustomer nailCustomer) {
        this.nailCustomer = nailCustomer;
    }

    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    public NailCustomerAppointment getAppointment() {
        return appointment;
    }

    public void setAppointment(NailCustomerAppointment appointment) {
        this.appointment = appointment;
    }

    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    public NailService getNailService() {
        return nailService;
    }

    public void setNailService(NailService nailService) {
        this.nailService = nailService;
    }

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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @OneToMany(mappedBy = "nailCustomerService", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailEmployeeService> getNailEmployeeServices() {
        return nailEmployeeServices;
    }

    public void setNailEmployeeServices(List<NailEmployeeService> nailEmployeeServices) {
        this.nailEmployeeServices = nailEmployeeServices;
    }

    /////////Transient attribute///////////
    private Long serviceId;
    private Long customerId;
    private Long appointmentId;

    @Transient
    public Long getServiceId() {
        return serviceId != null? serviceId: (nailService != null && nailService.getId() != null)? nailService.getId() : 0;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Transient
    public Long getCustomerId() {
        return customerId != null ? customerId: (nailCustomer != null && nailCustomer.getId() != null)? nailCustomer.getId() : 0;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAppointmentId() {
        return appointmentId != null? appointmentId: (appointment != null && appointment.getId() != null)? appointment.getId() : 0;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
}

