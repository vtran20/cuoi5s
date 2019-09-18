package com.easysoft.ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;


@Entity
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// Using @Cache instead @Cacheable to avoid HHH-5303
@Table(name = "payment_provider")
public class PaymentProvider extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private String htmlCode;
    private String providerClass;
    private int sequence;
    private String active;

    private List<PaymentProviderParam> paymentParams;
    private List<PaymentProviderSite> paymentProviderSites;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public void setHtmlCode(String htmlCode) {
        this.htmlCode = htmlCode;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getProviderClass() {
        return providerClass;
    }

    public void setProviderClass(String providerClass) {
        this.providerClass = providerClass;
    }

    @OneToMany(mappedBy = "paymentProvider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<PaymentProviderParam> getPaymentParams() {
        return paymentParams;
    }

    public void setPaymentParams(List<PaymentProviderParam> paymentParams) {
        this.paymentParams = paymentParams;
    }

    @OneToMany(mappedBy = "paymentProvider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<PaymentProviderSite> getPaymentProviderSites() {
        return paymentProviderSites;
    }

    public void setPaymentProviderSites(List<PaymentProviderSite> paymentProviderSites) {
        this.paymentProviderSites = paymentProviderSites;
    }

    @Column(name = "active", nullable = true, length = 1)
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }
}
