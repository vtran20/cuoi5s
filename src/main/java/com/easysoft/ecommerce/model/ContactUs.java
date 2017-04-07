package com.easysoft.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="contact_us")
public class ContactUs extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    private String addressTo;
    private String message;
    private String sendersEmail;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String read;
    private Site site;

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendersEmail() {
        return sendersEmail;
    }

    public void setSendersEmail(String sendersEmail) {
        this.sendersEmail = sendersEmail;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Column(name = "reviewed", nullable = true, length = 1)
    public String getRead() {
        return convertActiveFlag(read);
    }

    public void setRead(String read) {
        this.read = convertActiveFlag(read);
    }
}
