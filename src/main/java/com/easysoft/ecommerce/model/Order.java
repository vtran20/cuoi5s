package com.easysoft.ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="orders")
public class Order extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    public static String NEW_ORDER = "NEW_ORDER";
    public static String CANCELLED = "CANCELLED";
    public static String PAID = "PAID";
    public static String SHIPPED = "SHIPPED";
    private Long userId;

    //billing address
    private String firstName;
    private String lastName;
    private String address_1;
    private String address_2;
    private String district;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phone;

    //shipping address
    private String firstNameShipping;
    private String lastNameShipping;
    private String address_1Shipping;
    private String address_2Shipping;
    private String districtShipping;
    private String cityShipping;
    private String stateShipping;
    private String zipCodeShipping;
    private String countryShipping;
    private String phoneShipping;

    private String email;
    private String status;
    private Long totalPrice;
    private Date updatedDate;
    private PaymentProviderSite provider;
    public static Integer OWNER_PARTNER = 1;
    public static Integer CLIENT = 2;
    /*orderType=1: orders for the website upgrade/renew/buy service of their website
    * orderType=2: orders from the client website
    * */
    private Integer orderType;
    private Long commission;
    private Site site;
    private String thirdPartyOrderNumber;

    public Order() {
        super();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getFirstNameShipping() {
        return firstNameShipping;
    }

    public void setFirstNameShipping(String firstNameShipping) {
        this.firstNameShipping = firstNameShipping;
    }

    public String getLastNameShipping() {
        return lastNameShipping;
    }

    public void setLastNameShipping(String lastNameShipping) {
        this.lastNameShipping = lastNameShipping;
    }

    public String getAddress_1Shipping() {
        return address_1Shipping;
    }

    public void setAddress_1Shipping(String address_1Shipping) {
        this.address_1Shipping = address_1Shipping;
    }

    public String getAddress_2Shipping() {
        return address_2Shipping;
    }

    public void setAddress_2Shipping(String address_2Shipping) {
        this.address_2Shipping = address_2Shipping;
    }

    public String getDistrictShipping() {
        return districtShipping;
    }

    public void setDistrictShipping(String districtShipping) {
        this.districtShipping = districtShipping;
    }

    public String getCityShipping() {
        return cityShipping;
    }

    public void setCityShipping(String cityShipping) {
        this.cityShipping = cityShipping;
    }

    public String getStateShipping() {
        return stateShipping;
    }

    public void setStateShipping(String stateShipping) {
        this.stateShipping = stateShipping;
    }

    public String getZipCodeShipping() {
        return zipCodeShipping;
    }

    public void setZipCodeShipping(String zipCodeShipping) {
        this.zipCodeShipping = zipCodeShipping;
    }

    public String getCountryShipping() {
        return countryShipping;
    }

    public void setCountryShipping(String countryShipping) {
        this.countryShipping = countryShipping;
    }

    public String getPhoneShipping() {
        return phoneShipping;
    }

    public void setPhoneShipping(String phoneShipping) {
        this.phoneShipping = phoneShipping;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    @ManyToOne
    @JsonIgnore
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public PaymentProviderSite getProvider() {
        return provider;
    }

    public void setProvider(PaymentProviderSite provider) {
        this.provider = provider;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getCommission() {
        return commission;
    }

    public void setCommission(Long commission) {
        this.commission = commission;
    }

    @Column(nullable = true, length = 50)
    public String getThirdPartyOrderNumber() {
        return thirdPartyOrderNumber;
    }

    public void setThirdPartyOrderNumber(String thirdPartyOrderNumber) {
        this.thirdPartyOrderNumber = thirdPartyOrderNumber;
    }
}
