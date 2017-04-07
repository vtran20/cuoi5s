package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.model.ContactUs;
import com.easysoft.ecommerce.model.News;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.util.Date;

@Controller
@RequestMapping(value="/admin/customer")
public class CustomerAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAdminController.class);

    @Autowired
    private ServiceLocator serviceLocator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, ServiceLocatorHolder.getServiceLocator().getSystemContext().getServiceLocator().getLocale());
        //SimpleDateFormat df = new SimpleDateFormat(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get("DATE_FORMAT"));
        df.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
    }


    /***************************************************************************************************
     *  Implement Customer Management
     ***************************************************************************************************/
    /**
     * Default request base on fileName
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{action}.html", method = RequestMethod.GET)
    public String action(@PathVariable String action) throws Exception {
        return "admin/customer/" + action;
    }

    /**
     * This will be called when delete a message
     */
    @RequestMapping(value = "delete_message.html", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE}, produces="application/x-www-form-urlencoded; charset=UTF-8")
    @CSRFProtection
    public @ResponseBody
    String deleteMessage(@Valid Long id) {
        Messages messages = new Messages();
        try {
            if (id > 0) {
                Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
                ContactUs message = serviceLocator.getContactUsDao().findById(id, site.getId());
                if (message != null) {
                    serviceLocator.getContactUsDao().remove(message);
                    messages.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.deleted.success", null, LocaleContextHolder.getLocale()));
                } else {
                    messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.not.available", null, LocaleContextHolder.getLocale()));
                }
            } else {
                messages.addWarning(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("common.data.invalid", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            messages.addError("ERROR: Cannot delete this record.");
        }
        return messages.toString();
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }
}
