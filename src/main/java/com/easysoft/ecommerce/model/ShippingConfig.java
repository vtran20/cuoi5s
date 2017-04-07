package com.easysoft.ecommerce.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="shipping_config")
public class ShippingConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String shippingParam;
    private String shippingValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Id
    @Column(length = 20)
    public String getShippingParam() {
        return shippingParam;
    }

    public void setShippingParam(String shippingParam) {
        this.shippingParam = shippingParam;
    }

    public String getShippingValue() {
        return shippingValue;
    }

    public void setShippingValue(String shippingValue) {
        this.shippingValue = shippingValue;
    }
}
