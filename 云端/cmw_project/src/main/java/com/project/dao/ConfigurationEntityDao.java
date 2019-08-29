package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.ConfigurationEntity;

import java.util.List;

/**
 * Created by xieyanhao on 17/7/18.
 */
public interface ConfigurationEntityDao extends IBaseDao<ConfigurationEntity, Integer > {

    public ConfigurationEntity getConfigurationByName(String name);
    public List<ConfigurationEntity> getAllConfigurations();

}
