package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name="nail_customer_service")
public class NailCustomerService extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private Date serviceDate;
    private Date checkIn;

    private Long price;
    private String status;

    private NailCustomer nailCustomer;
    private NailService nailService;

    @ManyToOne
    @JsonIgnore
    public NailCustomer getNailCustomer() {
        return nailCustomer;
    }

    public void setNailCustomer(NailCustomer nailCustomer) {
        this.nailCustomer = nailCustomer;
    }

    @ManyToOne
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    /////////Transient attribute///////////
    private Long serviceId;
    private Long customerId;

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


}

