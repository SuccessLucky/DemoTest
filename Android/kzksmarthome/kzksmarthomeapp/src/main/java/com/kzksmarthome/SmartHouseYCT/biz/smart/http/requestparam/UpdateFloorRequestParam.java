package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: UpdateFloorRequestParam
 * @Description: 修改楼层请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class UpdateFloorRequestParam {
    int floor_id;
    String floor_name;
    String image;

    public int getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

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
