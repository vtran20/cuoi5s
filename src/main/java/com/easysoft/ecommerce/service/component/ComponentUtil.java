package com.easysoft.ecommerce.service.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * This is an utility class that provide utility methods for entire web application.
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class ComponentUtil {
    public static Map <String, ComponentInfo> commerceComponent = new HashMap<String, ComponentInfo>();

    public static void loadCommerceComponent(Document doc) {

        // Get the root element of the document, and start traversing
        Element rootElement = doc.getDocumentElement();

        if (rootElement == null) {
            throw new IllegalArgumentException("Root element of commerce component configuration resource is null.");
        }

        if (rootElement.getNodeName() == null || rootElement.getNodeName().compareTo("components") != 0) {
            throw new IllegalArgumentException("Root element of commerce component configuration is not \"components\".");
        }

        NodeList components = rootElement.getElementsByTagName("component");
        if (components != null && commerceComponent.isEmpty()) {
            for(int i=0; i<components.getLength() ; i++){
                Node componentNode = components.item(i);
                Element componentElement = (Element)componentNode;
                String name = componentElement.getAttribute("name");
                NodeList classes = componentElement.getElementsByTagName("classes");
                List <String> classCom = new ArrayList<String>();
                if (classes != null) {
                    NodeList temp = ((Element)classes.item(0)).getElementsByTagName("class");
                    for(int j=0; j<temp.getLength() ; j++){
                        Node nodeClass = temp.item(j);
                        if (nodeClass != null) {
                            String sClass = nodeClass.getFirstChild().getNodeValue();
                            if (!StringUtils.isEmpty(sClass)) {
                                classCom.add(sClass);
                            }
                        }
                    }
                }
                ComponentInfo com = new ComponentInfo();
                com.setName(name);
                com.setConponentInterface(componentElement.getAttribute("interface"));
                com.setComponentClass(classCom);

                commerceComponent.put(name, com);
            }
        }
    }

}
