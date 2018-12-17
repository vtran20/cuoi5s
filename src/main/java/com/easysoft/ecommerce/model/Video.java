package com.easysoft.ecommerce.model;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Indexed
@Entity
@Table (name="video")
public class Video extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private Date updatedDate;
    private String uri;

    private String name;
    private String description;
    private String videoId;
    private String thumbImgUrl;
    private String mediumImgUrl;
    private String largeImgUrl;
    private String keyword;
    private String active;
    private float sequence;
    private Site site;
    private List<Category> categories = new ArrayList<Category>();
    private String source; //Youtube...
    private int viewCount;
    private String duration;
    private String live;


    public Video() {
    }

    public Video(Date createdDate, String name, String authors, String active) {
        super(createdDate);
        this.updatedDate = createdDate;
        this.name = name;
        this.description = authors;
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

    @org.hibernate.annotations.Index(name = "videoIdIndex")
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getThumbImgUrl() {
        return thumbImgUrl;
    }

    public void setThumbImgUrl(String thumbImgUrl) {
        this.thumbImgUrl = thumbImgUrl;
    }

    public String getMediumImgUrl() {
        return mediumImgUrl;
    }

    public void setMediumImgUrl(String mediumImgUrl) {
        this.mediumImgUrl = mediumImgUrl;
    }

    public String getLargeImgUrl() {
        return largeImgUrl;
    }

    public void setLargeImgUrl(String largeImgUrl) {
        this.largeImgUrl = largeImgUrl;
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.NO)
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    @IndexedEmbedded
//    @ContainedIn
    @ManyToMany(mappedBy="videos")
    @JsonIgnore
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @ManyToOne
    @JsonIgnore
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Field(index=Index.UN_TOKENIZED, store=Store.NO)
    public float getSequence() {
        return sequence;
    }

    public void setSequence(float sequence) {
        this.sequence = sequence;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void removeVideoCategory(List<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            for (Category category : categories) {
                if (getCategories().contains(category)) {
                    getCategories().remove(category);
                }
                if (category.getVideos().contains(this)) {
                    category.getVideos().remove(this);
                }
            }
        }
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }
}
