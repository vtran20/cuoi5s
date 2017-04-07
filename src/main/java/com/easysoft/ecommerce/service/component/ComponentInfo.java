package com.easysoft.ecommerce.service.component;

import java.util.List;

/**
 * Contain information about component.
 * 
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ComponentInfo {

    private String name;
    private String conponentInterface;
    private List <String>componentClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConponentInterface() {
        return conponentInterface;
    }

    public void setConponentInterface(String conponentInterface) {
        this.conponentInterface = conponentInterface;
    }

    public List <String>getComponentClass() {
        return componentClass;
    }

    public void setComponentClass(List <String> componentClass) {
        this.componentClass = componentClass;
    }
}
