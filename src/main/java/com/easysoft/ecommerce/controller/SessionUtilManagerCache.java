package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.dao.UserSessionDao;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.fasterxml.uuid.Generators;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is an utility class that provide utility methods for handle SessionObject.
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class SessionUtilManagerCache {

    private static int cookieAge = 30 * 24 * 60 * 60;

    /**
     * Get cookie object from cookieName
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie findCookie(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];

                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }

            }
        }

        return null;

    }

    /**
     * Return the value of the cookie with the given name from within the request instance,
     * or null if no cookie with the name exists.
     *
     * @param request    The HttpServletRequest to look for the cookie within.
     * @param cookieName The name of the cookie to search for.
     * @return The value of the cookie that was found with the given name (case sensitive),
     *         or null if it doesn't exist.
     */
    public static String findCookieValue(HttpServletRequest request, String cookieName) {

        Cookie cookie = findCookie(request, cookieName);

        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }

    }

    public static String getUserSessionID(HttpServletRequest request, HttpServletResponse response) {
        String userSessionID = null;

        userSessionID = (String) request.getSession().getAttribute(SessionObject.USER_SESSION_ID_KEY);

        // Create a cookie if one does not exist
        if (userSessionID == null || StringUtils.isEmpty(userSessionID) || "null".equals(userSessionID) || "0".equals(userSessionID)) {

            // Look for the session id in the cookie if we have one
            String cookieValue = SessionUtilManagerCache.findCookieValue(request, SessionObject.USER_SESSION_ID_KEY);
            if (cookieValue != null) {
                userSessionID = cookieValue;
            } else {
                // Look in the URL for a userSessionID
                cookieValue = request.getParameter(SessionObject.USER_SESSION_ID_KEY);
                if (cookieValue != null) {
                    userSessionID = cookieValue;
                }
            }

            // Create a cookie if one does not exist
            if (userSessionID == null || StringUtils.isEmpty(userSessionID) || "null".equals(userSessionID) || "0".equals(userSessionID)) {

                userSessionID = Generators.timeBasedGenerator().generate().toString();
//                userSessionID = UUIDGenerator.getUUID();
                setUserSession(userSessionID, request, response);

            }

            //set the USER_SESSION_ID to the httpSession to make sure we clean the it out of cache when httpSession timeout
            request.getSession().setAttribute(SessionObject.USER_SESSION_ID_KEY, userSessionID);
        }

        return userSessionID;
    }

    /**
     * Sets the userSessionId for the current session
     *
     * @param userSessionID The user session id to store in the  session cookie.
     * @param request       The HttpServletRequest used to get the cookie
     * @param response      The HttpServletResponse used to set a new cookie
     */
    public static void setUserSession(String userSessionID, HttpServletRequest request, HttpServletResponse response) {
        Cookie sessionCookie = new Cookie(SessionObject.USER_SESSION_ID_KEY, userSessionID);
        sessionCookie.setMaxAge(cookieAge);

        // If a domain name was specified... set it.
        sessionCookie.setDomain(request.getServerName());

        // Allow the cookie to work for anything within this applications context
//        if (StringUtils.isEmpty(request.getContextPath()))
//            sessionCookie.setPath("/");
//        else
//            sessionCookie.setPath(request.getContextPath());

        response.addCookie(sessionCookie);
    }

    /**
     * Returns the user session and will create one if createFlag is true.
     * <p/>
     * We should cache session object in memory with session object id is key and session object is value. This help
     * avoid loading from database a lot.
     * <p/>
     * Note: We will create new SessionObject if the user don't have
     *
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return The user session retrieved from persis. storage.
     */

    public static SessionObject load(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject userSession = null;

        //try to get the session cookie
        String sessionId = getUserSessionID(request, response);
        //get the SessionObject
        userSession = loadSessionObject(sessionId);

        return (userSession);
    }

    public static SessionObject loadSessionObject(String sessionObjectId) throws Exception {

        Ehcache cache = ServiceLocatorHolder.getServiceLocator().getCacheManager().getCache("SessionObject");
        //get from cache
        SessionObject sessionObject = null;
        Element element = cache.get(sessionObjectId);
        if (element != null) {
            sessionObject = (SessionObject) element.getObjectValue();
        }

        if (sessionObject != null) {
            return sessionObject;
        } else {
            UserSessionDao usd = ServiceLocatorHolder.getServiceLocator().getSystemContext().getServiceLocator().getUserSessionDao();
            sessionObject = usd.getUserSession(sessionObjectId);

            //create a session if one does not exist
            if (sessionObject == null) {
                sessionObject = new SessionObject(sessionObjectId);
            }

            //put into cache
            cache.put(new Element(sessionObjectId, sessionObject));
            return sessionObject;
        }

    }

    public static void save(SessionObject sessionObject) throws Exception {
        UserSessionDao usd = ServiceLocatorHolder.getServiceLocator().getSystemContext().getServiceLocator().getUserSessionDao();
        usd.createOrUpdateUserSession(sessionObject);
    }

    public static void closeSession(String sessionObjectId) throws Exception {
        Ehcache cache = ServiceLocatorHolder.getServiceLocator().getCacheManager().getCache("SessionObject");
        //get from cache
        Element element = cache.get(sessionObjectId);
        SessionObject sessionObject = null;
        if (element != null) {
            sessionObject = (SessionObject) element.getObjectValue();
        }

        if (sessionObject != null) {
            UserSessionDao usd = ServiceLocatorHolder.getServiceLocator().getUserSessionDao();
            usd.createOrUpdateUserSession(sessionObject);
            cache.remove(sessionObjectId); //remove out of cache
            sessionObject = null;
        }
    }

    public static void clearSession(String sessionObjectId) throws Exception {
        Ehcache cache = ServiceLocatorHolder.getServiceLocator().getCacheManager().getCache("SessionObject");
        //get from cache
        Element element = cache.get(sessionObjectId);
        SessionObject sessionObject = null;
        if (element != null) {
            sessionObject = (SessionObject) element.getObjectValue();
        }

        if (sessionObject != null) {
            UserSessionDao usd = ServiceLocatorHolder.getServiceLocator().getUserSessionDao();
            sessionObject.setOrder(null);
            usd.createOrUpdateUserSession(sessionObject);
        }
    }

}
