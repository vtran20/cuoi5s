package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.controller.Constants;
import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.SiteService;
import com.easysoft.ecommerce.util.Messages;
import com.fasterxml.uuid.Generators;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
@Transactional
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteDao siteDao;
    @Autowired
    private SiteParamDao siteParamDao;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private SiteTemplateDao siteTemplateDao;
    @Autowired
    private SiteHeaderFooterDao siteHeaderFooterDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private ServiceLocator serviceLocator;

    @Override
    public Messages createOrUpdate(Site entity, HashMap <String, String> siteParams) throws Exception {
        Messages messages = new Messages();
        //Validate to see if the new domain is existing
        Site original = siteDao.findUniqueBy("id", entity.getId());
        if (entity.getDomain() != null && !entity.getDomain().equals(original.getDomain())) {
            List newDomain = ServiceLocatorHolder.getServiceLocator().getSiteDao().findBy("domain", entity.getDomain());
            if (newDomain != null && newDomain.size() > 0) {
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("domain.is.existing", null, LocaleContextHolder.getLocale()));
                return messages;
            }
        }

        //update site
        if (entity.isEmptyId()) {
            //create new site
            original = entity;
            original.setAppId(Generators.timeBasedGenerator().generate().toString());
            siteDao.persist(original);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        } else {
            boolean isChange = false;
            original= siteDao.findUniqueBy("id", entity.getId());
            if (entity.getName() != null && !entity.getName().equals(original.getName())) {
                original.setName(entity.getName());
                isChange = true;
            }
            if (entity.getDescription() != null && !entity.getDescription().equals(original.getDescription())) {
                original.setDescription(entity.getDescription());
                isChange = true;
            }
            if (entity.getDomain() != null && !entity.getDomain().equals(original.getDomain())) {
                original.setDomain(entity.getDomain());
                isChange = true;
            }
            // Update when have data change
            if (isChange) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                siteDao.merge(original);
            }
        }

        //SiteParam table
        //Save contact us email to SiteParam table.
        if (siteParams.get("CONTACT_US") != null && !siteParams.get("CONTACT_US").equals(original.getSiteParamsMap().get("CONTACT_US"))) {
            if (original.getSiteParamsMap().get("CONTACT_US") == null) { //Insert Contact us email
                SiteParam siteParam = new SiteParam();
                siteParam.setSite(original);
                siteParam.setSiteId(original.getId());
                siteParam.setKey("CONTACT_US");
                siteParam.setValue(siteParams.get("CONTACT_US"));
                siteParamDao.persist(siteParam);
            } else { //Update new contact us email.
                SiteParam siteParam = siteParamDao.findUniqueBy("key", "CONTACT_US", original.getId());
                siteParam.setValue(siteParams.get("CONTACT_US"));
                siteParamDao.merge(siteParam);
            }
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        }

        //Save contact us email to SiteParam table.
        if (siteParams.get("PHONE_CONTACT") != null && !siteParams.get("PHONE_CONTACT").equals(original.getSiteParamsMap().get("PHONE_CONTACT"))) {
            if (original.getSiteParamsMap().get("PHONE_CONTACT") == null) { //Insert Contact us email
                SiteParam siteParam = new SiteParam();
                siteParam.setSite(original);
                siteParam.setSiteId(original.getId());
                siteParam.setKey("PHONE_CONTACT");
                siteParam.setValue(siteParams.get("PHONE_CONTACT"));
                siteParamDao.persist(siteParam);
            } else { //Update new contact us email.
                SiteParam siteParam = siteParamDao.findUniqueBy("key", "PHONE_CONTACT", original.getId());
                siteParam.setValue(siteParams.get("PHONE_CONTACT"));
                siteParamDao.merge(siteParam);
            }
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        }

        //Save Language to SiteParam table.
        if (siteParams.get("LANGUAGE") != null && !siteParams.get("LANGUAGE").equals(original.getSiteParamsMap().get("LANGUAGE"))) {
            if (original.getSiteParamsMap().get("LANGUAGE") == null) { //Insert language
                SiteParam siteParam = new SiteParam();
                siteParam.setSite(original);
                siteParam.setSiteId(original.getId());
                siteParam.setKey("LANGUAGE");
                siteParam.setValue(siteParams.get("LANGUAGE"));
                siteParamDao.persist(siteParam);
            } else { //Update new language.
                SiteParam siteParam = siteParamDao.findUniqueBy("key", "LANGUAGE", original.getId());
                siteParam.setValue(siteParams.get("LANGUAGE"));
                siteParamDao.merge(siteParam);
            }
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        }
        //Save facebook fan page to SiteParam table.
        if (siteParams.get("FACEBOOK_FAN_PAGE") != null && !siteParams.get("FACEBOOK_FAN_PAGE").equals(original.getSiteParamsMap().get("FACEBOOK_FAN_PAGE"))) {
            if (original.getSiteParamsMap().get("FACEBOOK_FAN_PAGE") == null) { //Insert facebook fan page
                SiteParam siteParam = new SiteParam();
                siteParam.setSite(original);
                siteParam.setSiteId(original.getId());
                siteParam.setKey("FACEBOOK_FAN_PAGE");
                siteParam.setValue(siteParams.get("FACEBOOK_FAN_PAGE"));
                siteParamDao.persist(siteParam);
            } else { //Update new facebook fan page.
                SiteParam siteParam = siteParamDao.findUniqueBy("key", "FACEBOOK_FAN_PAGE", original.getId());
                siteParam.setValue(siteParams.get("FACEBOOK_FAN_PAGE"));
                siteParamDao.merge(siteParam);
            }
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        }
//        //Save google fan page to SiteParam table.
//        if (siteParams.get("GOOGLE_FAN_PAGE") != null && !siteParams.get("GOOGLE_FAN_PAGE").equals(original.getSiteParamsMap().get("GOOGLE_FAN_PAGE"))) {
//            if (original.getSiteParamsMap().get("GOOGLE_FAN_PAGE") == null) { //Insert google fan page
//                SiteParam siteParam = new SiteParam();
//                siteParam.setSite(original);
//                siteParam.setSiteId(original.getId());
//                siteParam.setKey("GOOGLE_FAN_PAGE");
//                siteParam.setValue(siteParams.get("GOOGLE_FAN_PAGE"));
//                siteParamDao.persist(siteParam);
//            } else { //Update new google fan page.
//                SiteParam siteParam = siteParamDao.findUniqueBy("key", "GOOGLE_FAN_PAGE", original.getId());
//                siteParam.setValue(siteParams.get("GOOGLE_FAN_PAGE"));
//                siteParamDao.merge(siteParam);
//            }
//            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
//        }
//        //Save youtube fan page to SiteParam table.
//        if (siteParams.get("YOUTUBE_FAN_PAGE") != null && !siteParams.get("YOUTUBE_FAN_PAGE").equals(original.getSiteParamsMap().get("YOUTUBE_FAN_PAGE"))) {
//            if (original.getSiteParamsMap().get("YOUTUBE_FAN_PAGE") == null) { //Insert youtube fan page
//                SiteParam siteParam = new SiteParam();
//                siteParam.setSite(original);
//                siteParam.setSiteId(original.getId());
//                siteParam.setKey("YOUTUBE_FAN_PAGE");
//                siteParam.setValue(siteParams.get("YOUTUBE_FAN_PAGE"));
//                siteParamDao.persist(siteParam);
//            } else { //Update new youtube fan page.
//                SiteParam siteParam = siteParamDao.findUniqueBy("key", "YOUTUBE_FAN_PAGE", original.getId());
//                siteParam.setValue(siteParams.get("YOUTUBE_FAN_PAGE"));
//                siteParamDao.merge(siteParam);
//            }
//            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
//        }
//        //Save twitter fan page to SiteParam table.
//        if (siteParams.get("TWITTER_FAN_PAGE") != null && !siteParams.get("TWITTER_FAN_PAGE").equals(original.getSiteParamsMap().get("TWITTER_FAN_PAGE"))) {
//            if (original.getSiteParamsMap().get("TWITTER_FAN_PAGE") == null) { //Insert twitter fan page
//                SiteParam siteParam = new SiteParam();
//                siteParam.setSite(original);
//                siteParam.setSiteId(original.getId());
//                siteParam.setKey("TWITTER_FAN_PAGE");
//                siteParam.setValue(siteParams.get("TWITTER_FAN_PAGE"));
//                siteParamDao.persist(siteParam);
//            } else { //Update new twitter fan page.
//                SiteParam siteParam = siteParamDao.findUniqueBy("key", "TWITTER_FAN_PAGE", original.getId());
//                siteParam.setValue(siteParams.get("TWITTER_FAN_PAGE"));
//                siteParamDao.merge(siteParam);
//            }
//            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
//        }
        //Save google analytics to SiteParam table.
        if (siteParams.get("GOOGLE_ANALYSIS_ACCOUNT") != null && !siteParams.get("GOOGLE_ANALYSIS_ACCOUNT").equals(original.getSiteParamsMap().get("GOOGLE_ANALYSIS_ACCOUNT"))) {
            if (original.getSiteParamsMap().get("GOOGLE_ANALYSIS_ACCOUNT") == null) { //Insert facebook fan page
                SiteParam siteParam = new SiteParam();
                siteParam.setSite(original);
                siteParam.setSiteId(original.getId());
                siteParam.setKey("GOOGLE_ANALYSIS_ACCOUNT");
                siteParam.setValue(siteParams.get("GOOGLE_ANALYSIS_ACCOUNT"));
                siteParamDao.persist(siteParam);
            } else { //Update new facebook fan page.
                SiteParam siteParam = siteParamDao.findUniqueBy("key", "GOOGLE_ANALYSIS_ACCOUNT", original.getId());
                siteParam.setValue(siteParams.get("GOOGLE_ANALYSIS_ACCOUNT"));
                siteParamDao.merge(siteParam);
            }
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        }

        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages;
    }

    @Override
    public void remove(Site site) {
        for (SiteParam sp : site.getSiteParams()) {
            siteParamDao.remove(sp);
        }
        siteDao.remove(site);

        // At least one default site must exist
        Site defaultSite = null;
        List<Site> dbSites = siteDao.findAll();
        for (Site db : dbSites) {
            if ("Y".equals(db.getDefaultSite())) {
                defaultSite = db;
            }
        }
        if (defaultSite == null && !dbSites.isEmpty()) {
            dbSites.get(0).setDefaultSite("Y");
        }
    }

    /**
     * This method will be called when selecting a template and creating new site.
     *
     * @param data
     * @param site
     * @return
     */
    @Override
    public Messages createSite(Map data, Site site) {
        Messages messages = new Messages();
        if (data == null) {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("please.select.template.or.subdomain", null, LocaleContextHolder.getLocale()));
            return messages;
        }
        Long templateId = (Long) data.get("templateId");
        Template selectedTemplate = templateDao.findById(templateId);
        if (selectedTemplate == null) {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("template.does.not.exist", null, LocaleContextHolder.getLocale()));
            return messages;
        }

        Site siteSample = siteDao.getSiteSample(selectedTemplate);
        Calendar now = Calendar.getInstance();
        if (siteSample != null) {
            //Create Site
            Site newSite = new Site();
            try {
                BeanUtils.copyProperties(newSite, siteSample);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            newSite.setId(null);
            newSite.setSiteType(2); //Client site
            newSite.setCatalogs(null);
            newSite.setSiteParams(null);
            newSite.setNewsCategories(null);
            newSite.setCategories(null);
            newSite.setProducts(null);
            newSite.setMenus(null);
            newSite.setCmsArea(null);
            newSite.setSiteTemplate(null);
            if (StringUtils.isNotEmpty((String) data.get("domain"))) {
                newSite.setDomain(data.get("domain")+"");
            } else {
                newSite.setDomain("");
            }
            String siteCode = data.get("siteCode")+"";
            newSite.setSiteCode(siteCode);
            Calendar cal = Calendar.getInstance();
            newSite.setCreatedDate(cal.getTime());
            newSite.setStartDate(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, Constants.TRIAL_USE);
            newSite.setEndDate(cal.getTime());
            String freeDomain = site.getSiteParam("FREE_SITE_DOMAIN");
            if (StringUtils.isEmpty(freeDomain)) {
                freeDomain = serviceLocator.getSystemContext().getGlobalConfig("free.site.domain");
            }
            newSite.setSubDomain(siteCode + "." + freeDomain);
            newSite.setParentSite(site); //retailer where the site is created
            newSite.setAppId(Generators.timeBasedGenerator().generate().toString());
            siteDao.persist(newSite);

            //Create a record for SiteProductService table. This record is for website hosting product.
            copySiteServices(siteSample, newSite);

            //Default create payment setup if this site is for ecommerce
            copyPaymentMethod(site, newSite);

            //Default create shipping setup if this site is for ecommerce
            copyShippingMethod(site, newSite);

            //Create Site param
            Map <String, String> siteParams = siteParamDao.getSiteParamsBySite(siteSample);
            newSite.setSiteParams(new ArrayList<SiteParam>());
            for (Map.Entry<String, String> entry : siteParams.entrySet()) {
                SiteParam newSiteParam = new SiteParam();
                newSiteParam.setSite(newSite);
                newSiteParam.setSiteId(newSite.getId());
                newSiteParam.setKey(entry.getKey());
                newSiteParam.setValue(entry.getValue());
                if (!newSite.getSiteParams().contains(newSiteParam)) {
                    newSite.getSiteParams().add(newSiteParam);
                }
                siteParamDao.persist(newSiteParam);
            }
            //Create user role
            List<UserRole> userRoles = userRoleDao.findUserRoles(siteSample);
            Long userId = (Long) data.get("userId");
            if (selectedTemplate == null) {
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("template.does.not.exist", null, LocaleContextHolder.getLocale()));
                return messages;
            }
            User user = serviceLocator.getUserDao().findById(userId);
            for (UserRole userRole : userRoles) {
                UserRole newUserRole = new UserRole();
                newUserRole.setSite(newSite);
                newUserRole.setRole(userRole.getRole());
                newUserRole.setUser(user);
                userRoleDao.persist(newUserRole);
            }

            //Create site template for new site
            SiteTemplate siteSampleTemplate = siteTemplateDao.findUniqueBy("site.id", siteSample.getId());
            if (siteSampleTemplate != null) {
                SiteTemplate newSiteTemplate = new SiteTemplate();
                try {
                    BeanUtils.copyProperties(newSiteTemplate, siteSampleTemplate);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newSiteTemplate.setId(null);
                newSiteTemplate.setSite(newSite);
                newSiteTemplate.setCreatedDate(now.getTime());
                newSiteTemplate.setUpdatedDate(now.getTime());
                newSiteTemplate.setTemplate(selectedTemplate);
                //Change color, skin color and other if users select before create the site.
                if (data.get("templateColorCode") != null && StringUtils.isNotEmpty(data.get("templateColorCode")+"")) {
                    newSiteTemplate.setTemplateColorCode(data.get("templateColorCode")+"");
                }
                if (data.get("skinColor") != null && StringUtils.isNotEmpty(data.get("skinColor")+"")) {
                    newSiteTemplate.setSkinColor(data.get("skinColor")+"");
                }
                if (data.get("fullWide") != null && StringUtils.isNotEmpty(data.get("fullWide")+"")) {
                    newSiteTemplate.setFullWide(data.get("fullWide")+"");
                }
                siteTemplateDao.persist(newSiteTemplate);
            }

            //Create Album
            List<Album> albums = serviceLocator.getAlbumDao().findActiveByOrder(null, null, null, siteSample.getId());
            for (Album album : albums) {
                Album newAlbum = new Album();
                try {
                    BeanUtils.copyProperties(newAlbum, album);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newAlbum.setId(null);
                newAlbum.setImages(null);
                newAlbum.setSite(newSite);
                newAlbum.setCreatedDate(now.getTime());
                newAlbum.setUpdatedDate(now.getTime());
                serviceLocator.getAlbumDao().persist(newAlbum);

                //Create album image
                List<AlbumImage> albumImages = serviceLocator.getAlbumImageDao().findAlbumImages(album.getId(), null, null, null, siteSample.getId(), true);
                newAlbum.setImages(new ArrayList<AlbumImage>());
                for (AlbumImage albumImage : albumImages) {
                    AlbumImage newAlbumImage = new AlbumImage();
                    try {
                        BeanUtils.copyProperties(newAlbumImage, albumImage);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    newAlbumImage.setId(null);
                    newAlbumImage.setSite(newSite);
                    newAlbumImage.setAlbum(newAlbum);
                    if (!newAlbum.getImages().contains(newAlbumImage)) {
                        newAlbum.getImages().add(newAlbumImage);
                    }
                    newAlbumImage.setCreatedDate(now.getTime());
                    newAlbumImage.setUpdatedDate(now.getTime());
                    serviceLocator.getAlbumImageDao().persist(newAlbumImage);
                }
            }


            //Create site content by cloning from the site sample
            List<Menu> menus = menuDao.getRootMenus(siteSample, "Y", "N");
            for (Menu menu : menus) {
                Menu newMenu = new Menu();
                createMenuPage (menu, newSite, newMenu, null);
                createSiteMenuPartContent(menu, siteSample, newSite, newMenu);
                //create submenu if any
                List<Menu> subMenus = menuDao.getSubMenus(siteSample, menu, "Y", "N");
                for (Menu subMenu : subMenus) {
                    Menu newSubMenu = new Menu();
                    createMenuPage (subMenu, newSite, newSubMenu, newMenu);
                    createSiteMenuPartContent(subMenu, siteSample, newSite, newSubMenu);
                }
            }

            //Copy header/footer from sample to new site
            SiteHeaderFooter sampleSiteHeaderFooter = siteHeaderFooterDao.findUniqueBy("site.id", siteSample.getId());
            if (sampleSiteHeaderFooter != null) {
                SiteHeaderFooter newSiteHeaderFooter = new SiteHeaderFooter();

                try {
                    BeanUtils.copyProperties(newSiteHeaderFooter, sampleSiteHeaderFooter);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newSiteHeaderFooter.setId(null);
                newSiteHeaderFooter.setSite(newSite);
                newSiteHeaderFooter.setCreatedDate(now.getTime());
                newSiteHeaderFooter.setUpdatedDate(now.getTime());
                siteHeaderFooterDao.persist(newSiteHeaderFooter);
            }


            //Copy SiteContact
            List<SiteContact> sampleSiteContacts = serviceLocator.getSiteContactDao().findByOrder(null, null, null, siteSample.getId());
            for (SiteContact siteContact : sampleSiteContacts) {
                SiteContact newSiteContact = new SiteContact();
                try {
                    BeanUtils.copyProperties(newSiteContact, siteContact);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newSiteContact.setId(null);
                newSiteContact.setSite(newSite);
                newSiteContact.setCreatedDate(now.getTime());
                serviceLocator.getSiteContactDao().persist(newSiteContact);
            }

            //Copy Site Support
            List<SiteSupport> sampleSiteSupports = serviceLocator.getSiteSupportDao().findByOrder(null, null, null, siteSample.getId());
            for (SiteSupport siteSupport : sampleSiteSupports) {
                SiteSupport newSiteSupport = new SiteSupport();
                try {
                    BeanUtils.copyProperties(newSiteSupport, siteSupport);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newSiteSupport.setId(null);
                newSiteSupport.setSite(newSite);
                newSiteSupport.setCreatedDate(now.getTime());
                serviceLocator.getSiteSupportDao().persist(newSiteSupport);
            }

            //Adding Nails parts
            //Store
            List<NailStore> stores = serviceLocator.getNailStoreDao().findByOrder(null, null, null, siteSample.getId());
            for (NailStore store : stores) {
                NailStore newStore = new NailStore();
                try {
                    BeanUtils.copyProperties(newStore, store);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newStore.setId(null);
                newStore.setSite(newSite);
                newStore.setNailCustomers(null);
                newStore.setNailCustomerPayments(null);
                newStore.setNailEmployees(null);
                newStore.setNailServices(null);
                newStore.setCreatedDate(now.getTime());
                serviceLocator.getNailStoreDao().persist(newStore);

                //Groups and Services
                try {
                    List <NailService> groups = serviceLocator.getNailServiceDao().getGroupServices(store.getId());
                    for (NailService group : groups) {
                        //Copy group
                        NailService newGroup = new NailService();
                        try {
                            BeanUtils.copyProperties(newGroup, group);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        newGroup.setId(null);
                        newGroup.setStore(newStore);
                        newGroup.setGroup(null);
                        newGroup.setNailCustomerServices(null);
                        newGroup.setServices(null);
                        newGroup.setCreatedDate(now.getTime());
                        serviceLocator.getNailServiceDao().persist(newGroup);

                        List <NailService> services = serviceLocator.getNailServiceDao().getServices(group.getId(), store.getId());
                        for (NailService service : services) {
                            //Copy service
                            NailService newService = new NailService();
                            try {
                                BeanUtils.copyProperties(newService, service);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            newService.setId(null);
                            newService.setStore(newStore);
                            newService.setGroup(newGroup);
                            newService.setNailCustomerServices(null);
                            newService.setServices(null);
                            newService.setCreatedDate(now.getTime());
                            serviceLocator.getNailServiceDao().persist(newService);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("create.new.site.successfully", null, LocaleContextHolder.getLocale()));
        return messages;
    }
    /**
     * This method will be called when create new site for partner.
     *
     * @param data
     * @param site
     * @return
     */
    @Override
    public Messages createPartnerSite(Map data, Site site) {
        Messages messages = new Messages();
        if (data == null) {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("please.select.template.or.subdomain", null, LocaleContextHolder.getLocale()));
            return messages;
        }
        Long templateId = (Long) data.get("templateId");
        Template selectedTemplate = templateDao.findById(templateId);
        if (selectedTemplate == null) {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("template.does.not.exist", null, LocaleContextHolder.getLocale()));
            return messages;
        }

        Site siteSample = siteDao.getSiteSample(selectedTemplate, "N");
        Calendar now = Calendar.getInstance();
        if (siteSample != null) {
            //Create Site
            Site newSite = new Site();
            try {
                BeanUtils.copyProperties(newSite, siteSample);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            newSite.setId(null);
            newSite.setSiteType(3); //partner site
            newSite.setCatalogs(null);
            newSite.setSiteParams(null);
            newSite.setNewsCategories(null);
            newSite.setCategories(null);
            newSite.setProducts(null);
            newSite.setMenus(null);
            newSite.setCmsArea(null);
            newSite.setDefaultSite("N");
            newSite.setSiteTemplate(null);
            if (StringUtils.isNotEmpty((String) data.get("domain"))) {
                newSite.setDomain(data.get("domain")+"");
            } else {
                newSite.setDomain("");
            }
            String siteCode = data.get("siteCode")+"";
            newSite.setSiteCode(siteCode);
            Calendar cal = Calendar.getInstance();
            newSite.setCreatedDate(cal.getTime());
            newSite.setStartDate(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, Constants.TRIAL_USE);
            newSite.setEndDate(cal.getTime());
            String freeDomain = site.getSiteParam("FREE_SITE_DOMAIN");
            if (StringUtils.isEmpty(freeDomain)) {
                freeDomain = serviceLocator.getSystemContext().getGlobalConfig("free.site.domain");
            }
            newSite.setSubDomain(siteCode + "." + freeDomain);
            newSite.setParentSite(site); //retailer where the site is created
            newSite.setAppId(Generators.timeBasedGenerator().generate().toString());
            siteDao.persist(newSite);

            copyTemplates(site, newSite);

            copySiteServices (siteSample, newSite);

            //Copy products from main site to partner one.
            List<Product> currentProducts = serviceLocator.getProductDao().findAll(site.getId());
            copyProduct2Partners(currentProducts, newSite);

            //Default create payment setup
            copyPaymentMethod(site, newSite);
            //Create Site param
            Map <String, String> siteParams = siteParamDao.getSiteParamsBySite(siteSample);
            newSite.setSiteParams(new ArrayList<SiteParam>());
            for (Map.Entry<String, String> entry : siteParams.entrySet()) {
                String key = entry.getKey();
                if (!serviceLocator.getSystemContext().getGlobalConfig("exclude.site.param").contains(key)) {
                    SiteParam newSiteParam = new SiteParam();
                    newSiteParam.setSite(newSite);
                    newSiteParam.setSiteId(newSite.getId());
                    newSiteParam.setKey(entry.getKey());
                    newSiteParam.setValue(entry.getValue());
                    if (!newSite.getSiteParams().contains(newSiteParam)) {
                        newSite.getSiteParams().add(newSiteParam);
                    }
                    siteParamDao.persist(newSiteParam);
                }
            }
            //Create user role
            Long userId = (Long) data.get("userId");
            User user = serviceLocator.getUserDao().findById(userId);
            UserRole newUserRole = new UserRole();
            newUserRole.setSite(newSite);
            newUserRole.setRole("ROLE_SITE_CONTENT_ADMIN");
            newUserRole.setUser(user);
            userRoleDao.persist(newUserRole);

            //Add Role for Partner
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole("ROLE_SITE_PARTNER_ADMIN");
            userRole.setSite(newSite);
            userRoleDao.persist(userRole);

            //Create site template for new site
            SiteTemplate siteSampleTemplate = siteTemplateDao.findUniqueBy("site.id", siteSample.getId());
            if (siteSampleTemplate != null) {
                SiteTemplate newSiteTemplate = new SiteTemplate();
                try {
                    BeanUtils.copyProperties(newSiteTemplate, siteSampleTemplate);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newSiteTemplate.setId(null);
                newSiteTemplate.setSite(newSite);
                newSiteTemplate.setCreatedDate(now.getTime());
                newSiteTemplate.setUpdatedDate(now.getTime());
                newSiteTemplate.setTemplate(selectedTemplate);
                siteTemplateDao.persist(newSiteTemplate);
            }

            //Create site content by cloning from the site sample
            List<Menu> menus = menuDao.getRootMenus(siteSample, "Y", "N");
            String excludeMenu = serviceLocator.getSystemContext().getGlobalConfig("exclude.uri.copy");
            for (Menu menu : menus) {
                if ((menu.getUri() != null && !excludeMenu.contains(menu.getUri())) || "".equals(menu.getUri())) {
                    Menu newMenu = new Menu();
                    createMenuPage (menu, newSite, newMenu, null);
                    createSiteMenuPartContent(menu, siteSample, newSite, newMenu);
                    //create submenu if any
                    List<Menu> subLevel2Menus = menuDao.getSubMenus(siteSample, menu, "Y", "N");
                    for (Menu subLevel2Menu : subLevel2Menus) {
                        if (subLevel2Menu.getUri() != null && !excludeMenu.contains(subLevel2Menu.getUri())) {
                            Menu newSubLevel2Menu = new Menu();
                            createMenuPage (subLevel2Menu, newSite, newSubLevel2Menu, newMenu);
                            createSiteMenuPartContent(subLevel2Menu, siteSample, newSite, newSubLevel2Menu);
                            //create submenu if any
                            List<Menu> subLevel3Menus = menuDao.getSubMenus(siteSample, subLevel2Menu, "Y", "N");
                            for (Menu subLevel3Menu : subLevel3Menus) {
                                if (subLevel3Menu.getUri() != null && !excludeMenu.contains(subLevel3Menu.getUri())) {
                                    Menu newSubLevel3Menu = new Menu();
                                    createMenuPage (subLevel3Menu, newSite, newSubLevel3Menu, newSubLevel2Menu);
                                    createSiteMenuPartContent(subLevel3Menu, siteSample, newSite, newSubLevel3Menu);
                                }
                            }
                        }
                    }
                }
            }

            //Copy header/footer from sample to new site
            SiteHeaderFooter sampleSiteHeaderFooter = siteHeaderFooterDao.findUniqueBy("site.id", siteSample.getId());
            if (sampleSiteHeaderFooter != null) {
                SiteHeaderFooter newSiteHeaderFooter = new SiteHeaderFooter();

                try {
                    BeanUtils.copyProperties(newSiteHeaderFooter, sampleSiteHeaderFooter);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newSiteHeaderFooter.setId(null);
                newSiteHeaderFooter.setSite(newSite);
                newSiteHeaderFooter.setCreatedDate(now.getTime());
                newSiteHeaderFooter.setUpdatedDate(now.getTime());
                siteHeaderFooterDao.persist(newSiteHeaderFooter);
            }
        }

        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("create.new.site.successfully", null, LocaleContextHolder.getLocale()));
        return messages;
    }

    private void copyTemplates(Site site, Site newSite) {
        List<Template> templates = serviceLocator.getTemplateDao().findActiveByOrder("active", "Y", null, site.getId());
        for (Template template: templates) {
            Template newTemplate = new Template();
            try {
                BeanUtils.copyProperties(newTemplate, template);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            newTemplate.setId(null);
            newTemplate.setSiteTemplates(null);
            newTemplate.setSite(newSite);
            serviceLocator.getTemplateDao().persist(newTemplate);
        }
    }

    private void copySiteServices (Site siteSample, Site clientSite) {

        List<SiteProductService> siteProductServices = serviceLocator.getSiteProductServiceDao().findActiveByOrder("site.id", siteSample.getId(), null);
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Calendar cal = Calendar.getInstance();
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, Constants.TRIAL_USE);
        Date endDate = cal.getTime();
        for (SiteProductService siteProductService : siteProductServices) {
            //Create a record for SiteProductService table. This record is for website hosting product.
            SiteProductService newSiteProductService = new SiteProductService();
            try {
                BeanUtils.copyProperties(newSiteProductService, siteProductService);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            newSiteProductService.setId(null);
            newSiteProductService.setActive("Y");
            newSiteProductService.setStartDate(startDate);
            newSiteProductService.setEndDate(endDate);
            //Get product from owner or partner
            Product product = serviceLocator.getProductDao().findUniqueBy("model",siteProductService.getProduct().getModel(), site.getId());
            ProductVariant productVariant = serviceLocator.getProductVariantDao().getProductVariantDefault(product.getId());
            newSiteProductService.setProduct(product);
            newSiteProductService.setProductVariant(productVariant);
            newSiteProductService.setSite(clientSite);
            serviceLocator.getSiteProductServiceDao().persist(newSiteProductService);
        }
    }
    /**
     * Copy missing products from the current site to partner site.
     * 1. Copy all products such as website, domain ...
     * 2. Copy all modules that belong product.
     *
     * @param currentProducts
     * @param partnerSite
     * @return
     */
    public void copyProduct2Partners (List<Product> currentProducts, Site partnerSite) {
        //Copy products
        List<Product> partnerParentProducts = serviceLocator.getProductDao().findAll(partnerSite.getId());
        for (Product currentProduct: currentProducts) {
            boolean hadProduct = false;
            for (Product partnerProduct: partnerParentProducts) {
                if (currentProduct.getModel().equals(partnerProduct.getModel())) {
                    hadProduct = true;
                    break;
                }
            }
            if (!hadProduct) {
                Product newProduct = new Product();
                try {
                    BeanUtils.copyProperties(newProduct, currentProduct);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newProduct.setId(null);
                newProduct.setSite(partnerSite);
                newProduct.setRelatedProducts(null);
                newProduct.setProductFiles(null);
                newProduct.setProductVariant(null);
                newProduct.setProductMarketing(null);
                newProduct.setCategories(null);
                newProduct.setAttributeValues(null);
                serviceLocator.getProductDao().persist(newProduct);

                //copy product variants
                List<ProductVariant> productVariants = serviceLocator.getProductVariantDao().findProductVariantByColorSize(currentProduct.getId(), "Y");
                for (ProductVariant productVariant: productVariants) {
                    ProductVariant newVariant = new ProductVariant();
                    try {
                        BeanUtils.copyProperties(newVariant, productVariant);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    newVariant.setId(null);
                    newVariant.setProduct(newProduct);
                    serviceLocator.getProductVariantDao().persist(newVariant);
                }
                //copy product images
                List<ProductFile> productFiles = serviceLocator.getProductFileDao().findProductFiles(currentProduct.getId(), null);
                for (ProductFile productFile : productFiles) {
                    ProductFile newProductFile = new ProductFile();
                    try {
                        BeanUtils.copyProperties(newProductFile, productFile);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    newProductFile.setId(null);
                    newProductFile.setProduct(newProduct);
                    serviceLocator.getProductFileDao().persist(newProductFile);
                }

            }
        }

        //copy related products
        for (Product currentProduct: currentProducts) {
            List<ProductToProduct> relatedProducts = serviceLocator.getProductDao().getRelateds(currentProduct.getId(), 0);
            for (ProductToProduct rel: relatedProducts) {
                //Find related product of the new site.
                Product partnerProduct = serviceLocator.getProductDao().findUniqueBy("model", currentProduct.getModel(), partnerSite.getId());
                Product relatedPartnerProduct = serviceLocator.getProductDao().findUniqueBy("model", rel.getRelateProduct().getModel(), partnerSite.getId());

                ProductToProduct related = serviceLocator.getProductDao().getRelated(partnerProduct.getId(), relatedPartnerProduct.getId());
                //If the related have not existed
                if (related == null) {
                    ProductToProduct newRelatedProduct = new ProductToProduct();
                    try {
                        BeanUtils.copyProperties(newRelatedProduct, rel);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    newRelatedProduct.setId(null);
                    newRelatedProduct.setProduct(partnerProduct);
                    newRelatedProduct.setRelateProduct(relatedPartnerProduct);
                    serviceLocator.getProductToProductDao().persist(newRelatedProduct);
                }
            }
        }
    }

    /**
     * Copy shipping method from the current site to the new site
     *
     * @param site
     * @param thisSite
     * @return
     */
    public void copyShippingMethod (Site site, Site thisSite) {
        List<ShippingSite> shippingSites = serviceLocator.getShippingSiteDao().findAll(site.getId());
        for (ShippingSite shippingSite : shippingSites) {
            ShippingSite newShippingSite = new ShippingSite();
            try {
                BeanUtils.copyProperties(newShippingSite, shippingSite);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            newShippingSite.setId(null);
            newShippingSite.setSite(thisSite);
            serviceLocator.getShippingSiteDao().persist(newShippingSite);
        }
    }
    /**
     * Copy payment method from the current site to the new site
     *
     * @param site
     * @param thisSite
     * @return
     */
    public void copyPaymentMethod (Site site, Site thisSite) {
        List<PaymentProviderSite> paymentProviderSites = serviceLocator.getPaymentProviderSiteDao().findAll(site.getId());
        for (PaymentProviderSite paymentProviderSite : paymentProviderSites) {
            PaymentProviderSite newPayment = new PaymentProviderSite();
            try {
                BeanUtils.copyProperties(newPayment, paymentProviderSite);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            newPayment.setId(null);
            newPayment.setSite(thisSite);
            serviceLocator.getPaymentProviderSiteDao().persist(newPayment);
        }
    }
        /**
        * This method will be called when delete site.
        *
        * @param data
        * @param site
        * @return
        */
    @Override
    public Messages deleteSite(Map data, Site site) {
        //TODO: Need to check permission for security
        Messages messages = new Messages();
        if (data == null) {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("please.select.template.or.subdomain", null, LocaleContextHolder.getLocale()));
            return messages;
        }
        Long deleteSiteId = (Long) data.get("sid");
        Site deleteSite = serviceLocator.getSiteDao().findById(deleteSiteId);
        //delete Site Support
        List<SiteSupport> siteSupports = serviceLocator.getSiteSupportDao().findAll(deleteSiteId);
        for (SiteSupport siteSupport : siteSupports) {
            serviceLocator.getSiteSupportDao().remove(siteSupport);
        }

        //delete Site Support
        List<SiteQuestionAnswer> siteQuestionAnswers = serviceLocator.getSiteQuestionAnswerDao().findAll(deleteSiteId);
        for (SiteQuestionAnswer siteQuestionAnswer : siteQuestionAnswers) {
            serviceLocator.getSiteQuestionAnswerDao().remove(siteQuestionAnswer);
        }

        //Delete SiteContact
        List<SiteContact> sampleSiteContacts = serviceLocator.getSiteContactDao().findAll(deleteSiteId);
        for (SiteContact siteContact : sampleSiteContacts) {
            serviceLocator.getSiteContactDao().remove(siteContact);
        }

        //Delete header/footer
        List<SiteHeaderFooter> siteHeaderFooters = siteHeaderFooterDao.findAll(deleteSiteId);
        for (SiteHeaderFooter siteHeaderFooter : siteHeaderFooters) {
            siteHeaderFooterDao.remove(siteHeaderFooter);
        }

        //Delete site content by cloning from the site sample
        List<Menu> menus = menuDao.getRootMenus(deleteSite, "N", "N");
        for (Menu menu : menus) {
            List<Menu> subMenus = menuDao.getSubMenus(deleteSite, menu, "N", "N");
            for (Menu subMenu : subMenus) {
//                List<SiteMenuPartContent> siteMenuPartContents = serviceLocator.getSiteMenuPartContentDao().findByOrder("menu.id", subMenu.getId(), null, deleteSite.getId());
//                for (SiteMenuPartContent siteMenuPartContent : siteMenuPartContents) {
//                    serviceLocator.getSiteMenuPartContentDao().remove(siteMenuPartContent);
//                }
                subMenu.setParentMenu(null);
                menuDao.remove(subMenu);
            }
            //remove Root
//            List<SiteMenuPartContent> siteMenuPartContents = serviceLocator.getSiteMenuPartContentDao().findByOrder("menu.id", menu.getId(), null, deleteSite.getId());
//            for (SiteMenuPartContent siteMenuPartContent : siteMenuPartContents) {
//                serviceLocator.getSiteMenuPartContentDao().remove(siteMenuPartContent);
//            }
            menu.setSubMenus(null);
            menuDao.remove(menu);
        }

        //Delete news
        List<News> listNews = serviceLocator.getNewsDao().findAll(deleteSiteId);
        for (News news: listNews) {
            List<NewsCategory> newsCategories = serviceLocator.getNewsDao().findNewscategoriesByNewsId(news.getId());
            news.removeNewsNewsCategory(newsCategories);
            serviceLocator.getNewsDao().remove(news);
        }

        List<NewsCategory> rootNewsCategories = serviceLocator.getNewsCategoryDao().getRootNewsCategories(deleteSite, "N");
        for (NewsCategory rootNewsCategory:rootNewsCategories) {
            List<NewsCategory> newsCategories = serviceLocator.getNewsCategoryDao().getSubNewsCategories(deleteSite, rootNewsCategory, "N");
            for (NewsCategory newsCategory:newsCategories) {
                List<NewsCategory> subNewsCategories = serviceLocator.getNewsCategoryDao().getSubNewsCategories(deleteSite, newsCategory, "N");
                for (NewsCategory subNewsCategory:subNewsCategories) {
                    subNewsCategory.setParentNewsCategory(null);
                    serviceLocator.getNewsCategoryDao().remove(subNewsCategory);
                }
                newsCategory.setParentNewsCategory(null);
                serviceLocator.getNewsCategoryDao().remove(newsCategory);
            }
            rootNewsCategory.setParentNewsCategory(null);
            serviceLocator.getNewsCategoryDao().remove(rootNewsCategory);
        }


        //Delete Album
        List<Album> albums = serviceLocator.getAlbumDao().findAll(deleteSite.getId());
        for (Album album : albums) {
            //Create image
            List<AlbumImage> albumImages = serviceLocator.getAlbumImageDao().findAlbumImages(album.getId(), null, null, null, deleteSite.getId(), false);
            for (AlbumImage albumImage : albumImages) {
                albumImage.setAlbum(null);
                serviceLocator.getAlbumImageDao().remove(albumImage);
            }
            //Delete album
            album.setImages(null);
            serviceLocator.getAlbumDao().remove(album);
        }

        //Delete site template for new site
        SiteTemplate siteTemplate = siteTemplateDao.findUniqueBy("site.id", deleteSite.getId());
        if (siteTemplate != null) {
            siteTemplateDao.remove(siteTemplate);
        }

        //Delete site template for new site
        List<Template> templates = templateDao.findAll(deleteSite.getId());
        for (Template template : templates) {
            templateDao.remove(template);
        }

        //Delete site payment provider for new site
        List<PaymentProviderSite> paymentProviderSites = serviceLocator.getPaymentProviderSiteDao().findAll(deleteSite.getId());
        for (PaymentProviderSite paymentProviderSite : paymentProviderSites) {
            serviceLocator.getPaymentProviderSiteDao().remove(paymentProviderSite);
        }
        //Delete products for new site
        List<Product> products = serviceLocator.getProductDao().findAll(deleteSite.getId());
        for (Product product : products) {
            serviceLocator.getProductDao().remove(product);
        }

        //Delete user role
        List<UserRole> userRoles = userRoleDao.findUserRoles(deleteSite);
        for (UserRole userRole : userRoles) {
            userRoleDao.remove(userRole);
        }

        //Delete Site param
        List<SiteParam> siteParams = siteParamDao.findAllOrder(null, deleteSite.getId());
        for (SiteParam siteParam : siteParams) {
            siteParamDao.remove(siteParam);
        }

        //Delete site service
        List<SiteProductService> siteProductServices = serviceLocator.getSiteProductServiceDao().findAllOrder(null, deleteSite.getId());
        for (SiteProductService siteProductService : siteProductServices) {
            serviceLocator.getSiteProductServiceDao().remove(siteProductService);
        }

        //Delete Site
        deleteSite.setCatalogs(null);
        deleteSite.setSiteParams(null);
        deleteSite.setNewsCategories(null);
        deleteSite.setCategories(null);
        deleteSite.setProducts(null);
        deleteSite.setMenus(null);
        deleteSite.setCmsArea(null);
        deleteSite.setSiteTemplate(null);
        siteDao.remove(deleteSite);



        messages.addInfo("Delete success");

        return messages;
    }

    @Override
    public Messages createOrUpdateFreeShippingLocal(Promotion promotion, Site site) {
        Messages messages = new Messages();
        boolean isChange = false;
        if (!promotion.isEmptyId()) { //had shipping setting
            Promotion originalPromotion = serviceLocator.getPromotionDao().findUniqueBy("id", promotion.getId(), site.getId());
            if (originalPromotion != null) {
                //TODO process startDate and endDate
                if (promotion.getName() != null && !promotion.getName().equals(originalPromotion.getName())) {
                    originalPromotion.setName(promotion.getName());
                    isChange = true;
                }
                if (promotion.getDescription() != null && !promotion.getDescription().equals(originalPromotion.getDescription())) {
                    originalPromotion.setDescription(promotion.getDescription());
                    isChange = true;
                }
                if (promotion.getActive() != null && !promotion.getActive().equals(originalPromotion.getActive())) {
                    originalPromotion.setActive(promotion.getActive());
                    isChange = true;
                }
                if (promotion.getPromoParam2() != null && !promotion.getPromoParam2().equals(originalPromotion.getPromoParam2())) {
                    originalPromotion.setPromoParam2(promotion.getPromoParam2());
                    isChange = true;
                }

                if (!isChange) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                } else {
                    //Update promotion
                    serviceLocator.getPromotionDao().merge(originalPromotion);
                    //update Promotion_Condition
                    ConditionClass conditionClass = serviceLocator.getConditionClassDao().findUniqueBy("className", "com.easysoft.ecommerce.service.condition.impl.ContainCondition");
                    Map input = new HashMap();
                    input.put("condition.id", conditionClass.getId());
                    input.put("promotion.id", originalPromotion.getId());
                    PromotionCondition promotionCondition = serviceLocator.getPromotionConditionDao().findUniqueBy(input, null);
                    //update Promotion Condition
                    if (promotionCondition != null) {
                        String expression = "'"+originalPromotion.getPromoParam2()+"'.contains('SHIPPING_CITY')";
                        if (!expression.equals(promotionCondition.getExpression())) {
                            promotionCondition.setExpression(expression);
                            serviceLocator.getPromotionConditionDao().merge(promotionCondition);
                        }
                        //insert new Promotion Condition
                    } else {
                        promotionCondition = new PromotionCondition();
                        promotionCondition.setPromotion(originalPromotion);
                        promotionCondition.setSequence(1);
                        promotionCondition.setExpression("'"+originalPromotion.getPromoParam2()+"'.contains('SHIPPING_CITY')");
                        promotionCondition.setCondition(conditionClass);
                        serviceLocator.getPromotionConditionDao().persist(promotionCondition);
                    }

                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("shipping.promotion.save.success", null, LocaleContextHolder.getLocale()));
                }
            }
        } else {
            //TODO process startDate and endDate
            promotion.setStartDate(new Date());
            promotion.setEndDate(null);
            promotion.setSite(site);
            promotion.setPromoType("SHIPPING");
            PromotionClass promotionClass = serviceLocator.getPromotionClassDao().findUniqueBy("className", "com.easysoft.ecommerce.service.promotion.impl.ShippingAward");
            promotion.setPromoClass(promotionClass);
            serviceLocator.getPromotionDao().persist(promotion);
            //Store Promotion Condition
            PromotionCondition promotionCondition = new PromotionCondition();
            promotionCondition.setPromotion(promotion);
            promotionCondition.setSequence(1);
            promotionCondition.setExpression("'"+promotion.getPromoParam2()+"'.contains('SHIPPING_CITY')");
            ConditionClass conditionClass = serviceLocator.getConditionClassDao().findUniqueBy("className", "com.easysoft.ecommerce.service.condition.impl.ContainCondition");
            promotionCondition.setCondition(conditionClass);
            serviceLocator.getPromotionConditionDao().persist(promotionCondition);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("shipping.promotion.save.success", null, LocaleContextHolder.getLocale()));

        }
        return messages;
    }

    public Messages createOrUpdateFreeShippingTotalPrice(Promotion promotion, Site site) {
        Messages messages = new Messages();
        boolean isChange = false;
        if (!promotion.isEmptyId()) { //had shipping setting
            Promotion originalPromotion = serviceLocator.getPromotionDao().findUniqueBy("id", promotion.getId(), site.getId());
            if (originalPromotion != null) {
                //TODO process startDate and endDate
                if (promotion.getName() != null && !promotion.getName().equals(originalPromotion.getName())) {
                    originalPromotion.setName(promotion.getName());
                    isChange = true;
                }
                if (promotion.getDescription() != null && !promotion.getDescription().equals(originalPromotion.getDescription())) {
                    originalPromotion.setDescription(promotion.getDescription());
                    isChange = true;
                }
                if (promotion.getActive() != null && !promotion.getActive().equals(originalPromotion.getActive())) {
                    originalPromotion.setActive(promotion.getActive());
                    isChange = true;
                }
                if (promotion.getPromoParam2() != null && !promotion.getPromoParam2().equals(originalPromotion.getPromoParam2())) {
                    originalPromotion.setPromoParam2(promotion.getPromoParam2());
                    isChange = true;
                }

                if (!isChange) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                } else {
                    //Update promotion
                    serviceLocator.getPromotionDao().merge(originalPromotion);
                    //update Promotion_Condition
                    ConditionClass conditionClass = serviceLocator.getConditionClassDao().findUniqueBy("className", "com.easysoft.ecommerce.service.condition.impl.SubOrderTotalCondition");
                    Map input = new HashMap();
                    input.put("condition.id", conditionClass.getId());
                    input.put("promotion.id", originalPromotion.getId());
                    PromotionCondition promotionCondition = serviceLocator.getPromotionConditionDao().findUniqueBy(input, null);
                    //update Promotion Condition
                    if (promotionCondition != null) {
                        String expression = "(SUB_TOTAL_PRICE - SUB_TOTAL_PRICE_DISCOUNT) >= "+originalPromotion.getPromoParam2();
                        if (!expression.equals(promotionCondition.getExpression())) {
                            promotionCondition.setExpression(expression);
                            serviceLocator.getPromotionConditionDao().merge(promotionCondition);
                        }
                        //insert new Promotion Condition
                    } else {
                        promotionCondition = new PromotionCondition();
                        promotionCondition.setPromotion(originalPromotion);
                        promotionCondition.setSequence(1);
                        promotionCondition.setExpression("(SUB_TOTAL_PRICE - SUB_TOTAL_PRICE_DISCOUNT) >= "+originalPromotion.getPromoParam2());
                        promotionCondition.setCondition(conditionClass);
                        serviceLocator.getPromotionConditionDao().persist(promotionCondition);
                    }
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("shipping.promotion.save.success", null, LocaleContextHolder.getLocale()));
                }
            }
        } else {
            //TODO process startDate and endDate
            promotion.setStartDate(new Date());
            promotion.setEndDate(null);
            promotion.setSite(site);
            promotion.setPromoType("SHIPPING");
            PromotionClass promotionClass = serviceLocator.getPromotionClassDao().findUniqueBy("className", "com.easysoft.ecommerce.service.promotion.impl.ShippingAward");
            promotion.setPromoClass(promotionClass);
            serviceLocator.getPromotionDao().persist(promotion);
            //Store Promotion Condition
            PromotionCondition promotionCondition = new PromotionCondition();
            promotionCondition.setPromotion(promotion);
            promotionCondition.setSequence(1);
            promotionCondition.setExpression("(SUB_TOTAL_PRICE - SUB_TOTAL_PRICE_DISCOUNT) >= "+promotion.getPromoParam2());
            ConditionClass conditionClass = serviceLocator.getConditionClassDao().findUniqueBy("className", "com.easysoft.ecommerce.service.condition.impl.SubOrderTotalCondition");
            promotionCondition.setCondition(conditionClass);
            serviceLocator.getPromotionConditionDao().persist(promotionCondition);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("shipping.promotion.save.success", null, LocaleContextHolder.getLocale()));

        }
        return messages;
    }

    private void createSiteMenuPartContent(Menu menu, Site siteSample, Site newSite, Menu newMenu) {
        //Copy site menu part content
        List<Row> rows = serviceLocator.getRowDao().findActiveByOrder("menu.id", menu.getId(), null);
        for (Row sampleRow : rows) {
            Row newRow = new Row();
            try {
                BeanUtils.copyProperties(newRow, sampleRow);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            newRow.setId(null);
            newRow.setMenu(newMenu);
            newRow.setPartContents(null);
            serviceLocator.getRowDao().persist(newRow);

            List<SiteMenuPartContent> siteMenuPartContents = serviceLocator.getSiteMenuPartContentDao().findActiveByOrder("row.id", sampleRow.getId(), null);
            for (SiteMenuPartContent sampleSiteMenuPartContent : siteMenuPartContents) {
                SiteMenuPartContent newSiteMenuPartContent = new SiteMenuPartContent();
                try {
                    BeanUtils.copyProperties(newSiteMenuPartContent, sampleSiteMenuPartContent);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                newSiteMenuPartContent.setId(null);
                newSiteMenuPartContent.setRow(newRow);
                newSiteMenuPartContent.setCreatedDate(new Date());
                serviceLocator.getSiteMenuPartContentDao().persist(newSiteMenuPartContent);
            }
        }

    }

    private void createMenuPage (Menu menu, Site newSite, Menu newMenu, Menu newParentMenu) {
        try {
            BeanUtils.copyProperties(newMenu, menu);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        newMenu.setId(null);
        newMenu.setSite(newSite);
        newMenu.setParentMenu(newParentMenu);
        newMenu.setSubMenus(null);
        newMenu.setRows(null);
        newMenu.setCreatedDate(new Date());
        newMenu.setUpdatedDate(new Date());
        menuDao.persist(newMenu);
    }

    @Override
    public Locale getSiteLocale(Site site) {
        String language = site.getSiteParam("LANGUAGE");

        Locale locale = null;
        if (language != null) {
            locale = LocaleUtils.toLocale(language);
        }
        return locale;
    }

    @Override
    public TimeZone getSiteTimeZone(Site site) {
        String timeZoneID = site.getSiteParamsMap().get("TIMEZONE");
        TimeZone timeZone = null;
        if (timeZoneID != null) {
            timeZone = TimeZone.getTimeZone(timeZoneID);
        }
        return timeZone;
    }

    @Override
    public String getSiteTheme(Site site) {
        if (site.getSiteTemplate() != null) {
            return site.getSiteTemplate().getTemplateCode();
        } else {
            return null;
        }
    }

}
