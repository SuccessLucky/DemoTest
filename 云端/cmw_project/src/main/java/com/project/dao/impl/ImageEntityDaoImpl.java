package com.project.dao.impl;

import com.project.common.dao.BaseDaoSupport;
import com.project.dao.ImageEntityDao;
import com.project.entity.ImageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/9.
 */
@Repository
public class ImageEntityDaoImpl extends BaseDaoSupport<ImageEntity, Integer> implements ImageEntityDao{

    @Override
    public List<ImageEntity> getImagesByCategory(int imageCategory) {

        String hql = "from ImageEntity u where u.imageCategory = ? ";
        return getListByHQL(hql, imageCategory);
    }
}
