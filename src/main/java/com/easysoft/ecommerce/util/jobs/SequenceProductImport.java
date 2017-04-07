package com.easysoft.ecommerce.util.jobs;

import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.dao.RelatedProductDao;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductToProduct;
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
 * Sequence product import
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class SequenceProductImport {

    public static int PRODUCT_A = 0;
    public static int SEQUENCE_B = 1;

    List<String> messages = new ArrayList<String>();

    ProductDao productDao;
    String sequenceImportFile;



    public SequenceProductImport(ProductDao productDao, String sequenceImportFile) {
        this.productDao = productDao;
        this.sequenceImportFile = sequenceImportFile;
    }

    public boolean processSequenceProductImport () {
        File inputWorkbook = new File(this.sequenceImportFile);
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
                        createSequenceProduct(productDao, sheet, i);
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

        if (error.size() > 0) {
            messages.addAll(error);
            return false;
        } else {
            return true;
        }
    }
    public void createSequenceProduct(ProductDao productDao, Sheet sheet, int row) throws Exception {
        String model = sheet.getCell(PRODUCT_A, row).getContents();
        Product product = productDao.findUniqueBy("model", model);
        if (product != null) {
            String sequence = sheet.getCell(SEQUENCE_B, row).getContents();
            if (!StringUtils.isEmpty(sequence) && StringUtils.isNumeric(sequence)) {
                product.setSequence(Integer.valueOf(sequence));
            } else {
                product.setSequence(999999);
            }
            productDao.merge(product);
        }
    }

    public List<String> getMessages() {
        return messages;
    }

}
