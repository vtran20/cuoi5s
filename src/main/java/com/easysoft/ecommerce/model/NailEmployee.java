package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table (name="nail_employee")
public class NailEmployee extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String active;
    private Date checkIn;

    private NailStore store;
    private List<NailEmployeeService> nailEmployeeServices;

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

    @OneToMany(mappedBy = "nailEmployee", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailEmployeeService> getNailEmployeeServices() {
        return nailEmployeeServices;
    }

    public void setNailEmployeeServices(List<NailEmployeeService> nailEmployeeServices) {
        this.nailEmployeeServices = nailEmployeeServices;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    /////////Transient attribute///////////
    @Transient
    @JsonProperty("name")
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

}
