package com.easysoft.ecommerce.web.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.isdc.wro.cache.CacheEntry;
import ro.isdc.wro.cache.CacheStrategy;
import ro.isdc.wro.cache.ContentHashEntry;
import ro.isdc.wro.manager.factory.ServletContextAwareWroManagerFactory;
import ro.isdc.wro.model.factory.FallbackAwareXmlModelFactory;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.group.DefaultGroupExtractor;
import ro.isdc.wro.model.group.GroupExtractor;
import ro.isdc.wro.model.resource.ResourceType;

import com.easysoft.ecommerce.service.ServiceLocatorHolder;

public class VersionawareServletContextAwareWroManagerFactory extends ServletContextAwareWroManagerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionawareServletContextAwareWroManagerFactory.class);
    @Override
    protected GroupExtractor newGroupExtractor() {
        return new GroupExtractor() {
            private GroupExtractor decorated = new DefaultGroupExtractor();
            @Override
            public String getGroupName(HttpServletRequest request) {
                String groupName = decorated.getGroupName(request);
                ServletContext servletContext = ServiceLocatorHolder.getServiceLocator().getServletContext();
                String version = (String) servletContext.getAttribute(RequestContextFilter.VERSION_SERVLET_CONTEXT_ATTR);
                if (groupName.startsWith(version)) {
                    groupName = groupName.substring(version.length());
                }
                return groupName;
            }

            @Override
            public ResourceType getResourceType(HttpServletRequest request) {
                return decorated.getResourceType(request);
            }

            @Override
            public boolean isMinimized(HttpServletRequest request) {
                return decorated.isMinimized(request);
            }

            @Override
            public String encodeGroupUrl(String groupName, ResourceType resourceType, boolean minimize) {
                return decorated.encodeGroupUrl(groupName, resourceType, minimize);
            }
        };
    }

    @Override
    protected CacheStrategy<CacheEntry, ContentHashEntry> newCacheStrategy() {
        // Disable WRO caching
        return new CacheStrategy<CacheEntry, ContentHashEntry>() {
            @Override
            public void clear() {
            }
            @Override
            public void destroy() {
            }
            @Override
            public ContentHashEntry get(CacheEntry key) {
                return null;
            }
            @Override
            public void put(CacheEntry key, ContentHashEntry value) {
            }
        };
    }
    @Override
    protected WroModelFactory newModelFactory(final ServletContext servletContext) {
        return new FallbackAwareXmlModelFactory() {
            @Override
            protected InputStream getConfigResourceAsStream() throws IOException {
                InputStream result;
                // Find all WRO config files
                List<String> wroFiles = new ArrayList<String>();
                if (servletContext.getResource("/WEB-INF/wro.xml") != null) {
                    wroFiles.add("/WEB-INF/wro.xml");
                }
                Set<String> paths = servletContext.getResourcePaths("/themes");
                for (String path : paths) {
                    if (path.endsWith("/")) {
                        if (servletContext.getResource(path + "wro.xml") != null) {
                            wroFiles.add(path + "wro.xml");
                        }
                    }
                }
                if (wroFiles.isEmpty()) {
                    result = null;
                } else if (wroFiles.size() == 1) {
                    result = servletContext.getResourceAsStream(wroFiles.get(0));
                } else {
                    try {
                        // Merge all WRO config files into one DOM
                        InputStream is0 = servletContext.getResourceAsStream(wroFiles.remove(0));
                        Document doc = parseXmlFile(is0, false);
                        while (!wroFiles.isEmpty()) {
                            InputStream is = servletContext.getResourceAsStream(wroFiles.remove(0));
                            Document doc2 = parseXmlFile(is, false);
                            NodeList list = doc2.getElementsByTagName("group");
                            for (int i = 0; i < list.getLength(); i++) {
                                org.w3c.dom.Element element = (org.w3c.dom.Element)list.item(i);
                                Node dup = doc.importNode(element, true);
                                doc.getDocumentElement().appendChild(dup);
                            }
                            is.close();
                        }
                        is0.close();

                        // Write DOM to byte array
                        Source source = new DOMSource(doc);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        Result sr = new StreamResult(bytes);
                        Transformer xformer = TransformerFactory.newInstance().newTransformer();
                        xformer.transform(source, sr);

                        result = new ByteArrayInputStream(bytes.toByteArray());
                    } catch (Exception e) {
                        LOGGER.error("Error when merging wro.xml files", e);
                        result = null;
                    }
                }
                return result;
            }
            private Document parseXmlFile(InputStream is, boolean validating) throws Exception {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(validating);
                Document doc = factory.newDocumentBuilder().parse(is);
                return doc;
            }
        };
    }
}
