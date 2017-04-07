package com.easysoft.ecommerce.web.filter;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TimeZoneResolver {
    public static final String TIME_ZONE_RESOLVER_BEAN_NAME = "timeZoneResolver";

    TimeZone resolveTimeZone(HttpServletRequest request);
    void setTimeZone(HttpServletRequest request, HttpServletResponse response, TimeZone timeZone);
}
