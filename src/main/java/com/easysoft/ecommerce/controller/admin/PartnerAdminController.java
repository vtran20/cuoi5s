package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.MenuDao;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value="/admin/partner")
public class PartnerAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerAdminController.class);

    @Autowired
    private ServiceLocator serviceLocator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get("DATE_FORMAT"));
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }


    /***************************************************************************************************
     *  Implement Partner Management
     ***************************************************************************************************/
    /**
     * View contents table
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/partner/" + action;
    }

    /**
     * This method is called when insert a new Product
     */
    @RequestMapping(value = {"search_site_client.html"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody Map searchProduct(HttpServletRequest request) throws Exception {
        Map result = new HashMap();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Map input = new HashMap();
        input.put("keyword", request.getParameter("keyword"));
        List sites = serviceLocator.getSiteDao().searchClientSite(input, site);
        result.put("draw", 1);
        result.put("recordsTotal", sites.size());
        result.put("recordsFiltered", sites.size());
        result.put("data", sites);
        return result;
    }
    /**
     * This method is called when insert a new Product
     */
    @RequestMapping(value = {"search_site_template.html"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody Map searchTemplate(HttpServletRequest request) throws Exception {
        Map result = new HashMap();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Map input = new HashMap();
        input.put("keyword", request.getParameter("keyword"));
        List sites = serviceLocator.getSiteDao().searchTemplateSite(input, site);
        result.put("draw", 1);
        result.put("recordsTotal", sites.size());
        result.put("recordsFiltered", sites.size());
        result.put("data", sites);
        return result;
    }

    /**
     * This method is called when insert a page
     */
    @RequestMapping(value = {"savesitetemplate.html"} , method = RequestMethod.POST)
    @CSRFProtection
    public @ResponseBody Map insertTemplate(HttpServletRequest request, @Valid Template entity, @Valid Long id) throws Exception {
        Map result = new HashMap();
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String subDomain = request.getParameter("subDomain");
        boolean isUpdate = false;
        Messages messages = new Messages();
        if (StringUtils.isNotEmpty(subDomain)) {
            Site siteSample = ServiceLocatorHolder.getServiceLocator().getSiteDao().getSiteByServerName(subDomain);
            if (siteSample != null) {
                if (entity.isEmptyId()) {
                    Template thisTemplate = ServiceLocatorHolder.getServiceLocator().getTemplateDao().findUniqueBy("siteSample.id", siteSample.getId(), site.getId());
                    if (thisTemplate != null) {
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.template.existed", null, LocaleContextHolder.getLocale()));
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        //Create new template
                        entity.setCreatedDate(calendar.getTime());
                        entity.setUpdatedDate(calendar.getTime());
                        Float maxSequence = ServiceLocatorHolder.getServiceLocator().getTemplateDao().getMaxSequenceBy("site.id", site.getId());
                        entity.setSequence(maxSequence+1);
                        entity.setSiteSample(siteSample);
                        entity.setSite(site);
                        entity.setActive("Y");
                        entity.setTemplateCode("m3x");
                        entity.setTemplateCssCode("m3x");
                        ServiceLocatorHolder.getServiceLocator().getTemplateDao().persist(entity);
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.create.successful", null, LocaleContextHolder.getLocale()));
                        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKey(site, "sitetemplates3"));
                        serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKey(site, "sitetemplates4"));
                    }
                } else {
                    Template thisTemplate = ServiceLocatorHolder.getServiceLocator().getTemplateDao().findUniqueBy("siteSample.id", siteSample.getId(), site.getId());
                    Template originalTemplate = ServiceLocatorHolder.getServiceLocator().getTemplateDao().findById(entity.getId());
                    if (thisTemplate != null && !thisTemplate.getId().equals(originalTemplate.getId())) {
                        messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.template.existed", null, LocaleContextHolder.getLocale()));
                    } else {
                        //Start updating Template
                        if (entity.getName() != null && !entity.getName().equals(originalTemplate.getName())) {
                            originalTemplate.setName(entity.getName());
                            isUpdate = true;
                        }
                        if (entity.getImageUrl() != null && !entity.getImageUrl().equals(originalTemplate.getImageUrl())) {
                            originalTemplate.setImageUrl(entity.getImageUrl());
                            isUpdate = true;
                        }
                        if (entity.getCrop() != null && !entity.getCrop().equals(originalTemplate.getCrop())) {
                            originalTemplate.setCrop(entity.getCrop());
                            isUpdate = true;
                        }
                        if (entity.getTemplateModel() != null && !entity.getTemplateModel().equals(originalTemplate.getTemplateModel())) {
                            originalTemplate.setTemplateModel(entity.getTemplateModel());
                            isUpdate = true;
                        }
                        if (originalTemplate.getSiteSample() != null && !subDomain.equalsIgnoreCase(originalTemplate.getSiteSample().getSubDomain())) {
                            originalTemplate.setSiteSample(siteSample);
                            isUpdate = true;
                        }
                        if (!isUpdate) {
                            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                        } else {
                            //update menu
                            originalTemplate.setUpdatedDate(new Date());
                            ServiceLocatorHolder.getServiceLocator().getTemplateDao().merge(originalTemplate);
                            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKey(site, "sitetemplates3"));
                            serviceLocator.getCacheData().removeCacheTag(ServiceLocatorHolder.getServiceLocator().getCacheKeyGenerator().generateCacheKey(site, "sitetemplates4"));
                        }
                    }
                }
            } else {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.template.does.not.existed", null, LocaleContextHolder.getLocale()));
            }
        }
        result.put("id", entity.getId());
        result.put("messages", messages.toString());
        return result;
    }

    /**
     * This method is called when insert a new Product
     */
    @RequestMapping(value = {"deletesitetemplate.html"}, method = RequestMethod.GET, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody String deleteTemplate(@Valid Long id) throws Exception {
        Template template = ServiceLocatorHolder.getServiceLocator().getTemplateDao().findById(id);
        Messages messages = new Messages();
        if (template != null) {
            ServiceLocatorHolder.getServiceLocator().getTemplateDao().remove(template);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
        } else {
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }
}