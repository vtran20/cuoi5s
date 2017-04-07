package com.easysoft.ecommerce.model;


import com.easysoft.ecommerce.dao.filter.CategoryFilterFactory;
import com.easysoft.ecommerce.dao.filter.PriceLongBridge;
import com.easysoft.ecommerce.dao.filter.PriceRangeFilter;
import com.easysoft.ecommerce.dao.filter.SiteFilterFactory;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Indexed
@Entity
@Table (name="product")
@FullTextFilterDefs({
        @FullTextFilterDef(name = "priceRangeFilter", impl = PriceRangeFilter.class, cache = FilterCacheModeType.INSTANCE_AND_DOCIDSETRESULTS),
        @FullTextFilterDef(name = "siteFilter", impl = SiteFilterFactory.class, cache = FilterCacheModeType.INSTANCE_AND_DOCIDSETRESULTS),
        @FullTextFilterDef(name = "categoryFilter", impl = CategoryFilterFactory.class, cache = FilterCacheModeType.INSTANCE_AND_DOCIDSETRESULTS)
        })
public class Product extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private Date updatedDate;
    private String uri;

    private String name;
    private String description;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    /*This value show price or price range of a product. This value will be calculate when insert or update product base
    * on product variant*/
    private String displayPrice;
    /*This value show price promo or price promo range of a product. This value will be calculate when insert or update product base
    * on product variant*/
    private String displayPricePromo;
    /*Price min is used for sort, filter only. We don't use for showing on front-end. For this field, we have a job
    * to set value for it*/
    private long priceMin;
    private String model;
    private String imageUrl;
    private String metaDescription;
    private String metaKeyword;
    private String keyword;
    private String active;
    private float sequence;
    /*This value define variant group
    * N: No variant group.
    * C: Color group only
    * S: Size group only
    * B: Have Color and Size in variants
    * */
    public static String NO_VARIANT_GROUP = "N";
    public static String COLOR_GROUP = "C";
    public static String SIZE_GROUP = "S";
    public static String BOTH_COLOR_SIZE_GROUP = "B";
//    private String variantGroup;

    private Site site;
    private List<Category> categories;

    private List<ProductVariant> productVariant;
    private List<ProductToProduct> relatedProducts;
    private List<ProductAttributeValue> attributeValues;
    private List<ProductFile> productFiles;
    private Integer weight ;
    private String productMarketing;
    private String newProduct;

    /******These field is used for main site or partners*****/
    private String module; //Y: this product is module
    private String required; //Y: this product is required for the site
    private String onePay;  //Y: pay one time, N: pay by month
    /******These field is used for main site or partners*****/

    public Product() {
        super();
    }

    public Product(Date createdDate, String name, String authors, long price,
            String imageUrl, String metaDescription, String metaKeyword, String active) {
        super(createdDate);
        this.updatedDate = createdDate;
        this.name = name;
        this.description = authors;
        this.imageUrl = imageUrl;
        this.metaDescription = metaDescription;
        this.metaKeyword = metaKeyword;
        this.active = active;
    }

    @Basic(optional = false)
    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    @Column(name="uri")
    @org.hibernate.annotations.Index(name = "uriIndex")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Field(index=Index.TOKENIZED, store=Store.YES)
    @FieldBridge(impl = com.easysoft.ecommerce.model.lucene.UnsignBridge.class)
    @Boost(2.0f)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Field(index=Index.TOKENIZED, store=Store.YES)
    @Boost(1.5f)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    public String getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    public String getMetaKeyword() {
        return metaKeyword;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword;
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.NO)
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    public String getDisplayPricePromo() {
        return displayPricePromo;
    }

    public void setDisplayPricePromo(String displayPricePromo) {
        this.displayPricePromo = displayPricePromo;
    }
    
    @Field(index=Index.TOKENIZED, store=Store.YES)
    @Boost(2.0f)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @IndexedEmbedded
//    @ContainedIn
    @ManyToMany(mappedBy="products", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

//    @ManyToOne
//    @IndexedEmbedded
//    public Catalog getCatalog() {
//        return catalog;
//    }
//
//    public void setCatalog(Catalog catalog) {
//        this.catalog = catalog;
//    }

    public void addCategory(Category category) {
        if (!getCategories().contains(category)) {
            getCategories().add(category);
        }
        if (!category.getProducts().contains(this)) {
            category.getProducts().add(this);
        }
    }

    @IndexedEmbedded
    @ManyToOne
    @JsonIgnore
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

//    @ContainedIn
    @OneToMany(mappedBy="product", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<ProductToProduct> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<ProductToProduct> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    @IndexedEmbedded
    @OneToMany(mappedBy="product", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<ProductVariant> getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(List<ProductVariant> productVariant) {
        this.productVariant = productVariant;
    }

    @OneToMany(mappedBy="product", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<ProductFile> getProductFiles() {
        return productFiles;
    }

    public void setProductFiles(List<ProductFile> productFiles) {
        this.productFiles = productFiles;
    }

    @IndexedEmbedded
    @OneToMany(mappedBy="product", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<ProductAttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<ProductAttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.NO)
    public float getSequence() {
        return sequence;
    }

    public void setSequence(float sequence) {
        this.sequence = sequence;
    }

    @Field(name="price",index=Index.UN_TOKENIZED, store=Store.YES)
    @FieldBridge (impl = PriceLongBridge.class, params = @Parameter(name="padding", value="10"))
    public long getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(long priceMin) {
        this.priceMin = priceMin;
    }

    @Field(index=Index.TOKENIZED, store=Store.NO)
    @FieldBridge(impl = com.easysoft.ecommerce.model.lucene.UnsignBridge.class)
    @Boost(2.0f)
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

//    public String getVariantGroup() {
//        return variantGroup;
//    }
//
//    public void setVariantGroup(String variantGroup) {
//        this.variantGroup = variantGroup;
//    }

    @Column (name = "weight")
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Column (name = "attribute1")
    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    @Column (name = "attribute2")
    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    @Column (name = "attribute3")
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    @Column (name = "attribute4")
    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    @Column (name = "attribute5")
    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    @Column(name="product_marketing", length = 1)
    public String getProductMarketing() {
        return productMarketing;
    }

    public void setProductMarketing(String productMarketing) {
        this.productMarketing = productMarketing;
    }

    @Column(nullable = true, length = 1)
    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    public String getNewProduct() {
        return convertActiveFlag(newProduct);
    }

    public void setNewProduct(String newProduct) {
        this.newProduct = convertActiveFlag(newProduct);
    }

    @Column(nullable = true, length = 1)
    public String getModule() {
        return convertActiveFlag(module);
    }

    public void setModule(String module) {
        this.module = convertActiveFlag(module);
    }

    @Column(nullable = true, length = 1)
    public String getRequired() {
        return convertActiveFlag(required);
    }

    public void setRequired(String required) {
        this.required = convertActiveFlag(required);
    }

    @Column(nullable = true, length = 1)
    public String getOnePay() {
        return convertActiveFlag(onePay);
    }

    public void setOnePay(String onePay) {
        this.onePay = convertActiveFlag(onePay);
    }

    /**************transient properties declare here****************/
//    private String location;
//
//    @Transient
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
}
