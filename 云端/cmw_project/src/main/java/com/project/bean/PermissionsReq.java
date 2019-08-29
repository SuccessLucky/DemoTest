package com.project.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/19.
 */
public class PermissionsReq {

    Integer member_id;
    List<Integer> scenes = new ArrayList<>();
    List<Integer> devices = new ArrayList<>();

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public List<Integer> getScenes() {
        return scenes;
    }

    public void setScenes(List<Integer> scenes) {
        this.scenes = scenes;
    }

    public List<Integer> getDevices() {
        return devices;
    }

    public void setDevices(List<Integer> devices) {
        this.devices = devices;
    }
}
