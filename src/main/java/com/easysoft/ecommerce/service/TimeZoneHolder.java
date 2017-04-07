package com.easysoft.ecommerce.service;

import java.util.TimeZone;

import org.springframework.core.NamedThreadLocal;

public class TimeZoneHolder {

    private static final ThreadLocal<TimeZone> timeZoneHolder = new NamedThreadLocal<TimeZone>("Time zone");

    public static void resetTimeZone() {
        timeZoneHolder.remove();
    }

    public static void setTimeZone(TimeZone timeZone) {
        timeZoneHolder.set(timeZone);
    }

    public static TimeZone getTimeZone() {
        TimeZone timeZone = timeZoneHolder.get();
        return timeZone;
    }
}
