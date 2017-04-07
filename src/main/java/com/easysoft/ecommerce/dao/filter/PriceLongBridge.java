package com.easysoft.ecommerce.dao.filter;

import org.hibernate.search.bridge.ParameterizedBridge;
import org.hibernate.search.bridge.StringBridge;
import org.hibernate.search.bridge.TwoWayStringBridge;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: vtran
 * Date: Sep 20, 2010
 * Time: 10:53:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceLongBridge implements TwoWayStringBridge, ParameterizedBridge {

    public static String PADDING_PROPERTY = "padding";
    private int padding = 10; //default


    @Override
    public void setParameterValues(Map parameters) {
        Object padding = parameters.get( PADDING_PROPERTY );
        if (padding != null) this.padding = Integer.valueOf((String) padding) ;
    }

    @Override
    public String objectToString(Object object) {
        String rawLong = ( (Long) object ).toString();
        if (rawLong.length() > padding) throw new IllegalArgumentException( "Try to pad on a number too big" );
        StringBuilder paddedLong = new StringBuilder( );
        for ( int padIndex = rawLong.length() ; padIndex < padding ; padIndex++ ) {
            paddedLong.append('0');
        }
        return paddedLong.append( rawLong ).toString();
    }

    public Object stringToObject(String stringValue) {
        return new Long(stringValue);
    }

}
