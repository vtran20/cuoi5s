package com.easysoft.ecommerce.web.filter;

import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.service.*;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.beans.propertyeditors.TimeZoneEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RequestContextFilter extends OncePerRequestFilter {

    public static final String VERSION_SERVLET_CONTEXT_ATTR = "version";

    @Override
    protected void initFilterBean() throws ServletException {
        getServletContext().setAttribute(VERSION_SERVLET_CONTEXT_ATTR, Long.toString(new Date().getTime()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1: RequestContextHolder: to expose request for callers
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes, false);
        logger.debug("Initialized RequestContextHolder.");

        // 2: BrowserURIHolder
        BrowserURIHolder.setURI(request.getRequestURI());

        WebApplicationContext rootApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        ServiceLocator serviceLocator = ServiceLocatorHolder.getServiceLocator();

        // 3: LocaleContextHolder: to expose LocaleContext to callers, like Spring Security
        String newLocale = request.getParameter(LocaleChangeInterceptor.DEFAULT_PARAM_NAME); // "locale"
        LocaleResolver localeResolver = (LocaleResolver) rootApplicationContext.getBean(DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME);
        SiteService siteService = serviceLocator.getSiteService();
        ConfigurationService configurationService = serviceLocator.getConfigurationService();
        Locale locale = null;
        if (newLocale != null) {
            // Handle event when user changing locale manually (using request parameter)
            LocaleEditor localeEditor = new LocaleEditor();
            localeEditor.setAsText(newLocale);
            locale = (Locale) localeEditor.getValue();
            localeResolver.setLocale(request, response, locale);
            logger.debug("User changed locale to " + newLocale);
        } else {
            // Determine web site locale (using database)
            Site site = serviceLocator.getSystemContext().getSite();
            if (site != null) {
                locale = siteService.getSiteLocale(site);
            }
            if (locale != null) {
                localeResolver.setLocale(request, response, locale);
                logger.debug("No user locale found in cookies, will use locale from database: " + locale);
            } else {
                locale = LocaleUtils.toLocale(configurationService.getDefaultLocale());
                localeResolver.setLocale(request, response, locale);
                logger.debug("User default locale vi_VN: " + locale);
            }
        }
        LocaleContextHolder.setLocale(locale, false);
        logger.debug("Initialized LocaleContextHolder: " + locale);

        // 4: URLREWRITE FILTER: Set user locale value into a request attribute to be used by urlrewritefilter rules
        request.setAttribute("locale", locale.toString());

        // 5: time zone
        String newTimeZone = request.getParameter("timeZone");
        NoDefaultCookieTimeZoneResolver timeZoneResolver =
                (NoDefaultCookieTimeZoneResolver) rootApplicationContext.getBean(TimeZoneResolver.TIME_ZONE_RESOLVER_BEAN_NAME);
        TimeZone timeZone;
        if (newTimeZone != null) {
            // Handle event when user changing time zone manually (using request parameter)
            TimeZoneEditor timeZoneEditor = new TimeZoneEditor();
            timeZoneEditor.setAsText(newTimeZone);
            timeZone = (TimeZone) timeZoneEditor.getValue();
            timeZoneResolver.setTimeZone(request, response, timeZone);
            logger.debug("User changed time zone to " + newTimeZone);
        } else {
            // Determine user timeZone (using cookie)
            timeZone = timeZoneResolver.resolveTimeZone(request);
            if (timeZone == null) {
                // Determine web site time zone (using database)
                Site site = serviceLocator.getSystemContext().getSite();
                if (site != null) {
                    timeZone = siteService.getSiteTimeZone(site);
                }
                if (timeZone == null) {
                    // get default time zone from config.properties file
                    timeZone = TimeZone.getTimeZone(configurationService.getDefaultTimeZone());
                }
                timeZoneResolver.setTimeZone(request, response, timeZone);
                logger.debug("No user time zone found in cookies, will use time zone from database: " + timeZone);
            } else {
                logger.debug("Detected user time zone from cookies: " + timeZone);
            }
        }
        TimeZoneHolder.setTimeZone(timeZone);
        logger.debug("Initialized TimeZoneHolder: " + timeZone);

        // 6: Theme name
        String newTheme = request.getParameter(ThemeChangeInterceptor.DEFAULT_PARAM_NAME); // "theme"
        ThemeResolver themeResolver = (ThemeResolver) rootApplicationContext.getBean(DispatcherServlet.THEME_RESOLVER_BEAN_NAME);
        String themeName = null;
        if (newTheme != null) {
            // Handle event when user changing theme manually (using request parameter)
            themeResolver.setThemeName(request, response, newTheme);
            themeName = newTheme;
            logger.debug("User changed theme to " + newTheme);
        } else {
            // Determine web site theme (using database)
            Site site = serviceLocator.getSystemContext().getSite();
            if (site != null) {
                themeName = siteService.getSiteTheme(site);
            }
            if (themeName == null) {
                // get default theme from config.properties file
                themeName = configurationService.getDefaultTheme();
            }
            logger.debug("No user theme found in cookies, will use theme from database: " + themeName);
            themeResolver.setThemeName(request, response, themeName);
        }
        ThemeNameHolder.setThemeName(themeName);

        // 7: JSTL: Set various JSTL config values to be used by JSTL tags later (for non-spring mvc jsp pages)
        Config.set(request, Config.FMT_LOCALE, locale);
        Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, configurationService.getMessageBundle());
        Config.set(request, Config.FMT_TIME_ZONE, timeZone);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 6: theme
            ThemeNameHolder.resetThemeName();

            // 5: time zone
            TimeZoneHolder.resetTimeZone();

            // 3:
            LocaleContextHolder.resetLocaleContext();
            // 2:
            BrowserURIHolder.resetURIHolder();
            // 1:
            RequestContextHolder.resetRequestAttributes();
            attributes.requestCompleted();
        }
    }
}
