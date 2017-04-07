package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE) // Using @Cache instead @Cacheable to avoid HHH-5303
@Table (name="catalog")
public class Catalog extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private String uri;
    private boolean active;
    private String description;
    private int sequence;

    private List<Category> categories;

//    private List<Product> products;

    private List<Site> sites;

    public Catalog() {
        super();
    }

    public Catalog(Date createdDate, String name, int sequence) {
        super(createdDate);
        this.name = name;
        this.sequence = sequence;
    }


    @Column(nullable = false, length = 50)
    @NotNull
    @Size (max=50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="uri", nullable = false, length = 50)
    @NotNull
    @Size (max=50)
    @Index(name = "uriIndex")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @ManyToMany(mappedBy="catalogs")
    @JsonIgnore
    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    @ManyToMany
     @JoinTable(name="catalog_to_category",
           joinColumns=@JoinColumn(name="catalog_id"),
           inverseJoinColumns=@JoinColumn(name="category_id"))
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

//    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    @OneToMany(mappedBy="catalog", cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
//    public List<Product> getProducts() {
//        return products;
//    }
//
//    public void setProducts(List<Product> products) {
//        this.products = products;
//    }
}
