package com.easysoft.ecommerce.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE) // Using @Cache instead @Cacheable to avoid HHH-5303
@Table (name="site_param")
@IdClass(SiteParamPK.class)
public class SiteParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long siteId;
    private String key;
    private String value;

    public SiteParam() {
    }

    public SiteParam( String key, String value, Site site) {
        this.siteId = site.getId();
        this.key = key;
        this.value = value;
        this.site = site;
    }

    @Id
    @Column (name = "SITE_ID")
    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    Site site;
    @Id
    @ManyToOne
    @JoinColumn(name = "SITE_ID", insertable = false, updatable = false)
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Id
    @Column (name = "PARAM_KEY")
    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }

    @Column (name = "PARAM_VALUE")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
