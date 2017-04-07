package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "widget_template")
public class WidgetTemplate extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String name;
    private String imageUrl;
    private String imageSize;
    private String content;
    private String active;
    private String type; // page, widget, footer
    private String widgetType; // content, slide, table
    private int sequence;
    private String supportField; // this attribute define the list of fields (in SiteMenuPartContent) will be used in this template. For example "title,content":

    @Column(nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = true, length = 255)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Lob
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "active", nullable = true, length = 1)
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    @Column(name = "type", length = 10)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public String getSupportField() {
        return supportField;
    }

    public void setSupportField(String supportField) {
        this.supportField = supportField;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }
}
