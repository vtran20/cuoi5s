package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name="nail_service")
public class NailService extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private long price;
    private int minutes;
    private String active;
    private float sequence;

    private NailStore store;
    private NailService group;
    private List <NailService> services;
    private List<NailCustomerService> nailCustomerServices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Column(name = "active", nullable = true, length = 1)
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    public NailStore getStore() {
        return store;
    }

    public void setStore(NailStore store) {
        this.store = store;
    }

    @OneToMany(mappedBy = "nailService", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailCustomerService> getNailCustomerServices() {
        return nailCustomerServices;
    }

    public void setNailCustomerServices(List<NailCustomerService> nailCustomerServices) {
        this.nailCustomerServices = nailCustomerServices;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    public NailService getGroup() {
        return group;
    }

    public void setGroup(NailService group) {
        this.group = group;
    }

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<NailService> getServices() {
        return services;
    }

    public void setServices(List<NailService> services) {
        this.services = services;
    }

    public float getSequence() {
        return sequence;
    }

    public void setSequence(float sequence) {
        this.sequence = sequence;
    }

    /////////Transient attribute///////////
    private Long groupId;

    @Transient
    public Long getGroupId() {
        return groupId != null? groupId : (group != null && group.getId() != null)? group.getId() : 0;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
