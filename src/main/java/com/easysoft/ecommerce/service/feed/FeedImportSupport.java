package com.easysoft.ecommerce.service.feed;

/**
 * Created by IntelliJ IDEA.
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FeedImportSupport implements FeedImport {
    public void init() throws Exception {
        //do something here if need.
    }

    public void destroy() {
        //do something here
    }

}
