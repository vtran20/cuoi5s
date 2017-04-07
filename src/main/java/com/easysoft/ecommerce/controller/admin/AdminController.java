package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.security.UserDetailsImpl;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public AdminController() {
    }

    @RequestMapping("login.html")
    public String login() {
        return "admin/login";
    }

    @RequestMapping("denied.html")
    public String denied() {
        return "admin/login";
    }

    /**
     * We login use webphattai.com then redirect to the website that user is working on.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("index.html")
    public ModelAndView index(HttpServletRequest request) throws Exception {
//        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Site siteLogin = principal.getSite();
//        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//        if (site.getDomain() != null && site.getDomain().equals(siteLogin.getDomain())) {
            return new ModelAndView("admin/index");
//        } else {
//            String domain = siteLogin.getDomain();
//            if (StringUtils.isEmpty(domain)) {
//                domain = siteLogin.getSubDomain();
//            }
//            return new ModelAndView("admin/r", "domain", domain);
//        }

    }
    @RequestMapping("{page}.html")
    public ModelAndView page(@PathVariable String page) throws Exception {
//        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Site siteLogin = principal.getSite();
//        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//        if (site.getDomain() != null && site.getDomain().equals(siteLogin.getDomain())) {
            return new ModelAndView("admin/"+page);
//        } else {
//            String domain = siteLogin.getDomain();
//            if (StringUtils.isEmpty(domain)) {
//                domain = siteLogin.getSubDomain();
//            }
//            return new ModelAndView("admin/r", "domain", domain);
//        }

    }

}
