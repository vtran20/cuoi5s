package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.security.CSRFProtection;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/admin/images")
public class ImagesController {

    @Autowired
    private ServiceLocator serviceLocator;

    @RequestMapping("index.html")
    public ModelAndView index() throws Exception {
        return new ModelAndView("admin/images/index");
    }
    @RequestMapping("{action}.html")
    public ModelAndView action(@PathVariable String action) throws Exception {
        return new ModelAndView("admin/images/"+action);
    }
    /**
     * This will be called when delete a Menu
     */
    @RequestMapping(value = "deleteimage.html", method = RequestMethod.GET)
    @CSRFProtection
    public @ResponseBody
    String deleteImage(@Valid String uri) {
        try {
            return URLEncoder.encode(WebUtil.encrypt(uri), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
