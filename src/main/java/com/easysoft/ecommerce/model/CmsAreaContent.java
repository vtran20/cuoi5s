package com.easysoft.ecommerce.model;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table (name="cms_area_content")
public class CmsAreaContent extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String content;
    private Date startDate;
    private Date endDate;
    private CmsArea cmsArea;
    private String active;

    public CmsAreaContent() {
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

    @ManyToOne (cascade=CascadeType.ALL)
    public CmsArea getCmsArea() {
        return cmsArea;
    }

    public void setCmsArea(CmsArea cmsArea) {
        this.cmsArea = cmsArea;
    }

    @Column(name = "active", nullable = true, length = 1)
    @org.hibernate.annotations.Index(name = "activeIndex")
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }
}
