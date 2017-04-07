package com.easysoft.ecommerce.model;


import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "album")
public class Album extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    private String uri;
    private String name;
    private String description;
    private String active;
    private Date updatedDate;
    private float sequence;
    private List<AlbumImage> images = new ArrayList<AlbumImage>();

    private Site site;

    public Album() {
        super();
    }

    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @Column(name = "uri", nullable = true, length = 255)
    @org.hibernate.annotations.Index(name = "uriIndex")
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @OneToMany(mappedBy = "album", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<AlbumImage> getImages() {
        return images;
    }

    public void setImages(List<AlbumImage> images) {
        this.images = images;
    }
}
