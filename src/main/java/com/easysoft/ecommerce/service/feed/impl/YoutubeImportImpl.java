package com.easysoft.ecommerce.service.feed.impl;

import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.feed.FeedImportSupport;
import com.easysoft.ecommerce.util.Messages;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Process applying promo code. Recalculating price of entire order.
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class YoutubeImportImpl extends FeedImportSupport implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(YoutubeImportImpl.class);

    @Override
    public void execute(Messages errors) throws Exception {

        //Get promotion record base on promotion code
        String promoCode = "";//sessionObject.getOrder().getPromoCode();
        if (!StringUtils.isEmpty(promoCode)) {
            Promotion promotion = ServiceLocatorHolder.getServiceLocator().getPromotionDao().getValidPromotion(promoCode);

//                if (PromotionUtil.isValidPromoCode(sessionObject, promotion)) {
//                    PromotionClass promotionClass = null;
//                    if (promotion != null) {
//                        promotionClass = ServiceLocatorHolder.getServiceLocator().getPromotionClassDao().getPromotionClass(promotion.getId(), true);
//                    }
//                    try {
//                        //Apply discount to the order
//                        if (promotionClass != null && !StringUtils.isEmpty(promotionClass.getClassName())) {
//                            Class c = null;
//                            c = Class.forName(promotionClass.getClassName());
//                            Award award = (Award) c.newInstance();
//                            award.execute(sessionObject, promotion);
//                        }
//                    } catch (ClassNotFoundException e) {
//                        LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promoCode, e);
//                        e.printStackTrace();
//                    } catch (InstantiationException e) {
//                        LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promoCode, e);
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promoCode, e);
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + promoCode, e);
//                        e.printStackTrace();
//                    }
//                } else {
//                    String[] p = new String[1];
//                    p[0] = promoCode;
//                    //remove promo code because invalid.
//                    sessionObject.getOrder().setPromoCode("");
//                    PromotionUtil.removePromotionDiscount(sessionObject);
//                    errors.addWarning("promocode_invalid", ServiceLocatorHolder.getServiceLocator().getMessageSource().getMessage("message.promocode.invalid.condition", p, LocaleContextHolder.getLocale()));
//                }
        }

        /*TODO: This is default promo code for entire the site. Process if it is available for the site
       Temporary, we apply default promo code for free shipping only.
       * */
//            String defaultPromoCode = sessionObject.getOrder().getDefaultPromoCode();
//            if (!StringUtils.isEmpty(defaultPromoCode)) {
//                Promotion promotion = ServiceLocatorHolder.getServiceLocator().getPromotionDao().getValidPromotion(defaultPromoCode);
//
//                if (PromotionUtil.isValidPromoCode(sessionObject, promotion)) {
//                    PromotionClass promotionClass = null;
//                    if (promotion != null) {
//                        promotionClass = ServiceLocatorHolder.getServiceLocator().getPromotionClassDao().getPromotionClass(promotion.getId(), true);
//                    }
//                    try {
//                        //Apply discount to the order
//                        if (promotionClass != null && !StringUtils.isEmpty(promotionClass.getClassName())) {
//                            Class c = null;
//                            c = Class.forName(promotionClass.getClassName());
//                            Award award = (Award) c.newInstance();
//                            award.execute(sessionObject, promotion);
//                        }
//                    } catch (ClassNotFoundException e) {
//                        LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + defaultPromoCode, e);
//                        e.printStackTrace();
//                    } catch (InstantiationException e) {
//                        LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + defaultPromoCode, e);
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + defaultPromoCode, e);
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        LOGGER.error("Expression error:" + promotionClass.getClassName() + ":" + defaultPromoCode, e);
//                        e.printStackTrace();
//                    }
//                }
//            }
    }


    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 50;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;
    /**
     * Global instance of the HTTP transport.
     */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     * @param args command line args.
     */
    public static void main(String[] args) {
        // Read the developer key from the properties file.
//        Properties properties = new Properties();
//        try {
//            InputStream in = YouTube.Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
//            properties.load(in);
//
//        } catch (IOException e) {
//            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
//                    + " : " + e.getMessage());
//            System.exit(1);
//        }

        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Prompt the user to enter a query term.
            String queryTerm = getInputQuery();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet,statistics");

            // Set your developer key from the Google Developers Console for
            // non-authenticated requests. See:
            // https://cloud.google.com/console
            String apiKey = "AIzaSyCjdekNBTgmJ3x6_hepaapf2BgQHE195Z4";//properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            search.setQ(queryTerm);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            //search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), queryTerm);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /*
     * Prompt the user to enter a query term and return the user-specified term.
     */
    private static String getInputQuery() throws IOException {

        String inputQuery = "";

        System.out.print("Please enter a search term: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        inputQuery = bReader.readLine();

        if (inputQuery.length() < 1) {
            // Use the string "YouTube Developers Live" as a default.
            inputQuery = "YouTube Developers Live";
        }
        return inputQuery;
    }

    /*
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, and thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
    }


}
