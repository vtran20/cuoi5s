package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.security.CSRFProtection;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * @deprecated
 */
public interface ISiteAdminController {

    @RequestMapping(value = "index.html", method = RequestMethod.GET)
    ModelAndView index() throws Exception;

    @RequestMapping(value = "form.html", method = RequestMethod.GET)
    ModelAndView displayForm() throws Exception;

    @RequestMapping(value = "index.html", method = RequestMethod.PUT)
    @CSRFProtection
    ModelAndView add(@Valid Site site) throws Exception;

    @RequestMapping(value = "{siteId}/index.html", method = RequestMethod.GET)
    ModelAndView view(@PathVariable("siteId") Long siteId) throws Exception;

    @RequestMapping(value = "{siteId}/index.html", method = RequestMethod.POST)
    @CSRFProtection
    ModelAndView update(@PathVariable("siteId") Long siteId, @Valid Site site) throws Exception;

    @RequestMapping(value = "{siteId}/index.html", method = RequestMethod.DELETE)
    @CSRFProtection
    ModelAndView delete(@PathVariable("siteId") Long siteId) throws Exception;

}