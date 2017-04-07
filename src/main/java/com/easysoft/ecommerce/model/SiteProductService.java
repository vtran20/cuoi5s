package com.easysoft.ecommerce.model;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.*;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// Using @Cache instead @Cacheable to avoid HHH-5303
@Table(name = "site_service")
public class SiteProductService extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private String active;//Y: create new Product Service, N: processed and expired date moved to Site table
    private Date startDate;
    private Date endDate;

    private Site site;
    private Product product;
    private ProductVariant productVariant;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @ManyToOne
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne
    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }
}
