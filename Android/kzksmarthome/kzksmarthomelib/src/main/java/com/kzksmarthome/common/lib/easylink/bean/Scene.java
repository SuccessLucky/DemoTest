package com.kzksmarthome.common.lib.easylink.bean;



/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class Scene {
    protected int id;
    protected String mac; //对应网关
    protected String scene_id;  //场景编号
    protected String scene_type;  //场景类型(0:联动   1:场景)
    protected String scene_type_id; //场景代号
    protected String scene_name; //场景昵称
    protected String equipments_id; //场景执行设备集合id
    protected String start_cmd; //场景触发指令
    protected String delete_cmd; //场景删除指令

    public Scene() {
    }

    public Scene(String mac, String scene_id, String scene_type, String scene_type_id, String scene_name, String equipments_id, String start_cmd, String delete_cmd) {
        this.mac=mac;
        this.scene_id = scene_id;
        this.scene_type = scene_type;
        this.scene_type_id = scene_type_id;
        this.scene_name = scene_name;
        this.equipments_id = equipments_id;
        this.start_cmd = start_cmd;
        this.delete_cmd=delete_cmd;
    }

    public String getDelete_cmd() {
        return delete_cmd;
    }

    public void setDelete_cmd(String delete_cmd) {
        this.delete_cmd = delete_cmd;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScene_id() {
        return scene_id;
    }

    public void setScene_id(String scene_id) {
        this.scene_id = scene_id;
    }

    public String getScene_type() {
        return scene_type;
    }

    public void setScene_type(String scene_type) {
        this.scene_type = scene_type;
    }

    public String getScene_type_id() {
        return scene_type_id;
    }

    public void setScene_type_id(String scene_type_id) {
        this.scene_type_id = scene_type_id;
    }

    public String getScene_name() {
        return scene_name;
    }

    public void setScene_name(String scene_name) {
        this.scene_name = scene_name;
    }

    public String getEquipments_id() {
        return equipments_id;
    }

    public void setEquipments_id(String equipments_id) {
        this.equipments_id = equipments_id;
    }

    public String getStart_cmd() {
        return start_cmd;
    }

    public void setStart_cmd(String start_cmd) {
        this.start_cmd = start_cmd;
    }
}
