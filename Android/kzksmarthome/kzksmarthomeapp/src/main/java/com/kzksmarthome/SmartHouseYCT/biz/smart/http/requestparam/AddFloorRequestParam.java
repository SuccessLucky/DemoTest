package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: AddFloorRequestParam
 * @Description: 增加楼层请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class AddFloorRequestParam {
    String floor_name;
    String image;

    public String getFloor_name() {
        return floor_name;
    }

    public void setFloor_name(String floor_name) {
        this.floor_name = floor_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
