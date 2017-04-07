package com.easysoft.ecommerce.util.jobs;

import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.dao.RelatedProductDao;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductToProduct;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.util.WebUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

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
public class RelatedProductImport {

    public static int PRODUCT_A = 0;
    public static int RELATED_B = 1;
    public static int RELATED_C = 2;
    public static int RELATED_D = 3;
    public static int RELATED_E = 4;
    public static int RELATED_F = 5;
    public static int RELATED_G = 6;

    List<String> messages = new ArrayList<String>();

    ProductDao productDao;
    RelatedProductDao relatedProductDao;
    String relatedProductImportFile;



    public RelatedProductImport(ProductDao productDao, RelatedProductDao relatedProductDao, String relatedProductImportFile) {
        this.productDao = productDao;
        this.relatedProductDao = relatedProductDao;
        this.relatedProductImportFile = relatedProductImportFile;
    }

    public boolean processRelatedProductImport () {
        File inputWorkbook = new File(this.relatedProductImportFile);
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
                        createRelatedProduct(productDao, relatedProductDao, sheet, i);
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
    public void createRelatedProduct(ProductDao productDao, RelatedProductDao relatedProductDao,Sheet sheet, int row) throws Exception {
        String model = sheet.getCell(PRODUCT_A, row).getContents();
        Product product = productDao.findUniqueBy("model", model);
        if (product != null) {

            //remove all from related product
            relatedProductDao.removeByProductId(product.getId());

            Product relatedProduct = null;
            String relatedModel = sheet.getCell(RELATED_B, row).getContents();
            if (!StringUtils.isEmpty(relatedModel)) {
                relatedProduct = productDao.findUniqueBy("model", relatedModel);
                // add related product
                if (relatedProduct != null) {
                    ProductToProduct related = new ProductToProduct();
                    related.setProduct(product);
                    related.setRelateProduct(relatedProduct);
                    related.setCreatedDate(new Date());
                    //product related
                    related.setRelationType(1);
                    relatedProductDao.persist(related);
                }
            }

            relatedModel = sheet.getCell(RELATED_C, row).getContents();
            if (!StringUtils.isEmpty(relatedModel)) {
                relatedProduct = productDao.findUniqueBy("model", relatedModel);
                // add related product
                if (relatedProduct != null) {
                    ProductToProduct related = new ProductToProduct();
                    related.setProduct(product);
                    related.setRelateProduct(relatedProduct);
                    related.setCreatedDate(new Date());
                    //product related
                    related.setRelationType(1);
                    relatedProductDao.persist(related);
                }
            }

            relatedModel = sheet.getCell(RELATED_D, row).getContents();
            if (!StringUtils.isEmpty(relatedModel)) {
                relatedProduct = productDao.findUniqueBy("model", relatedModel);
                // add related product
                if (relatedProduct != null) {
                    ProductToProduct related = new ProductToProduct();
                    related.setProduct(product);
                    related.setRelateProduct(relatedProduct);
                    related.setCreatedDate(new Date());
                    //product related
                    related.setRelationType(1);
                    relatedProductDao.persist(related);
                }
            }

            relatedModel = sheet.getCell(RELATED_E, row).getContents();
            if (!StringUtils.isEmpty(relatedModel)) {
                relatedProduct = productDao.findUniqueBy("model", relatedModel);
                // add related product
                if (relatedProduct != null) {
                    ProductToProduct related = new ProductToProduct();
                    related.setProduct(product);
                    related.setRelateProduct(relatedProduct);
                    related.setCreatedDate(new Date());
                    //product related
                    related.setRelationType(1);
                    relatedProductDao.persist(related);
                }
            }

            relatedModel = sheet.getCell(RELATED_F, row).getContents();
            if (!StringUtils.isEmpty(relatedModel)) {
                relatedProduct = productDao.findUniqueBy("model", relatedModel);
                // add related product
                if (relatedProduct != null) {
                    ProductToProduct related = new ProductToProduct();
                    related.setProduct(product);
                    related.setRelateProduct(relatedProduct);
                    related.setCreatedDate(new Date());
                    //product related
                    related.setRelationType(1);
                    relatedProductDao.persist(related);
                }
            }

            relatedModel = sheet.getCell(RELATED_G, row).getContents();
            if (!StringUtils.isEmpty(relatedModel)) {
                relatedProduct = productDao.findUniqueBy("model", relatedModel);
                // add related product
                if (relatedProduct != null) {
                    ProductToProduct related = new ProductToProduct();
                    related.setProduct(product);
                    related.setRelateProduct(relatedProduct);
                    related.setCreatedDate(new Date());
                    //product related
                    related.setRelationType(1);
                    relatedProductDao.persist(related);
                }
            }
        }

    }

    public List<String> getMessages() {
        return messages;
    }

}
