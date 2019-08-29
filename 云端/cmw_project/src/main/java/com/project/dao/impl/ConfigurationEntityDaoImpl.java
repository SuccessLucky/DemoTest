package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.ConfigurationEntityDao;
import com.project.entity.ConfigurationEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 17/7/18.
 */
@Repository
public class ConfigurationEntityDaoImpl extends BaseDaoSupport<ConfigurationEntity, Integer> implements ConfigurationEntityDao {
	
    @Override
    public ConfigurationEntity getConfigurationByName(String name) {
        String hql = "from ConfigurationEntity c where c.configName = ? ";
        List<ConfigurationEntity> rs = getListByHQL(hql, name);
        if (rs.size() >= 1)
            return rs.get(0);

        return null;
    }

    @Override
    public List<ConfigurationEntity> getAllConfigurations() {

        String hql = "form ConfigurationEntity";

        return getListByHQL(hql);
    }
}
