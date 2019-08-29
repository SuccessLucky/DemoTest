package com.project.dao;

import com.project.common.dao.IBaseDao;
import com.project.entity.ImageEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/9.
 */
public interface ImageEntityDao extends IBaseDao<ImageEntity, Integer > {

    public List<ImageEntity> getImagesByCategory(int imageCategory);

}
