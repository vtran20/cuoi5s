package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.SiteDao;
import com.easysoft.ecommerce.dao.SiteParamDao;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.SiteService;
import com.easysoft.ecommerce.service.impl.URLUTF8Encoder;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@RequestMapping("/admin/sites")
@Controller
public class SiteAdminController {

    @Autowired
    private SiteService service;
    @Autowired
    private SiteDao dao;
    @Autowired
    private SiteParamDao siteParamDao;
    @Autowired
    private ServiceLocator serviceLocator;

    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/sites/" + action;
    }
    @RequestMapping(value = "setting/{action}.html", method = RequestMethod.GET)
    public String settingAction(@PathVariable String action) throws Exception {
        return "admin/sites/setting/" + action;
    }

    @RequestMapping(value = "update.html", produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String update(@Valid Site site, HttpServletRequest request) throws Exception {
        String contactUsEmail = request.getParameter("contactUsEmail");
        String contactUsPhone = request.getParameter("contactUsPhone");
        String language = request.getParameter("language");
        HashMap<String, String> siteParams = new HashMap<String, String>();
        siteParams.put("CONTACT_US", contactUsEmail);
        siteParams.put("PHONE_CONTACT", contactUsPhone);
        siteParams.put("LANGUAGE", language);
        Messages messages = service.createOrUpdate(site, siteParams);
        serviceLocator.getCacheData().removeCommonCache(request.getServerName());
        return messages.toString();
    }

    @RequestMapping(value = "sp_update.html", method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String updateSiteParam(HttpServletRequest request) throws Exception {
        Site currentSite = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Messages messages = new Messages();
        Enumeration enumeration = request.getParameterNames();
        boolean isChange = false;
        while (enumeration.hasMoreElements()) {
            String parameterName = (String) enumeration.nextElement();
            String value = request.getParameter(parameterName);
            if (parameterName.equals("csrf")) continue;
            if (value != null && !value.equals(currentSite.getSiteParamsMap().get(parameterName))) {
                if (currentSite.getSiteParamsMap().get(parameterName) == null) { //Insert language
                    Site original = serviceLocator.getSiteDao().findUniqueBy("id", currentSite.getId());//get persist entity

                    SiteParam siteParam = new SiteParam();
                    siteParam.setSiteId(original.getId());
                    siteParam.setKey(parameterName);
                    siteParam.setValue(value);
                    siteParamDao.persist(siteParam);
                    isChange = true;
                    //Add Currency if we are update/insert currency format
                    if ("CURRENCY_FORMAT".equalsIgnoreCase(parameterName)) {
                        if ("#,###,##0".equalsIgnoreCase(value)) {
                            siteParam = new SiteParam();
                            siteParam.setSiteId(original.getId());
                            siteParam.setKey("CURRENCY");
                            siteParam.setValue("VND");
                            siteParamDao.persist(siteParam);
                        } else if ("#0.00".equalsIgnoreCase(value)) {
                            siteParam = new SiteParam();
                            siteParam.setSiteId(original.getId());
                            siteParam.setKey("CURRENCY");
                            siteParam.setValue("$");
                            siteParamDao.persist(siteParam);
                        }
                    }

                } else { //Update site param.
                    SiteParam siteParam = siteParamDao.findUniqueBy("key", parameterName, currentSite.getId());
                    siteParam.setValue(value);
                    siteParamDao.merge(siteParam);
                    isChange = true;
                    if ("CURRENCY_FORMAT".equalsIgnoreCase(parameterName)) {
                        if ("#,###,##0".equalsIgnoreCase(value)) {
                            siteParam = siteParamDao.findUniqueBy("key", "CURRENCY", currentSite.getId());
                            siteParam.setValue("VND");
                            siteParamDao.merge(siteParam);
                        } else if ("#0.00".equalsIgnoreCase(value)) {
                            siteParam = siteParamDao.findUniqueBy("key", "CURRENCY", currentSite.getId());
                            siteParam.setValue("$");
                            siteParamDao.merge(siteParam);
                        }
                    }

                }
            }
        }
        if (isChange) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            serviceLocator.getCacheData().removeCommonCache(request.getServerName());
            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
        } else {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }


    @RequestMapping(value = {"template_update.html","sp_update.html"}, method = RequestMethod.GET)
    public String updateGetMethod() throws Exception {
        return "redirect:/admin#/admin/sites/site_setting";
    }

    @RequestMapping(value = "select-template.html", method = RequestMethod.GET)
    public ModelAndView selectTemplate(@Valid Long templateId, HttpServletRequest request) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Messages messages = service.createSite(null, site);
        //remove site cache. TODO: Should implement such as AspectJ
        serviceLocator.getCacheData().removeCommonCache(request.getServerName());
        return new ModelAndView("admin/sites/create", "messages", messages);
    }

    @RequestMapping(value = "template_update.html", method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String updateSiteColor(@Valid SiteTemplate siteTemplate,HttpServletRequest request) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        SiteTemplate originalSiteTemplate = serviceLocator.getSiteTemplateDao().findUniqueBy("site.id", site.getId());
        Messages messages = new Messages();
        //Update site color
        if (siteTemplate != null && originalSiteTemplate != null && !siteTemplate.getTemplateColorCode().equals(originalSiteTemplate.getTemplateColorCode())) {
            originalSiteTemplate.setTemplateColorCode(siteTemplate.getTemplateColorCode());
            originalSiteTemplate.setUpdatedDate(new Date());
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("template.color.change", null, LocaleContextHolder.getLocale()));
        }
        //Update layout (wide for boxed)
        if (siteTemplate != null && originalSiteTemplate != null && !siteTemplate.getFullWide().equals(originalSiteTemplate.getFullWide())) {
            originalSiteTemplate.setFullWide(siteTemplate.getFullWide());
            originalSiteTemplate.setUpdatedDate(new Date());
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("template.layout.change", null, LocaleContextHolder.getLocale()));
        }
        //Update header (fix or not)
