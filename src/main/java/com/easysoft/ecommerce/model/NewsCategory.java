package com.easysoft.ecommerce.model;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "news_category")
public class NewsCategory extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    private String uri;

    private String name;
    private String description;
    private String active;
    private Date updatedDate;
    private float sequence;

    private NewsCategory parentNewsCategory;

    private List<NewsCategory> subNewsCategories;

    private List<News> newses;

    private Site site;

    public NewsCategory() {
        super();
    }

    public NewsCategory(Date createdDate, String name, float sequence) {
        super(createdDate);
        this.name = name;
        this.sequence = sequence;
    }


    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @Column(name = "uri", nullable = false, length = 255)
    @org.hibernate.annotations.Index(name = "uriIndex")
    @NotNull
    @Size(max = 255)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @Column(nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
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

    @Column(name = "active", nullable = true, length = 1)
    @org.hibernate.annotations.Index(name = "activeIndex")
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

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "parentNewsCategory", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    public List<NewsCategory> getSubNewsCategories() {
        return subNewsCategories;
    }

    public void setSubNewsCategories(List<NewsCategory> subNewsCategories) {
        this.subNewsCategories = subNewsCategories;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public NewsCategory getParentNewsCategory() {
        return parentNewsCategory;
    }

    public void setParentNewsCategory(NewsCategory parentNewsCategory) {
        this.parentNewsCategory = parentNewsCategory;
    }

    @ManyToMany (cascade = {CascadeType.DETACH})
    @JoinTable(name = "news_category_to_news",
            joinColumns = @JoinColumn(name = "news_category_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id"))
    public List<News> getNewses() {
        return newses;
    }

    public void setNewses(List<News> newses) {
        this.newses = newses;
    }


    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public boolean equals(Object o) {
        if (!(o instanceof News)) {
            return false;
        }
        News news = (News) o;
        return new EqualsBuilder()
                .append(this.getId(), news.getId())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(this.getId()).
                toHashCode();
    }

}
