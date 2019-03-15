package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "nail_store")
public class NailStore extends AbstractEntity {
    private static final long serialVersionUID = 1L;

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

    private String hourSun;
    private String hourMon;
    private String hourTue;
    private String hourWed;
    private String hourThu;
    private String hourFri;
    private String hourSat;

    private Site site;
    private List<NailCustomer> nailCustomers;
    private List<NailEmployee> nailEmployees;
    private List<NailService> nailServices;
    private List<NailCustomerPayment> nailCustomerPayments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "active", nullable = true, length = 1)
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
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

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailCustomer> getNailCustomers() {
        return nailCustomers;
    }

    public void setNailCustomers(List<NailCustomer> nailCustomers) {
        this.nailCustomers = nailCustomers;
    }

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailEmployee> getNailEmployees() {
        return nailEmployees;
    }

    public void setNailEmployees(List<NailEmployee> nailEmployees) {
        this.nailEmployees = nailEmployees;
    }

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailService> getNailServices() {
        return nailServices;
    }

    public void setNailServices(List<NailService> nailServices) {
        this.nailServices = nailServices;
    }

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailCustomerPayment> getNailCustomerPayments() {
        return nailCustomerPayments;
    }

    public void setNailCustomerPayments(List<NailCustomerPayment> nailCustomerPayments) {
        this.nailCustomerPayments = nailCustomerPayments;
    }

    public String getHourSun() {
        return hourSun;
    }

    public void setHourSun(String hourSun) {
        this.hourSun = hourSun;
    }

    public String getHourMon() {
        return hourMon;
    }

    public void setHourMon(String hourMon) {
        this.hourMon = hourMon;
    }

    public String getHourTue() {
        return hourTue;
    }

    public void setHourTue(String hourTue) {
        this.hourTue = hourTue;
    }

    public String getHourWed() {
        return hourWed;
    }

    public void setHourWed(String hourWed) {
        this.hourWed = hourWed;
    }

    public String getHourThu() {
        return hourThu;
    }

    public void setHourThu(String hourThu) {
        this.hourThu = hourThu;
    }

    public String getHourFri() {
        return hourFri;
    }

    public void setHourFri(String hourFri) {
        this.hourFri = hourFri;
    }

    public String getHourSat() {
        return hourSat;
    }

    public void setHourSat(String hourSat) {
        this.hourSat = hourSat;
    }
}
