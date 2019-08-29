package com.project.service;

import com.project.entity.ImageEntity;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/9.
 */
public interface ImageEntityService {

    public List<ImageEntity> getImagesByCategory(int imageCategory);

}
