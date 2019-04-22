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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

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
        } else if (site.getSiteType() == 4) { // 1 page template
            return "/content/onepage";
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

    @RequestMapping(value = "ajax/contact-us.html", method = RequestMethod.POST)
    public @ResponseBody String contactUsSubmitAjax(@Valid ContactUs entity, HttpServletRequest request) throws Exception {
        Messages messages = new Messages();
        Site site = serviceLocator.getSystemContext().getSite();
        String recaptchaResponse = request.getParameter("g-recaptcha-response");
        String secretParameter = serviceLocator.getSystemContext().getGlobalConfig("CAPTCHA_GOOGLE_SECRET_KEY");
        if (entity.isEmptyId()) {
            Date date = new Date();
            //Create menu record
            entity.setCreatedDate(date);
            entity.setSite(site);
            serviceLocator.getContactUsDao().persist(entity);
            //send email
            String contactEmail = site.getSiteParam("CONTACT_US");
            String sStoreId = request.getParameter("storeId");
            if (StringUtils.isNumeric(sStoreId)) {
                NailStore store = ServiceLocatorHolder.getServiceLocator().getNailStoreDao().findById(Long.valueOf(sStoreId), site.getId());
                if (store != null && StringUtils.isNotEmpty(store.getEmail())) {
                    contactEmail = store.getEmail();
                }
            }
            if (!StringUtils.isEmpty(contactEmail)) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("contact", entity);
                map.put("siteParam", site.getSiteParamsMap());
                EmailTemplate emailTemplate = serviceLocator.getEmailTemplateDao().getEmailTemplate("contact_us_message", serviceLocator.getLocale().toString());
                serviceLocator.getMailService().sendEmailFromTemplateContent(ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("global.email"), contactEmail, null, null, emailTemplate.getSubject(), map, emailTemplate.getTemplateContent());
            }
            messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("content.is.sent.successful", null, LocaleContextHolder.getLocale()));
        }
        return messages.toString();
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
    /***************************************************************************
     Implement Booking Page.
     ***************************************************************************/
    @RequestMapping("/booking.html")
    public ModelAndView booking(String uri, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("booking");
    }

    @RequestMapping(value = {"/load-timeslots.json"}, method = RequestMethod.GET)
    public
    @ResponseBody
    Map loadAvailableTimeslots(HttpServletRequest request) throws Exception {
        NailStore store = null;
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date(new Long(request.getParameter("currentDate"))));
        if (StringUtils.isNotEmpty(request.getParameter("storeId")) && StringUtils.isNumeric(request.getParameter("storeId"))) {
            Long storeId = new Long(request.getParameter("storeId"));
            store = ServiceLocatorHolder.getServiceLocator().getNailStoreDao().findById(storeId);
        }
        if (StringUtils.isNotEmpty(request.getParameter("date")) && store != null) {
            Date date = WebUtil.stringToDate(request.getParameter("date"), "yyyy-MM-dd");
            String dayOfWeek = WebUtil.dateToString(date, "EEE");
            String hours = null;
            if ("Sun".equals(dayOfWeek)) hours = store.getHourSun();
            if ("Mon".equals(dayOfWeek)) hours = store.getHourMon();
            if ("Tue".equals(dayOfWeek)) hours = store.getHourTue();
            if ("Wed".equals(dayOfWeek)) hours = store.getHourWed();
            if ("Thu".equals(dayOfWeek)) hours = store.getHourThu();
            if ("Fri".equals(dayOfWeek)) hours = store.getHourFri();
            if ("Sat".equals(dayOfWeek)) hours = store.getHourSat();
            if (hours != null) {
                if ("0".equals(hours)) {
                    return null;
                } else {
                    //09:30-19:00
                    String startEnd[] = hours.split("-");
                    if (startEnd.length == 2) {
                        String startDay = startEnd[0]; //09:30
                        String endDay = startEnd[1];  //19:00

                        Calendar startTime = WebUtil.getStartDate(date);
                        String startDateTime[] = startDay.split(":");
                        startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startDateTime[0])); //start hour
                        startTime.set(Calendar.MINUTE, Integer.valueOf(startDateTime[1])); //start minutes
                        if (currentDate.after(startTime)) {
                            startTime.set(Calendar.HOUR_OF_DAY, currentDate.get(Calendar.HOUR_OF_DAY));
                            startTime.add(Calendar.HOUR_OF_DAY, 2); //only make appointment next 2 hours
                        }

                        Calendar endTime = WebUtil.getStartDate(date);
                        String endDateTime[] = endDay.split(":");
                        endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endDateTime[0])); //start hour
                        endTime.set(Calendar.MINUTE, Integer.valueOf(endDateTime[1])); //start minutes

                        Long employeeId = 0l;
                        if (StringUtils.isNotEmpty(request.getParameter("employeeId"))) {
                            employeeId = Long.parseLong(request.getParameter("employeeId"));
                        }

                        List<NailCustomerAppointment> appointments = ServiceLocatorHolder.getServiceLocator().getNailCustomerAppointmentDao()
                                    .getCustomerAppointmentsByDate(WebUtil.getStartDate(date).getTime(), WebUtil.getEndDate(date).getTime(), store.getId(), employeeId);

                        //all available timeslots
                        Set <Date> timeslots = new LinkedHashSet<Date>();
                        while (!startTime.after(endTime)) {
                            timeslots.add(startTime.getTime());
                            startTime.add(Calendar.MINUTE, 15);
                        }

                        if (appointments != null && appointments.size() > 0) {
                            //remove booked timeslots
                            for (NailCustomerAppointment appt : appointments) {
                                Calendar time = Calendar.getInstance();
                                time.setTime(appt.getStartTime());
                                while (timeslots.contains(time.getTime()) && time.getTime().before(appt.getEndTime())) {
                                    timeslots.remove(time.getTime());
                                    time.add(Calendar.MINUTE, 15);
                                }
                            }

                        }

                        //remove timeslots that cannot book because is not enough time range. Start at a timeslot, if ahead of time have 1 hour available, then the timeslot is valid
                        Set <Date> timeslotsClone = new LinkedHashSet<Date>(timeslots);
                        //available slot if enough 1 hour
                        int slotSteps = 3;
                        for (Date dateTimeSlot : timeslotsClone) {
                            Calendar time = Calendar.getInstance();
                            time.setTime(dateTimeSlot);
                            for (int i = 0; i < slotSteps; i++) {
                                time.add(Calendar.MINUTE, 15);
                                if (timeslots.contains(time.getTime())) {
                                    //do nothing
                                } else {
                                    timeslots.remove(dateTimeSlot);
                                }
                            }
                        }
                        //Group by morning, afternoon and evening
                        Calendar afternoonTime = WebUtil.getStartDate(date);
                        afternoonTime.set(Calendar.HOUR_OF_DAY, 12);
                        Calendar eveningTime = WebUtil.getStartDate(date);
                        eveningTime.set(Calendar.HOUR_OF_DAY, 17);
                        Map result = new HashMap();
                        List morning = new ArrayList();
                        List afternoon = new ArrayList();
                        List evening = new ArrayList();
                        result.put("morning", morning);
                        result.put("afternoon", afternoon);
                        result.put("evening", evening);
                        for (Date ts : timeslots) {
                            if (ts.before(afternoonTime.getTime())) {
                                morning.add(WebUtil.dateToString(ts, "hh:mm a"));
                            } else if (ts.before(eveningTime.getTime())) {
                                afternoon.add(WebUtil.dateToString(ts, "hh:mm a"));
                            } else {
                                evening.add(WebUtil.dateToString(ts, "hh:mm a"));
                            }
                        }

                        return result;

                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @RequestMapping(value = {"/submit_appointment.html"}, method = RequestMethod.POST, produces="application/x-www-form-urlencoded; charset=UTF-8")
    public
    @ResponseBody
    String submitAppointment(HttpServletRequest request) {
        try {
            Map inputData = new HashMap();
            inputData.put("selectedStoreId", request.getParameter("selectedStoreId"));
            inputData.put("selectedDate", request.getParameter("selectedDate"));
            inputData.put("selectedTime", request.getParameter("selectedTime"));
            String services = request.getParameter("selectedServiceId");
            String []serviceIds = services != null? services.split(",") : null;
            inputData.put("selectedServiceId", serviceIds);
            inputData.put("selectedEmployeeId", request.getParameter("selectedEmployeeId"));
            inputData.put("firstName", request.getParameter("firstName"));
            inputData.put("lastName", request.getParameter("lastName"));
            String phone = request.getParameter("phone");
            if (StringUtils.isNotEmpty(phone)) {
                phone = phone.replaceAll("\\(", "");
                phone = phone.replaceAll("\\)", "");
                phone = phone.replaceAll("-", "");
                phone = phone.replaceAll(" ", "");
            }
            inputData.put("phone", phone);
            inputData.put("email", request.getParameter("email"));
            inputData.put("message", request.getParameter("message"));
            return serviceLocator.getSystemContext().getServiceLocator().getNailManagementService().makeAppointmentFromFrontEnd(inputData, new Long(request.getParameter("selectedStoreId"))).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Messages error = new Messages();
            error.addError("System error: Sorry we cannot make appointment right now.");
            return error.toString();
        }
    }

}
