package com.easysoft.ecommerce.model;


import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Indexed
@Entity
@Table (name="product_attribute_value")
public class ProductAttributeValue extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private Product product;
    private ProductAttribute productAttribute;
    private String key;
    private String value;

    public ProductAttributeValue() {
        super();
    }

    @ManyToOne
    @JoinColumn(name="product_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne
    public ProductAttribute getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(ProductAttribute productAttribute) {
        this.productAttribute = productAttribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Field(name= "key", index= Index.UN_TOKENIZED, store= Store.NO)
    @Column(name="attribute_key" ,nullable = false, length = 50)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
