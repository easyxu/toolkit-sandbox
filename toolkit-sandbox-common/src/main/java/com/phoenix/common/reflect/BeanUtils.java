package com.phoenix.common.reflect;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
/**
 *扩展Apache Commons BeanUtils, 提供一些反射方面缺失功能的封装.
 */
@SuppressWarnings("unchecked")
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BeanUtils.class);

	private BeanUtils() {
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException	如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {

		return getDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException
	 */
	public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException {
	
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				//  Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}

	/**
	 * 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException
	 */
	public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
	

		Field field = getDeclaredField(object, propertyName);

		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.info("error wont' happen");
		}
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException
	 */
	public static void forceSetProperty(Object object, String propertyName, Object newValue)
			throws NoSuchFieldException {
		
		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (IllegalAccessException e) {
			logger.info("Error won't happen");
		}
		field.setAccessible(accessible);
	}

	/**
	 * 执行私有方法
	 *
	 * @throws NoSuchMethodException
	 */
	public static Object invokePrivateMethod(Object object, String methodName, Object... params)
			throws NoSuchMethodException {
	
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				
			}
		}

		if (method == null)
			throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		method.setAccessible(accessible);
		return result;
	}

	/**
	 * ��Filed������ȡ��Field�б�.
	 */
	public static List<Field> getFieldsByType(Object object, Class type) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(type)) {
				list.add(field);
			}
		}
		return list;
	}


	
	public static Class getPropertyType(Class type, String name) throws NoSuchFieldException {
		return getDeclaredField(type, name).getType();
	}


	public static String getGetterName(Class type, String fieldName) {
	

		if (type.getName().equals("boolean")) {
			return "is" + StringUtils.capitalize(fieldName);
		} else {
			return "get" + StringUtils.capitalize(fieldName);
		}
	}


	public static Method getGetterMethod(Class type, String fieldName) {
		try {
			return type.getMethod(getGetterName(type, fieldName));
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
