package com.easysoft.ecommerce.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet to generate content for /robots.txt file
 */
public class RobotsTXTServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder content = new StringBuilder();
        content.append("User-agent: *\r\n");
        content.append("Disallow: /admin/\r\n");
        content.append("Disallow: /profile/\r\n");
        content.append("Disallow: /checkout/\r\n");
        //content.append("Disallow: /*?\r\n");
        
        String scheme = request.getScheme();
        String url = scheme;
        url += "://";
        url += request.getServerName();
        int port = request.getServerPort();
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url += ':';
            url += port;
        }
        url += request.getContextPath() + "/sitemap.xml";
        
        content.append("Sitemap: " + url + "\r\n");
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter w = response.getWriter();
        w.write(content.toString());
        w.flush();
        w.close();
    }

}
