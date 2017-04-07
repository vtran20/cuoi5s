package com.easysoft.ecommerce.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;

/**
 * Application Lifecycle Listener implementation class ServiceLocatorHolderSetupServletContextListener
 *
 */
public class ServiceLocatorHolderSetupServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        // 1: ServiceLocatorHolder: to expose ServiceLocator to callers
        WebApplicationContext rootApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        ServiceLocator serviceLocator = (ServiceLocator) rootApplicationContext.getBean(ServiceLocator.SERVICE_LOCATOR_BEAN_NAME);
        ServiceLocatorHolder.setServiceLocator(serviceLocator);

    }

    public void contextDestroyed(ServletContextEvent sce) {
        //update view count
        ServiceLocatorHolder.getServiceLocator().getJobsService().updateVideoStatistic();
        ServiceLocatorHolder.resetServiceLocator();
    }

}
