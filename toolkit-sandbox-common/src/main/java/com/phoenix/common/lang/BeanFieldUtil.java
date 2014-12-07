package com.phoenix.common.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanFieldUtil {

	public final static Logger logger = LoggerFactory
			.getLogger(BeanFieldUtil.class);

	public static Object getNestedFieldValue(String expression, Object bean) {
		// 如果表达式为空就返回本对象
		if (expression == null || "".equals(expression.trim())) {
			return bean;
		}

		Object ret = null;
		StringTokenizer tokenizer = new StringTokenizer(expression, ".");

		String token = null;
		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();

			try {
				ret = getFieldValue(token, bean);
				if (ret == null)
					break;
				bean = ret;
			} catch (SecurityException e) {// 不会发生
				e.printStackTrace();
			} catch (IllegalArgumentException e) {// 如果定义的get方法不符合javabean的定义规范则抛出
				logger.error(e.getMessage());
			} catch (NoSuchMethodException e) {// 如果没有此方法，抛出异常
				logger.error(e.getMessage());
			} catch (IllegalAccessException e) {// 不会发生
				e.printStackTrace();
			} catch (InvocationTargetException e) {// 不会发生
				e.printStackTrace();
			}
		}
		return ret;

	}

	public static Object getFieldValue(String fieldName, Object bean)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		String s = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		Method method = bean.getClass().getMethod(s);
		return method.invoke(bean);
	}
}
