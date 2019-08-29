package com.project.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/20.
 */
public class PermissionsResp {

    Integer member_id;
    List<SceneItemBean> scenes = new ArrayList<>();
    List<DeviceItemBean> devices = new ArrayList<>();

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public List<SceneItemBean> getScenes() {
        return scenes;
    }

    public void setScenes(List<SceneItemBean> scenes) {
        this.scenes = scenes;
    }

    public List<DeviceItemBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceItemBean> devices) {
        this.devices = devices;
    }

    public static class SceneItemBean {

        private Integer scene_id;
        private String name;
        private String image;

        public Integer getScene_id() {
            return scene_id;
        }

        public void setScene_id(Integer scene_id) {
            this.scene_id = scene_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class DeviceItemBean {

        private Integer device_id;
        private String device_name;
        private String image;
        private Integer room_id;
        private Integer floor_id;
        private String room_name;
        private String floor_name;

        public Integer getDevice_id() {
            return device_id;
        }

        public void setDevice_id(Integer device_id) {
            this.device_id = device_id;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getRoom_id() {
            return room_id;
        }

        public void setRoom_id(Integer room_id) {
            this.room_id = room_id;
        }

        public Integer getFloor_id() {
            return floor_id;
        }

        public void setFloor_id(Integer floor_id) {
            this.floor_id = floor_id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getFloor_name() {
            return floor_name;
        }

        public void setFloor_name(String floor_name) {
            this.floor_name = floor_name;
        }
    }

}
