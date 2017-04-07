package com.easysoft.ecommerce.model;


import org.hibernate.annotations.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "row")
public class Row extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private Date updatedDate;
    private String title;
    private String showTitle;
    private String background;
    private String active;
    private float sequence;
    private Menu menu;
    private WidgetTemplate widgetTemplate;
    private List<SiteMenuPartContent> partContents;

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = true, length = 1)
    public String getShowTitle() {
        return convertActiveFlag(showTitle);
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = convertActiveFlag(showTitle);
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

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

    @ManyToOne /*(cascade = CascadeType.ALL, fetch = FetchType.LAZY)*/
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @OneToMany(mappedBy="row", cascade = CascadeType.REMOVE, fetch=FetchType.LAZY)
    public List<SiteMenuPartContent> getPartContents() {
        return partContents;
    }

    public void setPartContents(List<SiteMenuPartContent> partContents) {
        this.partContents = partContents;
    }
    @ManyToOne
    public WidgetTemplate getWidgetTemplate() {
        return widgetTemplate;
    }

    public void setWidgetTemplate(WidgetTemplate widgetTemplate) {
        this.widgetTemplate = widgetTemplate;
    }

}
