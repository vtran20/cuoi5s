package com.easysoft.ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "album_image")
public class AlbumImage extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    private String uri; //uuid from the image server
    private String fileName;
    private String description;
    private String active;
    private String crop;
    private Date updatedDate;
    private Album album;

    private Site site;

    public AlbumImage() {
        super();
    }

    @Basic(optional = false)
    @Column(name = "uri", nullable = true, length = 255)
    @org.hibernate.annotations.Index(name = "uriIndex")
    @Size(max = 255)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(nullable = true, length = 2000)
    @Size(max = 2000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @ManyToOne
    @JsonIgnore
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
    @Column(nullable = true, length = 30)
    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

}
