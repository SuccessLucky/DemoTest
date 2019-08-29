package com.project.service.impl;

import com.project.dao.ImageEntityDao;
import com.project.entity.ImageEntity;
import com.project.service.ImageEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xieyanhao on 16/10/9.
 */
@Service
public class ImageEntityServiceImpl implements ImageEntityService {

    @Autowired
    private ImageEntityDao imageEntityDao;

    @Override
    public List<ImageEntity> getImagesByCategory(int imageCategory) {
        return imageEntityDao.getImagesByCategory(imageCategory);
    }
}