//        if (siteTemplate != null && originalSiteTemplate != null && !siteTemplate.getHeaderFix().equals(originalSiteTemplate.getHeaderFix())) {
//            originalSiteTemplate.setHeaderFix(siteTemplate.getHeaderFix());
//            originalSiteTemplate.setUpdatedDate(new Date());
//            messages.addInfo("The template header is updated");
//        }
        //Update header (fix or not)
        if (siteTemplate != null && originalSiteTemplate != null && !siteTemplate.getSkinColor().equals(originalSiteTemplate.getSkinColor())) {
            originalSiteTemplate.setSkinColor(siteTemplate.getSkinColor());
            originalSiteTemplate.setUpdatedDate(new Date());
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("template.skincolor.change", null, LocaleContextHolder.getLocale()));
        }
        if (!messages.isEmpty()) {
            serviceLocator.getSiteTemplateDao().merge(originalSiteTemplate);
        } else {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }

        serviceLocator.getCacheData().removeCommonCache(request.getServerName());
        return messages.toString();
    }
    @RequestMapping(value = "select_payment_providers.html", produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String selectPaymentProvider(@RequestParam String paymentProviderIds) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        List<PaymentProvider> paymentProviders = serviceLocator.getPaymentProviderDao().findObjectInBy(null, "id", paymentProviderIds, null, null, "sequence", null);
        for (PaymentProvider paymentProvider: paymentProviders) {
            PaymentProviderSite paymentProviderSite = new PaymentProviderSite();
            paymentProviderSite.setName(paymentProvider.getName());
            paymentProviderSite.setSequence(paymentProvider.getSequence());
            paymentProviderSite.setSite(site);
            paymentProviderSite.setPaymentProvider(paymentProvider);
            serviceLocator.getPaymentProviderSiteDao().persist(paymentProviderSite);
        }
        Messages messages = new Messages();
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
        return messages.toString();

    }

    @RequestMapping(value = "deletepaymentprovider.html", produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String deletePaymentProvider(@RequestParam Long id) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        PaymentProviderSite paymentProviderSite = serviceLocator.getPaymentProviderSiteDao().findById(id, site.getId());
        if (paymentProviderSite != null) {
            serviceLocator.getPaymentProviderSiteDao().remove(paymentProviderSite);
        }
        Messages messages = new Messages();
        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
        return messages.toString();
    }

    @RequestMapping(value = "update_payment_method.html", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView updatePaymentProvider(@Valid PaymentProviderSite paymentProviderSite, HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        boolean isChange = false;
        if (paymentProviderSite != null) {
            if (!paymentProviderSite.isEmptyId()) { //had header footer record
                PaymentProviderSite originalPaymentProviderSite = serviceLocator.getPaymentProviderSiteDao().findUniqueBy("id", paymentProviderSite.getId(), site.getId());
                if (originalPaymentProviderSite != null) {
                    if (paymentProviderSite.getName() != null && !paymentProviderSite.getName().equals(originalPaymentProviderSite.getName())) {
                        originalPaymentProviderSite.setName(paymentProviderSite.getName());
                        isChange = true;
                    }
                    if (paymentProviderSite.getDescription() != null && !paymentProviderSite.getDescription().equals(originalPaymentProviderSite.getDescription())) {
                        originalPaymentProviderSite.setDescription(paymentProviderSite.getDescription());
                        isChange = true;
                    }
                    if (!isChange) {
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                    } else {
                        serviceLocator.getPaymentProviderSiteDao().merge(originalPaymentProviderSite);
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                    }
                }
            } else { //don't have header footer record
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
            }
        }
        return new ModelAndView("admin/sites/header", "messages", messages);
    }

    @RequestMapping(value = "update_shipping_setting.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String updateShippingSetting(@Valid ShippingSite shippingSite, @RequestParam int typeShippingFee, HttpServletRequest request) throws Exception {
        //Validate
        Messages messages = new Messages();
        if (shippingSite != null) {
            if (shippingSite.getPercentOfFirstProduct() > 100 || shippingSite.getPercentOfFirstProduct() < 0) {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("shipping.percent.fee.warning", null, LocaleContextHolder.getLocale()));
            return messages.toString();
            }
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            boolean isChange = false;
            if (!shippingSite.isEmptyId()) { //had shipping setting
                ShippingSite originalShippingSite = serviceLocator.getShippingSiteDao().findUniqueBy("id", shippingSite.getId(), site.getId());
                if (originalShippingSite != null) {
                    if (shippingSite.getName() != null && !shippingSite.getName().equals(originalShippingSite.getName())) {
                        originalShippingSite.setName(shippingSite.getName());
                        isChange = true;
                    }
                    if (shippingSite.getDescription() != null && !shippingSite.getDescription().equals(originalShippingSite.getDescription())) {
                        originalShippingSite.setDescription(shippingSite.getDescription());
                        isChange = true;
                    }

                    //Shipping fee by Site
                    if (shippingSite.getUsePriceBySite() != null && !shippingSite.getUsePriceBySite().equals(originalShippingSite.getUsePriceBySite())) {
                        originalShippingSite.setUsePriceBySite(shippingSite.getUsePriceBySite());
                        isChange = true;
                    }
                    if (!shippingSite.getPriceBySite().equals(originalShippingSite.getPriceBySite())) {
                        originalShippingSite.setPriceBySite(shippingSite.getPriceBySite());
                        isChange = true;
                    }
                    //Shipping fee by Product
                    if (shippingSite.getUsePriceByProduct() != null && !shippingSite.getUsePriceByProduct().equals(originalShippingSite.getUsePriceByProduct())) {
                        originalShippingSite.setUsePriceByProduct(shippingSite.getUsePriceByProduct());
                        isChange = true;
                    }
                    if (!shippingSite.getPriceByProduct().equals(originalShippingSite.getPriceByProduct())) {
                        originalShippingSite.setPriceByProduct(shippingSite.getPriceByProduct());
                        isChange = true;
                    }
                    if (shippingSite.getPercentOfFirstProduct() != originalShippingSite.getPercentOfFirstProduct()) {
                        originalShippingSite.setPercentOfFirstProduct(shippingSite.getPercentOfFirstProduct());
                        isChange = true;
                    }

                    if (!isChange) {
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                    } else {
                        serviceLocator.getShippingSiteDao().merge(originalShippingSite);
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("shipping.method.save.success", null, LocaleContextHolder.getLocale()));
                    }
                }
            } else { //don't have shipping site
//                <spring:eval expression="serviceLocator.shippingSiteDao.findUniqueBy(null, null, site.id)" var="shippingSite"/>
                ShippingSite originalShippingSite = serviceLocator.getShippingSiteDao().findUniqueBy(null, null, site.getId());
                if (originalShippingSite != null) {
                    if (shippingSite.getName() != null && !shippingSite.getName().equals(originalShippingSite.getName())) {
                        originalShippingSite.setName(shippingSite.getName());
                        isChange = true;
                    }
                    if (shippingSite.getDescription() != null && !shippingSite.getDescription().equals(originalShippingSite.getDescription())) {
                        originalShippingSite.setDescription(shippingSite.getDescription());
                        isChange = true;
                    }

                    //Shipping fee by Site
                    if (shippingSite.getUsePriceBySite() != null && !shippingSite.getUsePriceBySite().equals(originalShippingSite.getUsePriceBySite())) {
                        originalShippingSite.setUsePriceBySite(shippingSite.getUsePriceBySite());
                        isChange = true;
                    }
                    if (!shippingSite.getPriceBySite().equals(originalShippingSite.getPriceBySite())) {
                        originalShippingSite.setPriceBySite(shippingSite.getPriceBySite());
                        isChange = true;
                    }
                    //Shipping fee by Product
                    if (shippingSite.getUsePriceByProduct() != null && !shippingSite.getUsePriceByProduct().equals(originalShippingSite.getUsePriceByProduct())) {
                        originalShippingSite.setUsePriceByProduct(shippingSite.getUsePriceByProduct());
                        isChange = true;
                    }
                    if (!shippingSite.getPriceByProduct().equals(originalShippingSite.getPriceByProduct())) {
                        originalShippingSite.setPriceByProduct(shippingSite.getPriceByProduct());
                        isChange = true;
                    }
                    if (shippingSite.getPercentOfFirstProduct() != originalShippingSite.getPercentOfFirstProduct()) {
                        originalShippingSite.setPercentOfFirstProduct(shippingSite.getPercentOfFirstProduct());
                        isChange = true;
                    }

                    if (!isChange) {
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                    } else {
                        serviceLocator.getShippingSiteDao().merge(originalShippingSite);
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("shipping.method.save.success", null, LocaleContextHolder.getLocale()));
                    }
                } else {
                    shippingSite.setSite(site);
                    serviceLocator.getShippingSiteDao().persist(shippingSite);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("shipping.method.save.success", null, LocaleContextHolder.getLocale()));
                }
            }
        }
        return messages.toString();
    }

    @RequestMapping(value = "update_discount_shipping.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String updatePromoShipping(@Valid Promotion promotion, HttpServletRequest request) throws Exception {
        //Validate
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String[] cities = request.getParameterValues("city");
        String cityList = "";
        if (promotion != null) {
            if ("FREESHIPPINGGREATER".equals(promotion.getPromoCode())) {
                messages = serviceLocator.getSiteService().createOrUpdateFreeShippingTotalPrice(promotion, site);
            } else if ("FREESHIPPINGINNER".equals(promotion.getPromoCode())) {
                if (cities != null) {
                    for (String city: cities) {
                        if (StringUtils.isEmpty(cityList)) {
                            cityList = city;
                        } else {
                            cityList += ","+city;
                        }
                    }
                }
                promotion.setPromoParam2(cityList);
                messages = serviceLocator.getSiteService().createOrUpdateFreeShippingLocal(promotion, site);
            }
        }
        return messages.toString();
    }
    @RequestMapping(value = "update_header_footer.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String updateHeaderFooter(@Valid SiteHeaderFooter siteHeaderFooter, HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        boolean isChange = false;
        if (siteHeaderFooter != null) {
            if (!siteHeaderFooter.isEmptyId()) { //had header footer record
                SiteHeaderFooter originalSiteHeaderFooter = serviceLocator.getSiteHeaderFooterDao().findUniqueBy("id",siteHeaderFooter.getId(), site.getId());
                if (originalSiteHeaderFooter != null) {
                    if (siteHeaderFooter.getLogoImg() != null && !siteHeaderFooter.getLogoImg().equals(originalSiteHeaderFooter.getLogoImg())) {
                        originalSiteHeaderFooter.setLogoImg(siteHeaderFooter.getLogoImg());
                        isChange = true;
                    }
                    if (siteHeaderFooter.getUseLogoImg() != null && !siteHeaderFooter.getUseLogoImg().equals(originalSiteHeaderFooter.getUseLogoImg())) {
                        if ("Y".equals(siteHeaderFooter.getUseLogoImg())) {
                            originalSiteHeaderFooter.setUseLogoImg(siteHeaderFooter.getUseLogoImg());
                        } else {
                            originalSiteHeaderFooter.setUseLogoImg(siteHeaderFooter.getUseLogoImg());
                        }
                        isChange = true;
                    }
                    if (siteHeaderFooter.getLogoText() != null && !siteHeaderFooter.getLogoText().equals(originalSiteHeaderFooter.getLogoText())) {
                        originalSiteHeaderFooter.setLogoText(siteHeaderFooter.getLogoText());
                        isChange = true;
                    }
                    if (siteHeaderFooter.getSloganText() != null && !siteHeaderFooter.getSloganText().equals(originalSiteHeaderFooter.getSloganText())) {
                        originalSiteHeaderFooter.setSloganText(siteHeaderFooter.getSloganText());
                        isChange = true;
                    }
                    if (isChange) {
                        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateHeaderCacheKey());
                        serviceLocator.getSiteHeaderFooterDao().merge(originalSiteHeaderFooter);
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                    } else {
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                    }
                }
            } else { //don't have header footer record
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateHeaderCacheKey());
                siteHeaderFooter.setSite(site);
                siteHeaderFooter.setUpdatedDate(new Date());
                serviceLocator.getSiteHeaderFooterDao().persist(siteHeaderFooter);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        return messages.toString();
    }

    @RequestMapping(value = "footer_content.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String updateFooterContent(@Valid SiteHeaderFooter siteHeaderFooter, HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        boolean isUpdate = false;
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (siteHeaderFooter != null) {
            SiteHeaderFooter originalSiteHeaderFooter = serviceLocator.getSiteHeaderFooterDao().findUniqueBy("site.id", site.getId());
            if (originalSiteHeaderFooter != null) { //had header footer record
                //Footer Content
                if (siteHeaderFooter.getFooterHeader() != null && !siteHeaderFooter.getFooterHeader().equals(originalSiteHeaderFooter.getFooterHeader())) {
                    originalSiteHeaderFooter.setFooterHeader(siteHeaderFooter.getFooterHeader());
                    isUpdate = true;
                }
                if (siteHeaderFooter.getFooterHeaderDisplay() != null && !siteHeaderFooter.getFooterHeaderDisplay().equals(originalSiteHeaderFooter.getFooterHeaderDisplay())) {
                    originalSiteHeaderFooter.setFooterHeaderDisplay(siteHeaderFooter.getFooterHeaderDisplay());
                    isUpdate = true;
                }
                if (siteHeaderFooter.getFooterContent() != null && !siteHeaderFooter.getFooterContent().equals(originalSiteHeaderFooter.getFooterContent())) {
                    originalSiteHeaderFooter.setFooterContent(siteHeaderFooter.getFooterContent());
                    isUpdate = true;
                }
                if (!isUpdate) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                } else {
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                    serviceLocator.getSiteHeaderFooterDao().merge(originalSiteHeaderFooter);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                }
            } else { //don't have header footer record
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                siteHeaderFooter.setSite(site);
                siteHeaderFooter.setUpdatedDate(new Date());
                serviceLocator.getSiteHeaderFooterDao().persist(siteHeaderFooter);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        return messages.toString();
    }

    @RequestMapping(value = "footer_support_update.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String updateFooterSupport(@Valid SiteHeaderFooter siteHeaderFooter, HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        boolean isUpdate = false;
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (siteHeaderFooter != null) {
            SiteHeaderFooter originalSiteHeaderFooter = serviceLocator.getSiteHeaderFooterDao().findUniqueBy("site.id", site.getId());
            if (originalSiteHeaderFooter != null) { //had header footer record
                //Footer Support
                if (siteHeaderFooter.getFooterSupportHeader() != null && !siteHeaderFooter.getFooterSupportHeader().equals(originalSiteHeaderFooter.getFooterSupportHeader())) {
                    originalSiteHeaderFooter.setFooterSupportHeader(siteHeaderFooter.getFooterSupportHeader());
                    isUpdate = true;
                }
                if (siteHeaderFooter.getFooterSupportDisplay() != null && !siteHeaderFooter.getFooterSupportDisplay().equals(originalSiteHeaderFooter.getFooterSupportDisplay())) {
                    originalSiteHeaderFooter.setFooterSupportDisplay(siteHeaderFooter.getFooterSupportDisplay());
                    isUpdate = true;
                }
                if (!isUpdate) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                } else {
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                    serviceLocator.getSiteHeaderFooterDao().merge(originalSiteHeaderFooter);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                }
            } else { //don't have header footer record
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                siteHeaderFooter.setSite(site);
                siteHeaderFooter.setUpdatedDate(new Date());
                serviceLocator.getSiteHeaderFooterDao().persist(siteHeaderFooter);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        return messages.toString();
    }
    @RequestMapping(value = "footer_contact_update.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String updateFooterContact(@Valid SiteHeaderFooter siteHeaderFooter, HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        boolean isUpdate = false;
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (siteHeaderFooter != null) {
            SiteHeaderFooter originalSiteHeaderFooter = serviceLocator.getSiteHeaderFooterDao().findUniqueBy("site.id", site.getId());
            if (originalSiteHeaderFooter != null) { //had header footer record
                //Footer Contact
                if (siteHeaderFooter.getFooterContactHeader() != null && !siteHeaderFooter.getFooterContactHeader().equals(originalSiteHeaderFooter.getFooterContactHeader())) {
                    originalSiteHeaderFooter.setFooterContactHeader(siteHeaderFooter.getFooterContactHeader());
                    isUpdate = true;
                }
                if (siteHeaderFooter.getFooterContactDisplay() != null && !siteHeaderFooter.getFooterContactDisplay().equals(originalSiteHeaderFooter.getFooterContactDisplay())) {
                    originalSiteHeaderFooter.setFooterContactDisplay(siteHeaderFooter.getFooterContactDisplay());
                    isUpdate = true;
                }

                if (!isUpdate) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                } else {
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                    serviceLocator.getSiteHeaderFooterDao().merge(originalSiteHeaderFooter);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                }
            } else { //don't have header footer record
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                siteHeaderFooter.setSite(site);
                siteHeaderFooter.setUpdatedDate(new Date());
                serviceLocator.getSiteHeaderFooterDao().persist(siteHeaderFooter);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        return messages.toString();
    }
    @RequestMapping(value = "footer_usefullink.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String updateFooterUsefulLink(@Valid SiteHeaderFooter siteHeaderFooter, HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        boolean isUpdate = false;
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (siteHeaderFooter != null) {
            SiteHeaderFooter originalSiteHeaderFooter = serviceLocator.getSiteHeaderFooterDao().findUniqueBy("site.id", site.getId());
            if (originalSiteHeaderFooter != null) { //had header footer record
                //Footer Useful Link
                if (siteHeaderFooter.getFooterUsefulLinkHeader() != null && !siteHeaderFooter.getFooterUsefulLinkHeader().equals(originalSiteHeaderFooter.getFooterUsefulLinkHeader())) {
                    originalSiteHeaderFooter.setFooterUsefulLinkHeader(siteHeaderFooter.getFooterUsefulLinkHeader());
                    isUpdate = true;
                }
                if (siteHeaderFooter.getFooterUsefulLinkDisplay() != null && !siteHeaderFooter.getFooterUsefulLinkDisplay().equals(originalSiteHeaderFooter.getFooterUsefulLinkDisplay())) {
                    originalSiteHeaderFooter.setFooterUsefulLinkDisplay(siteHeaderFooter.getFooterUsefulLinkDisplay());
                    isUpdate = true;
                }

                if (!isUpdate) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                } else {
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                    serviceLocator.getSiteHeaderFooterDao().merge(originalSiteHeaderFooter);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                }
            } else { //don't have header footer record
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                siteHeaderFooter.setSite(site);
                siteHeaderFooter.setUpdatedDate(new Date());
                serviceLocator.getSiteHeaderFooterDao().persist(siteHeaderFooter);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        return messages.toString();
    }
    @RequestMapping(value = "footer_newslink.html", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody
    String updateFooterNewsLink(@Valid SiteHeaderFooter siteHeaderFooter, HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        boolean isUpdate = false;
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (siteHeaderFooter != null) {
            SiteHeaderFooter originalSiteHeaderFooter = serviceLocator.getSiteHeaderFooterDao().findUniqueBy("site.id", site.getId());
            if (originalSiteHeaderFooter != null) { //had header footer record
                //Footer News Link
                if (siteHeaderFooter.getFooterNewsLinkHeader() != null && !siteHeaderFooter.getFooterNewsLinkHeader().equals(originalSiteHeaderFooter.getFooterNewsLinkHeader())) {
                    originalSiteHeaderFooter.setFooterNewsLinkHeader(siteHeaderFooter.getFooterNewsLinkHeader());
                    isUpdate = true;
                }
                if (siteHeaderFooter.getFooterNewsLinkDisplay() != null && !siteHeaderFooter.getFooterNewsLinkDisplay().equals(originalSiteHeaderFooter.getFooterNewsLinkDisplay())) {
                    originalSiteHeaderFooter.setFooterNewsLinkDisplay(siteHeaderFooter.getFooterNewsLinkDisplay());
                    isUpdate = true;
                }

                if (!isUpdate) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                } else {
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                    serviceLocator.getSiteHeaderFooterDao().merge(originalSiteHeaderFooter);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                }
            } else { //don't have header footer record
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                siteHeaderFooter.setSite(site);
                siteHeaderFooter.setUpdatedDate(new Date());
                serviceLocator.getSiteHeaderFooterDao().persist(siteHeaderFooter);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        return messages.toString();
    }

    @RequestMapping(value = "form.html", method = RequestMethod.GET)
    public ModelAndView displayForm() throws Exception {
        Site newSite = new Site();
        List<SiteParam> siteParams = new ArrayList<SiteParam>();
        newSite.setSiteParams(siteParams);
        siteParams.add(new SiteParam("CURRENCY", "VND", newSite));
        siteParams.add(new SiteParam("CURRENCY_FORMAT", "#0,000,000", newSite));
        siteParams.add(new SiteParam("DATE_FORMAT", "dd/MM/yyyy", newSite));
        siteParams.add(new SiteParam("LANGUAGE", "vi_VN", newSite));
        siteParams.add(new SiteParam("THEME", "default", newSite));

        return new ModelAndView("admin/sites/form", "command", newSite);
    }

    @RequestMapping(value = "savesitecontact.html", method = {RequestMethod.GET})
    public String saveSiteContact() throws Exception {
        return "redirect:/admin#admin/sites/contactus_index";
    }
    @RequestMapping(value = "savesitecontact.html", method = {RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String saveSiteContact(@Valid SiteContact siteContact) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Messages messages = new Messages();
        boolean isChange = false;
        if (siteContact != null) {
            if (siteContact.isEmptyId()) {
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateContactUsCacheKey(null));
                float maxSequence = getServiceLocator().getSiteContactDao().getMaxSequence(site.getId());
                siteContact.setSequence(maxSequence+1f);
                siteContact.setSite(site);
                getServiceLocator().getSiteContactDao().persist(siteContact);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            } else {
                //Update Site Contact
                SiteContact originalSiteContact = getServiceLocator().getSiteContactDao().findById(siteContact.getId());

                if (siteContact.getName() != null && !siteContact.getName().equals(originalSiteContact.getName())) {
                    originalSiteContact.setName(siteContact.getName());
                    isChange = true;
                }
                if (siteContact.getAddress_1() != null && !siteContact.getAddress_1().equals(originalSiteContact.getAddress_1())) {
                    originalSiteContact.setAddress_1(siteContact.getAddress_1());
                    isChange = true;
                }
                if (siteContact.getAddress_2() != null && !siteContact.getAddress_2().equals(originalSiteContact.getAddress_2())) {
                    originalSiteContact.setAddress_2(siteContact.getAddress_2());
                    isChange = true;
                }
                if (siteContact.getDistrict() != null && !siteContact.getDistrict().equals(originalSiteContact.getDistrict())) {
                    originalSiteContact.setDistrict(siteContact.getDistrict());
                    isChange = true;
                }
                if (siteContact.getZipCode() != null && !siteContact.getZipCode().equals(originalSiteContact.getZipCode())) {
                    originalSiteContact.setZipCode(siteContact.getZipCode());
                    isChange = true;
                }
                if (siteContact.getState() != null && !siteContact.getState().equals(originalSiteContact.getState())) {
                    originalSiteContact.setState(siteContact.getState());
                    isChange = true;
                }
                if (siteContact.getCity() != null && !siteContact.getCity().equals(originalSiteContact.getCity())) {
                    originalSiteContact.setCity(siteContact.getCity());
                    isChange = true;
                }
                if (siteContact.getEmail() != null && !siteContact.getEmail().equals(originalSiteContact.getEmail())) {
                    originalSiteContact.setEmail(siteContact.getEmail());
                    isChange = true;
                }
                if (siteContact.getPhone() != null && !siteContact.getPhone().equals(originalSiteContact.getPhone())) {
                    originalSiteContact.setPhone(siteContact.getPhone());
                    isChange = true;
                }
                if (siteContact.getFax() != null && !siteContact.getFax().equals(originalSiteContact.getFax())) {
                    originalSiteContact.setFax(siteContact.getFax());
                    isChange = true;
                }

                if (isChange) {
                    //update menu
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateContactUsCacheKey(null));
                    originalSiteContact.setSite(site);
                    serviceLocator.getSiteContactDao().merge(originalSiteContact);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                } else {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                }
            }
        }
        return messages.toString();
    }

    @RequestMapping(value = "deletesitecontact.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String  deleteSiteContact(@Valid Long id) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (id > 0) {
            List<SiteContact> siteContacts =  serviceLocator.getSiteContactDao().findBy("id", id, site.getId());
            if (siteContacts != null) {
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateContactUsCacheKey(null));
                for (SiteContact siteContact: siteContacts) {
                    serviceLocator.getSiteContactDao().remove(siteContact);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
            }
        } else {
            messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
        }

        return messages.toString();
    }

    @RequestMapping(value = "savesitesupport.html", method = {RequestMethod.GET})
    public String saveSiteSupportGet() throws Exception {
        return "redirect:/admin#admin/sites/support_index";
    }
    @RequestMapping(value = "savesitesupport.html", method = {RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String saveSiteSupport(@Valid SiteSupport siteSupport) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Messages messages = new Messages();
        if (siteSupport != null) {
            if (siteSupport.isEmptyId()) {
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateContactUsCacheKey(null));
                float maxSequence = getServiceLocator().getSiteSupportDao().getMaxSequence(site.getId());
                siteSupport.setSequence(maxSequence + 1f);
                siteSupport.setSite(site);
                getServiceLocator().getSiteSupportDao().persist(siteSupport);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            } else {
                boolean isChange = false;
                //Update Site Support
                SiteSupport originalSiteSupport = getServiceLocator().getSiteSupportDao().findById(siteSupport.getId());

                if (siteSupport.getName() != null && !siteSupport.getName().equals(originalSiteSupport.getName())) {
                    originalSiteSupport.setName(siteSupport.getName());
                    isChange = true;
                }
                if (siteSupport.getChatId() != null && !siteSupport.getChatId().equals(originalSiteSupport.getChatId())) {
                    originalSiteSupport.setChatId(siteSupport.getChatId());
                    isChange = true;
                }
                if (siteSupport.getChatType() != null && !siteSupport.getChatType().equals(originalSiteSupport.getChatType())) {
                    originalSiteSupport.setChatType(siteSupport.getChatType());
                    isChange = true;
                }
                if (siteSupport.getPhone() != null && !siteSupport.getPhone().equals(originalSiteSupport.getPhone())) {
                    originalSiteSupport.setPhone(siteSupport.getPhone());
                    isChange = true;
                }
                if (siteSupport.getTimeAvailable() != null && !siteSupport.getTimeAvailable().equals(originalSiteSupport.getTimeAvailable())) {
                    originalSiteSupport.setTimeAvailable(siteSupport.getTimeAvailable());
                    isChange = true;
                }

                if (isChange) {
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                    serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateContactUsCacheKey(null));
                    //update menu
                    originalSiteSupport.setSite(site);
                    serviceLocator.getSiteSupportDao().merge(originalSiteSupport);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                } else {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                }
            }
        }
        return messages.toString();
    }

    @RequestMapping(value = "saveadminuser.html", method = {RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String addAdminUser(@Valid String email) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Messages messages = new Messages();
        if (!StringUtils.isEmpty(email)) {
            if (EmailValidator.getInstance().isValid(email)) {
                User user = ServiceLocatorHolder.getServiceLocator().getUserDao().findUniqueBy("username", email);
                if (user != null) {
                    //assign this user to this site
                    UserRole userRole = new UserRole();
                    userRole.setRole("ROLE_SITE_CONTENT_ADMIN");
                    userRole.setSite(site);
                    userRole.setUser(user);
                    ServiceLocatorHolder.getServiceLocator().getUserRoleDao().persist(userRole);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("site", site);
                    map.put("email", email);

                    EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().getEmailTemplate("addadminuser", serviceLocator.getLocale().toString());
                    serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), user.getUsername(), null, null, emailTemplate.getSubject(), map, emailTemplate.getTemplateContent());
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("addedexistedadminuser.successfully.please.check.email", null, LocaleContextHolder.getLocale()));

                } else {
                    //create a new user, sending email and assign to this site.
                    user = new User();
                    user.setEmail(email);
                    user.setUsername(email);
                    String randomPassword = WebUtil.generateRandomPassword(10);
                    user.setPassword(serviceLocator.getUserService().encrypt(randomPassword));
                    user.setBlocked("N");
                    user.setSiteAdmin("N");
                    user.setSiteUser("Y");

                    //create new user
                    serviceLocator.getUserService().createOrUpdate(user);
                    //send email

                    //assign role to this user
                    UserRole userRole = new UserRole();
                    userRole.setRole("ROLE_SITE_CONTENT_ADMIN");
                    userRole.setSite(site);
                    userRole.setUser(user);
                    ServiceLocatorHolder.getServiceLocator().getUserRoleDao().persist(userRole);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("site", site);
                    map.put("existed", "Y");
                    map.put("email", email);
                    map.put("password", randomPassword);

                    EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().getEmailTemplate("addadminuser", serviceLocator.getLocale().toString());
                    serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), user.getUsername(), null, null, emailTemplate.getSubject(), map, emailTemplate.getTemplateContent());
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("addedadminuser.successfully.please.check.email", null, LocaleContextHolder.getLocale()));

                }
            } else {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.register.emailisinvalid", null, LocaleContextHolder.getLocale()));
            }
        } else {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.register.emailisinvalid", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }

    @RequestMapping(value = "deletesiteadminuser.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String deleteAdminUserAccount(@Valid Long id) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        List adminUsers = ServiceLocatorHolder.getServiceLocator().getUserDao().getAdminUsers(site.getId());
        if (id > 0 && adminUsers != null && adminUsers.size() > 1) {
            List<UserRole> userRoles = serviceLocator.getUserRoleDao().findUserRoles(id, site);
            if (userRoles != null) {
                for (UserRole userRole: userRoles) {
                    serviceLocator.getUserRoleDao().remove(userRole);
                }
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
            }
        } else {
            messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
        }

        return messages.toString();
    }

    @RequestMapping(value = "deletesitesupport.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String deleteSiteSupport(@Valid Long id) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (id > 0) {
            List<SiteSupport> siteSupports = serviceLocator.getSiteSupportDao().findBy("id", id, site.getId());
            if (siteSupports != null) {
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateContactUsCacheKey(null));
                for (SiteSupport siteSupport: siteSupports) {
                    serviceLocator.getSiteSupportDao().remove(siteSupport);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
            }
        } else {
            messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
        }

        return messages.toString();
    }

    @RequestMapping(value = "setting/savesitequestionanswer.html", method = {RequestMethod.GET})
    public String saveSiteQuestionAnswerGet() throws Exception {
        return "admin/sites/setting/question_answer_index";
    }
    @RequestMapping(value = "setting/savesitequestionanswer.html", method = {RequestMethod.POST, RequestMethod.GET})
    @CSRFProtection
    public ModelAndView saveSiteQuestionAnswer(@Valid SiteQuestionAnswer siteQuestionAnswer) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Messages messages = new Messages();
        if (siteQuestionAnswer != null) {
            if (siteQuestionAnswer.isEmptyId()) {
                float maxSequence = getServiceLocator().getSiteQuestionAnswerDao().getMaxSequence(site.getId());
                siteQuestionAnswer.setSequence(maxSequence + 1f);
                siteQuestionAnswer.setSite(site);
                getServiceLocator().getSiteQuestionAnswerDao().persist(siteQuestionAnswer);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            } else {
                boolean isChange = false;
                //Update Site Question & Answer
                SiteQuestionAnswer originalSiteQuestionAnswer = getServiceLocator().getSiteQuestionAnswerDao().findById(siteQuestionAnswer.getId());

                if (siteQuestionAnswer.getQuestion() != null && !siteQuestionAnswer.getQuestion().equals(originalSiteQuestionAnswer.getQuestion())) {
                    originalSiteQuestionAnswer.setQuestion(siteQuestionAnswer.getQuestion());
                    isChange = true;
                }
                if (siteQuestionAnswer.getAnswer() != null && !siteQuestionAnswer.getAnswer().equals(originalSiteQuestionAnswer.getAnswer())) {
                    originalSiteQuestionAnswer.setAnswer(siteQuestionAnswer.getAnswer());
                    isChange = true;
                }
                if (siteQuestionAnswer.getActive() != null && !siteQuestionAnswer.getActive().equals(originalSiteQuestionAnswer.getActive())) {
                    if ("Y".equals(siteQuestionAnswer.getActive())) {
                        originalSiteQuestionAnswer.setActive(siteQuestionAnswer.getActive());
                        isChange = true;
                    } else {
                        originalSiteQuestionAnswer.setActive(siteQuestionAnswer.getActive());
                        isChange = true;
                    }
                }

                if (isChange) {
                    //update question
                    originalSiteQuestionAnswer.setSite(site);
                    serviceLocator.getSiteQuestionAnswerDao().merge(originalSiteQuestionAnswer);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                } else {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                }
            }
        }
        return new ModelAndView("admin/sites/setting/question_answer_index", "messages", messages);
    }

    @RequestMapping(value = "setting/deletesitequestionanswer.html", method = {RequestMethod.POST, RequestMethod.GET})
    @CSRFProtection
    public ModelAndView deleteSiteQuestionAnswer(@Valid Long id) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (id > 0) {
            List<SiteQuestionAnswer> siteQuestionAnswers = serviceLocator.getSiteQuestionAnswerDao().findBy("id", id, site.getId());
            if (siteQuestionAnswers != null) {
                for (SiteQuestionAnswer siteQuestionAnswer: siteQuestionAnswers) {
                    serviceLocator.getSiteQuestionAnswerDao().remove(siteQuestionAnswer);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
            }
        } else {
            messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
        }

        return new ModelAndView("admin/sites/setting/question_answer_index", "messages", messages);
    }
    @RequestMapping(value = "{id}/activateusefullink.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String activateUsefulLink(@PathVariable Long id, @Valid String flag) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (id != null && id > 0) {
            Menu menu = serviceLocator.getMenuDao().findById(id, site.getId());
            if (menu != null) {
                if (!StringUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")) {
                    menu.setUsefulLink("Y");
                } else {
                    menu.setUsefulLink("N");
                }
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                this.serviceLocator.getMenuDao().merge(menu);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }
    @RequestMapping(value = "{id}/activatenewslink.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String activateNewsLink(@PathVariable Long id, @Valid String flag) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (id != null && id > 0) {
            News news = serviceLocator.getNewsDao().findById(id, site.getId());
            if (news != null) {
                if (!StringUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")) {
                    news.setFooterLink("Y");
                } else {
                    news.setFooterLink("N");
                }
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                this.serviceLocator.getNewsDao().merge(news);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }
    @RequestMapping(value = "removeusefullink.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteUsefulLink(@Valid Long id) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();

        if (id != null && id > 0) {
            Menu menu = serviceLocator.getMenuDao().findById(id, site.getId());
            if (menu != null) {
                menu.setUsefulLink("N");
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                this.serviceLocator.getMenuDao().merge(menu);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
            }
        }
        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }
    @RequestMapping(value = "removenewslink.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteNewsLinkFromFooter(@Valid Long id) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();

        if (id != null && id > 0) {
            News footerLink = serviceLocator.getNewsDao().findById(id, site.getId());
            if (footerLink != null) {
                footerLink.setFooterLink("N");
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                this.serviceLocator.getNewsDao().merge(footerLink);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
            }
        }
        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }

    @RequestMapping(value = "{id}/showSupportInFooter.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String showSupportInFooter(@PathVariable Long id, @Valid String flag) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();

        if (id != null && id > 0) {
            SiteSupport siteSupport = serviceLocator.getSiteSupportDao().findById(id, site.getId());
            if (siteSupport != null) {
                if (!StringUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")) {
                    siteSupport.setShowFooter("Y");
                } else {
                    siteSupport.setShowFooter("N");
                }
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                this.serviceLocator.getSiteSupportDao().merge(siteSupport);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }
    @RequestMapping(value = "removeSupportInFooter.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteSupportInFooter(@Valid Long id) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();

        if (id != null && id > 0) {
            SiteSupport siteSupport = serviceLocator.getSiteSupportDao().findById(id, site.getId());
            if (siteSupport != null) {
                siteSupport.setShowFooter("N");
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                this.serviceLocator.getSiteSupportDao().merge(siteSupport);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
            }
        }
        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }
    @RequestMapping(value = "{id}/showContactInFooter.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String showContactInFooter(@PathVariable Long id, @Valid String flag) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();

        if (id != null && id > 0) {
            SiteContact siteContact = serviceLocator.getSiteContactDao().findById(id, site.getId());
            if (siteContact != null) {
                if (!StringUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")) {
                    siteContact.setShowFooter("Y");
                } else {
                    siteContact.setShowFooter("N");
                }
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                this.serviceLocator.getSiteContactDao().merge(siteContact);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
            }
        }
        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }
    @RequestMapping(value = "removeContactInFooter.html", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteContactInFooter(@Valid Long id) throws Exception {
        Messages messages = new Messages();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();

        if (id != null && id > 0) {
            SiteContact siteContact = serviceLocator.getSiteContactDao().findById(id, site.getId());
            if (siteContact != null) {
                siteContact.setShowFooter("N");
                serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateFooterCacheKey());
                this.serviceLocator.getSiteContactDao().merge(siteContact);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
            }
        }
        if (messages.isEmpty()) {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }

    @RequestMapping(value = "setting/reorderquestion.html", method = RequestMethod.GET)
    public ModelAndView reOrder(HttpServletRequest request, @Valid String currentItem,  @Valid String orderList) throws Exception {
        Messages messages = new Messages();

        if (!WebUtil.reOrderValidation(orderList, currentItem)) {
            messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
            return new ModelAndView("admin/sites/setting/question_answer_index", "messages", messages);
        }

        /*Group parent*/
        List<String> parents = new ArrayList<String>();
        List<String> subs = new ArrayList<String>();
        String []array = orderList.split(",");
        Collections.addAll(parents, array);

        /*Reorder for parent*/
        Float seq = 0f;
            seq = WebUtil.getSequence(parents, currentItem);
            if (seq > 0) {
                String [] currItem = currentItem.split("-");
                SiteQuestionAnswer siteQuestionAnswer = serviceLocator.getSiteQuestionAnswerDao().findById(Long.valueOf(currItem[0]));
                siteQuestionAnswer.setSequence(seq);
                serviceLocator.getSiteQuestionAnswerDao().merge(siteQuestionAnswer);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.order.change.successfully", null, LocaleContextHolder.getLocale()));
            }

        return new ModelAndView("admin/sites/setting/question_answer_index", "messages", messages);
    }

    public SiteService getService() {
        return service;
    }

    public SiteDao getDao() {
        return dao;
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    public SiteParamDao getSiteParamDao() {
        return siteParamDao;
    }

    /**
     * This will be called when delete a logo image
     */
    @RequestMapping(value = "deletelogoimage.html", headers="Accept=*/*", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    @CSRFProtection
    public @ResponseBody Map deleteImageNews(@RequestParam(value = "id", required = false) Long id,
                                             @RequestParam(value = "logoImg", required = true) String logoImg
    ) {
        Map <String, Object> result = new HashMap<String, Object>();
        if (id != null && id > 0) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            SiteHeaderFooter siteHeaderFooter = serviceLocator.getSiteHeaderFooterDao().findById(id, site.getId());
            siteHeaderFooter.setLogoImg("");
            serviceLocator.getSiteHeaderFooterDao().merge(siteHeaderFooter);
        }
        String imageUri = "";
        //Parse thumbImg to get image-uri
        if (StringUtils.isNotEmpty(logoImg)) {
            String temp[] = logoImg.split("[/.]");
            if (temp.length > 2) {
                imageUri = temp[temp.length-2];
            }
        }
        result.put("image_uri", imageUri);
        try {
            result.put("key", URLEncoder.encode(WebUtil.encrypt(imageUri), "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
        }
        return result;
    }

}
