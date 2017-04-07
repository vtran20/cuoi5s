package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Indexed
@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE) // Using @Cache instead @Cacheable to avoid HHH-5303
@Table (name="category")
@NamedQueries({
@NamedQuery(name="getContent", query="SELECT c FROM Category c ORDER BY sequence")})
public class Category extends AbstractEntity  {

    private static final long serialVersionUID = 1L;
    private String uri;

    private String name;
    private String description;
    private String active;
    private Date updatedDate;
    private String metaDescription;
    private String metakeyword;
    private float sequence;

    private List<Catalog> catalogs;
    
    private Category parentCategory;

    private List<Category> subCategories;

    private List<Product> products;
    private List<Video> videos = new ArrayList<Video>();

    private List<RefinementValue> refinementValues;

    private Site site;

    private ProductAttributeGroup attributeGroup;

    public Category() {
        super();
    }

    public Category(Date createdDate, String name, float sequence) {
        super(createdDate);
        this.name = name;
        this.sequence = sequence;
    }


    @Basic(optional = false)
    @Field(index=Index.UN_TOKENIZED, store=Store.YES)
    @Column(name="uri", nullable = false, length = 50)
    @org.hibernate.annotations.Index(name = "uriIndex")
    @NotNull
    @Size (max=50)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Basic(optional = false)
    @Field(index= Index.UN_TOKENIZED, store=Store.YES)
    @Column(nullable = false, length = 50)
    @NotNull
    @Size (max=50)
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSequence() {
        return sequence;
    }

    public void setSequence(float sequence) {
        this.sequence = sequence;
    }

    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetakeyword() {
        return metakeyword;
    }

    public void setMetakeyword(String metakeyword) {
        this.metakeyword = metakeyword;
    }

    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy="parentCategory", cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
    @JsonIgnore
    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    @ManyToOne (cascade=CascadeType.ALL)
    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public void addSubCategory(Category category) {
        if (!getSubCategories().contains(category)) {
            getSubCategories().add(category);
            if (category.getParentCategory() != null) {
                category.getParentCategory().getSubCategories().remove(category);
            }
            category.setParentCategory(this);
        }
    }

//    public void addSubCategory(Category category) {
//        if (!getSubCategories().contains(category)) {
//            getSubCategories().add(category);
//        }
//        if (!category.getParentCategories().contains(this)) {
//            category.getParentCategories().add(this);
//        }
//    }



//    @OneToMany(mappedBy="subCategory")
//    public List<CategoryToCategory> getSubCategories() {
//        return subCategories;
//    }

//    public void setSubCategories(List<CategoryToCategory> subCategories) {
//        this.subCategories = subCategories;
//    }

//    public List<CategoryToCategory> getParentCategories() {
//        return parentCategories;
//    }
//
//    public void setParentCategories(List<CategoryToCategory> parentCategories) {
//        this.parentCategories = parentCategories;
//    }

    @ManyToMany
     @JoinTable(name="category_to_product",
           joinColumns=@JoinColumn(name="CAT_ID"),
           inverseJoinColumns=@JoinColumn(name="PROD_ID"))
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if (!getProducts().contains(product)) {
            getProducts().add(product);
        }
        if (!product.getCategories().contains(this)) {
            product.getCategories().add(this);
        }
    }

    public void removeProduct(Product product) {
        if (getProducts().contains(product)) {
            getProducts().remove(product);
        }
        if (product.getCategories().contains(this)) {
            product.getCategories().remove(this);
        }
    }

    @ManyToMany
    @JoinTable(name="category_to_video",
            joinColumns=@JoinColumn(name="category_id"),
            inverseJoinColumns=@JoinColumn(name="video_id"))
    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @OneToMany(mappedBy="category", fetch=FetchType.LAZY)
    @JsonIgnore
    public List<RefinementValue> getRefinementValues() {
        return refinementValues;
    }

    public void setRefinementValues(List<RefinementValue> refinementValues) {
        this.refinementValues = refinementValues;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @ManyToOne
    @JoinColumn(name="group_attribute_id")
    public ProductAttributeGroup getAttributeGroup() {
        return attributeGroup;
    }

    public void setAttributeGroup(ProductAttributeGroup attributeGroup) {
        this.attributeGroup = attributeGroup;
    }

    @ManyToMany(mappedBy="categories")
    @JsonIgnore
    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }
}
