package com.project.service;

import com.project.bean.SceneReq;
import com.project.bean.SceneResp;
import com.project.entity.GatewayEntity;
import com.project.entity.SceneEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/11.
 */
public interface SceneEntityService {

    public SceneEntity addScene(SceneReq sceneBean, GatewayEntity gateway) throws Exception;
    public void saveOrUpdateScene(SceneEntity entity);
    public List<SceneEntity> getScenesByGateway(int gatewayId, int sceneType);
    public SceneEntity getSceneById(int id);
    public SceneResp getSceneRespByScene(SceneEntity sceneEntity);
    public void updateScene(SceneReq sceneBean, SceneEntity sceneEntity) throws Exception;
    public void deleteScene(SceneEntity entity);

}
