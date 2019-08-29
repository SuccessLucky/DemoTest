package com.project.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 17/5/18.
 */
public class RoomDetailBean {

    private String floor_name;
    private Integer room_id;
    private String room_name;
    private String room_image;
    private List<DeviceResp> devices = new ArrayList<>();

    public Integer getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Integer room_id) {
        this.room_id = room_id;
    }

    public String getFloor_name() {
        return floor_name;
    }

    public void setFloor_name(String floor_name) {
        this.floor_name = floor_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_image() {
        return room_image;
    }

    public void setRoom_image(String room_image) {
        this.room_image = room_image;
    }

    public List<DeviceResp> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceResp> devices) {
        this.devices = devices;
    }
}
