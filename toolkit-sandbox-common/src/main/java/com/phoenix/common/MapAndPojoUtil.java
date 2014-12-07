/**
 * 
 */
package com.phoenix.common;


import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.phoenix.common.lang.StringUtil;
import com.phoenix.common.logging.Logger;
import com.phoenix.common.logging.LoggerFactory;
/**
 * @author xuxiang<xiang.leau@gmail.com>
 * @date 2011-8-2 下午06:51:35
 */
public class MapAndPojoUtil {
	private static final Logger log = LoggerFactory.getLogger(MapAndPojoUtil.class);
	  public static final String DEYU = "=";
	  public static final String MAOHAO = ";";

	  public static void fromPojoToMap(Object pojo, Map<String, Object> map)
	  {
	    if (pojo == null) {
	      return;
	    }

	    Date date = null;
	    Integer num = null;
	    String fieldName = null;
	    String fieldValue = null;
	    String methodName = null;
	    SimpleDateFormat dateFormat = null;
	    Class<?> clazz = pojo.getClass();
	    Method[] methods = clazz.getDeclaredMethods();

	    for (Method method : methods) {
	      methodName = method.getName();
	      if ((methodName.startsWith("get")) && (!methodName.equals("get"))) {
	        fieldName = methodName.substring(3);
	        if (fieldName.length() > 1) {
	          fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
	        }
	        else {
	          fieldName = fieldName.toLowerCase();
	        }
	        try
	        {
	          fieldValue = BeanUtils.getProperty(pojo, fieldName);
	          if (StringUtil.isNotEmpty(fieldValue)) {
	            if (method.getReturnType() == Date.class) {
	              dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

	              date = dateFormat.parse(fieldValue);
	              map.put(fieldName, date);
	            } else if (method.getReturnType() == Integer.class) {
	              num = Integer.valueOf(fieldValue);
	              map.put(fieldName, num);
	            } else {
	              map.put(fieldName, fieldValue);
	            }
	          }
	          else map.put(fieldName, fieldValue); 
	        }
	        catch (Exception e)
	        {
	          log.error("MapAndPojoUtil.fromPojoToMap Fail! Description:[Pojo拷贝到Map中出错fieldName=" + fieldName + "]" + e, e);
	          map = null;
	          return;
	        }

	        if (log.isDebugEnabled())
	          log.debug("将值" + fieldValue + "放入键" + fieldName);
	      }
	    }
	  }

	  public static void fromMapToPojo(Map<String, Object> map, Object pojo)
	  {
	    if (map == null) {
	      return;
	    }

	    String fieldName = null;
	    String methodName = null;
	    Object value = null;
	    Class<?> clazz = pojo.getClass();
	    Method[] methods = clazz.getDeclaredMethods();

	    for (Method method : methods) {
	      methodName = method.getName();
	      if ((methodName.startsWith("set")) && (!methodName.equals("set"))) {
	        fieldName = methodName.substring(3);
	        if (fieldName.length() > 1) {
	          fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
	        }
	        else {
	          fieldName = fieldName.toLowerCase();
	        }

	        value = map.get(fieldName);
	        if (log.isDebugEnabled())
	          log.debug("将值" + value + "放入键" + fieldName);
	        try
	        {
	          BeanUtils.setProperty(pojo, fieldName, value);
	        } catch (Exception e) {
	          log.error("MapAndPojoUtil.fromMapToPojo Fail! Description:[Map拷贝到Pojo中出错fieldName=" + fieldName + "]" + e, e);
	          pojo = null;
	          return;
	        }
	      }
	    }
	  }

	  public static String fromPojoToString(Object pojo)
	  {
	    if (pojo == null) {
	      return null;
	    }

	    String fieldName = null;
	    String fieldValue = null;
	    String methodName = null;
	    StringBuffer result = new StringBuffer();

	    Class<?> clazz = pojo.getClass();
	    Method[] methods = clazz.getDeclaredMethods();

	    for (Method method : methods) {
	      methodName = method.getName();
	      if ((methodName.startsWith("set")) && (!methodName.equals("set"))) {
	        fieldName = methodName.substring(3);
	        if (fieldName.length() > 1) {
	          fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
	        }
	        else {
	          fieldName = fieldName.toLowerCase();
	        }
	        try
	        {
	          fieldValue = BeanUtils.getProperty(pojo, fieldName);
	        } catch (Exception e) {
	          log.error("【av】MapAndPojoUtil.fromPojoToString Fail! Description:[Pojo转化为String时中出错fieldName=" + fieldName + "]" + e, e);
	          return null;
	        }

	        result.append(fieldName + "=" + fieldValue + ";");
	      }
	    }
	    return result.toString();
	  }
}
