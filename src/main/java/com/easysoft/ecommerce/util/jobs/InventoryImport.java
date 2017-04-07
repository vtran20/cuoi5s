package com.easysoft.ecommerce.util.jobs;

import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.dao.ProductVariantDao;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.util.WebUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Inventory import base on Model (SKU)
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class InventoryImport {

    public static int MODEL_A = 0;
    public static int SKU_B = 1;
    public static int INVENTORY_C = 2;
    public static int LOCATION_D = 3;

    List<String> messages = new ArrayList<String>();

    ProductDao productDao;
    ProductVariantDao productVariantDao;
    CategoryDao categoryDao;
    String productImportFile;



    public InventoryImport(ProductDao productDao, ProductVariantDao productVariantDao, CategoryDao categoryDao, String productImportFile) {
        this.productDao = productDao;
        this.productVariantDao = productVariantDao;
        this.categoryDao = categoryDao;
        this.productImportFile = productImportFile;
    }

    public boolean processProductImport () {
        File inputWorkbook = new File(this.productImportFile);
        if (!inputWorkbook.exists()) return true;
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Start from line 1 (line 0 is header)
            for (int i = 1; i < sheet.getRows(); i++) {
                if (isValidProduct(sheet, i)) {
                    try {
                        importInventory(productDao, productVariantDao, categoryDao, sheet, i);
                    } catch (Exception e) {
                        messages.add("Line " + (i+1) + ": Cannot Import inventory.");
                    }
                } else {
                    messages.add("Line " + (i+1) + " is invalid");
                }

            }
        } catch (BiffException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            for (String message: messages) {
                System.out.println(message);
            }
        }

        return false;
    }

    public boolean isValidProduct (Sheet sheet, int row) {
        List<String> error = new ArrayList<String>();
        if (StringUtils.isEmpty(sheet.getCell(MODEL_A, row).getContents())) {
            if (StringUtils.isEmpty(sheet.getCell(SKU_B, row).getContents())) {
                error.add("Line " + (row+1) + ": missing model number/SKU");
            }
        }
        if (StringUtils.isEmpty(sheet.getCell(INVENTORY_C, row).getContents())) {
            error.add("Line " + (row+1) + ": Inventory is empty");
        } else {
            if (!StringUtils.isNumeric(sheet.getCell(INVENTORY_C, row).getContents())) {
                error.add("Line " + (row+1) + ": Inventory is not a number");
            }
        }
        if (error.size() > 0) {
            messages.addAll(error);
            return false;
        } else {
            return true;
        }
    }
    public void importInventory(ProductDao productDao, ProductVariantDao productVariantDao, CategoryDao categoryDao, Sheet sheet, int row) throws Exception {
        String model = sheet.getCell(MODEL_A, row).getContents();
        String sku = sheet.getCell(SKU_B, row).getContents();
        String inventory = sheet.getCell(INVENTORY_C, row).getContents();
        String location = sheet.getCell(LOCATION_D, row).getContents();
        if (StringUtils.isEmpty(location)) {
            location = "";
        }
        if (!StringUtils.isEmpty(sku)) {
            //Update base on sku
            ProductVariant variant = productVariantDao.findUniqueBy("sku", sku);
            if (variant != null) {
                variant.setInventory(variant.getInventory() + Integer.valueOf(inventory));
                variant.setLocation(location);
                productVariantDao.merge(variant);
            } else {
                messages.add("Line " + (row+1) + ": cannot found product variant has sku = "+sku);
            }
        } else {
            //Update base on model
            Product product = productDao.findUniqueBy("model", model);
            if (product != null) {
                List<ProductVariant> variants = productVariantDao.findAll(product.getId(), "N");
                if (variants == null) {
                    messages.add("Line " + (row+1) + ": cannot found product variants of product model = "+model);
                } else {
                    if (variants.size() > 1) {
                        messages.add("Line " + (row+1) + ": This product of model = "+model + " has more than 1 product variant. Please use sku instead");
                    } else if (variants.size() == 0) {
                        messages.add("Line " + (row+1) + ": cannot found product variants of product model = "+model);
                    } else {//have only one product variant
                        ProductVariant variant = variants.get(0);
                        variant.setInventory(variant.getInventory() + Integer.valueOf(inventory));
                        variant.setLocation(location);
                        productVariantDao.merge(variant);
                    }
                }
            } else {
                messages.add("Line " + (row+1) + ": cannot found product has model = "+model);
            }
        }
    }

    public List<String> getMessages() {
        return messages;
    }
}
