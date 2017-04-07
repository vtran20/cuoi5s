package com.easysoft.ecommerce.service;

import com.easysoft.ecommerce.model.Site;

public interface SystemContext {

    public static final String SYSTEM_CONTEXT_BEAN_NAME = "systemContextImpl";

    ServiceLocator getServiceLocator();

    Site getSite();

    String getGlobalConfig(String key);

}
