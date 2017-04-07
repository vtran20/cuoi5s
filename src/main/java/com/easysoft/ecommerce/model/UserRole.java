package com.easysoft.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;
    private String role;
    private Site site;


    public UserRole() {
    }

    public UserRole(String role) {
        this.role = role;
    }

    @Id
    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @Column(name = "role")
    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }

    @Id
    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

}
