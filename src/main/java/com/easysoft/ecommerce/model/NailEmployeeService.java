package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table (name="nail_employee_service")
public class NailEmployeeService extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private long servicePay;
    private long tipPay;

    private NailEmployee nailEmployee;
    private NailCustomerService nailCustomerService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    public NailCustomerService getNailCustomerService() {
        return nailCustomerService;
    }

    public void setNailCustomerService(NailCustomerService nailCustomerService) {
        this.nailCustomerService = nailCustomerService;
    }

    @ManyToOne
    @JsonIgnore
    public NailEmployee getNailEmployee() {
        return nailEmployee;
    }

    public void setNailEmployee(NailEmployee nailEmployee) {
        this.nailEmployee = nailEmployee;
    }

    public long getServicePay() {
        return servicePay;
    }

    public void setServicePay(long servicePay) {
        this.servicePay = servicePay;
    }

    public long getTipPay() {
        return tipPay;
    }

    public void setTipPay(long tipPay) {
        this.tipPay = tipPay;
    }

    /////////Transient attribute///////////
    private Long employeeId;
    private Long customerServiceId;

    @Transient
    public Long getEmployeeId() {
        return employeeId!= null? employeeId : (nailEmployee != null && nailEmployee.getId() != null)? nailEmployee.getId() : 0;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Transient
    public Long getCustomerServiceId() {
        return customerServiceId!= null? customerServiceId : (nailCustomerService != null && nailCustomerService.getId() != null)? nailCustomerService.getId() : 0;
    }

    public void setCustomerServiceId(Long customerServiceId) {
        this.customerServiceId = customerServiceId;
    }
}
