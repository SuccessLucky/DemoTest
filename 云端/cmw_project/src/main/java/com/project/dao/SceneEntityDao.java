package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.SceneEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/11.
 */
public interface SceneEntityDao extends IBaseDao<SceneEntity, Integer > {

    public List<SceneEntity> getScenesByGateway(int gatewayId, int sceneType);
    public List<SceneEntity> getScenesByIds(List<Integer> ids);

}
