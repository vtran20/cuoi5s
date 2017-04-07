package com.easysoft.ecommerce.model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name="product_attribute_group")
public class ProductAttributeGroup extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String name;
    private int sequence;
    private boolean active;

    private List<Category> categories;

    private List<ProductAttribute> productAttributes;

    public ProductAttributeGroup() {
        super();
    }

    public ProductAttributeGroup(Date createdDate, String name, int sequence) {
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

    @OneToMany(mappedBy="attributeGroup")
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @OneToMany(mappedBy="attributeGroup")
    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }
}
