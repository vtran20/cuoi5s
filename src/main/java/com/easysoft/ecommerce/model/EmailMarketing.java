package com.easysoft.ecommerce.model;

import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="email_marketing")
public class EmailMarketing extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String optin;
    private String invalid;
    private long marketingOrder;
    private Date updatedDate;

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

    @Index(name = "emailIndex")
    @Column(nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOptin() {
        return optin;
    }

    public void setOptin(String optin) {
        this.optin = optin;
    }

    public String getInvalid() {
        return invalid;
    }

    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }

    public long getMarketingOrder() {
        return marketingOrder;
    }

    public void setMarketingOrder(long marketingOrder) {
        this.marketingOrder = marketingOrder;
    }

    public Date getUpdatedDate()
    {
        return updatedDate;
    }

    public void setUpdatedDate( Date updatedDate )
    {
        this.updatedDate = updatedDate;
    }
}
