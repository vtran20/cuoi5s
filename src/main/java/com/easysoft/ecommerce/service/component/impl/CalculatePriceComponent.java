package com.easysoft.ecommerce.service.component.impl;

import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.component.CommerceSupport;
import com.easysoft.ecommerce.util.Messages;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CalculatePriceComponent extends CommerceSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatePriceComponent.class);

    @Override
    public void execute(SessionObject sessionObject, Messages errors) throws Exception {
        if (sessionObject != null) {
            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            List<ItemMap> items = sessionObject.getOrder().getItems();
            if (items != null) {
                long subTotal = 0;
                long originalSubTotal = 0;
                for (Iterator<ItemMap> iterator = items.iterator(); iterator.hasNext();) {
                    ItemMap item = iterator.next();
                    /*
                    We need to update price of product to make sure the price is the last one. Avoiding some case,
                    users add items to cart and in that time, the items have a discount price. But now, the discount
                    is expired
                    */
//                    Product product = ServiceLocatorHolder.getServiceLocator().getProductDao().getProduct(item.getId());
                    if (item.getQuantity() != null) {
                        if (item.getProductVariantId() != null && item.getProductVariantId() > 0) {
                            ProductVariant productVariant = ServiceLocatorHolder.getServiceLocator().getProductVariantDao().findById(item.getProductVariantId());
                            if (/*product != null && */productVariant != null) {
    //                        item.setName(product.getName());
    //                        item.setColor(productVariant.getColorName());
    //                        item.setSize(productVariant.getSizeName());
                                item.setPrice(productVariant.getPrice());
                                item.setPriceDiscount(productVariant.getPricePromo());
                                item.setFinalPriceItem(productVariant.getPricePromo() > 0 ? productVariant.getPricePromo() : productVariant.getPrice());
                            }
                            subTotal += (item.getQuantity() * item.getFinalPriceItem()) - (item.getQuantity() * item.getPriceItemPromoDiscount());
                            originalSubTotal += (item.getQuantity() * item.getFinalPriceItem());
                        }
                    } else {
                        iterator.remove();//remove item doesn't have quantity
                    }
                }
                sessionObject.getOrder().setSubPriceTotal(subTotal);
                sessionObject.getOrder().setOriginalSubTotal(originalSubTotal);

                //Calculate tax base on subTotal
                String vatTax = site.getSiteParam("VAT_TAX");
                Float vat = 0f;
                if (StringUtils.isNotEmpty(vatTax)) {
                    vatTax = vatTax.replaceAll("%","");
                    vat = Float.parseFloat(vatTax);
                }
                Long totalTax = Math.round(subTotal*(vat/100d));
                sessionObject.getOrder().setTaxPrice(totalTax);
                Long subPriceDiscountTotal = sessionObject.getOrder().getSubPriceDiscountTotal();
                Long shippingPrice = sessionObject.getOrder().getShippingPrice();
                Long shippingPriceDiscount = sessionObject.getOrder().getShippingDiscountPrice();
                Long tax = sessionObject.getOrder().getTaxPrice();
                sessionObject.getOrder().setTotalPrice(subTotal - subPriceDiscountTotal + shippingPrice - shippingPriceDiscount + tax);
            }
        }
    }
}
