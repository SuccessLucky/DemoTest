package com.project.utils;

import com.project.common.entity.DomainObject;

import java.lang.reflect.Field;

public interface HashFieldSelector {
	public boolean shouldIncludeField(DomainObject obj, Field f);
}
