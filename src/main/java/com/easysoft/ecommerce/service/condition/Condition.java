package com.easysoft.ecommerce.service.condition;

import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocator;

/**
 * Created by IntelliJ IDEA.
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Condition {
    void init() throws Exception;
    String execute(SessionObject sessionObject, ItemMap item, User user, String expression) throws Exception;
    void destroy();
}
