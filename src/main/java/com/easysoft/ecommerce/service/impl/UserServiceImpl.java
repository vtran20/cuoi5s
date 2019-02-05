package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.dao.EmailMarketingDao;
import com.easysoft.ecommerce.dao.SiteDao;
import com.easysoft.ecommerce.dao.UserDao;
import com.easysoft.ecommerce.dao.UserRoleDao;
import com.easysoft.ecommerce.model.EmailMarketing;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.UserRole;
import com.easysoft.ecommerce.service.UserService;
import com.fasterxml.uuid.Generators;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private SiteDao siteDao;
    private UserDao userDao;
    private UserRoleDao userRoleDao;
    private EmailMarketingDao emailMarketingDao;
    private StandardPBEStringEncryptor strongEncryptor;

    @Autowired
    public UserServiceImpl(UserDao userDao, EmailMarketingDao emailMarketingDao, StandardPBEStringEncryptor strongEncryptor, SiteDao siteDao, UserRoleDao userRoleDao) {
        super();
        this.userDao = userDao;
        this.emailMarketingDao = emailMarketingDao;
        this.strongEncryptor = strongEncryptor;
        this.siteDao = siteDao;
        this.userRoleDao = userRoleDao;
    }

    @Override
    public void createOrUpdate(User entity) throws Exception {

        if (entity.getId() == null) {
            userDao.persist(entity);
            EmailValidator validator = EmailValidator.getInstance();
            if (validator.isValid(entity.getUsername())) {
                EmailMarketing emailMarketing = emailMarketingDao.findUniqueBy("email", entity.getUsername());
                if (emailMarketing == null) {
                    emailMarketing = new EmailMarketing();
                    emailMarketing.setEmail(entity.getUsername());
                    emailMarketing.setFirstName(entity.getFirstName());
                    emailMarketing.setLastName(entity.getLastName());
                    emailMarketing.setCreatedDate(new Date());
                    emailMarketing.setUpdatedDate(new Date());
                    emailMarketing.setMarketingOrder(0l);
                    emailMarketing.setOptin("Y");
                    emailMarketingDao.persist(emailMarketing);
                }
            }
        } else {
            userDao.merge(entity);
        }

    }

    @Override
    public void remove(User entity) throws Exception {
        userDao.remove(entity);
    }

    @Override
    public String encrypt(String str) {
        return strongEncryptor.encrypt(str);
    }

    @Override
    public String decrypt(String str) {
        return strongEncryptor.decrypt(str);
    }

    @Override
    public boolean isValidPassword(String password, String encryptPassword) {
        try {
            return !StringUtils.isEmpty(password) && !StringUtils.isEmpty(encryptPassword) && password.equals(strongEncryptor.decrypt(encryptPassword));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void activateAccount(User user, Date sDate, String domain, String siteCode) {
        Site site = new Site();
        if (sDate == null) {
            sDate = user.getCreatedDate();
        }

        //Create site
        site.setStartDate(sDate);
        //Set end date at the same day. We will use this column for expired date of website use the real domain.
        site.setEndDate(sDate);
        site.setSiteCode(siteCode);
        site.setActive("Y");
        site.setDefaultSite("N");
        site.setSubDomain(siteCode + "." + domain);
        site.setAppId(Generators.timeBasedGenerator().generate().toString());
        siteDao.persist(site);

        //Update user
        user.setBlocked("N");
        userDao.merge(user);

        //Add role for this account

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole("ROLE_SITE_CONTENT_ADMIN");
        userRole.setSite(site);
        userRoleDao.persist(userRole);
    }


}
