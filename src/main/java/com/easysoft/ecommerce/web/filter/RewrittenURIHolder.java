package com.easysoft.ecommerce.web.filter;

import org.springframework.core.NamedThreadLocal;

public abstract class RewrittenURIHolder {
    private static final ThreadLocal<String> uriHolder =
        new NamedThreadLocal<String>("RewrittenURIHolder");

    /**
     * Reset the rewritten request for the current thread.
     */
    public static void resetURIHolder() {
        uriHolder.set(null);
    }

    public static void setURI(String uri) {
        uriHolder.set(uri);
    }

    public static String getURI() {
        String uri = uriHolder.get();
        return uri;
    }

}
