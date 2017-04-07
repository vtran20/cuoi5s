package com.easysoft.ecommerce.service.impl;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.easysoft.ecommerce.security.WebAuthenticationDetailsImpl;

@Aspect
@Component
public class CSRFProtectionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSRFProtectionAdvice.class);

    @Before("@annotation(com.easysoft.ecommerce.security.CSRFProtection) || @within(com.easysoft.ecommerce.security.CSRFProtection)")
    public void doAccessCheck() {
        WebAuthenticationDetailsImpl details = (WebAuthenticationDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String validCsrf = details.getCsrf();
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String csrf = sra.getRequest().getParameter("csrf");
        if (validCsrf != null && !validCsrf.equals(csrf)) {
            LOGGER.warn("Invalid csrf parameter value. Is this a CSRF hacking attempt?");
            //TODO: Show error message in the login page.
            sra.getRequest().getRequestDispatcher("/admin/index.html");
            throw new AccessDeniedException("Access is denied. Invalid csrf value.");
        } else {
            LOGGER.info("Valid csrf parameter value. Will allow access.");
        }

    }
}
