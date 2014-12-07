package com.phoenix.dal.hibernate;

public interface Converter<T> {
	public T cast(Object src) throws ConverterException;
}
