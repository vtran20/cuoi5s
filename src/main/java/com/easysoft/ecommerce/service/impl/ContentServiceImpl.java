package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.dao.SiteMenuPartContentDao;
import com.easysoft.ecommerce.dao.WidgetTemplateDao;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.service.ContentService;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
//@Transactional
public class ContentServiceImpl implements ContentService {

    @Autowired
    private WidgetTemplateDao widgetTemplateDao;
    @Autowired
    private SiteMenuPartContentDao siteMenuPartContentDao;
    static Map <Long, Template>templates =  new HashMap<Long, Template>();

    private Template getTemplate (Long widgetTemplateId) {
        Template template = templates.get(widgetTemplateId);
        if (template == null) {
            //Load content template
            WidgetTemplate widgetTemplate = widgetTemplateDao.findById(widgetTemplateId);
            if (widgetTemplate != null) {
                //Create Velocity Template
                RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
                StringReader reader = new StringReader(widgetTemplate.getContent());
                SimpleNode node = null;
                try {
                    node = runtimeServices.parse(reader, widgetTemplateId+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                template = new Template();
                template.setRuntimeServices(runtimeServices);
                template.setData(node);
                template.initDocument();
                template.setEncoding("UTF-8");
                templates.put(widgetTemplateId, template);
            }
        }

        return template;
    }

    private Template getTemplate (WidgetTemplate widgetTemplate) {
        if (widgetTemplate != null) {
            Template template = templates.get(widgetTemplate.getId());
            if (template == null) {
                //Create Velocity Template
                RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
                StringReader reader = new StringReader(widgetTemplate.getContent());
                SimpleNode node = null;
                try {
                    node = runtimeServices.parse(reader, widgetTemplate.getId()+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                template = new Template();
                template.setRuntimeServices(runtimeServices);
                template.setData(node);
                template.initDocument();
                template.setEncoding("UTF-8");
                templates.put(widgetTemplate.getId(), template);
            }
            return template;
        }
        return null;
    }

    @Override
    public String merge(Map map, Long widgetTemplateId) {
        StringWriter write = new StringWriter();
        Template template = getTemplate(widgetTemplateId);
        if (template != null) {
            VelocityContext velocityContext = new VelocityContext(map);
            template.merge(velocityContext, write);
            return write.toString();
        }
        return "";
    }

//    @Override
//    public String merge(SiteMenuPartContent content) {
//        if (content != null) {
//            StringWriter write = new StringWriter();
//            WidgetTemplate widgetTemplate = siteMenuPartContentDao.getWidgetTemplate(ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite(), content.getId());
//            Template template = getTemplate(widgetTemplate);
//            if (template != null) {
//                Map<String, SiteMenuPartContent> map = new HashMap<String, SiteMenuPartContent>();
//                map.put("content", content);
//                VelocityContext velocityContext = new VelocityContext(map);
//                template.merge(velocityContext, write);
//                return write.toString();
//            }
//        }
//        return "";
//    }
    @Override
    public String merge(Row row) {
        if (row != null) {
            StringWriter write = new StringWriter();
            WidgetTemplate widgetTemplate = siteMenuPartContentDao.getWidgetTemplate(row.getId());
            List contents = null;
            Map priceMap = new HashMap();
            //Get products
            if ("product".equals(widgetTemplate.getType())) {
                contents = siteMenuPartContentDao.getProductContentParts(row.getId());
                //Calculate price/promotion for products
                Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
                if (contents != null) {
                    for (Object product: contents) {
                        Product prod = (Product) product;
                        String money = com.easysoft.ecommerce.util.MoneyRange.valueOf(prod.getDisplayPrice(), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString();
                        String promo = com.easysoft.ecommerce.util.MoneyRange.valueOf(prod.getDisplayPricePromo(), site.getSiteParamsMap().get("CURRENCY"), site.getSiteParamsMap().get("CURRENCY_FORMAT")).toString();
                        ProductFile productImage = ServiceLocatorHolder.getServiceLocator().getProductFileDao().getDefaultImage(prod.getId(), "PRODUCT_FILE_IMAGE");
                        Map temp = new HashMap();
                        temp.put("money", money);
                        temp.put("promo", promo);
                        temp.put("productImage", productImage);
                        priceMap.put(prod.getId(), temp);
                    }
                }

            } else if ("news".equals(widgetTemplate.getType())) {
                contents = siteMenuPartContentDao.getNewsContentParts(row.getId());
            } else {
                //Else get partContents
                contents = siteMenuPartContentDao.getContentParts(row.getId());
            }
            Template template = getTemplate(widgetTemplate);
            if (template != null && contents != null && contents.size() > 0) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("contents", contents);
                map.put("priceMap", priceMap);
                map.put("row", row);
                map.put("widgetTemplate", widgetTemplate);
                String imageServer = ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("image.server");
                map.put("imageServer",imageServer);
                VelocityContext velocityContext = new VelocityContext(map);
                template.merge(velocityContext, write);
                return write.toString();
            }
        }
        return "";
    }

}
