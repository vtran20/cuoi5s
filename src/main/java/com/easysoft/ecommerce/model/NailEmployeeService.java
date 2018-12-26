package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name="nail_employee_service")
public class NailEmployeeService extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private long creditPay;
    private long cashPay;
    private long giftPay;
    private long checkPay;
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
