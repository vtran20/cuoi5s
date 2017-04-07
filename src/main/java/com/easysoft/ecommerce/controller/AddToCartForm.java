package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.model.Product;

import javax.validation.constraints.Min;
import java.util.List;

public class AddToCartForm {

    private int quantity;
    private Long productId;
    private Long productVariantId;

    @Min(1)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Min(1)
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Min(1)
    public Long getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(Long productVariantId) {
        this.productVariantId = productVariantId;
    }
}
