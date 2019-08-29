package com.kzksmarthome.common.lib.easylink.bean;


/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class SceneEquipments {
    protected int id;
    protected String mac; //wifi上gateway网关
    protected String equipments_id; //场景执行设备集合id
    protected String equipment_id; //设备id
    protected String start_cmd; //设备指令

    public SceneEquipments() {
    }

    public SceneEquipments(String gateway_mac, String equipments_id, String equipment_id, String start_cmd) {
        this.mac=gateway_mac;
        this.equipments_id = equipments_id;
        this.equipment_id = equipment_id;
        this.start_cmd = start_cmd;
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

    public String getEquipments_id() {
        return equipments_id;
    }

    public void setEquipments_id(String equipments_id) {
        this.equipments_id = equipments_id;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
    }

    public String getStart_cmd() {
        return start_cmd;
    }

    public void setStart_cmd(String start_cmd) {
        this.start_cmd = start_cmd;
    }
}
