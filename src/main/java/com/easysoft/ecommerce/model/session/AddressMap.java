package com.easysoft.ecommerce.model.session;

import java.util.HashMap;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AddressMap will contain address information.
 *
 * User: Vu Tran
 * Date: Aug 18, 2010
 * Time: 4:23:36 PM
 * To change this template use File | Settings | File Templates.
 */
//@XStreamAlias("Address")
public class AddressMap extends HashMap <String, String> {

    private static final long serialVersionUID = 1L;

    public static final String FIRST_NAME_KEY = "FIRST_NAME";
    public static final String LAST_NAME_KEY = "LAST_NAME";
    public static final String ADDRESS_LINE_KEY = "ADDRESS_LINE";
    public static final String DISTRICT_KEY = "DISTRICT";
    public static final String CITY_KEY = "CITY";
    public static final String STATE_KEY = "STATE";
    public static final String ZIP_CODE_KEY = "ZIP_CODE";
    public static final String PHONE_KEY = "PHONE";
    public static final String EMAIL_KEY = "EMAIL";
    public static final String COUNTRY_KEY = "COUNTRY";

    public String getFirstName() {
        return this.get(FIRST_NAME_KEY);
    }

    public String getLastName() {
        return this.get(LAST_NAME_KEY);
    }

    public String getStreet() {
        return this.get(ADDRESS_LINE_KEY);
    }

    public String getDistrict() {
        return this.get(DISTRICT_KEY);
    }

    public String getCity() {
        return this.get(CITY_KEY);
    }

    public String getState() {
        return this.get(STATE_KEY);
    }

    public String getZipCode() {
        return this.get(ZIP_CODE_KEY);
    }

    public String getPhone() {
        return this.get(PHONE_KEY);
    }

    public String getEmail() {
        return this.get(EMAIL_KEY);
    }

    public String getCountry() {
        return this.get(COUNTRY_KEY);
    }

    public void setFirstName(String value) {
        this.put(FIRST_NAME_KEY, value);
    }

    public void setLastName(String value) {
        this.put(LAST_NAME_KEY, value);
    }

    public void setStreet(String value) {
        this.put(ADDRESS_LINE_KEY, value);
    }

    public void setDistrict(String value) {
        this.put(DISTRICT_KEY, value);
    }

    public void setZipCode(String value) {
        this.put(ZIP_CODE_KEY, value);
    }

    public void setCity(String value) {
        this.put(CITY_KEY, value);
    }

    public void setState(String value) {
        this.put(STATE_KEY, value);
    }

    public void setPhone(String value) {
        this.put(PHONE_KEY, value);
    }

    public void setEmail(String value) {
        this.put(EMAIL_KEY, value);
    }

    public void setCountry(String value) {
        this.put(COUNTRY_KEY, value);
    }

    public String getString(String key) {
        return this.get(key);
    }

    public void set(String key, String value) {
        this.put(key, value);
    }
}
