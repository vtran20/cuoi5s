package com.easysoft.ecommerce.model;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * MenuTemplate is used for the pages that have logic or need to control from backend. Ex: News, Contact Us ...
 */
@Entity
@Table(name = "menu_template")
public class MenuTemplate extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String uri;
    private String designUrl;
    private String active;
    private int sequence;

    public MenuTemplate() {
        super();
    }

    public MenuTemplate(Date createdDate, String name, int sequence) {
        super(createdDate);
        this.name = name;
        this.sequence = sequence;
    }


    @Basic(optional = false)
    @Column(name = "uri", nullable = true, length = 255)
    @org.hibernate.annotations.Index(name = "uriIndex")
    @Size(max = 255)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = true, length = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Column(name = "active", nullable = true, length = 1)
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    public String getDesignUrl() {
        return designUrl;
    }

    public void setDesignUrl(String designUrl) {
        this.designUrl = designUrl;
    }
}
