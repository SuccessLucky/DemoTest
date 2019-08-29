package com.project.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface RoleControl {
	/**
	 *
	 * 角色类型，以便决定是否具有相关权限
	 */
	String value() default "user";
}
