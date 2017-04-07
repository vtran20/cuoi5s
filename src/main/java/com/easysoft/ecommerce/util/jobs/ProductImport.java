package com.easysoft.ecommerce.util.jobs;

import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.dao.ProductVariantDao;
import com.easysoft.ecommerce.model.Category;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.util.WebUtil;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
public class ProductImport {

    public static int MODEL_A = 0;
    public static int CATEGORY_PATH_B = 1;
    public static int PRODUCT_NAME_C = 2;
    public static int DESCRIPTION_D = 3;
    public static int ORIGINAL_PRICE_E = 4;
    public static int SALE_PRICE_F = 5;
    public static int ACTIVE_G = 6;
    public static int ATTRIBUTE_1_H = 7;
    public static int ATTRIBUTE_2_I = 8;
    public static int ATTRIBUTE_3_J = 9;
    public static int ATTRIBUTE_4_K = 10;
    public static int ATTRIBUTE_5_L = 11;
    public static int INVENTORY_M = 12;
    public static int WEIGHT_N = 13;
    public static int O = 14;
    public static int P = 15;
    public static int Q = 16;
    public static int R = 17;
    public static int S = 18;
    public static int T = 19;
    public static int SKU_U = 20;
    public static int COLOR_CODE_V = 21;
    public static int COLOR_NAME_W = 22;
    public static int SIZE_CODE_X = 23;
    public static int SIZE_NAME_Y = 24;
    public static int IMAGE_CODE_VARIANT_Z = 25;

    List<String> messages = new ArrayList<String>();

    ProductDao productDao;
    ProductVariantDao productVariantDao;
    CategoryDao categoryDao;
    String productImportFile;



    public ProductImport(ProductDao productDao, ProductVariantDao productVariantDao, CategoryDao categoryDao, String productImportFile) {
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
                        createOrUpdateProduct(productDao, productVariantDao, categoryDao, sheet, i);
                    } catch (Exception e) {
                        messages.add("Line " + (i+1) + ": Cannot create or update proudct:"+e.getMessage());
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
            error.add("Line " + (row+1) + ": missing model number");
        }
        if (StringUtils.isEmpty(sheet.getCell(CATEGORY_PATH_B, row).getContents())) {
            error.add("Line " + (row+1) + ": missing category path");
        } else {
            if (sheet.getCell(CATEGORY_PATH_B, row).getContents().split("/").length < 2) {
                error.add("Line " + (row+1) + ": category path is incorrect format");
            }
        }
        if (StringUtils.isEmpty(sheet.getCell(PRODUCT_NAME_C, row).getContents())) {
            error.add("Line " + (row+1) + ": missing product name");
        }
        if (StringUtils.isEmpty(sheet.getCell(ORIGINAL_PRICE_E, row).getContents())) {
            error.add("Line " + (row+1) + ": missing original price");
        }
        if (StringUtils.isEmpty(sheet.getCell(SALE_PRICE_F, row).getContents())) {
            error.add("Line " + (row+1) + ": missing sale price");
        }

        if (StringUtils.isEmpty(sheet.getCell(WEIGHT_N, row).getContents())) {
            error.add("Line " + (row+1) + ": missing weight");
        }

