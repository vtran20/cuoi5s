package com.easysoft.ecommerce.controller.validator;

import com.easysoft.ecommerce.controller.AddToCartForm;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InventoryValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AddToCartForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddToCartForm form = (AddToCartForm) target;
        try {
            Product product = ServiceLocatorHolder.getServiceLocator().getProductDao().getProduct(form.getProductId());
            ProductVariant productVariant = ServiceLocatorHolder.getServiceLocator().getProductVariantDao().findById(form.getProductVariantId());
            if (form.getQuantity() > productVariant.getInventory()) {
                String arr[] = {String.valueOf(productVariant.getInventory()), product.getName(), product.getModel()};
                errors.rejectValue("quantity", "message.quantity.lager", arr, ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.quantity.lager", arr, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            errors.rejectValue("quantity", "message.quantity.empty");
        }

        
    }
}
