package com.easysoft.ecommerce.model;

//org.hibernate.LazyInitializationException: could not initialize proxy - no Session
/*
* Fix by using org.codehaus.jackson.annotate.JsonIgnore and comment out
*
*         <!--<dependency>-->
            <!--<groupId>com.fasterxml.jackson.core</groupId>-->
            <!--<artifactId>jackson-databind</artifactId>-->
            <!--<version>2.9.6</version>-->
        <!--</dependency>-->
        <!--<dependency>-->
            <!--<groupId>com.fasterxml.jackson.core</groupId>-->
            <!--<artifactId>jackson-annotations</artifactId>-->
            <!--<version>2.9.6</version>-->
        <!--</dependency>-->

* */
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table (name="nail_customer")

public class NailCustomer extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String active;
    private String status;

    private NailStore store;
    private List<NailCustomerService> nailCustomerServices;

    private Date checkIn;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "active", nullable = true, length = 1)
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    public NailStore getStore() {
        return store;
    }

    public void setStore(NailStore store) {
        this.store = store;
    }

    @OneToMany(mappedBy = "nailCustomer", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailCustomerService> getNailCustomerServices() {
        return nailCustomerServices;
    }

    public void setNailCustomerServices(List<NailCustomerService> nailCustomerServices) {
        this.nailCustomerServices = nailCustomerServices;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /////////////////////////Transient Attribute////////////////////////
}
