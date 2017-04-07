package com.easysoft.ecommerce.model;


import com.easysoft.ecommerce.util.WebUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="shipping_type")
public class ShippingType  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String shippingTypeCode;
    private String shippingTypeName;
    private int sequence;
    private String active;
    private List<ShippingFee> shippingFee;
    private List<ShippingLocation> shippingLocations;

    @Id
    @Column(name="shipping_type_code", length = 20)
    public String getShippingTypeCode() {
        return shippingTypeCode;
    }

    public void setShippingTypeCode(String shippingTypeCode) {
        this.shippingTypeCode = shippingTypeCode;
    }

    public String getShippingTypeName() {
        return shippingTypeName;
    }

    public void setShippingTypeName(String shippingTypeName) {
        this.shippingTypeName = shippingTypeName;
    }

    @OneToMany(mappedBy="shippingType")
    public List<ShippingFee> getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(List<ShippingFee> shippingFee) {
        this.shippingFee = shippingFee;
    }

    @OneToMany(mappedBy="shippingType")
    public List<ShippingLocation> getShippingLocations() {
        return shippingLocations;
    }

    public void setShippingLocations(List<ShippingLocation> shippingLocations) {
        this.shippingLocations = shippingLocations;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Column(name = "active", nullable = true, length = 1)
    public String getActive() {
        return WebUtil.convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = WebUtil.convertActiveFlag(active);
    }
}
