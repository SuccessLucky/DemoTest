package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: UpdateRoomRequestParam
 * @Description: 修改房间请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class UpdateRoomRequestParam {
    int room_id;
    String image;
    String room_name;

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }
}
