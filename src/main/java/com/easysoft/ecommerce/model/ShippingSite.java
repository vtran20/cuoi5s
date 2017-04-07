package com.easysoft.ecommerce.model;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "shipping_site")
public class ShippingSite extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String usePriceBySite;
    private Long priceBySite;
    private String usePriceByProduct;
    private Long priceByProduct;
    private Integer percentOfFirstProduct;
    private Site site;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsePriceBySite() {
        return usePriceBySite;
    }

    public void setUsePriceBySite(String usePriceBySite) {
        this.usePriceBySite = usePriceBySite;
    }

    public Long getPriceBySite() {
        return priceBySite;
    }

    public void setPriceBySite(Long priceBySite) {
        this.priceBySite = priceBySite;
    }

    public String getUsePriceByProduct() {
        return usePriceByProduct;
    }

    public void setUsePriceByProduct(String usePriceByProduct) {
        this.usePriceByProduct = usePriceByProduct;
    }

    public Long getPriceByProduct() {
        return priceByProduct;
    }

    public void setPriceByProduct(Long priceByProduct) {
        this.priceByProduct = priceByProduct;
    }

    public int getPercentOfFirstProduct() {
        return percentOfFirstProduct;
    }

    public void setPercentOfFirstProduct(int percentOfFirstProduct) {
        this.percentOfFirstProduct = percentOfFirstProduct;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
