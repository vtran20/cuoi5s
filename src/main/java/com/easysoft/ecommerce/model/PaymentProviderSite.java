package com.easysoft.ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
@Table(name = "payment_provider_site")
public class PaymentProviderSite extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private float sequence;
    private String businessEmail;
    private String merchantId;
    private String securePass;

    private Site site;
    private PaymentProvider paymentProvider;

    @Column(length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSequence() {
        return sequence;
    }

    public void setSequence(float sequence) {
        this.sequence = sequence;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSecurePass() {
        return securePass;
    }

    public void setSecurePass(String securePass) {
        this.securePass = securePass;
    }

    @ManyToOne
    @JsonIgnore
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @ManyToOne
    public PaymentProvider getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    @Column(length = 5000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
