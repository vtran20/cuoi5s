package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.condition.Condition;
import com.easysoft.ecommerce.util.Messages;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping()
public class GeneralController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralController.class);
    private ServiceLocator serviceLocator;

    @Autowired
    public GeneralController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @RequestMapping("/index.html")
    public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        if (site.getSiteType() == 1 || site.getSiteType() == 3) {
            return "/wpt";
        } else { //2
            return "/content/page";
        }
    }
    @RequestMapping("/environment.html")
    public @ResponseBody String environment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "<html><body>"+ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("environment")+"</body></html>";
    }

    /**
     * Forward to page for rendering html page.
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/{uri}.html")
    public String content(@PathVariable String uri) throws Exception {
        return uri;
    }

    @RequestMapping("/error.html")
    public String error() {
        return "syserror";
    }

    @RequestMapping("/baokim_{baokimCode}.html")
    public ModelAndView baokim(@PathVariable String baokimCode, HttpServletRequest request) {
        Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
        String registeredCode = site.getSiteParamsMap().get("BAOKIM_VERIFY_CODE");
        return new ModelAndView("/baokim", "code", registeredCode);
    }

    @RequestMapping("/utils/view.html")
    public String view() {
        return "utils/view";
    }

    @RequestMapping("/utils/test.html")
    public String test() {
        return "utils/test";
    }
    @RequestMapping("/utils/{uri}.html")
    public String test(@PathVariable String uri) {
        return "utils/"+uri;
    }

    @RequestMapping("/utils/utils.html")
    public ModelAndView utils(HttpServletRequest request) {
        return new ModelAndView("/utils/utils", "mode", request.getParameter("mode"));
    }

    @RequestMapping("/utils/promotions.html")
    public String promotions(HttpServletRequest request, HttpServletResponse response) {
        ExpressionParser parser = new SpelExpressionParser();
        List<Promotion> promotions = serviceLocator.getPromotionDao().getPromotions("Y");
        for (Promotion promotion : promotions) {
            List<PromotionCondition> promotionConditions = serviceLocator.getPromotionConditionDao().findBy("promotion.id", promotion.getId());
//            List<ConditionClass> conditionClass = serviceLocator.getConditionClassDao().getConditionClasses(promotion.getId(), true);

            String expression = "";
            String logical = "";
            for (PromotionCondition promotionCondition : promotionConditions) {
                ConditionClass conditionClass = serviceLocator.getConditionClassDao().findById(promotionCondition.getCondition().getId());
                if (StringUtils.isEmpty(expression)) {
                    expression = replaceValue(promotionCondition.getExpression(), conditionClass.getClassName(), request, response);
                    logical = promotionCondition.getLogical();
                } else {
                    if (StringUtils.isEmpty(logical)) {
                        throw new IllegalArgumentException("Logical is missing.");
                    } else {
                        String temp = replaceValue(promotionCondition.getExpression(), conditionClass.getClassName(), request, response);
                        if (!StringUtils.isEmpty(temp)) {
                            expression += " " + logical + " " + temp;
                            logical = promotionCondition.getLogical();
                        }
                    }
                }

//                expression = expression.replaceAll("SUB_TOTAL_PRICE", "3000");
//                expression = expression.replaceAll("domain", serviceLocator.getSystemContext().getSite().getDomain());
            }
            System.out.println("Expression=" + expression + " - " + parser.parseExpression(expression).getValue(Boolean.class));
        }

        return "utils/promotions";
    }

    private String replaceValue(String expression, String conditionClass, HttpServletRequest request, HttpServletResponse response) {
        String result = null;

        try {
            if (!StringUtils.isEmpty(expression) && !StringUtils.isEmpty(conditionClass)) {
                Class c = null;
                c = Class.forName(conditionClass);
                Condition condition = (Condition) c.newInstance();
                result = condition.execute(SessionUtil.load(request, response), null, null, expression);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("Expression error:" + expression + ":" + conditionClass, e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            LOGGER.error("Expression error:" + expression + ":" + conditionClass, e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            LOGGER.error("Expression error:" + expression + ":" + conditionClass, e);
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.error("Expression error:" + expression + ":" + conditionClass, e);
            e.printStackTrace();
        }

        return result;
    }

    /***************************************************************************
     Implement Contact Us Page.
    ***************************************************************************/
    @RequestMapping("/contact-us.html")
    public ModelAndView contactUs() throws Exception {
        return new ModelAndView("contactus");
    }

    @RequestMapping(value = "/contact-us.html", method = RequestMethod.POST)
    public ModelAndView contactUsSubmit(@Valid ContactUs entity, HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        Site site = serviceLocator.getSystemContext().getSite();
        String recaptchaResponse = request.getParameter("g-recaptcha-response");
        String secretParameter = serviceLocator.getSystemContext().getGlobalConfig("CAPTCHA_GOOGLE_SECRET_KEY");
        if (WebUtil.isValidCaptcha(recaptchaResponse, secretParameter, request.getRemoteAddr())) {
            if (entity.isEmptyId()) {
                Date date = new Date();
                //Create menu record
                entity.setCreatedDate(date);
                entity.setSite(site);
                serviceLocator.getContactUsDao().persist(entity);
                //send email
                String contactEmail = site.getSiteParam("CONTACT_US");
                if (!StringUtils.isEmpty(contactEmail)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("contact", entity);
                    map.put("siteParam", site.getSiteParamsMap());
                    EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().getEmailTemplate("contact_us_message", serviceLocator.getLocale().toString());
                    serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), contactEmail, null, null, emailTemplate.getSubject(), map, emailTemplate.getTemplateContent());
                }
                messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("content.is.sent.successful", null, LocaleContextHolder.getLocale()));
            }
        } else {
            messages.addError(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("login.forgot.captcha.error", null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("/contactus", "messages", messages);
    }
    /***************************************************************************
     Implement Question and Answer Page.
    ***************************************************************************/
    @RequestMapping(value = "/question-answer.html", method = {RequestMethod.POST, RequestMethod.GET})
    public String questionAnswer(HttpServletRequest request) throws Exception {
        return "question";
    }

    /***************************************************************************
     Implement ??? Page.
     ***************************************************************************/
    @RequestMapping("/edit-html.html")
    public ModelAndView editHTML() throws Exception {
        return new ModelAndView("edithtml");
    }

    /***************************************************************************
     Implement Gallery Page.
     ***************************************************************************/
    @RequestMapping("/page/gallery.html")
    public ModelAndView index(String uri, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("page/gallery");
    }
    @RequestMapping("/page/album.html")
    public ModelAndView album(String uri, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("page/album");
    }
}
