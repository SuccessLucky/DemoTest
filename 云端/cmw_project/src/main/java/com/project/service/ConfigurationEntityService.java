package com.project.service;

import com.project.entity.ConfigurationEntity;

import java.util.List;

/**
 * Created by xieyanhao on 17/7/18.
 */
public interface ConfigurationEntityService {

    public ConfigurationEntity getConfigurationById(int id);
    public ConfigurationEntity getConfigurationByName(String name);
    public List<ConfigurationEntity> getAllConfigurations();

}
