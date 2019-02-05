package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.dao.UserSessionDao;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.UserSession;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.fasterxml.uuid.Generators;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an utility class that provide utility methods for handle SessionObject.
 *
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class SessionUtil {

    //Store cookie 30 days.
    private static int cookieAge = 30*24*60*60;
//    private static Map<String, SessionObject> sessionObjectMap = Collections.synchronizedMap(new HashMap<String, SessionObject>());

    /**
     * Create cookie object from cookieName
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static void createCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String value, int cookieAge) {

        Cookie sessionCookie = new Cookie(cookieName, value);
        sessionCookie.setMaxAge(cookieAge);

        // If a domain name was specified... set it.
        sessionCookie.setDomain(request.getServerName());

        // Allow the cookie to work for anything within this applications context
//        if (StringUtils.isEmpty(request.getContextPath()))
        sessionCookie.setPath("/");
//        else
//            sessionCookie.setPath(request.getContextPath());

        response.addCookie(sessionCookie);
    }
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
            String cookieValue = SessionUtil.findCookieValue(request, SessionObject.USER_SESSION_ID_KEY);
            if (cookieValue != null) {
                userSessionID = cookieValue;
            } else {
                // Look in the URL for a cookieData
                cookieValue = request.getParameter(SessionObject.USER_SESSION_ID_KEY);
                if (cookieValue != null) {
                    userSessionID = cookieValue;
                }
            }

            // Create a cookie if one does not exist
            if (userSessionID == null || StringUtils.isEmpty(userSessionID) || "null".equals(userSessionID) || "0".equals(userSessionID)) {

                userSessionID = Generators.timeBasedGenerator().generate().toString();
//                cookieData = UUIDGenerator.getUUID();
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
     * @param request  The HttpServletRequest used to get the cookie
     * @param response The HttpServletResponse used to set a new cookie
     */
    public static void setUserSession(String userSessionID, HttpServletRequest request, HttpServletResponse response) {
        Cookie sessionCookie = new Cookie(SessionObject.USER_SESSION_ID_KEY, userSessionID);
        sessionCookie.setMaxAge(cookieAge);

        // If a domain name was specified... set it.
        sessionCookie.setDomain(request.getServerName());

        // Allow the cookie to work for anything within this applications context
//        if (StringUtils.isEmpty(request.getContextPath()))
        sessionCookie.setPath("/");
//        else
//            sessionCookie.setPath(request.getContextPath());

        response.addCookie(sessionCookie);
    }

    /**
     * Sets the userSessionId for the current session
     *
     * @param cookieData The user session id to store in the  session cookie.
     * @param request  The HttpServletRequest used to get the cookie
     * @param response The HttpServletResponse used to set a new cookie
     */
    public static void setUserSessionCookie(String cookieData, HttpServletRequest request, HttpServletResponse response, boolean expiredInSession) {
        Cookie sessionCookie = new Cookie(SessionObject.USER_SESSION_SECURE_COOKIE_KEY, cookieData);
        if (expiredInSession) {
            sessionCookie.setMaxAge(-1);
        } else {
            sessionCookie.setMaxAge(cookieAge);
        }
        // If a domain name was specified... set it.
        sessionCookie.setDomain(request.getServerName());

        // Allow the cookie to work for anything within this applications context
//        if (StringUtils.isEmpty(request.getContextPath()))
        sessionCookie.setPath("/");
//        else
//            sessionCookie.setPath(request.getContextPath());

        response.addCookie(sessionCookie);
    }

    /**
     * Expire User Session Secure Cookie
     *
     * @param request  The HttpServletRequest used to get the cookie
     * @param response The HttpServletResponse used to set a new cookie
     */
    public static void expireSecureSessionCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie sessionCookie = findCookie(request, SessionObject.USER_SESSION_SECURE_COOKIE_KEY);
        if (sessionCookie != null) {
            sessionCookie.setMaxAge(0);
            sessionCookie.setValue(null);
            sessionCookie.setDomain(request.getServerName());
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
        }
    }
    /**
     * Expire User Session Secure Cookie
     *
     * @param request  The HttpServletRequest used to get the cookie
     * @param response The HttpServletResponse used to set a new cookie
     */
    public static void expireUserSessionCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie sessionCookie = findCookie(request, SessionObject.USER_SESSION_ID_KEY);
        if (sessionCookie != null) {
            sessionCookie.setMaxAge(0);
            sessionCookie.setValue(null);
            sessionCookie.setDomain(request.getServerName());
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
        }
    }

    /**
     * Returns the user session and will create one if createFlag is true.
     *
     * We should cache session object in memory with session object id is key and session object is value. This help
     * avoid loading from database a lot.
     *
     * Note: We will create new SessionObject if the user don't have
     *
     * @param request    The HttpServletRequest
     * @param response   The HttpServletResponse
     * @return The user session retrieved from persis. storage.
     */

    public static SessionObject load(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionObject userSession = null;
        //try to get the session cookie
        String sessionId = getUserSessionID(request, response);
        //get the SessionObject
        userSession = loadSessionObject (sessionId);

        return (userSession);
    }

    public static SessionObject loadSessionObject (String sessionObjectId) throws Exception {
        SessionObject so = (SessionObject) ServiceLocatorHolder.getServiceLocator().getCacheData().getCommonCache(sessionObjectId);
        if (so != null) {
            return so;
        } else {
            UserSessionDao usd = ServiceLocatorHolder.getServiceLocator().getSystemContext().getServiceLocator().getUserSessionDao();
            so = usd.getUserSession(sessionObjectId);

            //create a session if one does not exist
            if (so == null) {
                so = new SessionObject(sessionObjectId);
                //Save the first time to make sure we don't generate the new session id for user session.
                save(so);
            }

            ServiceLocatorHolder.getServiceLocator().getCacheData().addCommonCache(sessionObjectId, so);
            return so;
        }
    }

    public static void save (SessionObject sessionObject) throws Exception {
        UserSessionDao usd = ServiceLocatorHolder.getServiceLocator().getSystemContext().getServiceLocator().getUserSessionDao();
        usd.createOrUpdateUserSession(sessionObject);
    }

    /**
     * This method is called when http session timeout. We remove userSession out of sessionObjectMap and save it into database.
     * @param sessionObjectId
     * @throws Exception
     */
    public static void closeSession (String sessionObjectId) throws Exception {
        SessionObject sessionObject = (SessionObject) ServiceLocatorHolder.getServiceLocator().getCacheData().getCommonCache(sessionObjectId);
        if (sessionObject != null) {
            UserSessionDao usd = ServiceLocatorHolder.getServiceLocator().getUserSessionDao();
            usd.createOrUpdateUserSession(sessionObject);
            ServiceLocatorHolder.getServiceLocator().getCacheData().removeCommonCache(sessionObjectId);
            sessionObject = null;
        }
    }

    /**
     * This method is called when we want to remove userSession out of database. We call when user placed order and we want to remove order information of this user.
     *
     * @param sessionObjectId
     * @throws Exception
     */
    public static void clearSession (String sessionObjectId) throws Exception {
        SessionObject sessionObject = (SessionObject) ServiceLocatorHolder.getServiceLocator().getCacheData().getCommonCache(sessionObjectId);
        if (sessionObject != null) {
            UserSessionDao usd = ServiceLocatorHolder.getServiceLocator().getUserSessionDao();
            sessionObject.setOrder(null);
            usd.createOrUpdateUserSession(sessionObject);
        }
    }
    /**
     * This method is called when we want to remove userSession out of database. We call when user placed order and we want to remove order information of this user.
     *
     * @param sessionObjectId
     * @throws Exception
     */
    public static void deleteSession (String sessionObjectId) throws Exception {
        ServiceLocatorHolder.getServiceLocator().getCacheData().removeCommonCache(sessionObjectId);
        UserSessionDao usd = ServiceLocatorHolder.getServiceLocator().getUserSessionDao();
        UserSession userSession = usd.findById(sessionObjectId);
        if (userSession != null) {
            usd.remove(userSession);
        }
    }

    /**
     * Login successfully if secure cookie in sessionObject equals secure cookie from the client
     * @param request
     * @param response
     * @return
     */
    public static boolean isLoggedIn (HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = SessionUtil.findCookieValue(request, SessionObject.USER_SESSION_SECURE_COOKIE_KEY);
        SessionObject so = null;
        try {
            so = load(request, response);
            return StringUtils.isNotEmpty(cookieValue) && cookieValue.equals(so.getSecureCookie());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Login successfully if secure cookie in sessionObject equals secure cookie from the client
     * @param request
     * @param response
     * @return
     */
    public static boolean isLoggedInForRestAPI (HttpServletRequest request, HttpServletResponse response) {
        if(!StringUtils.isEmpty(request.getHeader("app-id")) && request.getHeader("app-id").equals(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getAppId())) {
            return isLoggedIn(request, response);
        }
        return false;
    }

    /**
     * Load user object
     * @param request
     * @param response
     * @return
     */
    public static User loadUser (HttpServletRequest request, HttpServletResponse response) {
        if (isLoggedIn(request, response)) {
            try {
                SessionObject so = load(request, response);
                List<User> users = ServiceLocatorHolder.getServiceLocator().getUserDao().findByUsername(so.getUserName());
                if (users != null && users.size() == 1) {
                    return users.get(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    public static User loadUser (SessionObject so) {
        List<User> users = ServiceLocatorHolder.getServiceLocator().getUserDao().findByUsername(so.getUserName());
        if (users != null && users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }


    public static List<ItemMap> getRelatedProducts (List<ItemMap> items, Long parentProductId, Integer relatedTypeId, Long siteId) {
        List<ItemMap> result = new ArrayList<ItemMap>();
        if (siteId > 0) {
            for (ItemMap item: items) {
                if (item.getLong("SITE_ID").equals(siteId) && relatedTypeId!= null && relatedTypeId.equals(item.getInteger("RELATED_TYPE_ID")) && parentProductId != null && parentProductId.equals(item.getLong("PARENT_PRODUCT_ID"))) {
                    result.add(item);
                }
            }
        } else {
            //TODO: support for customer sites later
        }
        return result;
    }
    public static ItemMap getItemMap (List<ItemMap> items, Long id, Long siteId) {
        ItemMap result = null;
        if (siteId > 0) {
            for (ItemMap item: items) {
                if (item.getLong("SITE_ID").equals(siteId) && id != null && id.equals(item.getLong(ItemMap.ITEM_ID_KEY))) {
                    result = item;
                }
            }
        } else {
            //TODO: support for customer sites later
        }
        return result;
    }

    public static void removeUserInfo(SessionObject so) {
        so.setUserName("");
        so.setUserId(0l);
        so.setSecureCookie("");
    }

    public static void setSessionObjectAttribute(SessionObject so, User user) {
        so.setUserName(user.getUsername());
        so.set("FIRST_NAME", user.getFirstName());
        so.set("LAST_NAME", user.getLastName());
        so.setUserId(user.getId());
        //set billing address
        so.getAddresses().getBillingAddress().setFirstName(user.getFirstName());
        so.getAddresses().getBillingAddress().setLastName(user.getLastName());
        so.getAddresses().getBillingAddress().setStreet(user.getAddress_1());
        so.getAddresses().getBillingAddress().setDistrict(user.getDistrict());
        so.getAddresses().getBillingAddress().setCity(user.getCity());
        so.getAddresses().getBillingAddress().setState(user.getState());
        so.getAddresses().getBillingAddress().setZipCode(user.getZipCode());
        so.getAddresses().getBillingAddress().setCountry(user.getCountry());
        so.getAddresses().getBillingAddress().setPhone(user.getPhone());
        so.getAddresses().getBillingAddress().setEmail(user.getEmail());

        //set default shipping address
        so.getAddresses().getShippingAddress().setFirstName(user.getFirstNameShipping());
        so.getAddresses().getShippingAddress().setLastName(user.getLastNameShipping());
        so.getAddresses().getShippingAddress().setStreet(user.getAddress_1Shipping());
        so.getAddresses().getShippingAddress().setDistrict(user.getDistrictShipping());
        so.getAddresses().getShippingAddress().setCity(user.getCityShipping());
        so.getAddresses().getShippingAddress().setState(user.getStateShipping());
        so.getAddresses().getShippingAddress().setZipCode(user.getZipCodeShipping());
        so.getAddresses().getShippingAddress().setCountry(user.getCountryShipping());
        so.getAddresses().getShippingAddress().setPhone(user.getPhoneShipping());
    }

    public static void updateUserSessionId (User user, HttpServletRequest request, HttpServletResponse response) {
        Cookie userSessionId = SessionUtil.findCookie(request, SessionObject.USER_SESSION_ID_KEY);
        //if user object doesn't have user_session_id
        if (StringUtils.isEmpty(user.getUserSessionId())) {
            if (userSessionId != null) {
                user.setUserSessionId(userSessionId.getValue());
            }
        } else { //if have user_session_id, then use this and replace for the current one in the browser
            SessionUtil.setUserSession(user.getUserSessionId(), request, response);
        }
    }

}
