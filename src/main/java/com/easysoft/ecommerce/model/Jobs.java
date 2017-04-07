package com.easysoft.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table (name = "jobs")
public class Jobs extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    private String name;
    private String method;
    private String description;
    private String status;
    private Date updatedDate;

//    private Site site;

    public Jobs() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column (unique = true)
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

//    @ManyToOne
//    public Site getSite() {
//        return site;
//    }
//
//    public void setSite(Site site) {
//        this.site = site;
//    }

}
