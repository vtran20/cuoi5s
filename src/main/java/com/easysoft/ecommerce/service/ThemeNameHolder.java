package com.easysoft.ecommerce.service;

import org.springframework.core.NamedThreadLocal;

public class ThemeNameHolder {

    private static final ThreadLocal<String> themeNameHolder = new NamedThreadLocal<String>("ThemeName");

    public static void resetThemeName() {
        themeNameHolder.remove();
    }

    public static void setThemeName(String themeName) {
        themeNameHolder.set(themeName);
    }

    public static String getThemeName() {
        String themeName = themeNameHolder.get();
        return themeName;
    }
}
