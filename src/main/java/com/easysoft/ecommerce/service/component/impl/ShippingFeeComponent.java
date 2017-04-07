package com.easysoft.ecommerce.service.component.impl;

import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.component.CommerceSupport;
import com.easysoft.ecommerce.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingFeeComponent extends CommerceSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingFeeComponent.class);
    private int WEIGHT_FOR_PACK = 200;
    private int OIL_FEE_INTERNAL=10;
    private int OIL_FEE_EXTERNAL=15;
    private int VAT_FEE=10;

    @Override
    public void execute(SessionObject sessionObject, Messages errors) throws Exception {
        if (sessionObject != null) {
            List<ItemMap> items = sessionObject.getOrder().getItems();
            Integer weightTotal = 0;
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            if (items != null) {
                ShippingSite shippingSite = ServiceLocatorHolder.getServiceLocator().getShippingSiteDao().findUniqueBy(null, null, site.getId());
                Long shippingPriceTotal = 0l;
                if (shippingSite != null) {
                    //shipping price static for entire order
                    if ("Y".equals(shippingSite.getUsePriceBySite())) {
                        shippingPriceTotal = shippingSite.getPriceBySite();
                    } else if ("Y".equals(shippingSite.getUsePriceByProduct())) {
                        for (ItemMap item : items) {
                            /*
                            Calculate shipping fee base on each item. TODO: Right now, don't care about quantity

                            */
                            if (shippingPriceTotal == 0) {
                                shippingPriceTotal = shippingSite.getPriceByProduct();
                            } else {
                                shippingPriceTotal += shippingSite.getPercentOfFirstProduct()*Math.round(shippingSite.getPriceByProduct())/100;
                            }
                        }
                    }
                    sessionObject.getOrder().setShippingPrice(shippingPriceTotal);
                }

//                for (ItemMap item : items) {
//                    /*
//                    Calculate shipping fee base on each item.
//                    */
//                    weightTotal += item.getWeight()*item.getQuantity();
//                }
//
//                //Add weight for packing.
//                weightTotal += WEIGHT_FOR_PACK;
//
//                ShippingFee shippingFee = ServiceLocatorHolder.getServiceLocator().getShippingFeeDao().getShippingFee(weightTotal, sessionObject.getOrder().getShippingMethod());
//                ShippingLocation shippingLocation = ServiceLocatorHolder.getServiceLocator().getShippingLocationDao().getShippingLocation("HCM", sessionObject.getAddresses().getShippingAddress().getCity(), sessionObject.getOrder().getShippingMethod());
//
//                //Get price of shipping
//                Integer shippingPrice = 0;
//                if (shippingLocation != null && shippingFee != null) {
//                    shippingPrice = getShippingPrice(shippingFee, shippingLocation);
//
//                    //Special case. If weight > 2000
//                    if (weightTotal > 2000) {
//                        ShippingFee shippingFee2000 = ServiceLocatorHolder.getServiceLocator().getShippingFeeDao().getShippingFee(2000, sessionObject.getOrder().getShippingMethod());
//                        //price for 2000g
//                        shippingPrice = getShippingPrice(shippingFee2000, shippingLocation);
//                        //price for the rest weight
//                        Integer theRestWeight = weightTotal - 2000;
//                        int priceForTheRest = (((int)Math.ceil(theRestWeight/500) + 1)*getShippingPrice(shippingFee, shippingLocation));
//                        shippingPrice += priceForTheRest;
//                    }
//
//                    //Add VAT
//                    Integer vat = (int)Math.ceil(((VAT_FEE*shippingPrice)/100));
//                    //Add oil fee
//                    String areaCode = shippingLocation.getAreaCode();
//                    int oilPercent = 10;
//                    if ("INTERNAL".equalsIgnoreCase(areaCode)) {
//                        oilPercent = OIL_FEE_INTERNAL;
//                    } else {
//                        oilPercent = OIL_FEE_EXTERNAL;
//                    }
//                    Integer oil = (int)Math.ceil((oilPercent*shippingPrice)/100);
//                    shippingPrice = shippingPrice + vat + oil;
//
//                    if (shippingPrice > 0) {
//                        sessionObject.getOrder().setShippingPrice(Long.valueOf(shippingPrice));
//                    } else {
//                        //Default value
//                        sessionObject.getOrder().setShippingPrice(30000l);
//                    }
//
//                } else {
//                    errors.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.delivery.invalid", null, LocaleContextHolder.getLocale()));
//                }
            }
        }
    }

    private Integer getShippingPrice (ShippingFee shippingFee, ShippingLocation shippingLocation) {
        Integer shippingPrice = 0;

        if (shippingLocation != null && shippingFee != null) {
            String areaCode = shippingLocation.getAreaCode();
            if ("AREA1".equalsIgnoreCase(areaCode)) {
                shippingPrice = shippingFee.getArea1Fee();
            } else if ("AREA2".equalsIgnoreCase(areaCode)) {
                shippingPrice = shippingFee.getArea2Fee();
            } else if ("AREA3".equalsIgnoreCase(areaCode)) {
                shippingPrice = shippingFee.getArea3Fee();
            } else if ("AREA4".equalsIgnoreCase(areaCode)) {
                shippingPrice = shippingFee.getArea4Fee();
            } else if ("INTERNAL".equalsIgnoreCase(areaCode)) {
                shippingPrice = shippingFee.getInternalFee();
            }
        }
        return  shippingPrice;
    }
}
