/**
 * 
 */
package com.phoenix.common;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author xuxiang<xiang.leau@gmail.com>
 * @date 2011-8-2 下午06:35:58
 */
public class TheadContext {
	private static final Logger log = LoggerFactory.getLogger(TheadContext.class);

	  
	private static ThreadLocal<Map<Object, Object>> local = new ThreadLocal<Map<Object, Object>>() {
	    protected Map<Object, Object> initialValue() {
	      return new HashMap<Object, Object>();
	    }
	  };

	  private static Map<Object, Object> currentMap()
	  {
	    return (Map<Object, Object>)local.get();
	  }

	  public static void put(Object key, Object value) {
	    if (log.isDebugEnabled()) {
	      log.debug("Bound value [" + value + "] for key [" + key + "] to thread [" + Thread.currentThread().getName() + "]");
	    }
	    if (value == null) {
	      throw new NullPointerException("the value for key (" + key + ") is null.");
	    }

	    Map<Object, Object> context = currentMap();
	    context.put(key, value);
	  }

	  public static Object get(Object key) {
	    Map<Object, Object> context = currentMap();
	    Object value = context != null ? context.get(key) : null;

	    if (log.isDebugEnabled()) {
	      log.debug("Retrieved value [" + value + "] for key [" + key + "] bound to thread [" + Thread.currentThread().getName() + "]");
	    }

	    return value;
	  }

	  public static void clean()
	  {
	    if (local == null) {
	      return;
	    }

	    local.remove();
	  }
}
