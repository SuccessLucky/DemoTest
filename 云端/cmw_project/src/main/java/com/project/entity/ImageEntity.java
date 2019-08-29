package com.project.entity;

import com.project.common.entity.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xieyanhao on 16/9/30.
 */
@Entity
@Table(name = "image_tbl")
public class ImageEntity extends DomainObject {

    private String name; // 图片名称
    private String imageType; // 图片格式
    private int imageCategory; // 图片分类

    public static final int IMAGE_CATEGORY_COMMON = 0; // 公共
    public static final int IMAGE_CATEGORY_DEVICE = 1; // 设备
    public static final int IMAGE_CATEGORY_ROOM = 2; // 房间
    public static final int IMAGE_CATEGORY_SCENE_ = 3; // 场景

    public static Map<Integer, String> imageCategoryMap = new HashMap<Integer, String>();
    static {
        imageCategoryMap.put(0, "common/");
        imageCategoryMap.put(1, "device/");
        imageCategoryMap.put(2, "room/");
        imageCategoryMap.put(3, "scene/");
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "image_type")
    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @Column(name = "image_category", columnDefinition = "tinyint default 0")
    public int getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(int imageCategory) {
        this.imageCategory = imageCategory;
    }
}
