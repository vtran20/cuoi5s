package com.easysoft.ecommerce.web.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutoDetectClientTimeZoneFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getRequestURI().equals("/guesstz")) {
            List<Long> offsets = new ArrayList<Long>();
            for (String s : httpServletRequest.getParameter("offsets").split(",")) {
                offsets.add(Long.parseLong(s));
            }
            int maxEquals = 0;
            String maxEqualsID = null;
            List<String> ids = new ArrayList<String>();
            for (String id : TimeZone.getAvailableIDs()) {
                if (id.matches("^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*")) {
                    ids.add(id);
                }
            }
            for (String id : TimeZone.getAvailableIDs()) {
                if (!id.matches("^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*")) {
                    ids.add(id);
                }
            }
            for (String id : ids) {
                TimeZone tz = TimeZone.getTimeZone(id);
                Calendar cal = GregorianCalendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                cal.clear();
                cal.set(2000, 0, 1);
                int index = 0;
                int right = 0;
                while (cal.get(Calendar.YEAR) < 2011) {
                    int offset = -1 * (tz.getOffset(cal.getTime().getTime()) / 1000 / 60);
                    if (Math.abs(offset - offsets.get(index)) < 2) {
                        right++;
                    }
                    index++;
                    cal.add(Calendar.DATE, 1);
                }
                if (right > maxEquals) {
                    maxEquals = right;
                    maxEqualsID = id;
                }
            }
            httpServletResponse.getWriter().print(maxEqualsID);
        } else if (httpServletRequest.getRequestURI().equals("/guesstz.jsp")) {
            chain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            TimeZone timeZone = new NoDefaultCookieTimeZoneResolver().resolveTimeZone(httpServletRequest);
            if (timeZone == null && httpServletRequest.getMethod().equalsIgnoreCase("GET")) {
                String url = getUrl(httpServletRequest);
                httpServletResponse.sendRedirect("/guesstz.jsp?url=" + URLEncoder.encode(url, "UTF-8"));
            } else {
                chain.doFilter(httpServletRequest, httpServletResponse);
            }
        }
    }

    private static String getUrl(HttpServletRequest req) {
        String reqUrl = req.getRequestURL().toString();
        String queryString = req.getQueryString();
        if (queryString != null) {
            reqUrl += "?" + queryString;
        }
        return reqUrl;
    }

}
