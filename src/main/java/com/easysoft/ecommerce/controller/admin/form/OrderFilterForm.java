package com.easysoft.ecommerce.controller.admin.form;

import java.util.Date;

public class OrderFilterForm {

    private String[] orderStatus;
    private String city;
    private Date startDate;
    private Date endDate;

    public String[] getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String[] orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