        if (error.size() > 0) {
            messages.addAll(error);
            return false;
        } else {
            return true;
        }
    }
    public void createOrUpdateProduct(ProductDao productDao, ProductVariantDao productVariantDao, CategoryDao categoryDao, Sheet sheet, int row) throws Exception {
        String model = sheet.getCell(MODEL_A, row).getContents();
        Product original = productDao.findUniqueBy("model", model);
        if (!isValidProduct(sheet, row)) return ;
        /*New product*/
        if (original == null) {
            original = new Product();
            setProductAttribute(original, sheet, row);
            original.setCreatedDate(new Date());
            original.setSequence(999999);
            productDao.persist(original);

            //new product variant
            ProductVariant productVariant = new ProductVariant();
            setProductVariantAttribute(original, productVariant, sheet, row);
            productVariant.setCreatedDate(new Date());
            productVariantDao.persist(productVariant);

            //Create product to category
            String categoryURI = getCategoryURI(sheet.getCell(CATEGORY_PATH_B, row).getContents());
            if (StringUtils.isEmpty(categoryURI)) {
                categoryURI = getParentCategoryURI(sheet.getCell(CATEGORY_PATH_B, row).getContents());
            }
            if (!StringUtils.isEmpty(categoryURI)) {
                Category category = categoryDao.findUniqueBy("uri", categoryURI);
                if (category != null) {
                    categoryDao.addProductToCategory(category.getId(), original.getId());
                } else {
                    messages.add("Line: " + (row+1) + ": URI " +categoryURI + " doesn't exist");
                }
            }

        } else {
            //Update for product
            setProductAttribute(original, sheet, row);
            productDao.merge(original);
            String sku = sheet.getCell(SKU_U, row).getContents();
            ProductVariant productVariant;
            //if sku is existing, use it else get default product variant.
            if (!StringUtils.isEmpty(sku)) {
                productVariant = productVariantDao.findUniqueBy("sku", sku);
            } else {
                productVariant = productVariantDao.getProductVariantDefault(original.getId());
            }
            //Create new product variant if null
            if (productVariant == null) {
                productVariant = new ProductVariant();
            }
            setProductVariantAttribute(original, productVariant, sheet, row);
            if (productVariant.getId() != null) {
                productVariantDao.merge(productVariant);
            } else {
                productVariantDao.persist(productVariant);
            }

            //Add category to product in case the relationship is not available.
            String categoryURI = getCategoryURI(sheet.getCell(CATEGORY_PATH_B, row).getContents());
            if (StringUtils.isEmpty(categoryURI)) {
                categoryURI = getParentCategoryURI(sheet.getCell(CATEGORY_PATH_B, row).getContents());
            }
            if (!StringUtils.isEmpty(categoryURI)) {
                Category category1 = categoryDao.findUniqueBy("uri", categoryURI);
                List<Category> cats = categoryDao.getSubCategories(original.getId());
                if (category1 != null) {
                    boolean createCategoryProduct = true;
                    for (Category cat: cats) {
                        if (cat.getId().equals(category1.getId())) {
                            createCategoryProduct = false;
                            break;
                        } else {
                            //need create
                            createCategoryProduct = true;
                        }
                    }
                    if (createCategoryProduct) {
                        categoryDao.addProductToCategory(category1.getId(), original.getId());
                    }
                } else {
                    messages.add("Line: " + (row+1) + ": URI " +categoryURI + " doesn't exist");
                }
            }
        }
    }

    private void setProductAttribute (Product product, Sheet sheet, int row) {
        Date date = new Date();
        product.setModel(sheet.getCell(MODEL_A, row).getContents());
        product.setName(sheet.getCell(PRODUCT_NAME_C, row).getContents());
        product.setDescription(sheet.getCell(DESCRIPTION_D, row).getContents());
        product.setAttribute1(sheet.getCell(ATTRIBUTE_1_H, row).getContents());
        product.setAttribute2(sheet.getCell(ATTRIBUTE_2_I, row).getContents());
        product.setAttribute3(sheet.getCell(ATTRIBUTE_3_J, row).getContents());
        product.setAttribute4(sheet.getCell(ATTRIBUTE_4_K, row).getContents());
        product.setAttribute5(sheet.getCell(ATTRIBUTE_5_L, row).getContents());
        product.setImageUrl("/"+getCatalogURI(sheet.getCell(CATEGORY_PATH_B, row).getContents())+"/product/"+sheet.getCell(MODEL_A, row).getContents()+".jpg");
        product.setMetaDescription(sheet.getCell(PRODUCT_NAME_C, row).getContents());
        product.setMetaKeyword(sheet.getCell(PRODUCT_NAME_C, row).getContents());
        //uri will be uri + model to make sure it is unique
        product.setUri(WebUtil.getURI(sheet.getCell(PRODUCT_NAME_C, row).getContents())+"-" +sheet.getCell(MODEL_A, row).getContents());
        if ("Y".equalsIgnoreCase(sheet.getCell(ACTIVE_G, row).getContents())) {
            product.setActive("Y");
        } else {
            product.setActive("N");
        }
        product.setPriceMin(Long.valueOf(sheet.getCell(SALE_PRICE_F, row).getContents()));
        product.setDisplayPrice(sheet.getCell(SALE_PRICE_F, row).getContents());
        product.setUpdatedDate(date);
        if (StringUtils.isEmpty(sheet.getCell(WEIGHT_N, row).getContents())) {
            product.setWeight(200);//default is 200g
        } else {
            product.setWeight(Integer.valueOf(sheet.getCell(WEIGHT_N, row).getContents()));
        }

    }

    private void setProductVariantAttribute (Product product, ProductVariant productVariant, Sheet sheet, int row) {
        Date date = new Date();
        productVariant.setUpdatedDate(date);
        productVariant.setProduct(product);
        productVariant.setDefault("Y");
        if ("Y".equalsIgnoreCase(sheet.getCell(ACTIVE_G, row).getContents())) {
            productVariant.setActive("Y");
        } else {
            productVariant.setActive("N");
        }
        //set variant image
        if (StringUtils.isEmpty(sheet.getCell(IMAGE_CODE_VARIANT_Z, row).getContents())) {
            productVariant.setImageUrl("/"+getCatalogURI(sheet.getCell(CATEGORY_PATH_B, row).getContents())+"/product/"+sheet.getCell(MODEL_A, row).getContents()+".jpg");
        } else {
            productVariant.setImageUrl("/"+getCatalogURI(sheet.getCell(CATEGORY_PATH_B, row).getContents())+"/product/"+sheet.getCell(IMAGE_CODE_VARIANT_Z, row).getContents()+".jpg");
        }
        productVariant.setOriginalPrice(Long.valueOf(sheet.getCell(ORIGINAL_PRICE_E, row).getContents()));
        productVariant.setPrice(Long.valueOf(sheet.getCell(SALE_PRICE_F, row).getContents()));
        productVariant.setSku(sheet.getCell(SKU_U, row).getContents());
        productVariant.setColorCode(sheet.getCell(COLOR_CODE_V, row).getContents());
        productVariant.setColorName(sheet.getCell(COLOR_NAME_W, row).getContents());
        productVariant.setSizeCode(sheet.getCell(SIZE_CODE_X, row).getContents());
        productVariant.setSizeName(sheet.getCell(SIZE_NAME_Y, row).getContents());

    }

    /**
     * get uri of catalog or category.
     *
     * @param uriPath
     * @param index
     * @return
     */
    private String getURI (String uriPath, int index) {
        if (!StringUtils.isEmpty(uriPath)) {
            String []str = uriPath.split("/");
            if (str != null && str.length > index) {
                return str[index];
            } else {
                return null;
            }
        }
        return null;
    }

    private String getCatalogURI (String uriPath) {
        return getURI(uriPath, 0);
    }
    private String getParentCategoryURI (String uriPath) {
        return getURI(uriPath, 1);
    }
    private String getCategoryURI (String uriPath) {
        return getURI(uriPath, 2);
    }

    public List<String> getMessages() {
        return messages;
    }
}
