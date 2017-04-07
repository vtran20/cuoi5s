package com.easysoft.ecommerce.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table (name="video_import_class")
public class VideoImportClass extends AbstractEntity  {
    private static final long serialVersionUID = 1L;

    private String source;
    private String className;
    private String url;
    private String queryString;
    private String active;

    public VideoImportClass() {
        super();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }
}
