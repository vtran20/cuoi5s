package com.easysoft.ecommerce.util;

import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * User: vtran
 * Date: Aug 4, 2010
 * Time: 11:44:11 AM
 *
 */
public class CodeSample {

    //Implement cache example
    public void getCache () {
        Ehcache cache = ServiceLocatorHolder.getServiceLocator().getCacheManager().getCache("SesssionObject");
        Element element = cache.get("key");
        SessionObject so = null;
        if (element != null) {
            so = (SessionObject) element.getObjectValue();
        } else {
            so = new SessionObject("key");
            cache.put(new Element("key", so));
        }
    }


}
