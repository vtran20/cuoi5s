package com.easysoft.ecommerce.web.filter;

import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.TimeZoneHolder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;

public class ErrorPageRequestContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        WebApplicationContext rootApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

        // Simulate DispatcherServlet behavior for error pages
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootApplicationContext);
        LocaleResolver localeResolver = (LocaleResolver) rootApplicationContext.getBean(DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME);
        request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
        ThemeResolver themeResolver = rootApplicationContext.getBean(DispatcherServlet.THEME_RESOLVER_BEAN_NAME, ThemeResolver.class);
        request.setAttribute(DispatcherServlet.THEME_RESOLVER_ATTRIBUTE, themeResolver);
        request.setAttribute(DispatcherServlet.THEME_SOURCE_ATTRIBUTE, rootApplicationContext);

        // JSTL: Set various JSTL config values to be used by JSTL tags later (for non-spring mvc jsp pages)
        Config.set(request, Config.FMT_LOCALE, LocaleContextHolder.getLocale());
        Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, ServiceLocatorHolder.getServiceLocator().getConfigurationService().getMessageBundle());
        Config.set(request, Config.FMT_TIME_ZONE, TimeZoneHolder.getTimeZone());


        /*Fix error page: http://old.nabble.com/Error-and-404-page-decoration-workaround-td30203662.html*/
        request.removeAttribute("com.opensymphony.sitemesh.APPLIED_ONCE");

        filterChain.doFilter(request, response);
    }
}
