package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="shipping_location")
public class ShippingLocation extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private ShippingType shippingType;
    private String fromLocation;
    private String toLocation;//base on city
    private String areaCode;

    @Column(length = 20)
    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    @Column(length = 20)
    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }



    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @ManyToOne
    @JoinColumn(name = "shipping_type_code", insertable = false, updatable = false)
    public ShippingType getShippingType() {
        return shippingType;
    }

    public void setShippingType(ShippingType shippingType) {
        this.shippingType = shippingType;
    }

}
