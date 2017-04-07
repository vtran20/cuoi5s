package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table (name="product_file")
public class ProductFile extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private Product product;
    private String fileType;
    private String fileName;
    private String uri;
    private String crop;
    private String isDefault;

    @ManyToOne
    @JoinColumn(name="product_id")
    @JsonIgnore
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name="is_default")
    public String getDefault() {
        return isDefault;
    }

    public void setDefault(String aDefault) {
        isDefault = aDefault;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Column(nullable = true, length = 20)
    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }
}
