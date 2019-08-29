package com.project.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContext implements ApplicationContextAware {

	private static org.springframework.context.ApplicationContext application;

	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
		ApplicationContext.application = applicationContext;
	}

	public static Object getBean(String beanName) {
		if (application == null) {
			throw new IllegalStateException("bean factory is null");
		}
		Object bean = application.getBean(beanName);
		if (bean == null) {
			throw new IllegalStateException("bean is null, name[" + beanName + "]");
		}
		return bean;
	}
}
