package com.easysoft.ecommerce.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class WebAuthenticationDetailsImpl extends WebAuthenticationDetails {

    private Map parameterMap;
    private String serverName;
    private String csrf; // will be generated after logging in successfully to prevent CSRF attacks

    public WebAuthenticationDetailsImpl(HttpServletRequest request) {
        super(request);
    }

    @Override
    protected void doPopulateAdditionalInformation(HttpServletRequest request) {
        this.parameterMap = request.getParameterMap();
        this.serverName = request.getServerName();
    }

    public Map getParameterMap() {
        return this.parameterMap;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setCsrf(String csrf) {
        this.csrf = csrf;
    }

    public String getCsrf() {
        return csrf;
    }
}
