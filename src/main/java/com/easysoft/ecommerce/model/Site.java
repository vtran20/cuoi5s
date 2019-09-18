package com.easysoft.ecommerce.model;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.*;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// Using @Cache instead @Cacheable to avoid HHH-5303
@Table(name = "site")
public class Site extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    /*siteCode also is prefix string of subDomain. Ex: siteCode m01 -> subDomain will be m01.example.com
    * This code will be exactly the same with user.siteCode*/
    private String siteCode;
    private String name;
    private String description;
    private String domain;
    private String subDomain;
    private String defaultSite;
    private String active;
    private String appId;
    private Date startDate;
    private Date endDate;
    private Date updatedDate;
    /*siteType=1: Owner
    * siteType=2: client sites
    * siteType=3: Partner
    * */
    public static Integer OWNER = 1;
    public static Integer CLIENT = 2;
    public static Integer PARTNER = 3;
    private Integer siteType;

    private List<Catalog> catalogs = new ArrayList<Catalog>();

    private List<SiteParam> siteParams;
    private List<NewsCategory> newsCategories;
    private List<Category> categories;
    private List<Product> products;
    private List<Menu> menus;
    private List<NailStore> stores;

    private List<CmsArea> cmsArea;
    private SiteTemplate siteTemplate;
    private Site parentSite;
//    private List<Site> sites;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "site", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    public List<SiteParam> getSiteParams() {
        return siteParams;
    }

    public void setSiteParams(List<SiteParam> siteParams) {
        this.siteParams = siteParams;
    }

    @OneToOne(mappedBy = "site", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    public SiteTemplate getSiteTemplate() {
        return siteTemplate;
    }

    public void setSiteTemplate(SiteTemplate siteTemplate) {
        this.siteTemplate = siteTemplate;
    }

    @Transient
    /**
     * @deprecated use getSiteParamsMap (String) instead
     */
    public Map<String, String> getSiteParamsMap() {
        Map result = new HashMap();
        for (SiteParam siteParam : getSiteParams()) {
            result.put(siteParam.getKey(), siteParam.getValue());
        }
        return result;
    }

    @Transient
    public String getSiteParam(String key) {
        for (SiteParam siteParam : getSiteParams()) {
            if (siteParam.getKey().equals(key)) {
                return siteParam.getValue();
            }
        }
        return null;
    }

    @Column(name = "siteCode", unique = true, nullable = false, insertable = true, updatable = true, length = 50)
    public String getSiteCode() {
        if (siteCode != null) {
            return siteCode.toLowerCase();
        } else {
            return siteCode;
        }
    }

    public void setSiteCode(String siteCode) {
        if (siteCode != null) {
            this.siteCode = siteCode.toLowerCase();
        } else {
            this.siteCode = siteCode;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    @JoinTable(name = "site_to_catalog",
            joinColumns = @JoinColumn(name = "site_id"),
            inverseJoinColumns = @JoinColumn(name = "catalog_id"))
    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    public List<CmsArea> getCmsArea() {
        return cmsArea;
    }

    public void setCmsArea(List<CmsArea> cmsArea) {
        this.cmsArea = cmsArea;
    }

    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    public List<NailStore> getStores() {
        return stores;
    }

    public void setStores(List<NailStore> stores) {
        this.stores = stores;
    }

    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    public List<NewsCategory> getNewsCategories() {
        return newsCategories;
    }

    public void setNewsCategories(List<NewsCategory> newsCategories) {
        this.newsCategories = newsCategories;
    }

    @Column(name = "domain", unique = true, nullable = true, length = 255)
    @Index(name = "domainIndex")
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Column(name = "subdomain", unique = true, nullable = true, length = 255)
    @Index(name = "subDomainIndex")
    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "active", nullable = true, length = 1)
    @Index(name = "activeIndex")
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    @Column(name = "app_id", nullable = true, length = 40)
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "defaultSite", nullable = true, length = 1)
    public String getDefaultSite() {
        return defaultSite;
    }

    public void setDefaultSite(String defaultSite) {
        this.defaultSite = defaultSite;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getSiteType() {
        return siteType;
    }

    public void setSiteType(Integer siteType) {
        this.siteType = siteType;
    }

    @ManyToOne
    public Site getParentSite() {
        return parentSite;
    }

    public void setParentSite(Site parentSite) {
        this.parentSite = parentSite;
    }


    /**
     * ***********transient properties declare here***************
     */
    @Transient
    public boolean isDefault() {
        return "Y".equals(getDefaultSite());
    }
    @Transient
    public boolean isExpired() {
        return this.getEndDate() != null && this.getEndDate().before(new Date());
    }
}
