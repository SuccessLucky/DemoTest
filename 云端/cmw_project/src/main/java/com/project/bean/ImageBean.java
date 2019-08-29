package com.project.bean;

/**
 * Created by xieyanhao on 16/10/9.
 */
public class ImageBean {

    private Integer id;
    private String base_url;
    private String name; // 图片名称
    private String image_type; // 图片格式

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }
}
