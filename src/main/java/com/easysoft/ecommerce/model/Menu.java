package com.easysoft.ecommerce.model;


import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    private String uri;
    private String name;
    private String description;
    private String keyword;
    private String active;
    private String headerMenu;
    private String usefulLink;
    private String displayBreadcrumb;
    private String showRootCategory;
    private Date updatedDate;
    private float sequence;
    private String homePage;
    private String menuTemplate; //Y: menu module, E: menu external link
    private String designUrl;

    private Menu parentMenu;
    private List<Menu> subMenus;
    private List<Row> rows;

//    private Page page;
    private Site site;

    public Menu() {
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

    @Column(nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(nullable = true, length = 255)
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    @Column(nullable = true, length = 1)
    public String getHeaderMenu() {
        return convertActiveFlag(headerMenu);
    }

    public void setHeaderMenu(String headerMenu) {
        this.headerMenu = convertActiveFlag(headerMenu);
    }

    @Column(nullable = true, length = 1)
    public String getUsefulLink() {
        return convertActiveFlag(usefulLink);
    }

    public void setUsefulLink(String usefulLink) {
        this.usefulLink = convertActiveFlag(usefulLink);
    }

    @Column(nullable = true, length = 1)
    public String getDisplayBreadcrumb() {
        return convertActiveFlag(displayBreadcrumb);
    }

    public void setDisplayBreadcrumb(String displayBreadcrumb) {
        this.displayBreadcrumb = convertActiveFlag(displayBreadcrumb);
    }

    @Column(nullable = true, length = 1)
    public String getShowRootCategory() {
        return showRootCategory;
    }

    public void setShowRootCategory(String showRootCategory) {
        this.showRootCategory = showRootCategory;
    }

    @Column(name = "ishomepage", nullable = true, length = 1)
    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    @Column(name = "menuTemplate", nullable = true, length = 1)
    public String getMenuTemplate() {
        return menuTemplate;
    }

    public void setMenuTemplate(String menuTemplate) {
        this.menuTemplate = menuTemplate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @OneToMany(mappedBy = "parentMenu", fetch = FetchType.LAZY)
    public List<Menu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<Menu> subMenus) {
        this.subMenus = subMenus;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Menu getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

//    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    public Page getPage() {
//        return page;
//    }
//
//    public void setPage(Page page) {
//        this.page = page;
//    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getDesignUrl() {
        return designUrl;
    }

    public void setDesignUrl(String designUrl) {
        this.designUrl = designUrl;
    }

    /*When remove Menu (page), it will remove all rows belong that menu too*/
    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
