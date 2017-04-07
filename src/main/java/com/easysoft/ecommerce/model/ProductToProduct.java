package com.easysoft.ecommerce.model;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * 
 * User: vtran
 * Date: Jun 3, 2010
 * Time: 5:20:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name = "product_to_product")
public class ProductToProduct extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private Product product;
    private Product relateProduct;
    private int relationType;

    @ManyToOne
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne
    public Product getRelateProduct() {
        return relateProduct;
    }

    public void setRelateProduct(Product relateProduct) {
        this.relateProduct = relateProduct;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }
}
