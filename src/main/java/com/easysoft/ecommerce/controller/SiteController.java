package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.SiteService;
import com.easysoft.ecommerce.service.component.RunProcessComponent;
import com.easysoft.ecommerce.service.impl.URLUTF8Encoder;
import com.easysoft.ecommerce.service.payment.Payment;
import com.easysoft.ecommerce.util.*;
import com.fasterxml.uuid.Generators;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.expressme.openid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Note: User login successfully when username and userId is put in sessionObject
 * TODO: Validate server side: http://stackoverflow.com/questions/12146298/spring-mvc-how-to-perform-validation
 */

@Controller
@RequestMapping("/site")
public class SiteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteController.class);
    public static final Integer MODULE_RELATION_TYPE = 1;
    private ServiceLocator serviceLocator;
    @Autowired
    private SiteService service;


    @Autowired
    public SiteController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "site/" + action;
    }
    @RequestMapping(value = "partner/{action}.html", method = RequestMethod.GET)
    public String actionPartner(@PathVariable String action) throws Exception {
        return "site/partner/" + action;
    }

    @RequestMapping(value = "checkwebsite.html", method = RequestMethod.GET)
    public void checkwebsite(@Valid String siteCode, HttpServletResponse response) throws Exception {
        if (StringUtils.isNotBlank(siteCode)) {
            Site site = serviceLocator.getSiteDao().findUniqueBy("siteCode", siteCode);
            if (site != null) {
                response.getWriter().write("false");
            } else {
                response.getWriter().write("true");
            }
        }
    }

    @RequestMapping(value = "checkemailused.html", method = RequestMethod.GET)
    public void checkEmailUsed(@Valid String email, HttpServletResponse response) throws Exception {
        if (StringUtils.isNotBlank(email)) {
            User user = serviceLocator.getUserDao().findUniqueBy("username", email);
            if (user != null) {
                response.getWriter().write("false");
            } else {
                response.getWriter().write("true");
            }
        }
    }

    @RequestMapping(value = {"dang-ky.html"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView registerPost(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (SessionUtil.isLoggedIn(request, response)) {
            return new ModelAndView("/site/main");
        } else if ("GET".equalsIgnoreCase(request.getMethod())) {
            return new ModelAndView("/site/login");
        } else {
            Site site = serviceLocator.getSystemContext().getSite();

            Map result = new HashMap();
            String userName = request.getParameter("email");
            String password = request.getParameter("password");
            String repassword = request.getParameter("repassword");

            String recaptchaResponse = request.getParameter("g-recaptcha-response");
            String secretParameter = serviceLocator.getSystemContext().getGlobalConfig("CAPTCHA_GOOGLE_SECRET_KEY");
            Messages messages = new Messages();

            if (WebUtil.isValidCaptcha(recaptchaResponse, secretParameter, request.getRemoteAddr())) {

                if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(repassword)) {
                    //check password and repassword is the same
                    if (!password.equals(repassword)) {
                        messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.register.password.and.repassword.different", null, LocaleContextHolder.getLocale()));
                        result.put("entity", request.getParameterMap());
                        //check account existed
                    } else {
                        List<User> users = serviceLocator.getUserDao().findClientUserByUsername(userName, site);
                        if (users != null && !users.isEmpty()) {
                            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.register.emailinused", null, LocaleContextHolder.getLocale()));
                            result.put("entity", request.getParameterMap());
                        } else {
                            //OK, new account is valid
                            User user = new User();
                            user.setEmail(userName);
//                        user.setFirstName(firstName);
//                        user.setLastName(lastName);
//                        user.setPhone(phone);
                            user.setUsername(userName);
                            user.setPassword(serviceLocator.getUserService().encrypt(password));
                            user.setBlocked("Y");
                            user.setSiteAdmin("N");
                            user.setSiteUser("Y");

                            //create new user
                            serviceLocator.getUserService().createOrUpdate(user);
                            //send email

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("site", site);
                            map.put("user", user);
                            map.put("password", password);
                            map.put("siteParam", site.getSiteParamsMap());
//                        map.put("siteCode", siteCode);
                            //String code using for activating account
                            String code = "userId=" + user.getId() + "&email=" + userName + "&startDate=" + Calendar.getInstance().getTimeInMillis();
                            map.put("code", URLUTF8Encoder.encode(serviceLocator.getStrongEncryptor().encrypt(code)));

                            EmailSite emailSite = serviceLocator.getEmailSiteDao().getEmailInfor(site.getId(), "registerconfirm");
                            if (emailSite != null) {
                                EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().findById(emailSite.getEmailTemplateId());
                                String subject = emailTemplate.getSubject().replaceAll("[BRAND_NAME]", site.getName());
                                String[] bcc = null;
                                if (!StringUtils.isEmpty(emailSite.getBcc())) {
                                    bcc = emailSite.getBcc().split(",");
                                }
                                serviceLocator.getMailService().sendEmail(emailSite.getSendFrom(), userName, null, bcc, subject, map, emailTemplate.getTemplateFile());
                            } else {
                                serviceLocator.getMailService().sendEmail("noname@mail.com", site.getSiteParamsMap().get("WEBMASTER_EMAIL"), null, null, "Email template 'forgotpassword' has not been setup for the site " + site.getDomain(), "Email template 'registerconfirm' has not been setup for the site " + site.getDomain());
                            }
                            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("register.successfully.please.check.email", null, LocaleContextHolder.getLocale()));
                        }
                    }
                } else {
                    messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("please.enter.username.password", null, LocaleContextHolder.getLocale()));
                    result.put("entity", request.getParameterMap());
                }
            } else {
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("login.forgot.captcha.error", null, LocaleContextHolder.getLocale()));
                result.put("entity", request.getParameterMap());
            }
            result.put("messages",messages);
            return new ModelAndView("/site/login", result);
        }

    }

    @RequestMapping(value = "update_account.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView updateUser(@Valid User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equalsIgnoreCase("GET")) {
            return new ModelAndView("/site/myprofile");
        } else {
            Messages messages = new Messages();
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            boolean isChange = false;
            if (user != null) {
                if (!user.isEmptyId()) { //had header footer record
                    User originalUser = SessionUtil.loadUser(request, response);
                    if (originalUser != null) {
                        if (user.getFirstName() != null && !user.getFirstName().equals(originalUser.getFirstName())) {
                            originalUser.setFirstName(user.getFirstName());
                            isChange = true;
                        }
                        if (user.getLastName() != null && !user.getLastName().equals(originalUser.getLastName())) {
                            originalUser.setLastName(user.getLastName());
                            isChange = true;
                        }
                        if (user.getAddress_1() != null && !user.getAddress_1().equals(originalUser.getAddress_1())) {
                            originalUser.setAddress_1(user.getAddress_1());
                            isChange = true;
                        }
                        if (user.getCity() != null && !user.getCity().equals(originalUser.getCity())) {
                            originalUser.setCity(user.getCity());
                            isChange = true;
                        }
                        if (user.getPhone() != null && !user.getPhone().equals(originalUser.getPhone())) {
                            originalUser.setPhone(user.getPhone());
                            isChange = true;
                        }
                        if (!isChange) {
                            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                        } else {
                            serviceLocator.getUserDao().merge(originalUser);
                            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.saved.success", null, LocaleContextHolder.getLocale()));
                        }
                    }
                } else { //don't have header footer record
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.no.data.changed", null, LocaleContextHolder.getLocale()));
                }
            }
            return new ModelAndView("/site/myprofile", "messages", messages);
        }

    }
    @RequestMapping(value = "kich-hoat.html", method = RequestMethod.GET)
    public ModelAndView activateAccount(@Valid String code) throws Exception {
        Map map = new HashMap();
        Messages messages = new Messages();
        if (StringUtils.isEmpty(code)) {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.activate.invalid", null, LocaleContextHolder.getLocale()));
        } else {
            String decryptCode = serviceLocator.getStrongEncryptor().decrypt(code);
            QueryString queryString = new QueryString(decryptCode);
            String userId = queryString.getParameter("userId");
            String email = queryString.getParameter("email");
            if (StringUtils.isEmpty(userId)) {
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.activate.invalid", null, LocaleContextHolder.getLocale()));
            } else {
                User user = serviceLocator.getUserDao().findById(Long.valueOf(userId));
                if (user.getUsername().equals(email)) {
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("your.account.is.activated", null, LocaleContextHolder.getLocale()));
                    user.setBlocked("N");
                    serviceLocator.getUserDao().merge(user);
                } else {
                    messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.activate.invalid.email.invalid", null, LocaleContextHolder.getLocale()));
                }
            }
            map.put("userId", userId);
            map.put("email", email);
        }
        map.put("messages", messages);
        return new ModelAndView("site/login", map);

    }

    /**
     * This method will be called when selecting a sub domain for creating new site.
     *
     * @param siteCode
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"select-subdomain.json"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    Map selectSubDomain(@Valid String siteCode, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map result = new HashMap();
        if (StringUtils.isNotBlank(siteCode)) {
            Site site = serviceLocator.getSiteDao().findUniqueBy("siteCode", siteCode);
            if (site != null) {
                result.put("status", false);
                result.put("status_message", ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.register.sitecodeinused", null, LocaleContextHolder.getLocale()));
            } else {
                result.put("status", true);
                result.put("status_message", "");
                //TODO: When should remove this site code in session object
                SessionUtil.load(request, response).set("siteCode", siteCode);
                if (SessionUtil.isLoggedIn(request, response)) {
                    result.put("forward", "/site/main.html");
                } else {
                    result.put("forward", "/site/dang-ky.html");
                }
            }
        } else {
            result.put("status", false);
            result.put("status_message", ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.register.sitecodeinused", null, LocaleContextHolder.getLocale()));
        }

        return result;
    }

    /**
     * This method will be called when selecting a template for creating new site . It also be called when update template.
     *
     * @param templateId
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"select-template.html","create-site.html"}, method = RequestMethod.GET)
    //="+templateColorCode+"&="+skinColor+"&="+fullWide
    public ModelAndView selectTemplate(@Valid Long templateId,
                                       @RequestParam(value = "templateColorCode", required = false) String templateColorCode,
                                       @RequestParam(value = "skinColor", required = false) String skinColor,
                                       @RequestParam(value = "fullWide", required = false) String fullWide,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
//        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
//        Messages messages = service.createSite(templateId, site);
        if (templateId != null && templateId > 0) {
            SessionObject so = SessionUtil.load(request, response);
            so.set("templateId", templateId);
            so.set("templateColorCode", templateColorCode);
            so.set("skinColor", skinColor);
            so.set("fullWide", fullWide);
            return new ModelAndView("site/createsite");
        } else {
            if (StringUtils.isNumeric(SessionUtil.load(request, response).get("templateId")+"")) {
                return new ModelAndView("site/createsite");
            } else {
                return new ModelAndView("site/main");
            }
        }
    }
    /**
     * This method will be called when selecting a template for creating new site . It also be called when update template.
     *
     * @param templateId
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "create-site.html", method = RequestMethod.POST)
    public ModelAndView createWebsite(@Valid Long templateId, @Valid String siteCode, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = null;
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String recaptchaResponse = request.getParameter("g-recaptcha-response");
        String secretParameter = serviceLocator.getSystemContext().getGlobalConfig("CAPTCHA_GOOGLE_SECRET_KEY");

        //Change siteCode to lower case
        if (siteCode != null) {
            siteCode = siteCode.toLowerCase();
        }
        SessionObject so = SessionUtil.load(request, response);
        Map data = new HashMap();
        data.put("templateId", so.get("templateId"));
        data.put("templateColorCode", so.get("templateColorCode"));
        data.put("skinColor", so.get("skinColor"));
        data.put("fullWide", so.get("fullWide"));
        data.put("siteCode", siteCode);
        so.set("siteCode", siteCode);//update site code to Session Object
        if (WebUtil.isValidCaptcha(recaptchaResponse, secretParameter, request.getRemoteAddr())) {
            if (so.getUserId() > 0) {
                data.put("userId", so.getUserId());
            } else {
                messages = new Messages();
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.create.site.error.1", null, LocaleContextHolder.getLocale()));
                return new ModelAndView("site/createsite", "templateId", templateId);
            }
            messages = service.createSite(data, site);
            if (messages.hasErrors()) {
                return new ModelAndView("site/createsite", "messages", messages);
            } else {
                //Remove template, site code and other in SessionObject after created site.
                so.remove("templateId");
                so.remove("templateColorCode");
                so.remove("skinColor");
                so.remove("fullWide");
                so.remove("siteCode");
                return new ModelAndView("site/mysites", "messages", messages);
            }
        } else {
            messages = new Messages();
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.create.site.protect.code.incorrect", null, LocaleContextHolder.getLocale()));
            return new ModelAndView("site/createsite", "messages", messages);
        }

    }
    /**
     * This method will be called when selecting a template for creating new site . It also be called when update template.
     *
     * @param templateId
     * @param request
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "partner/create-partner-website.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView createPartnerWebsite(@Valid Long templateId, @Valid String siteCode, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = null;
        if (request.getMethod().equalsIgnoreCase("GET")) {
            return new ModelAndView("/site/partner/partner");
        }
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String recaptchaResponse = request.getParameter("g-recaptcha-response");
        String secretParameter = serviceLocator.getSystemContext().getGlobalConfig("CAPTCHA_GOOGLE_SECRET_KEY");

        //Change siteCode to lower case
        if (siteCode != null) {
            siteCode = siteCode.toLowerCase();
        }
        SessionObject so = SessionUtil.load(request, response);
        Map data = new HashMap();
        data.put("templateId", templateId);
        data.put("siteCode", siteCode);
        so.set("siteCode", siteCode);//update site code to Session Object
        if (WebUtil.isValidCaptcha(recaptchaResponse, secretParameter, request.getRemoteAddr())) {
            if (so.getUserId() > 0) {
                data.put("userId", so.getUserId());
            } else {
                messages = new Messages();
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.create.site.error.1", null, LocaleContextHolder.getLocale()));
                return new ModelAndView("site/partner/partner", "templateId", templateId);
            }
            User user = serviceLocator.getUserDao().findById(so.getUserId());
            if (user != null && "Y".equals(user.getPartnerStatus()) && user.getPartner() == 2) {
                messages = service.createPartnerSite(data, site);
            }
            if (messages != null && messages.hasErrors()) {
                return new ModelAndView("site/partner/partner", "messages", messages);
            } else {
                //Remove template, site code and other in SessionObject after created site.
                so.remove("templateId");
                so.remove("templateColorCode");
                so.remove("skinColor");
                so.remove("fullWide");
                so.remove("siteCode");
                return new ModelAndView("site/partner/partner", "messages", messages);
            }
        } else {
            messages = new Messages();
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.create.site.protect.code.incorrect", null, LocaleContextHolder.getLocale()));
            return new ModelAndView("site/partner/partner", "messages", messages);
        }

    }

    /**
     * This method will be called when selecting a template for creating new site . It also be called when update template.
     *
     * @param sid
     * @param code
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "delete-website-permission.html", method = RequestMethod.GET)
    public ModelAndView deleteWebsite(@Valid Long sid, @Valid String code, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages;
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        Map data = new HashMap();
        data.put("sid", sid);
        data.put("code", code);
        SessionObject so = SessionUtil.load(request, response);
        if (so != null && so.getUserId() > 0) {
            data.put("userId", so.getUserId());
        } else {
            messages = new Messages();
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("please.login.before.delete.site", null, LocaleContextHolder.getLocale()));
            return new ModelAndView("site/createsite");
        }
        messages = service.deleteSite(data, site);
        return new ModelAndView("site/mysites", "messages", messages);
    }

    @RequestMapping(value = {"login.html", "dang-nhap.html"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView loginAction(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (SessionUtil.isLoggedIn(request, response)) {
            return new ModelAndView("/site/main");
        } else {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            String rememberMe = request.getParameter("rememberMe");

            Messages error = new Messages();
            if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
                List<User> users = serviceLocator.getUserDao().findByUsername(userName);
                String source = request.getParameter("source");
                if (users != null && users.size() > 0) {
                    User user = users.get(0);
                    if (!"Y".equals(user.getBlocked())) {
                        if (StringUtils.isNotEmpty(user.getUserSessionId())) {
                            request.getSession().setAttribute(SessionObject.USER_SESSION_ID_KEY, user.getUserSessionId());
                        }
                        SessionObject so = SessionUtil.load(request, response);
                        //Generate USER_SESSION_SECURE_COOKIE
                        String userSessionCookie = Generators.timeBasedGenerator().generate().toString();
                        if (serviceLocator.getUserService().isValidPassword(password, user.getPassword())) {
                            SessionUtil.setSessionObjectAttribute(so, user);
                            //Remember me - cookie will be remember 30 days
                            if ("Y".equals(WebUtil.convertActiveFlag(rememberMe))) {
                                SessionUtil.setUserSessionCookie(userSessionCookie, request, response, false);
                            } else {
                                SessionUtil.setUserSessionCookie(userSessionCookie, request, response, true);
                            }
                            SessionUtil.updateUserSessionId(user, request, response);
                            serviceLocator.getUserDao().merge(user);
                            //set USER_SESSION_SECURE_COOKIE into sessionObject and save into UserSession
                            so.setSecureCookie(userSessionCookie);
                            SessionUtil.save(so);
                            if (StringUtils.isEmpty(source)) {
                                return new ModelAndView("redirect:/site/main.html");
                            } else {
                                return new ModelAndView("redirect:"+source);
                            }
                        } else if (serviceLocator.getUserService().isValidPassword(password, user.getTempPassword())) {
                            user.setTempPassword("");
                            //set data to sessionObject
                            SessionUtil.setSessionObjectAttribute(so, user);
                            //set user session secure
                            SessionUtil.setUserSessionCookie(userSessionCookie, request, response, true);
                            SessionUtil.updateUserSessionId(user, request, response);
                            serviceLocator.getUserDao().merge(user);
                            //set USER_SESSION_SECURE_COOKIE into sessionObject and save into UserSession
                            so.setSecureCookie(userSessionCookie);
                            SessionUtil.save(so);
                            return new ModelAndView("/site/change_password");
                        } else {
                            error.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("error.username.password.incorrect", null, LocaleContextHolder.getLocale()));
                            return new ModelAndView("/site/login", "error", error);
                        }
                    } else {
                        error.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("error.account.blocked", null, LocaleContextHolder.getLocale()));
                        return new ModelAndView("/site/login", "error", error);
                    }
                } else {
                    error.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("login.forgot.password.error", null, LocaleContextHolder.getLocale()));
                    return new ModelAndView("/site/login", "error", error);
                }
            } else {
//                error.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("error.username.password.blank", null, LocaleContextHolder.getLocale()));
                return new ModelAndView("/site/login", "error", error);
            }
        }

    }

    @RequestMapping(value = {"change_password.html"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView changePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return new ModelAndView("/site/change_password");
        } else {
            User user = SessionUtil.loadUser(request, response);
            String password = request.getParameter("password");
            String repassword = request.getParameter("repassword");
            if (user != null && password != null && password.equals(repassword)) {
                user.setPassword(serviceLocator.getStrongEncryptor().encrypt(password));
                serviceLocator.getUserDao().merge(user);
                Messages messages = new Messages();
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("change.password.successfully", null, LocaleContextHolder.getLocale()));
                return new ModelAndView("/site/change_password", "messages", messages);
            }
            Messages error = new Messages();
            error.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("error.username.password.blank", null, LocaleContextHolder.getLocale()));
            return new ModelAndView("/site/login", "messages", error);
        }
    }

    @RequestMapping("main.html")
    public String billingShipping(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        if (StringUtils.isEmpty(so.getUserName())) {
            return "redirect:/site/login.html";
        } else {
            return "/site/main";
        }
    }

    @RequestMapping(value = "change_account.html", method = RequestMethod.GET)
    public String changeAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/site/main";
    }

    @RequestMapping(value = "change_account.html", method = RequestMethod.POST)
    public ModelAndView changeAccountPost(@Valid User entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = createOrUpdateUser(entity);
        return new ModelAndView("/site/main", "messages", messages);
    }

    @RequestMapping(value = {"logout.html", "dang-xuat.html"}, method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionObject so = SessionUtil.load(request, response);
        SessionUtil.removeUserInfo(so);
        request.getSession().invalidate();
        SessionUtil.save(so);
        SessionUtil.expireSecureSessionCookie(request, response);
        SessionUtil.expireUserSessionCookie(request, response);
        return "redirect:/";
    }

    @RequestMapping(value = "go-to-sitemanager.html", method = RequestMethod.GET)
    public ModelAndView gotoSiteManager(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //TODO: Do more secure here such as adding date/time so we only login during the day.
        Map map = new HashMap();
        String code = request.getParameter("code");
        if (StringUtils.isNotEmpty(code)) {
            String decryptCode = serviceLocator.getStrongEncryptor().decrypt(code);
            QueryString queryString = new QueryString(decryptCode);
            map.put("domain", queryString.getParameter("domain"));
            map.put("j_username", queryString.getParameter("j_username"));
            map.put("j_password", queryString.getParameter("j_password"));
        }
        return new ModelAndView("/site/autologin", map);
    }


    @RequestMapping("forgotpassword.html")
    public String forgotPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/site/forgot_password_popup";
    }

    @RequestMapping("reset_password.html")
    public ModelAndView sendTempPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = new Messages();
        String userName = request.getParameter("userName");
        Site site = serviceLocator.getSystemContext().getSite();
        String recaptchaResponse = request.getParameter("g-recaptcha-response");
        String secretParameter = serviceLocator.getSystemContext().getGlobalConfig("CAPTCHA_GOOGLE_SECRET_KEY");
        if (WebUtil.isValidCaptcha(recaptchaResponse, secretParameter, request.getRemoteAddr())) {
            /*Generate temporary password and save it*/
            List<User> users = serviceLocator.getUserDao().findByUsername(userName);
            if (users != null) {
                if (users.size() > 1) {
                    LOGGER.error("UserName="+userName+" is duplicated");
                }
                User user = users.get(0);
                String randomPassword = WebUtil.generateRandomPassword(10);
                user.setTempPassword(serviceLocator.getUserService().encrypt(randomPassword));
                serviceLocator.getUserDao().merge(user);

                /*send email to the customer*/
                Map<String, String> map = new HashMap<String, String>();
                map.put("firstName", user.getFirstName());
                map.put("lastName", user.getLastName());
                map.put("domain", site.getDomain());
                map.put("siteName", site.getName());
                map.put("phoneContact", site.getSiteParamsMap().get("PHONE_CONTACT"));
                map.put("tempPassword", randomPassword);
                EmailSite emailSite = serviceLocator.getEmailSiteDao().getEmailInfor(site.getId(), "forgotpassword");
                if (emailSite != null) {
                    EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().findById(emailSite.getEmailTemplateId());
                    String subject = emailTemplate.getSubject();
                    serviceLocator.getMailService().sendEmail(emailSite.getSendFrom(), userName, null, null, subject, map, emailTemplate.getTemplateFile());
                } else {
                    serviceLocator.getMailService().sendEmail("noname@mail.com", site.getSiteParamsMap().get("WEBMASTER_EMAIL"), null, null, "Email template 'forgotpassword' has not been setup for the site " + site.getDomain(), "Email template 'forgotpassword' has not been setup for the site " + site.getDomain());
                }
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("reset.password.successfully", null, LocaleContextHolder.getLocale()));
                return new ModelAndView("/site/forgot_password_popup", "messages", messages);
            } else {
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("login.forgot.password.error", null, LocaleContextHolder.getLocale()));
                return new ModelAndView("/site/forgot_password_popup", "messages", messages);
            }
        } else {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("login.forgot.captcha.error", null, LocaleContextHolder.getLocale()));
            return new ModelAndView("/site/forgot_password_popup", "messages", messages);
        }
    }


    static final long ONE_HOUR = 3600000L;
    static final long TWO_HOUR = ONE_HOUR * 2L;
    static final String ATTR_MAC = "openid_mac";
    static final String ATTR_ALIAS = "openid_alias";

    @RequestMapping(value = "openidlogin.html", method = RequestMethod.GET)
    public String loginUsingOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String op = request.getParameter("op");
        String source = request.getParameter("source");
        OpenIdManager manager = new OpenIdManager();
        manager.setRealm("http://" + serviceLocator.getSystemContext().getSite().getDomain());
        manager.setReturnTo("http://" + serviceLocator.getSystemContext().getSite().getDomain() + "/site/openidlogin.html");

        if (op == null) {
            SessionObject so = SessionUtil.load(request, response);

            // check sign on result from Google or Yahoo:
            checkNonce(request.getParameter("openid.response_nonce"));
            // get authentication:
            byte[] mac_key = (byte[]) request.getSession().getAttribute(ATTR_MAC);
            String alias = (String) request.getSession().getAttribute(ATTR_ALIAS);
            source = (String) request.getSession().getAttribute("source");
            Authentication authentication = manager.getAuthentication(request, mac_key, alias);

            /*
             * 1.  Check account is exist
             * 2.  If exist, return home page
             * 3.  If not, create account and return home page.
             */
            List<User> users = serviceLocator.getUserDao().findClientUserByUsername(authentication.getEmail(), serviceLocator.getSystemContext().getSite());
            if (users != null && !users.isEmpty()) {
                User user = users.get(0);
                so.setUserName(authentication.getEmail());
                so.setUserId(user.getId());
                so.getAddresses().getBillingAddress().setFirstName(authentication.getFirstname());
                so.getAddresses().getBillingAddress().setLastName(authentication.getLastname());
                if (StringUtils.isEmpty(source)) {
                    return "/site/main";
                } else {
                    return source;
                }
            } else {
                //OK, new account is valid
                so.setUserName(authentication.getEmail());
                so.getAddresses().getBillingAddress().setFirstName(authentication.getFirstname());
                so.getAddresses().getBillingAddress().setLastName(authentication.getLastname());
                User user = new User();
                user.setEmail(authentication.getEmail());
                user.setFirstName(authentication.getFirstname());
                user.setLastName(authentication.getLastname());
                user.setUsername(authentication.getEmail());
                user.setBlocked("N");
                user.setSiteAdmin("N");
                user.setSiteUser("Y");
                //TODO: user customer instead
                //user.setSite(serviceLocator.getSystemContext().getSite());

                //create new user
                serviceLocator.getUserService().createOrUpdate(user);
                so.setUserId(user.getId());
                if (StringUtils.isEmpty(source)) {
                    return "/site/main";
                } else {
                    return source;
                }
            }
        }
        if (op.equals("Google") || op.equals("Yahoo")) {
            // redirect to Google or Yahoo sign on page:
            Endpoint endpoint = manager.lookupEndpoint(op);
            Association association = manager.lookupAssociation(endpoint);
            request.getSession().setAttribute(ATTR_MAC, association.getRawMacKey());
            request.getSession().setAttribute(ATTR_ALIAS, endpoint.getAlias());
            request.getSession().setAttribute("source", source);
            String url = manager.getAuthenticationUrl(endpoint, association);
            response.sendRedirect(url);
        } else {
            throw new ServletException("Unsupported OP: " + op);
        }


        return "/site/login";
    }

    void showAuthentication(PrintWriter pw, Authentication auth) {
        pw.print("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Test JOpenID</title></head><body><h1>You have successfully signed on!</h1>");
        pw.print("<p>Identity: " + auth.getIdentity() + "</p>");
        pw.print("<p>Email: " + auth.getEmail() + "</p>");
        pw.print("<p>Full name: " + auth.getFullname() + "</p>");
        pw.print("<p>First name: " + auth.getFirstname() + "</p>");
        pw.print("<p>Last name: " + auth.getLastname() + "</p>");
        pw.print("<p>Gender: " + auth.getGender() + "</p>");
        pw.print("<p>Language: " + auth.getLanguage() + "</p>");
        pw.print("</body></html>");
        pw.flush();
    }

    /**
     * Sample of nonce:  2009-10-21T02:11:39Zrhco-EsNzi8FtQ
     *
     * @param nonce
     */
    void checkNonce(String nonce) {
        // check response_nonce to prevent replay-attack:
        if (nonce == null || nonce.length() < 20)
            throw new OpenIdException("Verify failed.");
        // make sure the time of server is correct:
        long nonceTime = getNonceTime(nonce);
        long diff = Math.abs(System.currentTimeMillis() - nonceTime);
        if (diff > ONE_HOUR)
            throw new OpenIdException("Bad nonce time.");
    }

    long getNonceTime(String nonce) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .parse(nonce.substring(0, 19) + "+0000")
                    .getTime();
        } catch (ParseException e) {
            throw new OpenIdException("Bad nonce time.");
        }
    }

    private Messages createOrUpdateUser(User entity) throws Exception {
        Messages messages = new Messages();

        if (entity.getId() == null) {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.not.exist", null, LocaleContextHolder.getLocale()));
        } else {
            //Update User
            User original = serviceLocator.getUserDao().findById(entity.getId());
            if (entity.getFirstName() != null && !entity.getFirstName().equals(original.getFirstName())) {
                original.setFirstName(entity.getFirstName());
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.firstname.changed", null, LocaleContextHolder.getLocale()));
            }
            if (entity.getLastName() != null && !entity.getLastName().equals(original.getLastName())) {
                original.setLastName(entity.getLastName());
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.lastname.changed", null, LocaleContextHolder.getLocale()));
            }
            if (entity.getAddress_1() != null && !entity.getAddress_1().equals(original.getAddress_1())) {
                original.setAddress_1(entity.getAddress_1());
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.address.changed", null, LocaleContextHolder.getLocale()));
            }
            if (entity.getDistrict() != null && !entity.getDistrict().equals(original.getDistrict())) {
                original.setDistrict(entity.getDistrict());
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.district.changed", null, LocaleContextHolder.getLocale()));
            }
            if (entity.getCity() != null && !entity.getCity().equals(original.getCity())) {
                original.setCity(entity.getCity());
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.city.changed", null, LocaleContextHolder.getLocale()));
            }
            if (entity.getPhone() != null && !entity.getPhone().equals(original.getPhone())) {
                original.setPhone(entity.getPhone());
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.phone.changed", null, LocaleContextHolder.getLocale()));
            }
            if (entity.getEmail() != null && !entity.getEmail().equals(original.getEmail())) {
                original.setEmail(entity.getEmail());
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.email.changed", null, LocaleContextHolder.getLocale()));
            }
            if (entity.getUsername() != null && !entity.getUsername().equals(original.getUsername())) {
                original.setUsername(entity.getUsername());
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.username.changed", null, LocaleContextHolder.getLocale()));
            }
            if (entity.getPassword() != null && !entity.getPassword().equals(serviceLocator.getUserService().decrypt(original.getPassword()))) {
                original.setPassword(serviceLocator.getUserService().encrypt(entity.getPassword()));
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.password.changed", null, LocaleContextHolder.getLocale()));
            }


            if (messages.isEmpty()) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("account.no.changed", null, LocaleContextHolder.getLocale()));
            } else {
                serviceLocator.getUserDao().merge(original);
            }
        }
        return messages;
    }

    //Remove order information out of session object
    @RequestMapping(value = "clear-session.html", method = RequestMethod.GET)
    public String deleteUserSession(@Valid String usid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.isNotEmpty(usid)) {
            SessionUtil.deleteSession(usid);
        }
        return "redirect:/";
    }

    /**
     * Checkout process of website.
     */
    @RequestMapping(value = "checkout/basket.html", method = RequestMethod.GET)
    public ModelAndView basket(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionObject so = SessionUtil.load(request, response);
        Messages messages = new Messages();
        RunProcessComponent process = new RunProcessComponent();
        process.runProcessComponent(so, "checkout-basket", messages);
        List <ItemMap>items = so.getOrder().getItems();
        for (Iterator<ItemMap> iterator = items.iterator(); iterator.hasNext();) {
            ItemMap item = iterator.next();
            if (item.getLong("SITE_ID") == null || item.getLong("SITE_ID") <= 0) {
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.product.is.invalid", new String[]{item.getName()}, null, LocaleContextHolder.getLocale()));
                iterator.remove();//remove invalid item
            }
        }
        return new ModelAndView("/site/checkout/basket", "messages", messages);
    }

    @RequestMapping(value = "checkout/addtocart.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView addItemToCart(
            @RequestParam(value = "thisSiteId", required = true) Long thisSiteId,
            @Valid AddToCartForm form,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {

//        Validator validator = new InventoryValidator();
//        validator.validate(form, result);
//        if (result.hasErrors()) {
//            Product product = this.serviceLocator.getProductDao().getProduct(form.getProductId());
//            ProductVariant productVariant = this.serviceLocator.getProductVariantDao().findById(form.getProductVariantId());
//            ModelAndView mav = new ModelAndView("/product/"+product.getUri()+"/index.html");
//            mav.getModel().putAll(result.getModel());
//            return mav;
//        }
        Messages messages = new Messages();
        Product product = this.serviceLocator.getProductDao().getProduct(form.getProductId());
//        ProductVariant productVariant = this.serviceLocator.getProductVariantDao().findById(form.getProductVariantId());
        SessionObject so = SessionUtil.load(request, response);
        List<ItemMap> items = so.getOrder().getItems();
        Site site = serviceLocator.getSystemContext().getSite();
        if (product != null) {
            //If product is existing in cart, don't need to add anymore
            if (getExistingItem(product, thisSiteId, items) != null) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.product.is.added.to.cart", null, LocaleContextHolder.getLocale()));
            } else {
                ItemMap item = new ItemMap();
                item.setId(product.getId());
                item.setName(product.getName());
                item.setModelNumber(product.getModel());
                ProductVariant productVariant = this.serviceLocator.getSiteProductServiceDao().getProductVariant(product.getId(), thisSiteId);
                if (productVariant != null) {
                    item.setProductVariantId(productVariant.getId());
                }
                item.setQuantity(1); //always quantity = 1
                item.set("SITE_ID", thisSiteId);
                so.getOrder().addItem(item);
                List<Product> relatedProducts = this.serviceLocator.getProductDao().getAddedModules(site.getId(), thisSiteId);
                for (Product relatedProduct: relatedProducts) {
                    item = new ItemMap();
                    item.setId(relatedProduct.getId());
                    item.setName(relatedProduct.getName());
                    item.setModelNumber(relatedProduct.getModel());
                    productVariant = this.serviceLocator.getSiteProductServiceDao().getProductVariant(relatedProduct.getId(), thisSiteId);
                    if (productVariant != null) {
                        item.setProductVariantId(productVariant.getId());
                    }
                    item.set("PARENT_PRODUCT_ID", product.getId());
                    item.set("RELATED_TYPE_ID", MODULE_RELATION_TYPE);

                    item.setQuantity(1); //always quantity = 1
                    item.set("SITE_ID", thisSiteId);
                    so.getOrder().addItem(item);
                }
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.product.is.added.to.cart", null, LocaleContextHolder.getLocale()));
            }
            SessionUtil.save(so);
            RunProcessComponent process = new RunProcessComponent();
            process.runProcessComponent(so, "checkout-basket", messages);
        } else {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.empty", null, LocaleContextHolder.getLocale()));
        }

        return new ModelAndView("/site/checkout/basket", "messages", messages);
    }

    @RequestMapping(value = "checkout/addmoduletocart.html", method = {RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody String  addModuleToCart(
            @RequestParam(value = "thisSiteId", required = true) Long thisSiteId,
            @Valid AddToCartForm form,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = new Messages();
        Product product = this.serviceLocator.getProductDao().getProduct(form.getProductId());
        SiteProductService service = serviceLocator.getSiteProductServiceDao().getSiteProductService(form.getProductId(), thisSiteId);

        SessionObject so = SessionUtil.load(request, response);
        List<ItemMap> items = so.getOrder().getItems();
        if (product != null) {
            //If product is existing in cart, don't need to add anymore
            if (getExistingItem(product, thisSiteId, items) != null) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.product.is.added.to.cart", null, LocaleContextHolder.getLocale()));
                //Add SiteProductService
                if (service == null) {
                    service = new SiteProductService();
                    service.setActive("Y");
                    ProductVariant productVariant = this.serviceLocator.getProductVariantDao().getProductVariantDefault(product.getId());
                    service.setProductVariant(productVariant);
                    Site thisSite = serviceLocator.getSiteDao().findById(thisSiteId);
                    service.setSite(thisSite);
                    service.setProduct(product);
                    serviceLocator.getSiteProductServiceDao().persist(service);
                }
            } else {
                //Add SiteProductService
                if (service == null) {
                    service = new SiteProductService();
                    service.setActive("Y");
                    ProductVariant productVariant = this.serviceLocator.getProductVariantDao().getProductVariantDefault(product.getId());
                    service.setProductVariant(productVariant);
                    Site thisSite = serviceLocator.getSiteDao().findById(thisSiteId);
                    service.setSite(thisSite);
                    service.setProduct(product);
                    serviceLocator.getSiteProductServiceDao().persist(service);
                }
                //Add item to cart
                ItemMap item = new ItemMap();
                item.setId(product.getId());
                item.setName(product.getName());
                item.setModelNumber(product.getModel());
                ProductVariant productVariant = this.serviceLocator.getSiteProductServiceDao().getProductVariant(product.getId(), thisSiteId);
                if (productVariant != null) {
                    item.setProductVariantId(productVariant.getId());
                }
                //Difference between product and module is modules have parent_product_id
                item.set("SITE_ID", thisSiteId);
                Product parentProduct =  serviceLocator.getProductDao().getParentProduct(product.getId(), MODULE_RELATION_TYPE, "Y");
                if (parentProduct != null) {
                    item.set("PARENT_PRODUCT_ID", parentProduct.getId());
                    item.set("RELATED_TYPE_ID", MODULE_RELATION_TYPE);
                    ItemMap parentItem = SessionUtil.getItemMap(items, parentProduct.getId(), thisSiteId);
                    if (parentItem != null) {
                        item.setQuantity(parentItem.getQuantity()); //quantity = parent's quality
                    } else {
                        item.setQuantity(1); //always quantity = 1
                    }
                }

                so.getOrder().addItem(item);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.product.is.added.to.cart", null, LocaleContextHolder.getLocale()));
            }
            SessionUtil.save(so);
            RunProcessComponent process = new RunProcessComponent();
            process.runProcessComponent(so, "checkout-basket", messages);
        } else {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.empty", null, LocaleContextHolder.getLocale()));
        }

        return messages.toString();
    }

    @RequestMapping(value = "checkout/addmoduletosite.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView addModuleToSite(
            @RequestParam(value = "thisSiteId", required = true) Long thisSiteId,
            @Valid AddToCartForm form,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = new Messages();
        SiteProductService service = serviceLocator.getSiteProductServiceDao().getSiteProductService(form.getProductId(), thisSiteId);
        Product product = this.serviceLocator.getProductDao().getProduct(form.getProductId());
        if (product != null) {
            //If product is existing in site, don't need to add anymore
            if (service != null) {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.module.is.existing.in.site", new String[]{product.getName()}, LocaleContextHolder.getLocale()));
            } else {
                service = new SiteProductService();
                service.setActive("Y");
                ProductVariant productVariant = this.serviceLocator.getProductVariantDao().getProductVariantDefault(product.getId());
                service.setProductVariant(productVariant);
                Site thisSite = serviceLocator.getSiteDao().findById(thisSiteId);
                service.setSite(thisSite);
                service.setProduct(product);
                serviceLocator.getSiteProductServiceDao().persist(service);
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.module.is.added.to.site", new String[]{product.getName()}, LocaleContextHolder.getLocale()));
                //TODO: Calculate price/total cost for all additional modules until end date of Hosting website
            }
        } else {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.empty", null, LocaleContextHolder.getLocale()));
        }

        return new ModelAndView("/site/modules", "messages", messages);
    }

    @RequestMapping(value = "checkout/removemodulefromsite.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView removeModuleFromSite(
            @RequestParam(value = "thisSiteId", required = true) Long thisSiteId,
            @Valid AddToCartForm form,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = new Messages();
        SiteProductService service = serviceLocator.getSiteProductServiceDao().getSiteProductService(form.getProductId(), thisSiteId);
        Product product = this.serviceLocator.getProductDao().getProduct(form.getProductId());
        if (product != null && service != null) {
            serviceLocator.getSiteProductServiceDao().remove(service);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.module.is.removed.to.site", new String[]{product.getName()}, LocaleContextHolder.getLocale()));
        } else {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.empty", null, LocaleContextHolder.getLocale()));
        }

        return new ModelAndView("/site/modules", "messages", messages);
    }

    @RequestMapping(value = "checkout/removemodulefromcart.html", method = {RequestMethod.POST}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public @ResponseBody String  deleteModuleFromcart(
            @RequestParam(value = "productId", required = true) Long productId,
            @RequestParam(value = "thisSiteId", required = true) Long thisSiteId,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = new Messages();
        Product product = this.serviceLocator.getProductDao().getProduct(productId);
        SessionObject so = SessionUtil.load(request, response);
        List<ItemMap> items = so.getOrder().getItems();
        ItemMap it = getExistingItem(product, thisSiteId, items);

        //Remove SiteProducService
        SiteProductService service = serviceLocator.getSiteProductServiceDao().getSiteProductService(productId, thisSiteId);
        if (product != null && service != null) {
            serviceLocator.getSiteProductServiceDao().remove(service);
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.module.is.removed.to.site", new String[]{product.getName()}, LocaleContextHolder.getLocale()));
        }
        //Remove from cart
        if (it != null) {
            if (it.isChildItem()) {
                items.remove(it);
            }
            SessionUtil.save(so);
            RunProcessComponent process = new RunProcessComponent();
            process.runProcessComponent(so, "checkout-basket", messages);
        }
        return messages.toString();
    }

    @RequestMapping(value = "checkout/deletecart.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView deleteCart(
            @RequestParam(value = "productId", required = true) Long productId,
            @RequestParam(value = "thisSiteId", required = true) Long thisSiteId,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = new Messages();
        Product product = this.serviceLocator.getProductDao().getProduct(productId);
        SessionObject so = SessionUtil.load(request, response);
        List<ItemMap> items = so.getOrder().getItems();
        ItemMap it = getExistingItem(product, thisSiteId, items);
        if (it != null) {
            List<ItemMap> relatedItems = SessionUtil.getRelatedProducts(items, product.getId(), MODULE_RELATION_TYPE, it.getLong("SITE_ID"));
            if (it.isChildItem()) {
                items.remove(it);
            } else {
                //Remove related products (modules)
                for (ItemMap iter : relatedItems) {
                    items.remove(iter);
                }
                //remove product
                items.remove(it);
            }
            SessionUtil.save(so);
            RunProcessComponent process = new RunProcessComponent();
            process.runProcessComponent(so, "checkout-basket", messages);
        } else {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.empty", null, LocaleContextHolder.getLocale()));
        }

        return new ModelAndView("/site/checkout/basket", "messages", messages);
    }

    @RequestMapping(value = "checkout/updatecart.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView updateCart(
            @RequestParam(value = "data", required = false) String data,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Messages messages = new Messages();
        SessionObject so = SessionUtil.load(request, response);
        List<ItemMap> items = so.getOrder().getItems();
        boolean isUpdate = false;
        //Select hosting - diskspace
        if (StringUtils.isNotEmpty(data)) {
            String []arr = data.split("-");
            Long productId = 0l;
            Long productVariantId = 0l;
            Long thisSiteId = 0l;
            if (arr.length > 2) {
                productId = Long.parseLong(arr[0]);
                productVariantId = Long.parseLong(arr[1]);
                thisSiteId = Long.parseLong(arr[2]);
            }
            Product product = this.serviceLocator.getProductDao().findById(productId);
            ItemMap it = getExistingItem(product, thisSiteId, items);
            if (it != null) {
                it.setProductVariantId(productVariantId);
                isUpdate = true;
            }
        }

        for (int i = 0; i < items.size(); i++) {
            ItemMap item = items.get(i);
            String quantity = request.getParameter("quantity_" + item.getProductVariantId()+"_"+item.get("SITE_ID"));
            if (!StringUtils.isEmpty(quantity) && !item.isChildItem()) {
                if (StringUtils.isNumeric(quantity)) {
                    Integer qty = Integer.valueOf(quantity);
                    if (qty > 0) {
                        ProductVariant productVariant = this.serviceLocator.getProductVariantDao().findById(item.getProductVariantId());
                        Product product = this.serviceLocator.getProductDao().findById(item.getId());
                        if (productVariant != null && productVariant.getInventory() > 0) {
                            if (qty > productVariant.getInventory()) {
                                item.setQuantity(productVariant.getInventory());
                                String arr[] = {String.valueOf(productVariant.getInventory()), item.getName(), product.getModel()};
                                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.lager", arr, LocaleContextHolder.getLocale()));
                            } else {
                                item.setQuantity(qty);
                                List<ItemMap> relatedItems = SessionUtil.getRelatedProducts(items, product.getId(), MODULE_RELATION_TYPE, item.getLong("SITE_ID"));
                                for (ItemMap childItem: relatedItems) {
                                    childItem.setQuantity(qty);
                                }
                            }
                        } else {
                            messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.empty", null, LocaleContextHolder.getLocale()));
                        }
                        //update weight in case have any change.
                        item.setWeight(product.getWeight());
                    } else {
                        items.remove(i);//remove if user enter negative number
                    }
                } else {
                    items.remove(i);//remove if user leave blank field
                }
                isUpdate = true;
            }
        }

        if (isUpdate) {
            SessionUtil.save(so);
            RunProcessComponent process = new RunProcessComponent();
            process.runProcessComponent(so, "checkout-basket", messages);
        }
        return new ModelAndView("/site/checkout/basket", "messages", messages);
    }

    private ItemMap getExistingItem(Product product, Long thisSiteId, List<ItemMap> items) {
        for (ItemMap it : items) {
            //product & site existed
            if ((it.getId().longValue() == product.getId().longValue()) &&
                    (it.getLong("SITE_ID").equals(thisSiteId))) {
                return it;
            }
        }
        return null;
    }

    private boolean isVariantExistedInCart(ProductVariant productVariant, List<ItemMap> items) {
        for (ItemMap it : items) {
            if (it.getProductVariantId().longValue() == productVariant.getId().longValue()) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping(value = "/checkout/applypromocode.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView applyPromoCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        if (so.getOrder().getItems() == null || so.getOrder().getItems().isEmpty()) {
            return new ModelAndView("/site/checkout/basket");
        }
        Messages message = new Messages();
        String promoCode = request.getParameter("promoCode");
        if (StringUtils.isEmpty(promoCode)) {
            message.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.promocode.required", null, LocaleContextHolder.getLocale()));
        } else {
            Promotion promotion = serviceLocator.getPromotionDao().getValidPromotion(promoCode);
            if (promotion != null) {
                so.getOrder().setPromoCode(promoCode);
                RunProcessComponent process = new RunProcessComponent();
                process.runProcessComponent(so, "checkout-applyPromoCode", message);
            } else {
                String[] p = new String[1];
                p[0] = promoCode;
                message.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.promocode.invalid", p, LocaleContextHolder.getLocale()));
            }
        }
        String dest = request.getParameter("dest");
        if (StringUtils.isEmpty(dest)) {
            return new ModelAndView("/site/checkout/basket", "promo_messages", message);
        } else {
            return new ModelAndView(dest, "promo_messages", message);
        }
    }

    @RequestMapping(value = "/checkout/removepromocode.html")
    public ModelAndView removePromoCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        PromotionUtil.removePromotionDiscount(so);
        RunProcessComponent process = new RunProcessComponent();
        process.runProcessComponent(so, "checkout-basket", null);

        return new ModelAndView("/site/checkout/basket");
    }

    //Review Page
    @RequestMapping(value = "/checkout/review.html", method = RequestMethod.POST)
    public ModelAndView saveUserAndReviewOrder(@Valid BillingShippingForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);

        //set billing address
        so.getAddresses().getBillingAddress().setFirstName(form.getFirstNameForBilling());
        so.getAddresses().getBillingAddress().setLastName(form.getLastNameForBilling());
        so.getAddresses().getBillingAddress().setStreet(form.getAddress1ForBilling());
        so.getAddresses().getBillingAddress().setDistrict(form.getDistrictForBilling());
        so.getAddresses().getBillingAddress().setCity(form.getCityForBilling());
        so.getAddresses().getBillingAddress().setState(form.getStateForBilling());
        so.getAddresses().getBillingAddress().setZipCode(form.getZipCodeForBilling());
        so.getAddresses().getBillingAddress().setCountry(form.getCountryForBilling());
        so.getAddresses().getBillingAddress().setPhone(form.getPhoneForBilling());
        so.getAddresses().getBillingAddress().setEmail(form.getEmailAddressForBilling());

        so.getOrder().setShippingMethod(form.getShippingCode());
        so.getOrder().setPaymentMethod(form.getPaymentProviderId());
//        so.getOrder().set("SHIPPING_TO_BILLING", form.isShipToBillingAddress());
        RunProcessComponent process = new RunProcessComponent();
        Messages message = new Messages();
        process.runProcessComponent(so, "checkout-shipping", message);
        SessionUtil.save(so);
        return new ModelAndView("/site/checkout/review");
    }

    @RequestMapping(value = "/checkout/review.html")
    public String reviewOrderGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        if (so.getOrder().getItems() == null || so.getOrder().getItems().isEmpty()) {
            return "/site/checkout/basket";
        } else {
//            if (StringUtils.isEmpty(so.getUserName())) {
//                return "redirect:/user/login.html?source=/checkout/review";
//            }
        }
        return "/site/checkout/review";
    }

    /**
     * Place Order
     *
     * This method will place order. Here is what the method will do:
     * 1.  Create order on Order and Order_Session tables
     * 2.  Store the order status is New Order. (Notes: New Order: order just created, have not paid yet)
     * 3.  Process update expired date for the service (base on each service)
     * 4.  Redirect to Payment Provider
     * 5.  Send email to the client said that order was created. But not make a payment.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkout/payment.html")
    public String payment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        if (so.getOrder().getItems() == null || so.getOrder().getItems().isEmpty()) {
            return "/site/checkout/basket";
        } else if ("GET".equalsIgnoreCase(request.getMethod())) {
            return "/site/checkout/review";
        }

        /* 1.  Create order on Order and Order_Session tables
         * 2.  Store the order status is New Order. (Notes: New Order: order just created, have not paid yet)
         * 3.  Process update expired date for the service (base on each service)
         * 4.  TODO: Redirect to Payment Provider if any
         * 5.  Send email confirm
         * */

        Order order = serviceLocator.getOrderService().createServiceOrder(so);
        so.getOrder().set("ORDER_ID", String.valueOf(order.getId()));
        //Send email to user
        Site site = serviceLocator.getSystemContext().getSite();
        SessionObject orderObject = serviceLocator.getOrderService().getOrderSession(order.getId(), so.getUserId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("site", site);
        map.put("siteParam", site.getSiteParamsMap());
        map.put("order", order);
        map.put("orderObject", orderObject);
        for (ItemMap item : orderObject.getOrder().getItems()) {
            item.set("FINAL_PRICE_ITEM_EMAIL", Money.valueOf(String.valueOf(item.getFinalPriceItem()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
            item.set("TOTAL_PRICE_ITEM_EMAIL", Money.valueOf(String.valueOf(item.getFinalPriceItem() * item.getQuantity()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
            if (item.getPriceItemPromoDiscount() > 0) {
                item.set("PRICE_ITEM_PROMO_DISCOUNT_EMAIL", Money.valueOf(String.valueOf(item.getPriceItemPromoDiscount() * item.getQuantity()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
            }
        }
        orderObject.getOrder().set("SUB_TOTAL_PRICE_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getSubPriceTotal()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        orderObject.getOrder().set("TOTAL_PRICE_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getTotalPrice()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        if (orderObject.getOrder().getTaxPrice() != null) {
            orderObject.getOrder().set("TAX_PRICE_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getTaxPrice()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        } else {
            orderObject.getOrder().set("TAX_PRICE_EMAIL", Money.valueOf("0", site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        }
        orderObject.getOrder().set("SHIPPING_PRICE_EMAIL", Money.valueOf(String.valueOf(orderObject.getShippingFee()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        //shipping discount
        if (orderObject.getOrder().getShippingDiscountPrice() != null) {
            orderObject.getOrder().set("SHIPPING_PRICE_DISCOUNT_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getShippingDiscountPrice()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        } else {
            orderObject.getOrder().set("SHIPPING_PRICE_DISCOUNT_EMAIL", "");
        }
        //billing discount
        if (orderObject.getOrder().getSubPriceDiscountTotal() != null) {
            orderObject.getOrder().set("SUB_TOTAL_PRICE_DISCOUNT_EMAIL", Money.valueOf(String.valueOf(orderObject.getOrder().getSubPriceDiscountTotal()), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString());
        } else {
            orderObject.getOrder().set("SUB_TOTAL_PRICE_DISCOUNT_EMAIL", "");
        }

        EmailSite emailSite = serviceLocator.getEmailSiteDao().getEmailInfor(site.getId(), "orderconfirm");
        if (emailSite != null) {
            EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().findById(emailSite.getEmailTemplateId());
            String subject = emailTemplate.getSubject().replaceAll("[BRAND_NAME]", site.getName());
            String[] bcc = null;
            if (!StringUtils.isEmpty(site.getSiteParam("CONTACT_US"))) {
                bcc = site.getSiteParam("CONTACT_US").split(",");
            }
            serviceLocator.getMailService().sendEmail(emailSite.getSendFrom(), order.getEmail(), null, bcc, subject, map, emailTemplate.getTemplateFile());
        } else {
            EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().getEmailTemplate("orderconfirm", serviceLocator.getLocale().toString());
            String subject = emailTemplate.getSubject().replaceAll("[BRAND_NAME]", site.getName());
            String[] bcc = null;
            if (!StringUtils.isEmpty(site.getSiteParam("CONTACT_US"))) {
                bcc = site.getSiteParam("CONTACT_US").split(",");
            }
            String from = site.getSiteParamsMap().get("CONTACT_US");
            if (StringUtils.isEmpty(from)) {
                from = "info@webphattai.com";
            }
            serviceLocator.getMailService().sendEmail(from, order.getEmail(), null, bcc, subject, map, emailTemplate.getTemplateFile());
        }

        //Remove session object
        so.getOrder().setItems(null);

        Long providerId = so.getOrder().getPaymentMethod();
        PaymentProvider paymentProvider = serviceLocator.getPaymentProviderDao().getPaymentProviderSelected(providerId);//PaymentUtil.getProvider(providerId);
        Class c = Class.forName(paymentProvider.getProviderClass());
        Payment payment = (Payment) c.newInstance();
        String url = payment.createRequestUrl(so, serviceLocator.getSystemContext().getSite());
        if (StringUtils.isNotEmpty(url) && url.contains("?")) {
            url += "&orderId="+order.getId();
        } else {
            url += "?orderId="+order.getId();
        }
        so.setOrder(null);
        SessionUtil.save(so);
        return "redirect:/site" + url;
    }

    /** This action will be called after make a payment. It will callback to this method.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkout/receipt.html", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView receipt(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionObject so = SessionUtil.load(request, response);
        String orderId = request.getParameter("orderId");
        Site site = serviceLocator.getSystemContext().getSite();
        Order order = serviceLocator.getOrderSessionDao().getOrder(new Long(orderId), so.getUserId(), site);
        if (order != null) {
            SessionObject orderSessionObject = serviceLocator.getOrderSessionDao().getOrderSession(order.getId(), so.getUserId(), ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite());
            boolean flag = false; //
            if (order.getStatus().equals(Order.NEW_ORDER)) {
                //Payment provider call back after successfully payment.
                Long providerId = orderSessionObject.getOrder().getPaymentMethod();
                PaymentProvider paymentProvider = serviceLocator.getPaymentProviderDao().getPaymentProviderForSite(providerId, site.getId());
                Class c = Class.forName(paymentProvider.getProviderClass());
                Payment payment = (Payment) c.newInstance();
                Map map = request.getParameterMap();
                Map mapResponse = new HashMap();
                if (map != null) {
                    //iterate through the java.util.Map and display posted parameter values
                    //the keys of the Map.Entry objects ae type String; the values are type String[],
                    //or String array
                    for (Object o : map.entrySet()) {
                        Map.Entry me = (Map.Entry) o;
                        String key = (String) me.getKey();
                        String value = "";
                        String[] arr = (String[]) me.getValue();
                        for (String anArr : arr) {
                            //print commas after multiple values,
                            //except for the last one
                            if (StringUtils.isEmpty(value)) {
                                value = anArr;
                            } else {
                                value += "," + anArr;
                            }

                        }
                        mapResponse.put(key, value);
                    }
                }

                //flag = true, mean you complete payment.
                orderSessionObject.getOrder().set("ORDER_ID", orderId);
                payment.verifyResponseUrl(orderSessionObject, mapResponse, serviceLocator.getSystemContext().getSite());
            }
            return new ModelAndView("/site/checkout/receipt", "order", order);
        } else {
            return new ModelAndView("/index");
        }
    }

    /*********************************************
     * Retailer
     *********************************************/
    @RequestMapping(value = {"partner/upgrade_business_partner.html"}, method = {RequestMethod.GET})
    public ModelAndView upgradeBusinessPartner(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!SessionUtil.isLoggedIn(request, response)) {
            return new ModelAndView("/site/partner/partner");
        } else {
            Site site = serviceLocator.getSystemContext().getSite();
            Messages messages = new Messages();
            //OK, new account is valid
            User user = SessionUtil.loadUser(request, response);
            if (user != null) {
                if ("Y".equals(user.getPartnerStatus())) {
                    if (2 == user.getPartner()) {
                        return new ModelAndView("/site/partner/partner");
                    }
                    user.setPartner(2);//Business
                    //update user
                    serviceLocator.getUserService().createOrUpdate(user);
                    //Create contact us for approve
                    ContactUs contactUs = new ContactUs();
                    Date date = new Date();
                    contactUs.setCreatedDate(date);
                    contactUs.setAddressTo("Partner");
                    contactUs.setFirstName(user.getFirstName());
                    contactUs.setLastName(user.getLastName());
                    contactUs.setSendersEmail(user.getUsername());
                    contactUs.setMessage(user.getUsername()+" - Upgrade to Business Partner");
                    contactUs.setRead("N");
                    contactUs.setSite(site);
                    serviceLocator.getContactUsDao().persist(contactUs);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("upgrade.from.personal.to.business.successfully", null, LocaleContextHolder.getLocale()));

                    //Sending Email to Customer
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("site", site);
                    map.put("user", user);
                    map.put("siteParam", site.getSiteParamsMap());
                    EmailTemplate approveNotify = serviceLocator.getEmailTemplateDao().getEmailTemplate("approvedPartner", serviceLocator.getLocale().toString());
                    String []bcc = null;
                    if (!StringUtils.isEmpty(site.getSiteParam("CONTACT_US"))) {
                        bcc = site.getSiteParam("CONTACT_US").split(",");
                    }
                    serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), user.getUsername(), null, bcc, approveNotify.getSubject(), map, approveNotify.getTemplateContent());

                } else {
                    user.setPartner(2);//Business

                    //update user
                    serviceLocator.getUserService().createOrUpdate(user);
                    //send email
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("site", site);
                    map.put("user", user);
                    map.put("siteParam", site.getSiteParamsMap());
                    EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().getEmailTemplate("registerPartnerConfirm", serviceLocator.getLocale().toString());
                    String []bcc = null;
                    if (!StringUtils.isEmpty(site.getSiteParam("CONTACT_US"))) {
                        bcc = site.getSiteParam("CONTACT_US").split(",");
                    }
                    serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), user.getUsername(), null, bcc, emailTemplate.getSubject(), map, emailTemplate.getTemplateContent());
                    //Create contact us for approve
                    ContactUs contactUs = new ContactUs();
                    Date date = new Date();
                    contactUs.setCreatedDate(date);
                    contactUs.setAddressTo("Partner");
                    contactUs.setFirstName(user.getFirstName());
                    contactUs.setLastName(user.getLastName());
                    contactUs.setSendersEmail(user.getUsername());
                    EmailTemplate approveNotify = serviceLocator.getEmailTemplateDao().getEmailTemplate("notifyingPartner", serviceLocator.getLocale().toString());
                    if (approveNotify != null) {
                        contactUs.setMessage(WebUtil.parseVelocityContent(map, approveNotify.getTemplateContent()));
                    }
                    contactUs.setRead("N");
                    contactUs.setSite(site);
                    serviceLocator.getContactUsDao().persist(contactUs);

                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("register.successfully.please.wait.for.confirm", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("register.unsuccessful", null, LocaleContextHolder.getLocale()));
            }

            return new ModelAndView("/site/partner/partner", "messages", messages);
        }
    }
    @RequestMapping(value = {"partner/upgrade_personal_partner.html"}, method = {RequestMethod.GET})
    public ModelAndView upgradePersonalPartner(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!SessionUtil.isLoggedIn(request, response)) {
            return new ModelAndView("/site/partner/partner");
        } else {
            Site site = serviceLocator.getSystemContext().getSite();
            Messages messages = new Messages();
            //OK, new account is valid
            User user = SessionUtil.loadUser(request, response);
            if (user != null) {
                //the user was partner
                if ("Y".equals(user.getPartnerStatus())) {
                    return new ModelAndView("/site/partner/partner");
                }
                user.setPartner(1);//Personal

                //update user
                serviceLocator.getUserService().createOrUpdate(user);
                //send email
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("site", site);
                map.put("user", user);
                map.put("siteParam", site.getSiteParamsMap());

                EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().getEmailTemplate("registerPartnerConfirm", serviceLocator.getLocale().toString());
                String []bcc = null;
                if (!StringUtils.isEmpty(site.getSiteParam("CONTACT_US"))) {
                    bcc = site.getSiteParam("CONTACT_US").split(",");
                }
                serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), user.getUsername(), null, bcc, emailTemplate.getSubject(), map, emailTemplate.getTemplateContent());

                //Create contact us for approve
                ContactUs contactUs = new ContactUs();
                Date date = new Date();
                contactUs.setCreatedDate(date);
                contactUs.setAddressTo("Partner");
                contactUs.setFirstName(user.getFirstName());
                contactUs.setLastName(user.getLastName());
                contactUs.setSendersEmail(user.getUsername());
                EmailTemplate approveNotify = serviceLocator.getEmailTemplateDao().getEmailTemplate("notifyingPartner", serviceLocator.getLocale().toString());
                if (approveNotify != null) {
                    contactUs.setMessage(WebUtil.parseVelocityContent(map, approveNotify.getTemplateContent()));
                }
                contactUs.setRead("N");
                contactUs.setSite(site);
                serviceLocator.getContactUsDao().persist(contactUs);

                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("register.successfully.please.wait.for.confirm", null, LocaleContextHolder.getLocale()));
            } else {
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("register.unsuccessful", null, LocaleContextHolder.getLocale()));
            }

            return new ModelAndView("/site/partner/partner", "messages", messages);
        }
    }
    @RequestMapping(value = {"partner/partner_register.html"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView partnerRegister(@Valid User user, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (SessionUtil.isLoggedIn(request, response)) {
            return new ModelAndView("/site/partner/partner");
        } else if ("GET".equalsIgnoreCase(request.getMethod())) {
                return new ModelAndView("site/partner/partner_login");
        } else {
            Site site = serviceLocator.getSystemContext().getSite();

            Map result = new HashMap();
            String userName = request.getParameter("email");
            String password = request.getParameter("password");
            String repassword = request.getParameter("repassword");

            String recaptchaResponse = request.getParameter("g-recaptcha-response");
            String secretParameter = serviceLocator.getSystemContext().getGlobalConfig("CAPTCHA_GOOGLE_SECRET_KEY");
            Messages messages = new Messages();

            if (WebUtil.isValidCaptcha(recaptchaResponse, secretParameter, request.getRemoteAddr())) {

                if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(repassword)) {
                    //check password and repassword is the same
                    if (!password.equals(repassword)) {
                        messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.register.password.and.repassword.different", null, LocaleContextHolder.getLocale()));
                        result.put("entity", request.getParameterMap());
                        //check account existed
                    } else {
                        List<User> users = serviceLocator.getUserDao().findClientUserByUsername(userName, site);
                        if (users != null && !users.isEmpty()) {
                            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("site.register.emailinused", null, LocaleContextHolder.getLocale()));
                            result.put("entity", request.getParameterMap());
                        } else {
                            //OK, new account is valid
                            user.setEmail(userName);
                            user.setUsername(userName);
                            user.setPassword(serviceLocator.getUserService().encrypt(password));
                            user.setBlocked("N");
                            user.setSiteAdmin("N");
                            user.setSiteUser("Y");
                            user.setPartnerStatus("N");

                            //create new user
                            serviceLocator.getUserService().createOrUpdate(user);

                            //send email
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("site", site);
                            map.put("user", user);
                            map.put("password", password);
                            map.put("siteParam", site.getSiteParamsMap());

                            EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().getEmailTemplate("registerPartnerConfirm", serviceLocator.getLocale().toString());
                            String []bcc = null;
                            if (!StringUtils.isEmpty(site.getSiteParam("CONTACT_US"))) {
                                bcc = site.getSiteParam("CONTACT_US").split(",");
                            }
                            serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), user.getUsername(), null, bcc, emailTemplate.getSubject(), map, emailTemplate.getTemplateContent());

                            //Create contact us for approve
                            ContactUs contactUs = new ContactUs();
                            Date date = new Date();
                            contactUs.setCreatedDate(date);
                            contactUs.setAddressTo("Partner");
                            contactUs.setFirstName(user.getFirstName());
                            contactUs.setLastName(user.getLastName());
                            contactUs.setSendersEmail(user.getUsername());
                            EmailTemplate approveNotify = serviceLocator.getEmailTemplateDao().getEmailTemplate("notifyingPartner", serviceLocator.getLocale().toString());
                            if (approveNotify != null) {
                                contactUs.setMessage(WebUtil.parseVelocityContent(map, approveNotify.getTemplateContent()));
                            }
                            contactUs.setRead("N");
                            contactUs.setSite(site);
                            serviceLocator.getContactUsDao().persist(contactUs);

                            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("register.successfully.please.wait.for.confirm", null, LocaleContextHolder.getLocale()));
                        }
                    }
                } else {
                    messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("please.enter.username.password", null, LocaleContextHolder.getLocale()));
                    result.put("entity", request.getParameterMap());
                }
            } else {
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("login.forgot.captcha.error", null, LocaleContextHolder.getLocale()));
                result.put("entity", request.getParameterMap());
            }
            result.put("messages",messages);
            return new ModelAndView("/site/partner/partner_login", result);
        }

    }

    @RequestMapping(value = "modules.html")
    public ModelAndView listModules(@Valid SearchForm form) throws Exception {
        return new ModelAndView("site/modules", "command", form);
    }
}