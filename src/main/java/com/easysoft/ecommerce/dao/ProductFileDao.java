package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductFile;

import java.util.List;


public interface ProductFileDao extends GenericDao<ProductFile, Long> {
    List<ProductFile> findProductFiles (Long productId, String fileType);
    public ProductFile getDefaultImage (Long productId, String fileType);
    void deleteProductFiles (Long productId, String fileType);
    void addMoreImages (Product product, int numImages);

    void resetDefaultProductImage(Long productId);

    ProductFile getProductImage(Long imageId, Long productId);
}