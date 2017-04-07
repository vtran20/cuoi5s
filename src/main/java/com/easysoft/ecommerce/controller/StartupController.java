package com.easysoft.ecommerce.controller;

import com.easysoft.ecommerce.service.FileSystemService;
import com.easysoft.ecommerce.service.IndexService;
import com.easysoft.ecommerce.service.JobsService;
import com.easysoft.ecommerce.service.component.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * This class is used to rebuild the index directory for all entities in database at the time
 * Spring framework is started. The @PostConstruct annotation indicates the method to be ran at startup.
 */
@Component
public class StartupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupController.class);

    @Autowired
    private IndexService indexService;
    @Autowired
    private JobsService jobsService;
    @Autowired
    private FileSystemService fsService;
    @Autowired
    private ServletContext servletContext;


    /**
     * The path to our configuration file (relative to the root of our web app).
     */
    private String commerceComponentPath = "/WEB-INF/commerce-component.xml";

    /**
     * Initialize this servlet by attempting to load the config
     * file and then passing it to the configuration module for parsing.
     *
     * @throws ServletException If the configuration file can not be loaded,
     *                          or a parsing error occurs
     */
    private void init() throws ServletException {

        // Initialize the context-relative path to our configuration resources
        String value = servletContext.getInitParameter("config");
        if (value != null) {
            commerceComponentPath = value;
        }

        InputStream input = null;

        try {
            // Get an input stream to our config file
            input = servletContext.getResourceAsStream(commerceComponentPath);

            if (input == null) {
                String message = "Unable to retrieve resource at path: " + commerceComponentPath;

                System.out.println(message);

                throw new Exception(message);
            }

            // Parse the configuration file and initialize all of our configuration objects
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            Document doc = factory.newDocumentBuilder().parse(input);
            ComponentUtil.loadCommerceComponent(doc);

        }
        catch (Throwable e) {
            String errorMessage = "Initialization error. Cause: " + e.getMessage();
            LOGGER.error(errorMessage, e);

            System.out.println(errorMessage);
            throw new ServletException(e);
        }

        LOGGER.info("Completed initializing configuration file: " + commerceComponentPath);

    }

    @PostConstruct
    public void initialize() throws Exception {

        /*
         * Comment this code. We don't rebuild index at the start up server. Call productDao.rebuildIndex(Product.class); instead 
         *
         jobsService.runPromoPriceJob();
         //Rebuild the index folder from scratch using database data
         indexService.index();
         */

//        fsService.download();

        init();
    }

}
