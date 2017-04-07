package com.easysoft.ecommerce.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Date createdDate;

    public AbstractEntity(Date createdDate) {
        this.createdDate = createdDate;
    }

    public AbstractEntity() {
        this.createdDate = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * ***********transient properties declare here***************
     */
    @Transient
    public String convertActiveFlag(String active) {
        if ("on".equalsIgnoreCase(active) || "Y".equalsIgnoreCase(active) || "true".equals(active)) {
            return "Y";
        } else {
            return "N";
        }
    }
    @Transient
    public boolean isEmptyId() {
        return (this.id == null || this.id == 0);
    }

}
