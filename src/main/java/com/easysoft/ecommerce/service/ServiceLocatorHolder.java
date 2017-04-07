package com.easysoft.ecommerce.service;


/**
 * Simple holder class that allows to access an instance of ServiceLocator.
 */
public abstract class ServiceLocatorHolder {
    private static ServiceLocator serviceLocatorHolder;

    /**
     * Reset the ServiceLocator for the current thread.
     */
    public static void resetServiceLocator() {
        serviceLocatorHolder = null;
    }

    /**
     * Associate the given ServiceLocator with the current thread, <i>not</i>
     * exposing it as inheritable for child threads.
     *
     * @param serviceLocator
     *            the current ServiceLocator, or <code>null</code> to reset the thread-bound context
     */
    public static void setServiceLocator(ServiceLocator serviceLocator) {
        serviceLocatorHolder = serviceLocator;
    }

    /**
     * Return the ServiceLocator associated with the current thread, if any.
     *
     * @return the current ServiceLocator, or <code>null</code> if none
     */
    public static ServiceLocator getServiceLocator() {
        ServiceLocator ServiceLocator = serviceLocatorHolder;
        return ServiceLocator;
    }
}
