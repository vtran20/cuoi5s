package com.easysoft.ecommerce.service.component.impl;

import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.model.ShippingFee;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.component.CommerceSupport;
import com.easysoft.ecommerce.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class InventoryCheckingComponent extends CommerceSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryCheckingComponent.class);

    @Override
    public void execute(SessionObject sessionObject, Messages errors) throws Exception {
        if (sessionObject != null) {
            List<ItemMap> items = sessionObject.getOrder().getItems();
            List<ItemMap> emptyItems = new ArrayList<ItemMap>();
            for (ItemMap it : items) {
                if (it.getProductVariantId() != null && it.getProductVariantId() > 0) {
                    ProductVariant variant = ServiceLocatorHolder.getServiceLocator().getProductVariantDao().findById(it.getProductVariantId());
                    if (variant != null && variant.getInventory() <= 0) {
                        String arr[] = {it.getName(), it.getModelNumber()};
                        errors.addInfo(ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.variants.empty", arr, LocaleContextHolder.getLocale()));
                        emptyItems.add(it);
                    }
                }
            }
            if (!emptyItems.isEmpty()) {
                items.removeAll(emptyItems);
            }
        }
    }
}
