package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "shipping_fee")
public class ShippingFee extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private ShippingType shippingType;
    private Integer weightFrom;
    private Integer weightTo;
    private Integer internalFee;
    private Integer area1Fee;
    private Integer area2Fee;
    private Integer area3Fee;
    private Integer area4Fee;

    public ShippingFee() {}

    public Integer getWeightFrom() {
        return weightFrom;
    }

    public void setWeightFrom(Integer weightFrom) {
        this.weightFrom = weightFrom;
    }

    public Integer getWeightTo() {
        return weightTo;
    }

    public void setWeightTo(Integer weightTo) {
        this.weightTo = weightTo;
    }

    @ManyToOne
    @JoinColumn(name = "shipping_type_code", insertable = false, updatable = false)
    public ShippingType getShippingType() {
        return shippingType;
    }

    public void setShippingType(ShippingType shippingType) {
        this.shippingType = shippingType;
    }

    public Integer getInternalFee() {
        return internalFee;
    }

    public void setInternalFee(Integer internalFee) {
        this.internalFee = internalFee;
    }

    public Integer getArea1Fee() {
        return area1Fee;
    }

    public void setArea1Fee(Integer area1Fee) {
        this.area1Fee = area1Fee;
    }

    public Integer getArea2Fee() {
        return area2Fee;
    }

    public void setArea2Fee(Integer area2Fee) {
        this.area2Fee = area2Fee;
    }

    public Integer getArea3Fee() {
        return area3Fee;
    }

    public void setArea3Fee(Integer area3Fee) {
        this.area3Fee = area3Fee;
    }

    public Integer getArea4Fee() {
        return area4Fee;
    }

    public void setArea4Fee(Integer area4Fee) {
        this.area4Fee = area4Fee;
    }
}
