package com.easysoft.ecommerce.service.promotion;

import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.PromotionClass;
import com.easysoft.ecommerce.model.User;
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
public interface Award {
    void init() throws Exception;
    String execute (SessionObject sessionObject, User user, Promotion promotion) throws Exception;
    void destroy();
}
