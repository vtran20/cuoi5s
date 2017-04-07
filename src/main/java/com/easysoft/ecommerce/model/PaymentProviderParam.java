package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;

/**
 * This entity is used to store sessionObject.
 *
 * User: vtran
 * Date: Jun 1, 2010
 * Time: 5:46:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name="payment_provider_params")
public class PaymentProviderParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long paymentProviderId;

    private PaymentProvider paymentProvider;

    private String key;
    private String value;
    
    @Id
    @Column (name = "payment_provider_id")
    public Long getPaymentProviderId() {
        return paymentProviderId;
    }

    public void setPaymentProviderId(Long paymentProviderId) {
        this.paymentProviderId = paymentProviderId;
    }

    @ManyToOne
    @JoinColumn (name = "payment_provider_id")
    public PaymentProvider getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    @Id
    @Column (name = "payment_key", length = 50)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column (name = "payment_value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
