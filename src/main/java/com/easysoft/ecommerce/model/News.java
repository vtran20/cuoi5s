package com.easysoft.ecommerce.model;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Indexed
@Entity
@Table(name = "news")
public class News extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    private Date updatedDate;

    private String title;
    private String thumbImg;
    private String uri;
    private String shortDescription;
    private String preShortDescription;
    private String content;
    private String preContent;
    private String active;
    private float sequence;
    private List<NewsCategory> newsCategories = new ArrayList<NewsCategory>();
    private Site site;
    private String footerLink;

    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @Column(name = "uri")
    @org.hibernate.annotations.Index(name = "uriIndex")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    @FieldBridge(impl = com.easysoft.ecommerce.model.lucene.UnsignBridge.class)
    @org.hibernate.annotations.Index(name = "uriIndex")
    @Boost(2.0f)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }

    @Column(name = "short_description", length = 500, nullable = true)
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Column(name = "pre_short_description", length = 500, nullable = true)
    public String getPreShortDescription() {
        return preShortDescription;
    }

    public void setPreShortDescription(String preShortDescription) {
        this.preShortDescription = preShortDescription;
    }

    @Lob
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Lob
    public String getPreContent() {
        return preContent;
    }

    public void setPreContent(String preContent) {
        this.preContent = preContent;
    }

    @ManyToMany(mappedBy = "newses", cascade= CascadeType.DETACH, fetch=FetchType.LAZY)
    public List<NewsCategory> getNewsCategories() {
        return newsCategories;
    }

    public void setNewsCategories(List<NewsCategory> newsCategories) {
        this.newsCategories = newsCategories;
    }


    @Field(index = Index.UN_TOKENIZED, store = Store.NO)
    @Column(name = "active", nullable = true, length = 1)
    @org.hibernate.annotations.Index(name = "activeIndex")
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    public float getSequence() {
        return sequence;
    }

    public void setSequence(float sequence) {
        this.sequence = sequence;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public void removeNewsNewsCategory(List<NewsCategory> newsCategories) {
        if (newsCategories != null && !newsCategories.isEmpty()) {
            for (NewsCategory newsCategory : newsCategories) {
                if (getNewsCategories().contains(newsCategory)) {
                    getNewsCategories().remove(newsCategory);
                }
                if (newsCategory.getNewses().contains(this)) {
                    newsCategory.getNewses().remove(this);
                }
            }
        }
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

    @Column(nullable = true, length = 1)
    public String getFooterLink() {
        return convertActiveFlag(footerLink);
    }

    public void setFooterLink(String footerLink) {
        this.footerLink = convertActiveFlag(footerLink);
    }
}
