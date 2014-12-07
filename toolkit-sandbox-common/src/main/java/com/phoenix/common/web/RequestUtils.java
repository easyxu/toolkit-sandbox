package com.phoenix.common.web;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.PropertyUtils;

public class RequestUtils {
	/**
	 * Maps lowercase JSP scope names to their PageContext integer constant
	 * values.
	 */
	private static Map<String,Integer> scopes = new HashMap<String,Integer>();

	private RequestUtils() {
	}

	static {
		scopes.put("page", new Integer(PageContext.PAGE_SCOPE));
		scopes.put("request", new Integer(PageContext.REQUEST_SCOPE));
		scopes.put("session", new Integer(PageContext.SESSION_SCOPE));
		scopes.put("application", new Integer(PageContext.APPLICATION_SCOPE));
	}

	/**
	 * Converts the scope name into its corresponding PageContext constant
	 * value.
	 * 
	 * @param pScopeName
	 *            Can be "page", "request", "session", or "application" in any
	 *            case.
	 * @return The constant representing the scope (ie.
	 *         PageContext.REQUEST_SCOPE).
	 * @throws JspException
	 *             if the scopeName is not a valid name.
	 * @since Struts 1.1
	 */
	public static int getScope(String pScopeName) throws JspException {
		Integer scope = (Integer) scopes.get(pScopeName.toLowerCase());
		if (scope == null) {
			throw new JspException("Invalid bean scope:" + pScopeName);
		}
		return scope.intValue();
	}

	/**
	 * Locate and return the specified bean, from an optionally specified scope,
	 * in the specified page context. If no such bean is found, return
	 * <code>null</code> instead. If an exception is thrown, it will have
	 * already been saved via a call to <code>saveException()</code>.
	 * 
	 * @param pageContext
	 *            Page context to be searched
	 * @param name
	 *            Name of the bean to be retrieved
	 * @param scopeName
	 *            Scope to be searched (page, request, session, application) or
	 *            <code>null</code> to use <code>findAttribute()</code>
	 *            instead
	 * @return JavaBean in the specified page context
	 * @exception JspException
	 *                if an invalid scope name is requested
	 */
	public static Object lookup(PageContext pageContext, String name,
			String scopeName) throws JspException {
		if (scopeName == null) {
			return pageContext.findAttribute(name);
		}
		try {
			return pageContext.getAttribute(name, getScope(scopeName));

		} catch (JspException e) {
			throw e;
		}

	}

	/**
	 * <p>
	 * Create and return an absolute URL for the specified context-relative
	 * path, based on the server and context information in the specified
	 * request.
	 * </p>
	 * 
	 * @param request
	 *            The servlet request we are processing
	 * @param path
	 *            The context-relative path (must start with '/')
	 * 
	 * @return absolute URL based on context-relative path
	 * 
	 * @exception java.net.MalformedURLException
	 *                if we cannot create an absolute URL
	 */
	public static URL absoluteURL(HttpServletRequest request, String path)
			throws MalformedURLException {

		return (new URL(serverURL(request), request.getContextPath() + path));

	}

	/**
	 * 
	 * @param pValue
	 * @return
	 */
	public static boolean isEmpty(String pValue) {
		if (pValue == null) {
			return true;
		}
		if ("null".equalsIgnoreCase(pValue.trim())) {
			return true;
		}

		if ("".equalsIgnoreCase(pValue.trim())) {
			return true;
		}

		return false;

	}

	/**
	 * 获取url路径
	 * 
	 * @param pUrl
	 * @param pRequest
	 * @return
	 */
	public static String getUrl(String pUrl, HttpServletRequest pRequest) {
		if (pUrl == null) {
			return null;
		}
		if (pRequest == null) {
			return null;
		}
		String contextPath = pRequest.getContextPath();
		/**
		 * @todo: 1:判断url格式，是否跨域url,如果是，不需要加contextpath 2:
		 */
		return contextPath + adjustUrl(pUrl);
	}

	/**
	 * 给未添加"/"url加上"/"
	 * 
	 * @param pUrl
	 * @return
	 */
	public static String adjustUrl(String pUrl) {
		if (pUrl == null) {
			return null;
		}
		// 如果pUrl没有带"/"，则加上.
		final String SPLIT = "/";
		int index = pUrl.indexOf(SPLIT);
		if (index < 0) {
			return SPLIT + pUrl;
		} else {
			return pUrl;
		}
	}

	public static URL serverURL(HttpServletRequest request)
			throws MalformedURLException {
		StringBuffer url = new StringBuffer();
		String scheme = request.getScheme();
		int port = request.getServerPort();
		if (port < 0)
			port = 80;
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		if (scheme.equals("http") && port != 80 || scheme.equals("https")
				&& port != 443) {
			url.append(':');
			url.append(port);
		}
		return new URL(url.toString());
	}

	public static URL requestURL(HttpServletRequest request)
			throws MalformedURLException {
		StringBuffer url = new StringBuffer();
		String scheme = request.getScheme();
		int port = request.getServerPort();
		if (port < 0)
			port = 80;
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		if (scheme.equals("http") && port != 80 || scheme.equals("https")
				&& port != 443) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getRequestURI());
		return new URL(url.toString());
	}

	public static Class applicationClass(String className)
			throws ClassNotFoundException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader == null)
			classLoader = (RequestUtils.class).getClassLoader();
		return classLoader.loadClass(className);
	}

	public static Object lookup(PageContext pageContext, String name,
			String property, String scope) throws JspException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// :TODO: Remove after Struts 1.2

		 // Look up the requested bean, and return if requested
        Object bean = lookup(pageContext, name, scope);
        if (bean == null) {
            JspException e = null;
            if (scope == null) {
                e = new JspException();
            } else {
                e = new JspException();
            }
          
            throw e;
        }

        if (property == null) {
            return bean;
        }

        // Locate and return the specified property
       
            return PropertyUtils.getProperty(bean, property);

       

	}

	

}

