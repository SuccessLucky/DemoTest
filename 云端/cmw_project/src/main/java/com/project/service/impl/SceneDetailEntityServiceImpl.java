package com.project.service.impl;

import com.project.dao.SceneDetailEntityDao;
import com.project.entity.SceneDetailEntity;
import com.project.service.SceneDetailEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xieyanhao on 16/10/13.
 */
@Service
public class SceneDetailEntityServiceImpl implements SceneDetailEntityService {

    @Autowired
    private SceneDetailEntityDao sceneDetailEntityDao;

    @Override
    @Transactional
    public void addSceneDetail(SceneDetailEntity entity) {
        sceneDetailEntityDao.save(entity);
    }

    @Override
    @Transactional
    public void saveOrUpdateSceneDetail(SceneDetailEntity entity) {
        sceneDetailEntityDao.saveOrUpdate(entity);
    }

    @Override
    public SceneDetailEntity getSceneDetailById(int id) {
        return sceneDetailEntityDao.get(id);
    }

    @Override
    @Transactional
    public void updateSceneDetail(SceneDetailEntity entity) {
        sceneDetailEntityDao.update(entity);
    }

    @Override
    @Transactional
    public void deleteSceneDetail(SceneDetailEntity entity) {
        sceneDetailEntityDao.delete(entity);
    }
}
