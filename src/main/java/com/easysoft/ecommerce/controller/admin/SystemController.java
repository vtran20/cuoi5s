package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.model.EmailSite;
import com.easysoft.ecommerce.model.EmailTemplate;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.impl.URLUTF8Encoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.easysoft.ecommerce.service.SystemService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/admin/system")
public class SystemController {

    private SystemService systemService;
    @Autowired
    private ServiceLocator serviceLocator;

    @Autowired
    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @RequestMapping (value="index.html", method=RequestMethod.GET)
    public ModelAndView index() throws Exception {
        return new ModelAndView("admin/system/index");
    }

    @RequestMapping (value="update-seo-uris.html", method=RequestMethod.GET)
    public ModelAndView updateSEOURIs() throws Exception {
        //TODO: test this method again.
        systemService.updateSEOURIs();
        return new ModelAndView("admin/system/index");
    }
//    <a href="/admin/system/.html?partnerLevel=1&userName=${user.userName}">Approve</a></br>
//    <a href="/admin/system/deny.html?userName=${user.userName}">Deny</a></br>
//    <a href="#/admin/customer/index" data-url="/admin/customer/index">

    @RequestMapping (value="approve.html", method=RequestMethod.GET)
    public @ResponseBody
    String approvePartner(HttpServletRequest request) throws Exception {
        String message = "";
        String userName = request.getParameter("username");
        List<User> users = serviceLocator.getUserDao().findByUsername(userName);
        if (users != null) {
            if (users.size() > 1) {
                message = "Duplicate users. Please recheck: " + userName;
            } else if (users.size() == 0) {
                message = "Khong tim thay user";
            } else { //==1
                User user = users.get(0);
                String partnerLevel = request.getParameter("partnerLevel");
                int level = 1;
                if (StringUtils.isNumeric(partnerLevel)) {
                    level = Integer.parseInt(partnerLevel);
                }
                user.setPartnerLevel(level);
                user.setPartnerStatus("Y");
                user.setUpdatedDate(new Date());
                serviceLocator.getUserDao().merge(user);
                //Sending Email to Customer
                Site site = serviceLocator.getSystemContext().getSite();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("site", site);
                map.put("user", user);
                map.put("siteParam", site.getSiteParamsMap());
                EmailTemplate approveNotify = serviceLocator.getEmailTemplateDao().getEmailTemplate("approvedPartner", serviceLocator.getLocale().toString());
                serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), userName, null, null, approveNotify.getSubject(), map, approveNotify.getTemplateContent());
                message = "User was approved";
            }
        } else {
            message = "Khong tim thay user";
        }
        return message;
    }

    @RequestMapping (value="deny.html", method=RequestMethod.GET)
    public @ResponseBody
    String denyPartner(HttpServletRequest request) throws Exception {
        String message = "";
        String userName = request.getParameter("username");
        List<User> users = serviceLocator.getUserDao().findByUsername(userName);
        if (users != null) {
            if (users.size() > 1) {
                message = "Duplicate users. Please recheck: " + userName;
            } else if (users.size() == 0) {
                message = "Khong tim thay user";
            } else { //==1
                User user = users.get(0);
                user.setPartnerLevel(0);
                user.setPartnerStatus("N");
                user.setUpdatedDate(new Date());
                serviceLocator.getUserDao().merge(user);

                //Sending Email to Customer
                Site site = serviceLocator.getSystemContext().getSite();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("site", site);
                map.put("user", user);
                map.put("siteParam", site.getSiteParamsMap());
                EmailTemplate approveNotify = serviceLocator.getEmailTemplateDao().getEmailTemplate("deniedPartner", serviceLocator.getLocale().toString());
                serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), userName, null, null, approveNotify.getSubject(), map, approveNotify.getTemplateContent());
                message = "User was denied";

            }
        } else {
            message = "Khong tim thay user";
        }
        return message;
    }

}
