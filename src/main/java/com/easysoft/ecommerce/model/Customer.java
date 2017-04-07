package com.easysoft.ecommerce.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "users")
public class Customer extends AbstractEntity  {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String tempPassword;
    private Site site;
    private String email;
    private String siteAdmin;
    private String siteUser;
    private String blocked;
    /*siteCode also is prefix string of subDomain. Ex: siteCode m01 -> subDomain will be m01.example.com
    * This code will be exactly the same with site.siteCode*/
    private String siteCode;

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

    public Customer() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getSiteAdmin() {
        return convertActiveFlag(siteAdmin);
    }

    public void setSiteAdmin(String siteAdmin) {
        this.siteAdmin = convertActiveFlag(siteAdmin);
    }

    public String getSiteUser() {
        return convertActiveFlag(siteUser);
    }

    public void setSiteUser(String siteUser) {
        this.siteUser = convertActiveFlag(siteUser);
    }

    public String getBlocked() {
        return convertActiveFlag(blocked);
    }

    public void setBlocked(String blocked) {
        this.blocked = convertActiveFlag(blocked);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    /**
     * ***********transient properties declare here***************
     */
    @Transient
    public boolean isBlockedAsBoolean() {
        return "Y".equals(getBlocked());
    }

}
