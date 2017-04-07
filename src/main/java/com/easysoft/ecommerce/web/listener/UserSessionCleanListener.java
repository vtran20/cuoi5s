package com.easysoft.ecommerce.web.listener;

/**
 * Created by IntelliJ IDEA.
 *
 * User: vtran
 * Date: Aug 18, 2010
 * Time: 10:44:32 PM
 * To change this template use File | Settings | File Templates.
 */

import com.easysoft.ecommerce.controller.SessionUtil;
import com.easysoft.ecommerce.model.session.SessionObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class UserSessionCleanListener implements HttpSessionListener {

    // Public constructor is required by servlet spec
    public UserSessionCleanListener() {
        
    }
    private int count = 0;
    private ServletContext context = null;

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public synchronized  void sessionCreated(HttpSessionEvent se) {
        count++;
        se.getSession().setAttribute("count",new Integer(count));
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        count--;
        se.getSession().setAttribute("count",new Integer(count));
        try {
            SessionUtil.closeSession((String) session.getAttribute(SessionObject.USER_SESSION_ID_KEY));
        } catch (Exception e) {
            System.out.print("Having issue when try closing Session");
            e.printStackTrace();
        }
    }

}
