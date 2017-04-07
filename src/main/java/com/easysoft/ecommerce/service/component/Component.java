package com.easysoft.ecommerce.service.component;

import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.util.Messages;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Component {
    void init() throws Exception;
    void execute (SessionObject sessionObject, Messages errors) throws Exception;
    void destroy();
}
