package com.easysoft.ecommerce.web.filter;

import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;


public class NoDefaultCookieTimeZoneResolver extends CookieGenerator implements TimeZoneResolver {

    public static final String TIME_ZONE_REQUEST_ATTRIBUTE_NAME = NoDefaultCookieTimeZoneResolver.class.getName() + ".TIMEZONE";

    public static final String DEFAULT_COOKIE_NAME = NoDefaultCookieTimeZoneResolver.class.getName() + ".TIMEZONE";

    private TimeZone defaultTimeZone;


    public NoDefaultCookieTimeZoneResolver() {
        setCookieName(DEFAULT_COOKIE_NAME);
    }

    protected TimeZone getDefaultTimeZone() {
        return this.defaultTimeZone;
    }

    public TimeZone resolveTimeZone(HttpServletRequest request) {
        TimeZone timeZone = (TimeZone) request.getAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME);
        if (timeZone != null) {
            return timeZone;
        }

        // Retrieve and parse cookie value.
        Cookie cookie = WebUtils.getCookie(request, getCookieName());
        if (cookie != null) {
            timeZone = TimeZone.getTimeZone(cookie.getValue());
            if (logger.isDebugEnabled()) {
                logger.debug("Parsed cookie value [" + cookie.getValue() + "] into time zone '" + timeZone + "'");
            }
            if (timeZone != null) {
                request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, timeZone);
                return timeZone;
            }
        }

        return determineDefaultTimeZone(request);
    }

    public void setTimeZone(HttpServletRequest request, HttpServletResponse response, TimeZone timeZone) {
        if (timeZone != null) {
            // Set request attribute and add cookie.
            request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, timeZone);
            addCookie(response, timeZone.getID());
        } else {
            // Set request attribute to fallback time zone and remove cookie.
            request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, determineDefaultTimeZone(request));
            removeCookie(response);
        }
    }

    public void setDefaultTimeZone(TimeZone defaultTimeZone) {
        throw new UnsupportedOperationException();
    }

    protected TimeZone determineDefaultTimeZone(HttpServletRequest request) {
        return null;
    }
}
