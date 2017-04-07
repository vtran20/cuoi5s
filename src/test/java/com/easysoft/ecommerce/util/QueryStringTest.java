package com.easysoft.ecommerce.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * QueryString Tester.
 *
 * @author <Authors name>
 * @since <pre>09/18/2010</pre>
 * @version 1.0
 */
public class QueryStringTest extends TestCase {
    public QueryStringTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetParams() throws Exception {
        QueryString query = new QueryString("abc=1&categoryId=9");
        assertEquals(query.add("categoryId","10").toString(),"abc=1&categoryId=10");
        query = new QueryString("abc=1&categoryId=9");
        assertEquals(query.addAndKeepOrgParam("categoryId","10").toString(),"abc=1&categoryId=9&categoryId=10");
        query = new QueryString("");
        assertEquals(query.add("categoryId","10").toString(),"categoryId=10");
        assertEquals(query.addAndKeepOrgParam("categoryId","11").toString(),"categoryId=10&categoryId=11");
        assertEquals(query.add("categoryId","10").toString(),"categoryId=10");
        assertEquals(query.addAndKeepOrgParam("categoryId","10").toString(),"categoryId=10");
        QueryString query1 = (QueryString) query.clone();
        assertEquals(query.addAndKeepOrgParam("categoryId","11").toString(),"categoryId=10&categoryId=11");
        assertEquals(query1.toString(),"categoryId=10");

        query = new QueryString (      "refinement=price&price=0-100000&refinement=productVariant.color&productVariant.color=BLUE");
        assertEquals(query.toString(), "refinement=price&refinement=productVariant.color&price=0-100000&productVariant.color=BLUE");
        query1 = (QueryString) query.clone();
        assertEquals(query.toString(), query1.toString());


    }

    public static Test suite() {
        return new TestSuite(QueryStringTest.class);
    }
}
