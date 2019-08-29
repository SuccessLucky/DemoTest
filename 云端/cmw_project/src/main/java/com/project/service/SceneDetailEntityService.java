package com.project.service;

import com.project.entity.SceneDetailEntity;

/**
 * Created by xieyanhao on 16/10/13.
 */
public interface SceneDetailEntityService {

    public void addSceneDetail(SceneDetailEntity entity);
    public void saveOrUpdateSceneDetail(SceneDetailEntity entity);
    public SceneDetailEntity getSceneDetailById(int id);
    public void updateSceneDetail(SceneDetailEntity entity);
    public void deleteSceneDetail(SceneDetailEntity entity);

}
