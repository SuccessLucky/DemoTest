package com.project.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/19.
 */
public class PermissionsBean {

    List<Integer> scenes = new ArrayList<>();
    List<Integer> devices = new ArrayList<>();

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
