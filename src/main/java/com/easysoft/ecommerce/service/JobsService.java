package com.easysoft.ecommerce.service;

import java.util.List;


public interface JobsService {
    public static String JOB_STATUS_COMPLETED = "COMPLETED";
    public static String JOB_STATUS_RUNNING = "RUNNING";
    public static String JOB_STATUS_FAIL = "FAIL";
    public static String JOB_STATUS_STOP = "STOP";

    /**
     * This job will do:
     * 1. Base on promotion start/end date to remove promotion price out of product variant table.
     * 2. Calculate price and insert into Product table for displaying on front-end. 
     * 3. Update priceMin column in Product table to support sort/filter function
     * 4. Rerun lucene index
     *
     * @throws Exception
     */
    public List<String> runPromoPriceJob() throws Exception;
    /**
     * Import products to the database from excel file
     *
     * @return
     */
    public List<String> importProducts ();
    public List<String> importInventory ();
    public List<String> importRelatedProduct();
    public List<String> importSequenceProduct();
    public List<String> importMoreImagesProduct();
    public List<String> autoEmailSending ();
    public List<String> marketingEmailSending ();
    public void filterValidEmail ();
    void rebuildIndex(Class... entityTypes);
    void updateVideoStatistic();
    void updateExpiredDateForDemoSite();
    void deleteUserSession();
}