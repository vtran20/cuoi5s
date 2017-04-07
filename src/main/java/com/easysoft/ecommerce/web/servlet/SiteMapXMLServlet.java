package com.easysoft.ecommerce.web.servlet;

import com.easysoft.ecommerce.dao.CategoryDao;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Servlet to generate content for /sitemap.xml file
 */
public class SiteMapXMLServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String SITEMAP_NS_URL = "http://www.sitemaps.org/schemas/sitemap/0.9";
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            XMLOutputFactory output = XMLOutputFactory.newInstance();

            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            XMLStreamWriter writer = output.createXMLStreamWriter(response.getWriter());
            writer.writeStartDocument("UTF-8", "1.0");
            writer.setDefaultNamespace(SITEMAP_NS_URL);

            writer.writeStartElement(SITEMAP_NS_URL, "urlset");
            writer.writeDefaultNamespace("http://www.sitemaps.org/schemas/sitemap/0.9");

            Site site = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite();
            String url = "http://"+site.getDomain();

            writeURL(writer, url, null, null);
            /*
            List<Catalog> catalogs = ServiceLocatorHolder.getServiceLocator().getCatalogDao().getAllCatalogsBySite(site);
            //Catalogs
            for (Catalog catalog: catalogs) {
                generateUrlsForCatalog(response, writer, url, catalog);
                CategoryDao categoryDao = ServiceLocatorHolder.getServiceLocator().getCategoryDao();
                List<Category> categories = categoryDao.getRootCategories(site, catalog.getId(), true);
                //Categories
                for (Category category: categories) {
                    generateUrlsForCategory(response, writer, url, category);
                    List<Category> subCategories = ServiceLocatorHolder.getServiceLocator().getCategoryService().getSubCategories(category.getId());
                    //Subcategories
                    for (Category subCategory: subCategories) {
                        generateUrlsForSubCategory(response, writer, url, category, subCategory);
                        List<Product> products = ServiceLocatorHolder.getServiceLocator().getProductDao().getProductBySubCategory(subCategory.getId(), 0, Integer.MAX_VALUE, null, false);
                        //Products
                        for (Product product: products) {
                            generateUrlsForProduct(response, writer, url, category, subCategory, product);
                        }
                    }
                }

            }
            */
//            List <Video> videos = ServiceLocatorHolder.getServiceLocator().getVideoDao().findBy("active", "Y");
//            for (Video video : videos) {
//                    generateUrlsForVideo (response, writer, url, video);
//            }
            writer.writeEndElement();// urlset

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void generateUrlsForCatalog(HttpServletResponse response, XMLStreamWriter writer, String siteUrl, Catalog catalog) throws XMLStreamException {

        String path = "";
        if (catalog != null) {
            path = "/catalog/"+catalog.getUri()+"-"+catalog.getId()+"/"+"index.html";
            writeURL(writer, siteUrl + response.encodeURL(path), null, null);
        }
    }

    @SuppressWarnings("unchecked")
    private static void generateUrlsForCategory(HttpServletResponse response, XMLStreamWriter writer, String siteUrl, Category category) throws XMLStreamException {

        String path = "";
        if (category != null) {
            path = "/category/"+category.getUri()+"-"+category.getId()+"/"+"index.html";
            writeURL(writer, siteUrl + response.encodeURL(path), null, formatter.format(category.getUpdatedDate()));
        }
    }

    @SuppressWarnings("unchecked")
    private static void generateUrlsForSubCategory(HttpServletResponse response, XMLStreamWriter writer, String siteUrl, Category category, Category subCategory) throws XMLStreamException {

        String path = "";
        if (subCategory != null) {
            path = "/category/"+category.getUri()+"-"+category.getId()+"/"+subCategory.getUri()+"-"+subCategory.getId()+"/"+"index.html";
            writeURL(writer, siteUrl + response.encodeURL(path), null, formatter.format(subCategory.getUpdatedDate()));
        }
    }

    @SuppressWarnings("unchecked")
    private static void generateUrlsForProduct(HttpServletResponse response, XMLStreamWriter writer, String siteUrl, Category category, Category subCategory, Product product) throws XMLStreamException {

        String path = "";
        if (product != null) {
            path = "/product/"+category.getUri()+"-"+category.getId()+"/"+subCategory.getUri()+"-"+subCategory.getId()+"/"+product.getUri()+"-"+product.getId()+"/"+"index.html";
            writeURL(writer, siteUrl + response.encodeURL(path), null, formatter.format(product.getUpdatedDate()));
        }
    }
    @SuppressWarnings("unchecked")
    private static void generateUrlsForVideo(HttpServletResponse response, XMLStreamWriter writer, String siteUrl, Video video) throws XMLStreamException {

        String path = "";
        if (video != null) {
            path = "/video/"+video.getUri()+"/"+video.getId()+".html?videoId="+video.getVideoId();
            writeURL(writer, siteUrl + response.encodeURL(path), null, formatter.format(video.getUpdatedDate()));
        }
    }
    private static void generateUrlsForPage(HttpServletResponse response, XMLStreamWriter writer, String siteUrl, Menu page) throws XMLStreamException {

        String path = "";
        if (page != null) {
            path = "/"+page.getUri();
            writeURL(writer, siteUrl + response.encodeURL(path), null, formatter.format(page.getUpdatedDate()));
        }
    }

    private static void writeURL(XMLStreamWriter writer, String loc, String priority, String lastmod) throws XMLStreamException {

        // <url>
        //   <loc>http://www.google.com/3dwh_dmca.html</loc>
        //   <priority>0.5000</priority>
        //   <lastmod>2011-04-20T11:30:00-07:00</lastmod>
        // </url

        writer.writeStartElement(SITEMAP_NS_URL, "url");

        writer.writeStartElement(SITEMAP_NS_URL, "loc");
        writer.writeCharacters(loc);
        writer.writeEndElement(); // loc

        if (!StringUtils.isEmpty(priority)) {
            writer.writeStartElement(SITEMAP_NS_URL, "priority");
            writer.writeCharacters(priority);
            writer.writeEndElement(); // priority
        }

        if (!StringUtils.isEmpty(lastmod)) {
            writer.writeStartElement(SITEMAP_NS_URL, "lastmod");
            writer.writeCharacters(lastmod);
            writer.writeEndElement(); // lastmod
        }

        writer.writeEndElement(); // url
    }

}
