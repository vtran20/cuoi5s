package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table (name="product_attribute")
public class ProductAttribute extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String name;
    private String key;
    private int sequence;
    private boolean active;
    private ProductAttributeGroup attributeGroup;
    private List<ProductAttributeValue> attributeValues;

    public ProductAttribute() {
        super();
    }

    public ProductAttribute(Date createdDate, String name, int sequence) {
        super(createdDate);
        this.name = name;
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @ManyToOne
    @JoinColumn(name="group_attribute_id")
    public ProductAttributeGroup getAttributeGroup() {
        return attributeGroup;
    }

    public void setAttributeGroup(ProductAttributeGroup attributeGroup) {
        this.attributeGroup = attributeGroup;
    }

    @OneToMany(mappedBy="productAttribute")
    public List<ProductAttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<ProductAttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    @Column(name="attribute_key" ,nullable = false, length = 50)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
