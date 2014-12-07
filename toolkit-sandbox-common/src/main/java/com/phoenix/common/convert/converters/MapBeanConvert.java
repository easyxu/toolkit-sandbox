package com.phoenix.common.convert.converters;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Example: 
 * Map map = new HashMap(); 
 * map.put("email", "email");
 * map.put("endtm", "2007-01-11");
 * map.put("password", "23232");
 * map.put("roleid", "1"); 
 * map.put("status", "3"); 
 * User user = (User)MapBeanConvert.mapTolass( "org.User", map); 
 * System.out.println(user.getPassword());
 * System.out.println(user.getEmail()); 
 * System.out.println(user.getStatus());
 * System.out.println(user.getEndtm());
 * 
 * @author  xuxiang
 * 
 */
@SuppressWarnings("unchecked")
public class MapBeanConvert {

	static Logger logger = LoggerFactory
			.getLogger(MapBeanConvert.class);

	/**
	 * Mapת
	 * 	 * 
	 * @param formName
	 *            bean名称
	 * @param map
	 * @return
	 */

	public static Object mapToclass(String formName, Map map) {
		Object bean = null;
		try {
			Class classType = Class.forName(formName);
			bean = classType.newInstance();
			PropertyDescriptor props[] = propertyDescriptors(classType);
			for (int i = 0; i < props.length; i++) {
				Object object = map.get(props[i].getName());
				if (object != null) {
					Method setter = props[i].getWriteMethod();
					object = processType(object, props[i].getPropertyType());
					setter.invoke(bean, new Object[] { object });
				}
			}
		} catch (Exception e) {
			logger.error("map2class:", e);
		}
		return bean;
	}

	/**
	 * 
	 * 
	 * @param object
	 * @param propType
	 * @return
	 */
	private static Object processType(Object object, Class propType) {
		Object reobject = null;
		if (propType.equals(String.class)) {
			reobject = (String) object;
		} else if (propType.equals(Integer.TYPE)
				|| propType.equals(Integer.class)) {
			reobject = new Integer((String) object);
		} else if (propType.equals(Boolean.TYPE)
				|| propType.equals(Boolean.class)) {
			reobject = new Boolean((String) object);
		} else if (propType.equals(Long.TYPE)
				|| propType.equals(Long.class)) {
			reobject = new Long((String) object);
		} else if (propType.equals(Double.TYPE)
				|| propType.equals(Double.class)) {
			reobject = new Double((String) object);
		} else if (propType.equals(Float.TYPE)
				|| propType.equals(Float.class)) {
			reobject = new Float((String) object);
		} else if (propType.equals(Short.TYPE)
				|| propType.equals(Short.class)) {
			reobject = new Short((String) object);
		} else if (propType.equals(Byte.TYPE)
				|| propType.equals(Byte.class)) {
			reobject = new Byte((String) object);
		} else if (propType.equals(Date.class)) {
			reobject = processDate(object);
		}
		return reobject;
	}

	/**
	 * 
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static Date processDate(Object object) {

		if (object != null) {
			String dateStr = (String) object;
			dateStr = dateStr.replaceAll("-", "");
			dateStr = dateStr.replaceAll(":", "");
			dateStr = dateStr.trim();
			int year = 1900;
			int month = 1;
			int date = 1;
			int hrs = 0;
			int min = 0;
			int sec = 0;
			if (dateStr.length() >= 4) {
				year = Integer.parseInt(dateStr.substring(0, 4));
			}
			if (dateStr.length() >= 6) {
				month = Integer.parseInt(dateStr.substring(4, 6));
			}
			if (dateStr.length() >= 8) {
				date = Integer.parseInt(dateStr.substring(6, 8));
			}
			if (dateStr.length() >= 10) {
				hrs = Integer.parseInt(dateStr.substring(8, 10));
			}
			if (dateStr.length() >= 12) {
				min = Integer.parseInt(dateStr.substring(10, 12));
			}
			if (dateStr.length() >= 14) {
				sec = Integer.parseInt(dateStr.substring(12, 14));
			}
			return new Date(year, month - 1, date, hrs, min, sec);
		} else {
			return null;
		}

	}

	private static PropertyDescriptor[] propertyDescriptors(Class c) {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);
		} catch (IntrospectionException e) {
			logger.error("PropertyDescriptor:", e);
		}
		return beanInfo.getPropertyDescriptors();
	}
}
