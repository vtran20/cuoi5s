package com.easysoft.ecommerce.web.filter;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * A CookieLocaleResolver class which always return null instead of default locale.
 */
public class NoDefaultCookieLocaleResolver extends CookieLocaleResolver {
    @Override
    public void setDefaultLocale(Locale defaultLocale) {
        throw new UnsupportedOperationException();
    }
    @Override
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        return null;
    }
}
