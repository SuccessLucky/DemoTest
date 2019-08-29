package com.project.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 17/5/21.
 */
public class RoomSequenceReq {


    Integer room_id;
    List<Integer> device_ids = new ArrayList<>();

    public Integer getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Integer room_id) {
        this.room_id = room_id;
    }

    public List<Integer> getDevice_ids() {
        return device_ids;
    }

    public void setDevice_ids(List<Integer> device_ids) {
        this.device_ids = device_ids;
    }
}
