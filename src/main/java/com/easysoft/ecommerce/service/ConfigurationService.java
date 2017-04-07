package com.easysoft.ecommerce.service;

public interface ConfigurationService {
    String getDefaultLocale();
    String getDefaultTimeZone();
    String getMessageBundle();
    String getDefaultTheme();
    String getWorkingDir();
}
