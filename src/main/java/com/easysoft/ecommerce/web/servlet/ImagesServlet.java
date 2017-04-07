package com.easysoft.ecommerce.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easysoft.ecommerce.service.image.*;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.easysoft.ecommerce.service.ServiceLocatorHolder;

/**
 * @deprecated We don't use this image processing anymore.
 */
public class ImagesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Cache getCacheService() {
        //Removed this cache
        return ServiceLocatorHolder.getServiceLocator().getCacheManager().getCache("ImagesServlet");
    }

    @Override
    protected long getLastModified(HttpServletRequest request) {
        long result = -1; // unknown
        try {
            FileCommand cmd = getCommand(request);

            // try to re-use cached results
            Cache cache = getCacheService();
            Element element = cache.get(cmd);
            if (element != null) {
                result = element.getCreationTime();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    private String getImagePath(HttpServletRequest request) {
        String filePath = null;
        try {
            // Get image path from request
            String requestedImage = request.getParameter("path");
            File file = ServiceLocatorHolder.getServiceLocator().getFileSystemService().find(requestedImage);
            filePath = file.getAbsolutePath();
        } catch (Exception e) {
            filePath = null;
        }
        return filePath;
    }

    @SuppressWarnings("rawtypes")
    private FileCommand getCommand(HttpServletRequest request) {
        // Build command
        FileCommand cmd = null;

        String imagePath  = getImagePath(request);

        // Populate resize or crop command with options ("width", "height", "keepRatio") / (x, y, width, height)
        BeanWrapper bw = null;
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            if (name.startsWith("resize.")) {
                if (cmd == null) {
                    cmd = new ImageResizeCommand();
                    bw = new BeanWrapperImpl(cmd);
                }
                String propertyName = name.substring("resize.".length());
                String value = request.getParameter(name);
                bw.setPropertyValue(propertyName, value);

            } else if (name.startsWith("crop.")) {
                if (cmd == null) {
                    cmd = new ImageCropCommand();
                    bw = new BeanWrapperImpl(cmd);
                }
                String propertyName = name.substring("crop.".length());
                String value = request.getParameter(name);
                bw.setPropertyValue(propertyName, value);
            } else if (name.startsWith("cropresize.")) {
                if (cmd == null) {
                    cmd = new ImageCropResizeCommand();
                    bw = new BeanWrapperImpl(cmd);
                }
                String propertyName = name.substring("cropresize.".length());
                String value = request.getParameter(name);
                bw.setPropertyValue(propertyName, value);
            }
        }

        // Populate "in" option
        File in = new File(imagePath);
        cmd.setIn(in);
        return cmd;
    }

    /**
     * Image will be get from temp folder first. If it doesn'n exist, get the original image and resize as request.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // http://dodungtreem.com/assets/ddte/product/images/prod_1.jpg.image?resize.width=200&resize.keepRatio=true
        // http://dodungtreem.com/assets/ddte/product/images/prod_1.jpg.image?crop.width=200&crop.height=200&crop.x=0&crop.y=0
        // http://dodungtreem.com/assets/ddte/product/images/prod_1.jpg.image?cropresize.width=200&cropresize.height=200
        String imagePath = null;
        try {
            imagePath  = getImagePath(request);
        } catch (Exception e) {
        }
        if (imagePath == null || !(new File(imagePath).exists())) {
            response.setStatus(404);
            return;
        }

        FileCommand cmd = getCommand(request);

        // try to re-use cached results
        File out = null;
        String mimeType = null;

        Cache cache = getCacheService();
        Element element = cache.get(cmd);
        if (element != null) {
            Object[] cachedValue = (Object[]) element.getValue();
            File file = (File) cachedValue[0];
            if (file.exists()) {
                out = file;
                mimeType = (String) cachedValue[1];
            }
        }


        if (out != null) {
            // Object in cache

        } else { // object not found in cache
            // Populate "out" option
            File tempDir = (File) getServletContext() .getAttribute("javax.servlet.context.tempdir");
            out = File.createTempFile("imageservlet", ".tmp", tempDir);
            cmd.setOut(out);

            // generate new image
            try {
                cmd.execute();

                // Content-Type
                try {
                    MagicMatch match = Magic.getMagicMatch(out, false);
                    mimeType = match.getMimeType();
                } catch (Exception e) {
                    // ignore content-type
                }

                // cache the result, but first set "out" option to null, so cmd will be equals when lookup next time
                cmd.setOut(null);
                cache.put(new Element(cmd, new Object[]{out, mimeType}));

            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(500);
            }
        }

        response.setContentType(mimeType);

        // Content-Length
        response.setContentLength((int)out.length());

        // Cache headers
        // http://code.google.com/speed/page-speed/docs/caching.html
        long lastModifiedMillis = out.lastModified();
        Calendar cal = Calendar.getInstance();
        long now = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, 1);
        long expires = cal.getTimeInMillis();
        response.setDateHeader("Last-Modified", lastModifiedMillis);
        response.setDateHeader("Date", now);
        response.setDateHeader("Expires", expires);
        response.setHeader("Cache-Control", "public, max-age=315360000, post-check=315360000, pre-check=315360000");

        // File content
        FileInputStream fis = null;
        OutputStream os = null;
        fis = new FileInputStream(out);
        os = response.getOutputStream();
        try {
            IOUtils.copy(fis, os);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(fis);
        }
    }
}
