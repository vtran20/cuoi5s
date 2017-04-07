package com.easysoft.ecommerce.util;

import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductVariant;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class support in case the product variants support both color and size.
 *
 * User: Vu Tran
 * Date: Aug 4, 2010
 * Time: 11:44:11 AM
 *
 */
public class ProductVariantSelect {

    private Map<String, List<ProductVariant>> color = null;

    public ProductVariantSelect (List<ProductVariant> productVariants, Product product) {
        if (productVariants != null && !productVariants.isEmpty() && productVariants.size() > 1) {
            String previousColor = "";
            List<ProductVariant> temp = null;
            for (ProductVariant pv: productVariants) {
                String colorCode = pv.getColorCode();
                if (previousColor.equals(colorCode)) {
                    if (temp != null) {
                        temp.add(pv);
                    }
                } else {
                    temp = new ArrayList<ProductVariant>();
                    temp.add(pv);
                    color.put(colorCode, temp);
                }
                previousColor = colorCode;
            }
        }
    }

    public Map<String, List<ProductVariant>> getColor() {
        return color;
    }
}
