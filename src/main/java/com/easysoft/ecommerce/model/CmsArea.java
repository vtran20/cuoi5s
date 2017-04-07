package com.easysoft.ecommerce.model;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name="cms_area")
public class CmsArea extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    /*
     * The cmsName should be:  NAME[:PATH]
     * We add [:PATH] when dynamic is Y, in this time, the content will base on path as well.
     *
     */
    private String cmsName;
    private String path;

    //There properties below will be used for content pages. So they will need title, metaKeyword and metaDescription
    private String title;
    private String metaKeyword;
    private String metaDescription;

    private Site site;

    private List<CmsAreaContent> contents = new ArrayList<CmsAreaContent>();

    public CmsArea() {
        super();
    }
    @Column (nullable = false)
    public String getCmsName() {
        return cmsName;
    }

    public void setCmsName(String cmsName) {
        this.cmsName = cmsName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    @OneToMany(mappedBy="cmsArea", cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
    public List<CmsAreaContent> getContents() {
        return contents;
    }

    public void setContents(List<CmsAreaContent> contents) {
        this.contents = contents;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

}
