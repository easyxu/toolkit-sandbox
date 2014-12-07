package com.phoenix.dal.hibernate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Converters {

	private static Map<Class<?>, Converter<?>> converters = new HashMap<Class<?>, Converter<?>>();

	private final static String NUMBER_ERR = "number format err :";
	private final static String DATE_ERR = "Date format err :";
	static {
		converters.put(Integer.class, new Converter<Integer>() {
			public Integer cast(Object src) throws ConverterException {

				if (src == null)
					return null;

				if (src instanceof Number) {
					return ((Number) src).intValue();
				} else {
					try {
						return Integer.parseInt(src.toString());
					} catch (NumberFormatException ex) {
						throw new ConverterException(NUMBER_ERR
								+ src.toString());
					}
				}

			}
		});
		converters.put(String.class, new Converter<String>() {
			public String cast(Object src) throws ConverterException {
				if (src == null)
					return null;
				return src.toString();
			}
		});
		converters.put(Long.class, new Converter<Long>() {
			public Long cast(Object src) throws ConverterException {

				if (src == null)
					return null;
				if (src instanceof Number) {
					return ((Number) src).longValue();
				}
				else {
					try {
						return Long.parseLong(src.toString());
					} catch (NumberFormatException ex) {
						throw new ConverterException(NUMBER_ERR
								+ src.toString());
					}
				}

			}
		});

		converters.put(Date.class, new Converter<Date>() {
			@SuppressWarnings("deprecation")
            public Date cast(Object src) throws ConverterException {

				if (src == null)
					return null;
				if (src instanceof java.sql.Date) {
					return new Date(((java.sql.Date) src).getTime());
				}
				else {
					try {
						return new Date(src.toString());
					} catch (NumberFormatException ex) {
						throw new ConverterException(DATE_ERR
								+ src.toString());
					}
				}

			}
		});

		converters.put(Boolean.class, new Converter<Boolean>() {
			public Boolean cast(Object src) throws ConverterException {
				if (src == null)
					return null;
				try {
					return Boolean.valueOf(src.toString());
				} catch (NumberFormatException ex) {
					throw new ConverterException(NUMBER_ERR
							+ src.toString());
				}
			}
		});
		converters.put(boolean.class, new Converter<Boolean>() {
			public Boolean cast(Object src) throws ConverterException {
				if (src == null)
					return null;
				try {
					return Boolean.valueOf(src.toString());
				} catch (NumberFormatException ex) {
					throw new ConverterException(NUMBER_ERR
							+ src.toString());
				}
			}
		});

	}

	public static Converter<?> getConverter(Class<?> targetType) {
		return converters.get(targetType);
	}

}
