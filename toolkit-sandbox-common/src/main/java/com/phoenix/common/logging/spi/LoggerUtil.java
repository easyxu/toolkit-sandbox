/**
 * 
 */
package com.phoenix.common.logging.spi;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.phoenix.common.lang.MessageUtil;
import com.phoenix.common.logging.Logger;
/**
 * TODO
 * @author xiang.xu
 * @时间 2011-5-6 上午01:29:26
 */
public class LoggerUtil {

	public static String getMessage(Logger logger, Object key, Object[] params)
	  {
	    ResourceBundle bundle = logger.getResourceBundle();
	    String keyStr = String.valueOf(key);
	    String message = null;

	    if (bundle == null)
	      logger.error("Resource bundle not set for logger \"" + logger + "\"");
	    else {
	      try
	      {
	        message = MessageUtil.getMessage(bundle, keyStr, params);
	      } catch (MissingResourceException e) {
	        logger.error("No resource is associated with key \"" + keyStr + "\" in logger \"" + logger + "\"", e);
	      }

	    }

	    return message == null ? keyStr : message;
	  }
}
