package com.phoenix.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class SpringObjectFactoryHolder implements BeanFactoryAware{
	private static BeanFactory beanFactory;
	public static BeanFactory getBeanFactory(){
		return beanFactory;
	}

	@SuppressWarnings("static-access")
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
