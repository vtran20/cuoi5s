package com.easysoft.ecommerce.service;

import com.easysoft.ecommerce.model.User;

import java.util.Date;

public interface UserService {

    void createOrUpdate(User user) throws Exception;

    void remove(User user) throws Exception;

    String encrypt(String str);

    String decrypt(String str);

    boolean isValidPassword(String password, String encryptPassword);

    void activateAccount(User user, Date sDate, String domain, String siteCode);
}