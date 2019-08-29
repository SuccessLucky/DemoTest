package com.project.common.config;

import com.project.entity.ConfigurationEntity;
import com.project.service.ConfigurationEntityService;
import com.project.utils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtils {

	private static Log logger = LogFactory.getLog(ConfigUtils.class);

	private static ConfigurationEntityService configurationService;

	private static ConfigUtils s_instance;

	public ConfigUtils() {

	}

	public static synchronized ConfigUtils shared() {
		if (s_instance == null) {
			s_instance = new ConfigUtils();
		}
		if (configurationService == null) {
			configurationService = new BeanUtils().getBean("configurationService");
		}
		return s_instance;
	}

	public String getGlobalConfig(String name) {
		ConfigurationEntity config = configurationService.getConfigurationByName(name);
		String s = "";
		if (config != null) {
			s = config.getConfigValue();
		}
		return s;
	}
}
