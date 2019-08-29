package com.project.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * 在 spring 容器中获取对象
 *
 */
public class BeanUtils {

    public <BEAN_TYPE> BEAN_TYPE getBean(String beanName) {

        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();

        return (BEAN_TYPE) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(beanName);
    }

}
