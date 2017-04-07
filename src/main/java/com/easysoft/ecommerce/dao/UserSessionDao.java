package com.easysoft.ecommerce.dao;

import java.io.IOException;

import com.easysoft.ecommerce.model.UserSession;
import com.easysoft.ecommerce.model.session.SessionObject;

public interface UserSessionDao extends GenericDao<UserSession, String> {
    SessionObject getUserSession (String userSessionId) throws IOException, ClassNotFoundException;
    void createOrUpdateUserSession (SessionObject sessionObject) throws IOException, ClassNotFoundException;
    void deleteUserSession () ;
}