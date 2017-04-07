package com.easysoft.ecommerce.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class SetupRewrittenURIHolderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1: RequestContextHolder: to expose request for callers
        RewrittenURIHolder.setURI(request.getRequestURI());
        logger.debug("Initialized RewrittenRequestHolder.");

        try {
            filterChain.doFilter(request, response);
        }
        finally {
            // 1:
            RewrittenURIHolder.resetURIHolder();
        }
    }
}
