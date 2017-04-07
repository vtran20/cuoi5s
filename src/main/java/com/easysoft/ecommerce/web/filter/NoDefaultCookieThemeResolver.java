package com.easysoft.ecommerce.web.filter;

import org.springframework.web.servlet.theme.CookieThemeResolver;

/**
 * A CookieLocaleResolver class which always return null instead of default locale.
 */
public class NoDefaultCookieThemeResolver extends CookieThemeResolver {
    @Override
    public void setDefaultThemeName(String defaultThemeName) {
        throw new UnsupportedOperationException();
    }
    @Override
    public String getDefaultThemeName() {
        return null;
    }
}
