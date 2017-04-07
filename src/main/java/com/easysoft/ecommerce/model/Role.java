package com.easysoft.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Role is defined as a privilege for a function. Role will be represented as a tree. If you have a role 'A', you will have
 * all roles that are the children of it
 */

@Entity
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private String role;
    private String childRole = "NONE";

    public Role() {
    }

    public Role(String role, String childRole) {
        this.childRole = childRole;
        this.role = role;
    }

    @Id
    @Column(name = "role" )
    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }

    @Id
    @Column(name = "child_role")
    public String getChildRole() {
        return childRole;
    }

    public void setChildRole(String childRole) {
        this.childRole = childRole;
    }
}
