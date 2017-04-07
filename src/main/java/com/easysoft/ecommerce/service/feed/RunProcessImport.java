package com.easysoft.ecommerce.service.feed;

import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.component.Component;
import com.easysoft.ecommerce.service.component.ComponentInfo;
import com.easysoft.ecommerce.service.component.ComponentUtil;
import com.easysoft.ecommerce.util.Messages;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunProcessImport {

    public void runProcessComponent(String name, Messages errors) throws Exception {
        if (!ComponentUtil.commerceComponent.isEmpty() && !StringUtils.isEmpty(name)) {
            ComponentInfo componentInfo = ComponentUtil.commerceComponent.get(name);
            if (componentInfo != null) {
                List<String> classes = componentInfo.getComponentClass();
                if (classes != null) {
                    for (String str : classes) {
                        if (!StringUtils.isEmpty(str)) {
                            Class c = Class.forName(str);
                            Component component = (Component) c.newInstance();
//                            component.execute(sessionObject, errors);
                        }
                    }
                }
            }
        }
    }

}
