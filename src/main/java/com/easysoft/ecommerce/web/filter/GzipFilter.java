package com.easysoft.ecommerce.web.filter;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.constructs.web.GenericResponseWrapper;
import net.sf.ehcache.constructs.web.ResponseUtil;
import net.sf.ehcache.constructs.web.filter.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

/**
 * An improved version of net.sf.ehcache.constructs.web.filter.GzipFilter class which can detect the case where the
 * filter is called after two (or more) rd.forward() calls and GzipFilter is enabled for REQUEST and FORWARD.
 * In the original version, the filter cannot detect the case and gzip the result twice (or more).
 * This version will allow gzipping only once using a thread-local variable flag.
 */
public class GzipFilter extends Filter {

    private static final Logger LOG = LoggerFactory.getLogger(GzipFilter.class);

    private static final ThreadLocal<Boolean> gzipFlagHolder = new NamedThreadLocal<Boolean>("gzipFlagHolder");

    /**
     * Performs initialisation.
     * @param filterConfig
     */
    protected void doInit(FilterConfig filterConfig) throws Exception {
        //nothing required.
    }


    /**
     * A template method that performs any Filter specific destruction tasks.
     * Called from {@link #destroy()}
     */
    protected void doDestroy() {
        //noop
    }

    /**
     * Performs the filtering for a request.
     */
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response,
                            final FilterChain chain) throws Exception {
        if (!isIncluded(request) && acceptsEncoding(request, "gzip") && !response.isCommitted()) {
            try {
                gzipFlagHolder.set(Boolean.TRUE);

                // Client accepts zipped content
                if (LOG.isDebugEnabled()) {
                    LOG.debug(request.getRequestURL() + ". Writing with gzip compression");
                }

                // Create a gzip stream
                final ByteArrayOutputStream compressed = new ByteArrayOutputStream();
                final GZIPOutputStream gzout = new GZIPOutputStream(compressed);

                // Handle the request
                final GenericResponseWrapper wrapper = new GenericResponseWrapper(response, gzout);
                wrapper.setDisableFlushBuffer(true);
                chain.doFilter(request, wrapper);
                wrapper.flush();

                gzout.close();

                //return on error or redirect code, because response is already committed
                int statusCode = wrapper.getStatus();
                if (statusCode != HttpServletResponse.SC_OK) {
                    return;
                }

                //Saneness checks
                byte[] compressedBytes = compressed.toByteArray();
                boolean shouldGzippedBodyBeZero = ResponseUtil.shouldGzippedBodyBeZero(compressedBytes, request);
                boolean shouldBodyBeZero = ResponseUtil.shouldBodyBeZero(request, wrapper.getStatus());
                if (shouldGzippedBodyBeZero || shouldBodyBeZero) {
                    compressedBytes = new byte[0];
                }

                // Write the zipped body
                ResponseUtil.addGzipHeader(response);
                response.setContentLength(compressedBytes.length);


                response.getOutputStream().write(compressedBytes);
            } finally {
                gzipFlagHolder.remove();
            }
        } else {
            // Client does not accept zipped content - don't bother zipping
            if (LOG.isDebugEnabled()) {
                LOG.debug(request.getRequestURL()
                        + ". Writing without gzip compression because the request does not accept gzip.");
            }
            chain.doFilter(request, response);
        }
    }


    /**
     * Checks if the request uri is an include.
     * These cannot be gzipped.
     */
    private boolean isIncluded(final HttpServletRequest request) {
        final String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
        final boolean includeRequest = !(uri == null) || (gzipFlagHolder.get() != null);

        if (includeRequest && LOG.isDebugEnabled()) {
            LOG.debug(request.getRequestURL() + " resulted in an include request. This is unusable, because" +
                    "the response will be assembled into the overrall response. Not gzipping.");
        }
        return includeRequest;
    }

    /**
     * Determine whether the user agent accepts GZIP encoding. This feature is part of HTTP1.1.
     * If a browser accepts GZIP encoding it will advertise this by including in its HTTP header:
     * <p/>
     * <code>
     * Accept-Encoding: gzip
     * </code>
     * <p/>
     * Requests which do not accept GZIP encoding fall into the following categories:
     * <ul>
     * <li>Old browsers, notably IE 5 on Macintosh.
     * <li>Internet Explorer through a proxy. By default HTTP1.1 is enabled but disabled when going
     * through a proxy. 90% of non gzip requests seen on the Internet are caused by this.
     * </ul>
     * As of September 2004, about 34% of Internet requests do not accept GZIP encoding.
     *
     * @param request
     * @return true, if the User Agent request accepts GZIP encoding
     */
    protected boolean acceptsGzipEncoding(HttpServletRequest request) {
        return acceptsEncoding(request, "gzip");
    }


}
