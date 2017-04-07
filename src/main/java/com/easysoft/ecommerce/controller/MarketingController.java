package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.model.EmailMarketing;
import com.easysoft.ecommerce.model.EmailPlan;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.impl.URLUTF8Encoder;
import com.easysoft.ecommerce.util.MoneyRange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping
public class MarketingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketingController.class);
    private ServiceLocator serviceLocator;

    @Autowired
    public MarketingController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @RequestMapping("/marketing/register.html")
    public void register(HttpServletRequest request, @Valid EmailMarketing emailMarketing, HttpServletResponse response) throws Exception {
        //Decrypt email if invalid
        if (!EmailValidator.getInstance().isValid(emailMarketing.getEmail())) {
            String realEmail = serviceLocator.getStrongEncryptor().decrypt(emailMarketing.getEmail());
            emailMarketing.setEmail(realEmail);
        }

        EmailMarketing email = serviceLocator.getEmailMarketingDao().findUniqueBy("email", emailMarketing.getEmail());
        if (email == null) {
            emailMarketing.setOptin("Y");
            emailMarketing.setMarketingOrder(0l);
            emailMarketing.setUpdatedDate(new Date());
            serviceLocator.getEmailMarketingDao().persist(emailMarketing);
        } else {
            email.setUpdatedDate(new Date());
            serviceLocator.getEmailMarketingDao().merge(email);
        }
        response.setContentType("image/jpg");
        int width = 1;
        int height = 1;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] pixels = new int[width*height];
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (i == j) {
                    pixels[j*width + i] = Color.WHITE.getRGB();
                }
                else {
                    pixels[j*width + i] = Color.WHITE.getRGB();
                }
            }
        }
        image.setRGB(0, 0, width, height, pixels, 0, width);

        ServletOutputStream sout = response.getOutputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte []imageInByte = baos.toByteArray();
        sout.write(imageInByte);
        baos.flush();
        baos.close();
        sout.flush();
        sout.close();

    }

    @RequestMapping(value="/marketing/subscribe.html", method = RequestMethod.GET)
    public ModelAndView subscribe(HttpServletRequest request, @Valid EmailMarketing emailMarketing) throws Exception {

        EmailMarketing email = null;
        if (!StringUtils.isEmpty(emailMarketing.getEmail())) {

            //Decrypt email if invalid
            if (!EmailValidator.getInstance().isValid(emailMarketing.getEmail())) {
                String realEmail = serviceLocator.getStrongEncryptor().decrypt(emailMarketing.getEmail());
                emailMarketing.setEmail(realEmail);
            }

            email = serviceLocator.getEmailMarketingDao().findUniqueBy("email", emailMarketing.getEmail());
            if (email != null) {
                email.setOptin("Y");
                if (email.getFirstName() != null && !email.getFirstName().equals(emailMarketing.getFirstName())) {
                    email.setFirstName(emailMarketing.getFirstName());
                }
                if (email.getLastName() != null && !email.getLastName().equals(emailMarketing.getLastName())) {
                    email.setLastName(emailMarketing.getLastName());
                }
                serviceLocator.getEmailMarketingDao().merge(email);
                return new ModelAndView("user/subscribe", "email", email);
            } else {
                emailMarketing.setOptin("Y");
                emailMarketing.setMarketingOrder(0l);
                emailMarketing.setUpdatedDate(new Date());
                serviceLocator.getEmailMarketingDao().persist(emailMarketing);
                return new ModelAndView("user/subscribe", "email", emailMarketing);
            }
        }
        return new ModelAndView("user/subscribe", "email", null);
    }

    @RequestMapping(value="/marketing/subscribe.html", method = RequestMethod.POST)
    public ModelAndView subscribePost(HttpServletRequest request, @Valid EmailMarketing emailMarketing) throws Exception {
        return subscribe(request, emailMarketing);
    }

    @RequestMapping(value = "/marketing/unsubscribe.html", method = RequestMethod.GET)
    public ModelAndView unsubscribe(HttpServletRequest request, @Valid EmailMarketing emailMarketing) throws Exception {
        EmailMarketing email = null;
        if (!StringUtils.isEmpty(emailMarketing.getEmail())) {

            //Decrypt email if invalid
            if (!EmailValidator.getInstance().isValid(emailMarketing.getEmail())) {
                String realEmail = serviceLocator.getStrongEncryptor().decrypt(emailMarketing.getEmail());
                emailMarketing.setEmail(realEmail);
            }

            email = serviceLocator.getEmailMarketingDao().findUniqueBy("email", emailMarketing.getEmail());
            if (email != null && "N".equalsIgnoreCase(emailMarketing.getOptin())) {
                email.setOptin("N");
                serviceLocator.getEmailMarketingDao().merge(email);
            }
        }
        return new ModelAndView("user/unsubscribe", "email", email);
    }
    @RequestMapping(value = "/marketing/unsubscribe.html", method = RequestMethod.POST)
    public ModelAndView unsubscribePost(HttpServletRequest request, @Valid EmailMarketing emailMarketing) throws Exception {
        return unsubscribe(request, emailMarketing);
    }
    @RequestMapping(value = "/marketing/giam-gia.html", method = RequestMethod.GET)
    public void viewMailInBrowser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String emailTemplate = request.getParameter("emailTemplate");
        String email = request.getParameter("email");
        List<Product> products = serviceLocator.getProductDao().findBy("productMarketing", "Y");
        Map map = new HashMap();
        map.put("email", URLUTF8Encoder.encode(email));
        map.put("products", products);
        map.put("MoneyRange", MoneyRange.class);
        map.put("site", serviceLocator.getSystemContext().getSite());
        String text = VelocityEngineUtils.mergeTemplateIntoString(
                serviceLocator.getVelocityEngine(), emailTemplate, "UTF-8", map);

        PrintWriter out = response.getWriter();
        out.println(text);
    }
    @RequestMapping(value = "/marketing/email-marketing.html", method = RequestMethod.GET)
    public void viewMailInBrowserEmailMarketing(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String emailPlanId = request.getParameter("emailPlanId");
        EmailPlan emailPlan = null;
        if (StringUtils.isNumeric(emailPlanId)) {
            emailPlan = serviceLocator.getEmailPlanDao().findById(new Long(emailPlanId));
        }
        // get current email marketing or get the last one if it is not available
        if (emailPlan == null) {
            emailPlan = serviceLocator.getEmailPlanDao().getEmailPlan(serviceLocator.getSystemContext().getSite());
            if (emailPlan == null) {
                List <EmailPlan>list = serviceLocator.getEmailPlanDao().findAll();
                if (list != null) {
                    emailPlan = list.get(list.size()-1);
                }
            }
        }

        String email = request.getParameter("email");
        List<Product> products = getMarketingProducts(emailPlan);
        Map map = new HashMap();
        map.put("email", URLUTF8Encoder.encode(email));
        map.put("products", products);
        map.put("cmsTop", emailPlan.getCmsTop());
        map.put("cmsBottom", emailPlan.getCmsBottom());
        map.put("MoneyRange", MoneyRange.class);
        map.put("site", serviceLocator.getSystemContext().getSite());
        map.put("emailPlan", emailPlan);
        String text = VelocityEngineUtils.mergeTemplateIntoString(
                serviceLocator.getVelocityEngine(), emailPlan.getEmailTemplate(), "UTF-8", map);

        PrintWriter out = response.getWriter();
        out.println(text);
    }


    private List<Product> getMarketingProducts (EmailPlan emailPlan) {
        List <Product> products = null;
        if (emailPlan != null) {
            products = new ArrayList<Product>();
            Product product;
            String model = emailPlan.getModel1();
            if ( !StringUtils.isEmpty(model)) {
                product = serviceLocator.getProductDao().findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel2();
            if ( !StringUtils.isEmpty(model)) {
                product = serviceLocator.getProductDao().findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel3();
            if ( !StringUtils.isEmpty(model)) {
                product = serviceLocator.getProductDao().findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel4();
            if ( !StringUtils.isEmpty(model)) {
                product = serviceLocator.getProductDao().findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel5();
            if ( !StringUtils.isEmpty(model)) {
                product = serviceLocator.getProductDao().findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel6();
            if ( !StringUtils.isEmpty(model)) {
                product = serviceLocator.getProductDao().findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel7();
            if ( !StringUtils.isEmpty(model)) {
                product = serviceLocator.getProductDao().findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel8();
            if ( !StringUtils.isEmpty(model)) {
                product = serviceLocator.getProductDao().findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel9();
            if ( !StringUtils.isEmpty(model)) {
                product = serviceLocator.getProductDao().findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

        }
        return products;
    }

}
