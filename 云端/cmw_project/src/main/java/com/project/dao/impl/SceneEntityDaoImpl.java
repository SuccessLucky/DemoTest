package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.SceneEntityDao;
import com.project.entity.SceneEntity;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/11.
 */
@Repository
public class SceneEntityDaoImpl extends BaseDaoSupport<SceneEntity, Integer> implements SceneEntityDao {

    @Override
    public List<SceneEntity> getScenesByGateway(int gatewayId, int sceneType) {
        String hql = "from SceneEntity s where s.gateway.id = ? ";
        if (sceneType >= 0) {
            hql += " and sceneType = ? ";
            return getListByHQL(hql, gatewayId, sceneType);
        } else {
            return getListByHQL(hql, gatewayId);
        }
    }

    @Override
    public List<SceneEntity> getScenesByIds(List<Integer> ids) {
        if (ids.size() == 0) {
            return null;
        }

        String hql = "from SceneEntity s where s.id in (:ids) ";
        Query query = getSession().createQuery(hql);
        query.setParameterList("ids", ids.toArray());
        return query.list();
    }
}
