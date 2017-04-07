package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.UserSessionDao;
import com.easysoft.ecommerce.model.UserSession;
import com.easysoft.ecommerce.model.session.SessionObject;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

@Repository
public class UserSessionDaoImpl extends GenericDaoImpl<UserSession, String> implements UserSessionDao {

    public SessionObject getUserSession(String userSessionId) throws IOException, ClassNotFoundException {
        SessionObject sessionObject = null;
        UserSession userSession = findById(userSessionId);
        if (userSession != null) {
            //Having problem with jdk 1.7
            //XStream xstream = new XStream();
            //Object object = xstream.fromXML(userSession.getUserSessionData());
            //Convert to binary object to support jdk1.7
            byte[] data = userSession.getUserSessionData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            Object object =  is.readObject();

            if (object instanceof SessionObject) {
                sessionObject = (SessionObject) object;
            }
        }

        return sessionObject;
    }

    public void createOrUpdateUserSession(SessionObject sessionObject) throws IOException, ClassNotFoundException {
        if (sessionObject == null) return;
        if (sessionObject.getId() == null) return;

        UserSession us = findById(sessionObject.getId());

        //Having problem with jdk 1.7
        //XStream xstream = new XStream();
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
        //xstream.toXML(sessionObject, writer);
        //String data = outputStream.toString("UTF-8");

        //Convert to binary object to support jdk1.7
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(sessionObject);
        byte data[] = out.toByteArray();

        if (us == null) {
            us = new UserSession();
            us.setUserSessionId(sessionObject.getId());
            us.setUserSessionData(data);
            us.setUpdatedDate(new Date());
            persist(us);
        } else {
            us.setUserSessionData(data);
            us.setUpdatedDate(new Date());
            merge(us);
        }

        os.close();
        os = null;
        out.close();
        out = null;
    }

    @Override
    public void deleteUserSession() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        this.getSessionFactory().getCurrentSession().createQuery("delete from UserSession a where a.updatedDate  < :date").
                setParameter("date", cal.getTime()).executeUpdate();
    }
}