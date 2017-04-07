package com.easysoft.ecommerce.util.jobs;

import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.EmailMarketing;
import com.easysoft.ecommerce.model.EmailPlan;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.service.impl.URLUTF8Encoder;
import com.easysoft.ecommerce.util.MoneyRange;
import net.sf.ehcache.CacheManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.velocity.app.VelocityEngine;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Marketing email sending
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 */

public class MarketingEmailSending extends AutoEmailSending {

    EmailPlanDao emailPlanDao;
    EmailMarketingDao emailMarketingDao;
    SessionFactory sessionFactory;

    public MarketingEmailSending(SessionFactory sessionFactory, EmailMarketingDao emailMarketingDao, EmailPlanDao emailPlanDao, ProductDao productDao, EmailServerDao emailServerDao, StandardPBEStringEncryptor strongEncryptor, VelocityEngine velocityEngine, EmailSiteDao emailSiteDao, EmailTemplateDao emailTemplateDao, CacheManager cacheManager, Site site, String emailListFile, String emailProcessedFile) {
        super(productDao, emailServerDao, strongEncryptor, velocityEngine, emailSiteDao, emailTemplateDao, cacheManager, site, emailListFile, emailProcessedFile);
        this.sessionFactory = sessionFactory;
        this.emailPlanDao = emailPlanDao;
        this.emailMarketingDao = emailMarketingDao;
    }

    public boolean sendingEmailFromFile() {

        EmailPlan emailPlan = emailPlanDao.getEmailPlan(site);

        if (emailPlan != null) {

            List<Product> products = getMarketingProducts(emailPlan);

            String subject = emailPlan.getSubject();
            Session session = this.sessionFactory.getCurrentSession();

            //Starting sending
            try {

                EmailValidator emailValidator = EmailValidator.getInstance();
                //Email Server configuration
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                mailSender.setHost(site.getSiteParamsMap().get("MARKETING_SMTP_SERVER"));
                mailSender.setPort(new Integer(site.getSiteParamsMap().get("MARKETING_PORT_SERVER")));
                mailSender.setUsername(site.getSiteParamsMap().get("MARKETING_USER_SERVER"));
                mailSender.setPassword(site.getSiteParamsMap().get("MARKETING_PASS_SERVER"));
                mailSender.setDefaultEncoding("UTF-8");
                Properties prop = new Properties();
                prop.setProperty("mail.smtp.auth", site.getSiteParamsMap().get("MARKETING_SMTP_AUTH"));
                prop.setProperty("mail.smtp.starttls.enable", site.getSiteParamsMap().get("MARKETING_SMTP_STARTTLS"));
                mailSender.setJavaMailProperties(prop);

                try {

                    //starting sending email on each server.
                    int emailsPerSend = new Integer(site.getSiteParamsMap().get("MARKETING_EMAILS_PER_SEND"));
                    List<EmailMarketing> emailList = emailMarketingDao.getEmailMarketings("Y", emailPlan.getId(), emailsPerSend);
                    Map map = new HashMap();
                    map.put("products", products);
                    map.put("cmsTop", emailPlan.getCmsTop());
                    map.put("cmsBottom", emailPlan.getCmsBottom());
                    map.put("MoneyRange", MoneyRange.class);
                    map.put("site", site);
                    map.put("emailPlan", emailPlan);
                    int i = 0;
                    for (EmailMarketing emailMarketing : emailList) {
                        try {
                            String emailWillSend = emailMarketing.getEmail();
                            if (emailValidator.isValid(emailWillSend)) {
                                map.put("email", URLUTF8Encoder.encode(strongEncryptor.encrypt(emailWillSend)));
                                sendEmail(site.getSiteParamsMap().get("MARKETING_EMAIL_FROM"), emailWillSend, null, null, subject, map, emailPlan.getEmailTemplate(), mailSender);
                            } else {
                                emailMarketing.setInvalid("Y");
                            }
                        } catch (Exception e) {
                            messages.add("SMTP:" + mailSender.getUsername() + ": Exception message:" + e.getMessage());
                            emailMarketing.setInvalid("Y");
                        } finally {
                            emailMarketing.setMarketingOrder(emailPlan.getId());
                            session.save(emailMarketing);
                            if (++i % 500 == 0) { //500, same as the JDBC batch size
                                //flush a batch of inserts and release memory:
                                session.flush();
                                session.clear();
                            }
                        }
                    }

                } catch (Exception e) {
                    messages.add("SMTP:" + mailSender.getUsername() + ": Exception message:" + e.getMessage());
                }
            } finally {
                //flush a batch of inserts and release memory of the rest items
                session.flush();
                session.clear();
            }
        }
        return true;
    }

    protected List<Product> getMarketingProducts (EmailPlan emailPlan) {
        List <Product> products = null;
        if (emailPlan != null) {
            products = new ArrayList<Product>();
            Product product;
            String model = emailPlan.getModel1();
            if ( !StringUtils.isEmpty(model)) {
                product = productDao.findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel2();
            if ( !StringUtils.isEmpty(model)) {
                product = productDao.findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel3();
            if ( !StringUtils.isEmpty(model)) {
                product = productDao.findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel4();
            if ( !StringUtils.isEmpty(model)) {
                product = productDao.findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel5();
            if ( !StringUtils.isEmpty(model)) {
                product = productDao.findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel6();
            if ( !StringUtils.isEmpty(model)) {
                product = productDao.findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel7();
            if ( !StringUtils.isEmpty(model)) {
                product = productDao.findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel8();
            if ( !StringUtils.isEmpty(model)) {
                product = productDao.findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

            model = emailPlan.getModel9();
            if ( !StringUtils.isEmpty(model)) {
                product = productDao.findUniqueBy("model", model);
                if (product != null) {
                    products.add(product);
                }
            }

        }
        return products;
    }

}
