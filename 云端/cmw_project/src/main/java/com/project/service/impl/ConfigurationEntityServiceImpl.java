package com.project.service.impl;

import com.project.dao.ConfigurationEntityDao;
import com.project.entity.ConfigurationEntity;
import com.project.service.ConfigurationEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xieyanhao on 17/7/18.
 */
@Service("configurationService")
public class ConfigurationEntityServiceImpl implements ConfigurationEntityService{

    @Autowired
    private ConfigurationEntityDao configurationEntityDao;

    @Override
    public ConfigurationEntity getConfigurationById(int id) {
        return configurationEntityDao.get(id);
    }

    @Override
    public ConfigurationEntity getConfigurationByName(String name) {
        return configurationEntityDao.getConfigurationByName(name);
    }

    @Override
    public List<ConfigurationEntity> getAllConfigurations() {
        return configurationEntityDao.getAllConfigurations();
    }
}
