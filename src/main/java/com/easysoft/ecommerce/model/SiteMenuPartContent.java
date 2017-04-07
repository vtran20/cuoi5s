package com.easysoft.ecommerce.model;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Some notices:
 *
 * 1. The content will be show base on start date and end date. End date is empty or null mean, this content will show forever.
 * In case, the end date is null, we should get the current date + 100 years.
 * 2. When insert new content, must check to make sure the start date is not the same.
 * 3. If many contents are available for showing in the same time. The priority should be follow: start date close current date.
 *
 *
 * User: Vu Tran
 * Date: Aug 23, 2010
 * Time: 5:46:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table (name="site_menu_part_content")
public class SiteMenuPartContent extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String header;
    private String title;
    private String imgUrl;
    private String videoUrl;
    private String content;
    private String link;
    private String crop;

    private Date startDate;
    private Date endDate;
    private float sequence;
    private String active;
    private Row row;

    public SiteMenuPartContent() {
        super();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 100);
        endDate = cal.getTime();
    }

    @Lob
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    //    @ManyToOne
//    public Menu getMenu() {
//        return menu;
//    }
//
//    public void setMenu(Menu menu) {
//        this.menu = menu;
//    }

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @ManyToOne /*(cascade=CascadeType.ALL)*/
    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    @Column(nullable = true, length = 20)
    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }
}
