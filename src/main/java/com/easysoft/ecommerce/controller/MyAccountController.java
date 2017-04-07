package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.model.EmailSite;
import com.easysoft.ecommerce.model.EmailTemplate;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import com.easysoft.ecommerce.web.servlet.CaptchaServlet;
import org.apache.commons.lang.StringUtils;
import org.expressme.openid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Note: User login succesfully when username and userId is put in sessionObject
 */

@Controller
@RequestMapping()
public class MyAccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyAccountController.class);
    private ServiceLocator serviceLocator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get("DATE_FORMAT"));
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @Autowired
    public MyAccountController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @RequestMapping(value = "/user/login.html", method = RequestMethod.GET)
    public ModelAndView loginByGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        if (!StringUtils.isEmpty(source)) {
            return new ModelAndView("/user/login", "source", source);
        } else {
            return new ModelAndView("/user/login", "source", "");
        }
    }

    @RequestMapping(value = "/user/login.html", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        SessionObject so = SessionUtil.load(request, response);

        Map error = new HashMap();
        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
            List<User> users = serviceLocator.getUserDao().findClientUserByUsername(userName, serviceLocator.getSystemContext().getSite());
            String source = request.getParameter("source");
            if (users != null && users.size() > 0) {
                User user = users.get(0);
                if (serviceLocator.getUserService().isValidPassword(password, user.getPassword())) {
                    setSessionObjectAttribute(so, user);
                    if (StringUtils.isEmpty(source)) {
                        return new ModelAndView("/user/main");
                    } else {
                        return new ModelAndView(source);
                    }
                } else if (serviceLocator.getUserService().isValidPassword(password, user.getTempPassword())) {
                    user.setTempPassword("");
                    serviceLocator.getUserDao().merge(user);
                    setSessionObjectAttribute(so, user);
                    if (StringUtils.isEmpty(source)) {
                        return new ModelAndView("/user/main");
                    } else {
                        return new ModelAndView(source);
                    }
                } else {
                    error.put("error", "error.username.password.incorrect");
                    return new ModelAndView("/user/login", error);
                }
            } else {
                error.put("error", "login.forgot.password.error");
                return new ModelAndView("/user/login", error);
            }
        } else {
            error.put("error", "error.username.password.blank");
            return new ModelAndView("/user/login", error);
        }
    }

    @RequestMapping("/user/main.html")
    public String billingShipping(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject so = SessionUtil.load(request, response);
        if (StringUtils.isEmpty(so.getUserName())) {
            return "redirect:/user/login.html";
        } else {
            return "/user/main";
        }
    }

    @RequestMapping(value = "/user/change_account.html", method = RequestMethod.GET)
    public String changeAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/user/main";
    }

    @RequestMapping(value = "/user/change_account.html", method = RequestMethod.POST)
    public ModelAndView changeAccountPost(@Valid User entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Messages messages = createOrUpdateUser(entity);
        return new ModelAndView("/user/main", "messages", messages);
    }

    private void setSessionObjectAttribute(SessionObject so, User user) {
        so.setUserName(user.getUsername());
        so.setUserId(user.getId());
        //set billing address
        so.getAddresses().getBillingAddress().setFirstName(user.getFirstName());
        so.getAddresses().getBillingAddress().setLastName(user.getLastName());
        so.getAddresses().getBillingAddress().setStreet(user.getAddress_1());
        so.getAddresses().getBillingAddress().setDistrict(user.getDistrict());
        so.getAddresses().getBillingAddress().setCity(user.getCity());
        so.getAddresses().getBillingAddress().setState(user.getState());
        so.getAddresses().getBillingAddress().setZipCode(user.getZipCode());
        so.getAddresses().getBillingAddress().setCountry(user.getCountry());
        so.getAddresses().getBillingAddress().setPhone(user.getPhone());
        so.getAddresses().getBillingAddress().setEmail(user.getEmail());

        //set default shipping address
        so.getAddresses().getShippingAddress().setFirstName(user.getFirstNameShipping());
        so.getAddresses().getShippingAddress().setLastName(user.getLastNameShipping());
        so.getAddresses().getShippingAddress().setStreet(user.getAddress_1Shipping());
        so.getAddresses().getShippingAddress().setDistrict(user.getDistrictShipping());
        so.getAddresses().getShippingAddress().setCity(user.getCityShipping());
        so.getAddresses().getShippingAddress().setState(user.getStateShipping());
        so.getAddresses().getShippingAddress().setZipCode(user.getZipCodeShipping());
        so.getAddresses().getShippingAddress().setCountry(user.getCountryShipping());
        so.getAddresses().getShippingAddress().setPhone(user.getPhoneShipping());
    }

    @RequestMapping(value = "/user/logout.html", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        SessionObject so = SessionUtil.load(request, response);
        so.setUserName("");
        so.setUserId(0l);
        SessionUtil.save(so);
        return "/index";
    }

    @RequestMapping("/user/new_account.html")
    public ModelAndView createNewAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String source = request.getParameter("source");
        SessionObject so = SessionUtil.load(request, response);
        Map error = new HashMap();

        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(repassword)) {
            //check password and repassword is the same
            if (!password.equals(repassword)) {
                error.put("error_new_account", "error.password.repassword.notthesame");
                return new ModelAndView("/user/login", error);
                //check account existed
            } else {
                List<User> users = serviceLocator.getUserDao().findClientUserByUsername(userName, serviceLocator.getSystemContext().getSite());
                if (users != null && !users.isEmpty()) {
                    error.put("error_new_account", "error.username.existed");
                    return new ModelAndView("/user/login", error);
                } else {
                    //OK, new account is valid
                    so.setUserName(userName);
                    so.getAddresses().getBillingAddress().setFirstName(firstName);
                    so.getAddresses().getBillingAddress().setLastName(lastName);
                    User user = new User();
                    user.setEmail(userName);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setUsername(userName);
                    user.setPassword(serviceLocator.getUserService().encrypt(password));
                    user.setBlocked("N");
                    user.setSiteAdmin("N");
                    user.setSiteUser("Y");
                    //TODO: user customer instead
                    //user.setSite(serviceLocator.getSystemContext().getSite());

                    //create new user
                    serviceLocator.getUserService().createOrUpdate(user);
                    so.setUserId(user.getId());
                    if (StringUtils.isEmpty(source)) {
                        return new ModelAndView("/user/main");
                    } else {
                        return new ModelAndView(source);
                    }
                }
            }
        } else {
            error.put("error_new_account", "error.username.password.blank");
            return new ModelAndView("/user/login", error);
        }
    }

    @RequestMapping("/user/forgotpassword.html")
    public String forgotPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/user/forgot_password_popup";
    }

    @RequestMapping("/user/reset_password.html")
    public ModelAndView sendTempPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Site site = serviceLocator.getSystemContext().getSite();
        String userName = request.getParameter("userName");
        String recaptchaResponse = request.getParameter("g-recaptcha-response");
        String secretParameter = serviceLocator.getSystemContext().getGlobalConfig("CAPTCHA_GOOGLE_SECRET_KEY");
        if (WebUtil.isValidCaptcha(recaptchaResponse, secretParameter, request.getRemoteAddr())) {
            /*Generate temporary password and save it*/
            User user = serviceLocator.getUserDao().getClientUser(userName, site);
            if (user != null) {
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
                return new ModelAndView("/user/forgot_password_popup", "sent", "sent");
            } else {
                Messages messages = new Messages();
                messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("login.forgot.password.error", null, LocaleContextHolder.getLocale()));
                return new ModelAndView("/user/forgot_password_popup", "messages", messages);
            }
        } else {
            Messages messages = new Messages();
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("login.forgot.captcha.error", null, LocaleContextHolder.getLocale()));
            return new ModelAndView("/user/forgot_password_popup", "messages", messages);
        }
    }


    static final long ONE_HOUR = 3600000L;
    static final long TWO_HOUR = ONE_HOUR * 2L;
    static final String ATTR_MAC = "openid_mac";
    static final String ATTR_ALIAS = "openid_alias";

    @RequestMapping(value = "/user/openidlogin.html", method = RequestMethod.GET)
    public String loginUsingOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String op = request.getParameter("op");
        String source = request.getParameter("source");
        OpenIdManager manager = new OpenIdManager();
        manager.setRealm("http://" + serviceLocator.getSystemContext().getSite().getDomain());
        manager.setReturnTo("http://" + serviceLocator.getSystemContext().getSite().getDomain() + "/user/openidlogin.html");

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
                    return "/user/main";
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
                    return "/user/main";
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


        return "/user/login";
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

}
