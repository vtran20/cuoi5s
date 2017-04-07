package com.easysoft.ecommerce.service.impl;

import java.util.Properties;

import com.easysoft.ecommerce.service.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {
    private Properties config;

    public ConfigurationServiceImpl(Properties config) {
        this.config = config;
    }

    public String getDefaultLocale() {
        return config.getProperty("defaultLocale");
    }

    public String getDefaultTimeZone() {
        return config.getProperty("defaultTimeZone");
    }

    public String getDefaultTheme() {
        return config.getProperty("defaultTheme");
    }

    public String getMessageBundle() {
        return config.getProperty("messageBundle");
    }

    public String getWorkingDir() {
        return config.getProperty("workingDir");
    }
}
