package com.easysoft.ecommerce.util.jobs;

import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.dao.ProductFileDao;
import com.easysoft.ecommerce.dao.RelatedProductDao;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductToProduct;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Define constant
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class MoreImagesProductImport {

    public static int PRODUCT_A = 0;
    public static int NUMBER_IMAGES_B = 1;

    List<String> messages = new ArrayList<String>();

    ProductDao productDao;
    ProductFileDao productFileDao;
    String moreImagesImportFile;



    public MoreImagesProductImport(ProductDao productDao, ProductFileDao productFileDao, String moreImagesImportFile) {
        this.productDao = productDao;
        this.productFileDao = productFileDao;
        this.moreImagesImportFile = moreImagesImportFile;
    }

    public boolean processMoreImageProductImport() {
        File inputWorkbook = new File(this.moreImagesImportFile);
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
                        createMoreImageProduct(productDao, productFileDao, sheet, i);
                    } catch (Exception e) {
                        messages.add("Line " + (i+1) + ": Cannot create or update related proudct:"+e.getMessage());
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
        if (StringUtils.isEmpty(sheet.getCell(PRODUCT_A, row).getContents())) {
            error.add("Line " + (row+1) + ": product is missing model number");
        }
        if (StringUtils.isEmpty(sheet.getCell(NUMBER_IMAGES_B, row).getContents())) {
            error.add("Line " + (row+1) + ": Number of images cannot empty");
        }
        if (!StringUtils.isNumeric(sheet.getCell(NUMBER_IMAGES_B, row).getContents())) {
            error.add("Line " + (row+1) + ": Number of images have been a numeric");
        }

        if (error.size() > 0) {
            messages.addAll(error);
            return false;
        } else {
            return true;
        }
    }
    public void createMoreImageProduct(ProductDao productDao, ProductFileDao productFileDao, Sheet sheet, int row) throws Exception {
        String model = sheet.getCell(PRODUCT_A, row).getContents();
        Product product = productDao.findUniqueBy("model", model);
        if (product != null) {
            String numberImages = sheet.getCell(NUMBER_IMAGES_B, row).getContents();
            if (StringUtils.isNumeric(numberImages)) {

                //remove all from more image of product
                productFileDao.deleteProductFiles(product.getId(), "MORE_IMAGE");

                productFileDao.addMoreImages (product, Integer.valueOf(numberImages));
            }
        } else {
            messages.add("Line " + (row+1) + ": Cannot find the product with the model " + model);
        }

    }


    public List<String> getMessages() {
        return messages;
    }

}
