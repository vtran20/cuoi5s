package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.controller.Constants;
import com.easysoft.ecommerce.controller.VideoController;
import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.Jobs;
import com.easysoft.ecommerce.model.ProductVariant;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.Video;
import com.easysoft.ecommerce.service.JobsService;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.util.WebUtil;
import com.easysoft.ecommerce.util.jobs.*;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.apache.velocity.app.VelocityEngine;
import org.hibernate.SessionFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Service
@Transactional //Don't need transaction for jobs
public class JobsServiceImpl implements JobsService {

    public static String LOG_PATH = "/usr/local/logs/jobs/";

    @Autowired
    private JobsDao jobsDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductVariantDao productVariantDao;
    @Autowired
    private RelatedProductDao relatedProductDao;
    @Autowired
    private ProductFileDao productFileDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private StandardPBEStringEncryptor strongEncryptor;
    @Autowired
    private EmailServerDao emailServerDao;
    @Autowired
    private EmailSiteDao emailSiteDao;
    @Autowired
    private EmailTemplateDao emailTemplateDao;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private UserSessionDao userSessionDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private EmailMarketingDao emailMarketingDao;
    @Autowired
    private EmailPlanDao emailPlanDao;
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private ServiceLocator serviceLocator;

    @Override
    public List<String> runPromoPriceJob() throws Exception {
        setJobStatus("runPromoPriceJob", JobsService.JOB_STATUS_RUNNING);
        List<String> messages = productDao.runPromoPriceJob();
        Ehcache cache = cacheManager.getEhcache("CacheTag");
        if (cache != null) {
            cache.removeAll();
        }
        setJobStatus("runPromoPriceJob", JobsService.JOB_STATUS_COMPLETED);
        writeFile(LOG_PATH+"runPromoPriceJob-"+WebUtil.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss")+".log", messages);
        return messages;
    }

    @Override
    public List<String> importProducts () {
        setJobStatus("importProducts", JobsService.JOB_STATUS_RUNNING);
        /*
        1.  Load file. If empty, ignore, else load the file.
        2.  Import
        3.  Set status of the job
        4.  Back up the file to backup and delete the current file.
         */

        String path = "/usr/local/data/backup/products-import"+ WebUtil.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss")+".xls";
        backup("/usr/local/data/products-import.xls", path);
        //We use backup file to make sure don't upload 2 times
        ProductImport importProcess = new ProductImport(productDao, productVariantDao, categoryDao, path);
        boolean status = importProcess.processProductImport();
        
        productDao.updateProductPrice();
        //Build index was called at updateProductPrice method
        //productDao.rebuildIndex(Product.class);
        
        if (status) {
            setJobStatus("importProducts", JobsService.JOB_STATUS_COMPLETED);
        } else {
            setJobStatus("importProducts", JobsService.JOB_STATUS_FAIL);
        }
        return importProcess.getMessages();
    }
    @Override
    public List<String> importInventory () {
        setJobStatus("importInventory", JobsService.JOB_STATUS_RUNNING);

        String path = "/usr/local/data/backup/inventory-import"+ WebUtil.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss")+".xls";
        backup("/usr/local/data/inventory-import.xls", path);

        //We use backup file to make sure don't upload 2 times
        InventoryImport importProcess = new InventoryImport(productDao, productVariantDao, categoryDao,path);
        boolean status = importProcess.processProductImport();

        productDao.updateProductPrice();
        productDao.rebuildIndex(ProductVariant.class);

        if (status) {
            setJobStatus("importInventory", JobsService.JOB_STATUS_COMPLETED);
        } else {
            setJobStatus("importInventory", JobsService.JOB_STATUS_FAIL);
        }
        return importProcess.getMessages();
    }
    @Override
    public List<String> importRelatedProduct () {
        setJobStatus("importRelatedProduct", JobsService.JOB_STATUS_RUNNING);

        String path = "/usr/local/data/backup/related-product-import"+ WebUtil.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss")+".xls";
        backup("/usr/local/data/related-product-import.xls", path);

        //We use backup file to make sure don't upload 2 times
        RelatedProductImport importProcess = new RelatedProductImport(productDao, relatedProductDao, path);
        boolean status = importProcess.processRelatedProductImport();

        if (status) {
            setJobStatus("importRelatedProduct", JobsService.JOB_STATUS_COMPLETED);
        } else {
            setJobStatus("importRelatedProduct", JobsService.JOB_STATUS_FAIL);
        }
        return importProcess.getMessages();
    }
    @Override
    public List<String> importMoreImagesProduct () {
        setJobStatus("importMoreImagesProduct", JobsService.JOB_STATUS_RUNNING);

        String path = "/usr/local/data/backup/more-images-product-import-"+ WebUtil.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss")+".xls";
        backup("/usr/local/data/more-images-product-import.xls", path);

        //We use backup file to make sure don't upload 2 times
        MoreImagesProductImport importProcess = new MoreImagesProductImport(productDao, productFileDao, path);
        boolean status = importProcess.processMoreImageProductImport();

        if (status) {
            setJobStatus("importMoreImagesProduct", JobsService.JOB_STATUS_COMPLETED);
        } else {
            setJobStatus("importMoreImagesProduct", JobsService.JOB_STATUS_FAIL);
        }
        return importProcess.getMessages();
    }
    @Override
    public List<String> importSequenceProduct () {
        setJobStatus("importSequenceProduct", JobsService.JOB_STATUS_RUNNING);

        String path = "/usr/local/data/backup/sequence-product-import"+ WebUtil.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss")+".xls";
        backup("/usr/local/data/sequence-product-import.xls", path);

        //We use backup file to make sure don't upload 2 times
        SequenceProductImport importProcess = new SequenceProductImport(productDao, path);
        boolean status = importProcess.processSequenceProductImport();

        if (status) {
            setJobStatus("importSequenceProduct", JobsService.JOB_STATUS_COMPLETED);
        } else {
            setJobStatus("importSequenceProduct", JobsService.JOB_STATUS_FAIL);
        }
        return importProcess.getMessages();
    }

    @Override
    public List<String> autoEmailSending () {
        setJobStatus("autoEmailSending", JobsService.JOB_STATUS_RUNNING);

        String path = "/usr/local/data/email-list.txt";
        String emailProcessedFile = "/usr/local/data/count.txt";
//        backup("/usr/local/data/sequence-product-import.xls", path);
        //TODO: is using default site
        Site site = siteDao.getDefaultSite();
        AutoEmailSending emailSending = new AutoEmailSending(productDao,emailServerDao, strongEncryptor, velocityEngine, emailSiteDao, emailTemplateDao, cacheManager, site, path, emailProcessedFile);
        boolean status = emailSending.sendingEmailFromFile();

        if (status) {
            setJobStatus("autoEmailSending", JobsService.JOB_STATUS_COMPLETED);
        } else {
            setJobStatus("autoEmailSending", JobsService.JOB_STATUS_FAIL);
        }
        writeFile(LOG_PATH+"autoEmailSending-"+WebUtil.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss")+".log", emailSending.getMessages());
        return emailSending.getMessages();
    }

    @Override
    public List<String> marketingEmailSending () {
        setJobStatus("marketingEmailSending", JobsService.JOB_STATUS_RUNNING);

        //TODO: is using default site
        Site site = siteDao.getDefaultSite();
        MarketingEmailSending marketingEmailSending = new MarketingEmailSending(sessionFactory, emailMarketingDao, emailPlanDao, productDao,emailServerDao, strongEncryptor, velocityEngine, emailSiteDao, emailTemplateDao, cacheManager, site, null, null);
        boolean status = marketingEmailSending.sendingEmailFromFile();

        if (status) {
            setJobStatus("marketingEmailSending", JobsService.JOB_STATUS_COMPLETED);
        } else {
            setJobStatus("marketingEmailSending", JobsService.JOB_STATUS_FAIL);
        }
        writeFile(LOG_PATH+"marketingEmailSending-"+WebUtil.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss")+".log", marketingEmailSending.getMessages());
        return marketingEmailSending.getMessages();
    }

    @Override
    public void filterValidEmail() {
        setJobStatus("filterValidEmail", JobsService.JOB_STATUS_RUNNING);

        String path = "/usr/local/data/email-list.txt";
        String emailProcessedFile = "/usr/local/data/count.txt";
        //TODO: is using default site
        Site site = siteDao.getDefaultSite();
        AutoEmailSending emailSending = new AutoEmailSending(productDao,emailServerDao, strongEncryptor, velocityEngine, emailSiteDao, emailTemplateDao, cacheManager, site, path, emailProcessedFile);
        boolean status = emailSending.filterValidEmail(path);

        if (status) {
            setJobStatus("filterValidEmail", JobsService.JOB_STATUS_COMPLETED);
        } else {
            setJobStatus("filterValidEmail", JobsService.JOB_STATUS_FAIL);
        }
    }

    @Override
    public void deleteUserSession () {
        this.userSessionDao.deleteUserSession();
    }
    @Override
    public void rebuildIndex(Class... entityTypes) {
        setJobStatus("rebuildIndex", JobsService.JOB_STATUS_RUNNING);
        productDao.rebuildIndex(entityTypes);
        setJobStatus("rebuildIndex", JobsService.JOB_STATUS_COMPLETED);

    }

    /**
     * Update view count of video and remove out of map.
     */
    @Override
    public void updateVideoStatistic() {
        setJobStatus("updateVideoStatistic", JobsService.JOB_STATUS_RUNNING);
        int size = VideoController.videosCache.size();
        int i = 0;
        for (String s : VideoController.videosCache.keySet()) {
            Video v = VideoController.videosCache.get(s);
            //Video video = videoDao.findById(v.getId());
            List list = videoDao.findBy("videoId", s);
            if (list != null && list.size() > 0) {
                Video video = (Video) list.get(0);
                //add count on the current cache object and the current in the DB.
                video.setViewCount(v.getViewCount() + video.getViewCount());
                videoDao.merge(video);
            }
        }
        VideoController.videosCache.clear();
        setJobStatus("updateVideoStatistic", JobsService.JOB_STATUS_COMPLETED);
    }

    @Override
    public void updateExpiredDateForDemoSite() {
        setJobStatus("updateExpiredDateForDemoSite", JobsService.JOB_STATUS_RUNNING);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        serviceLocator.getSiteDao().updateExpiredDateForDemoSite(cal.getTime());
        setJobStatus("updateExpiredDateForDemoSite", JobsService.JOB_STATUS_COMPLETED);
    }


    private void setJobStatus (String method, String status, String name, String description) {
        setJobStatus(method, status, name, description, null);
    }
    private void setJobStatus (String method, String status, Site site) {
        setJobStatus(method, status, null, null, site);
    }
    private void setJobStatus (String method, String status) {
        setJobStatus(method, status, null, null, null);
    }
    /*
    TODO: Don't support by site right now. We will support in the future if need.
     */
    private void setJobStatus (String method, String status, String name, String description, Site site) {
        /*This will be a template for how implement a job*/
        List<Jobs> jobs = jobsDao.findBy("method", method);
        Jobs job = null;
        if (jobs != null && jobs.size() >= 1) {
            job = jobs.get(0);
        } else {
            job = new Jobs ();
            job.setName(name);
            job.setDescription(description);
        }
        job.setMethod(method);
        job.setStatus(status);
        job.setUpdatedDate(new Date());
        if (job.getId() == null) {
            jobsDao.persist(job);
        } else {
            jobsDao.merge(job);
        }
    }

    private boolean backup (String source, String dest) {
        // File (or directory) with old name
        File file = new File(source);
        // Destination directory
        File file2 = new File(dest);
        // File (or directory) with new name
        return file.renameTo(file2);
    }

    private void writeFile (String fileName, List<String> messages) {
        File file = new File(fileName);
        Writer output = null;
        try {
            StringBuilder buf = new StringBuilder();
            for (String value: messages) {
                buf.append(value).append('\n');
            }
            output = new BufferedWriter(new FileWriter(file));
            output.write(buf.toString());
            output.close();
        } catch (IOException ignored) {

        }
    }


}
