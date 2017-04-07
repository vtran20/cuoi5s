package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Indexed
@Entity
@Table (name="product_variant")
public class ProductVariant extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private Product product;
    private long price;
    /*pricePromo is price that after discount. This value will be set to 0 if end promotion (base on promoStartDate and promoEndDate of Product table)*/
    private long pricePromo;
    private long originalPrice;
    private Date promoStartDate;
    private Date promoEndDate;
    private String sku;
    private String imageUrl;
    private String colorCode;
    private String colorName;
    private String colorCodeRBG;
    private String colorCodeForFilter;
    private String sizeCode;
    private String sizeName;
    private String sizeCodeForFilter;
    private String barcode;
    private String active;
    private int inventory;
    private String location;
    private int sequence;
    private Date updatedDate;
    private String isDefault;
    

//    public ProductVariant() {
//        super();
//    }
//
    @ManyToOne
    @JoinColumn(name="product_id")
    @JsonIgnore
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getPricePromo() {
        return pricePromo;
    }

    public void setPricePromo(long pricePromo) {
        this.pricePromo = pricePromo;
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Date getPromoStartDate() {
        return promoStartDate;
    }

    public void setPromoStartDate(Date promoStartDate) {
        if (promoStartDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(promoStartDate);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            this.promoStartDate = cal.getTime();
        } else {
            this.promoStartDate = promoStartDate;
        }
    }

    public Date getPromoEndDate() {
        return promoEndDate;
    }

    public void setPromoEndDate(Date promoEndDate) {
        this.promoEndDate = promoEndDate;
    }
    
    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Field(name= "inventory",index=Index.UN_TOKENIZED, store=Store.NO)
    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    @Field(name= "location",index=Index.UN_TOKENIZED, store=Store.YES)
    @Column(length = 5)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

//    @Field(index=Index.UN_TOKENIZED, store=Store.NO)
    @Boost(2.0f)
    @Column (name = "sku", length = 20)
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Field(name= "color", index=Index.UN_TOKENIZED, store=Store.NO)
    public String getColorCodeForFilter() {
        return colorCodeForFilter;
    }

    public void setColorCodeForFilter(String colorCodeForFilter) {
        this.colorCodeForFilter = colorCodeForFilter;
    }

    @Field(name= "size",index=Index.UN_TOKENIZED, store=Store.NO)
    public String getSizeCodeForFilter() {
        return sizeCodeForFilter;
    }

    public void setSizeCodeForFilter(String sizeCodeForFilter) {
        this.sizeCodeForFilter = sizeCodeForFilter;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getColorCodeRBG() {
        return colorCodeRBG;
    }

    public void setColorCodeRBG(String colorCodeRBG) {
        this.colorCodeRBG = colorCodeRBG;
    }

    @Column (name="is_default")
    public String getDefault() {
        return convertActiveFlag(isDefault);
    }

    public void setDefault(String aDefault) {
        isDefault = convertActiveFlag(aDefault);
    }
}
