package org.vist.vistadmin.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlashScopeFilter implements Filter {
	
	public static final String FLASH_PREFIX = "flash.";
	public static final String WARNING_MESSAGE_KEY = "warning_message_key";
	
    private static final String FLASH_SESSION_KEY = "FLASH_SESSION_KEY";
    private static final int FLESH_LENGTH = 6;
    private static Logger logger = LoggerFactory.getLogger(FlashScopeFilter.class.getName());

    /**
     * Copies flash attributes back to request without the flash designation.
     * @param httpRequest
     * @param response
     */
    private void copyBack(HttpServletRequest httpRequest, ServletResponse response) {
        Enumeration<String> e = httpRequest.getAttributeNames();
        while (e.hasMoreElements()) {
            String paramName = e.nextElement();
            if (paramName.startsWith(FLASH_PREFIX)) {
                Object value = httpRequest.getAttribute(paramName);
                paramName = paramName.substring(FLESH_LENGTH, paramName.length());
                httpRequest.setAttribute(paramName, value);
            }
        }
    }

    private boolean hasRequestContentTypeContainJson(ServletRequest request) {
        boolean result = false;
        if (request.getContentType() != null) {
            result = request.getContentType().contains("json");
        }
        return result;
    }

    /** Activity after chain.
     * It copies back all existing keys in the flashscope map to the request, and removes the flashscope map.
     * @param request
     * @param response
     */
    private void postChain(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        copyBack(httpRequest, response);
        Enumeration<String> e = httpRequest.getAttributeNames();
        //store any flash scoped params in the user's session for the
        String incomingUrl = ((HttpServletRequest) request).getRequestURI().toLowerCase();
        if (request instanceof HttpServletRequest && !hasRequestContentTypeContainJson(request) && !incomingUrl.contains("audit")
                && !incomingUrl.contains("/resources")) {
            httpRequest = (HttpServletRequest) request;
            Map<String, Object> flashParams = new HashMap<String, Object>();
            e = httpRequest.getAttributeNames();
            while (e.hasMoreElements()) {
                String paramName = e.nextElement();
                if (paramName.startsWith(FLASH_PREFIX)) {
                    Object value = request.getAttribute(paramName);
                    paramName = paramName.substring(FLESH_LENGTH, paramName.length());
                    flashParams.put(paramName, value);
                }
            }
            if (flashParams.size() > 0) {
                HttpSession session = httpRequest.getSession(false);
                if (session != null) {
                    session.setAttribute(FLASH_SESSION_KEY, flashParams);
                }
            }
        }
    }

    /** Activity after chain.
     * Saves existing FLASH_PREFIX starting request attributes to a special map, which is in turn saved into the http session.
     * Also saves the FLASH_PREFIX starting attributes back to the request without the FLASH_PREFIX part.
     * @param request
     * @param response
     */
    @SuppressWarnings("unchecked")
    private void preChain(ServletRequest request, ServletResponse response) {
        String incomingUrl = ((HttpServletRequest) request).getRequestURI().toLowerCase();
        if (request instanceof HttpServletRequest && !hasRequestContentTypeContainJson(request) && !incomingUrl.contains("audit")
                && !incomingUrl.contains("/resources-")) {
            logger.debug(" prechain, session {}, incoming url: {}", ((HttpServletRequest) request).getSession().getId(), incomingUrl);
            //reinstate any flash scoped params from the users session
            //and clear the session
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(false);
            if (session != null) {
                Map<String, Object> flashParams = (Map<String, Object>) session.getAttribute(FLASH_SESSION_KEY);
                if (flashParams != null) {
                    for (Map.Entry<String, Object> flashEntry : flashParams.entrySet()) {
                        request.setAttribute(flashEntry.getKey(), flashEntry.getValue());
                    }
                    session.removeAttribute(FLASH_SESSION_KEY);
                }
            }
        }
    }

    /** Empty.
     */
    @Override
    public void destroy() {
        //no-op
    }

    /** Saves the parameters starting with "flash" into a session-located map, from which they are brought out for exactly one request.
     *  @param request request
     *  @param response response
     *  @param chain chain
     *  @throws IOException a
     *  @throws ServletException a
     */
    @Override
    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        preChain(request, response);
        //process the chain
        chain.doFilter(request, response);
        postChain(request, response);
    }

    /** Empty.
     *  @param filterConfig no op
     *  @throws ServletException no op
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //no-op
    }

}
