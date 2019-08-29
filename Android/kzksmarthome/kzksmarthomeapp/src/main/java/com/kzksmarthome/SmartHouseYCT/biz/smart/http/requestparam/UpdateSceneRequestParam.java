package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SimpleSceneDetailInfo;

import java.util.List;

/**
 * @Title: UpdateSceneRequestParam
 * @Description: 修改场景请求参数
 * @author laixj
 * @date 2016/10/15 7:13
 * @version V1.0
 */
public class UpdateSceneRequestParam {
    /**
     * scene_id : 3
     * name : 上学去
     * image : 0008.jpg
     * scene_details : [{"scene_detail_id":4,"device_id":1,"device_state1":1,"device_state2":0,"device_state3":0,"other_status":"30"},{"scene_detail_id":6,"device_id":3,"device_state1":1,"device_state2":0,"device_state3":0,"other_status":"50"},{"scene_detail_id":null,"device_id":3,"device_state1":1,"device_state2":0,"device_state3":0,"other_status":"50"}]
     */

    private int scene_id;
    private String name;
    private String image;
    private int scene_type;
    private String serial_number;
    private int need_linkage;
    private int need_time_delay;
    private int need_timing;
    private int need_security_on;
    private int need_security_off;
    private String delay_time;
    private String timing_time;
    private String sub_command_identifer;
    private String reserved_property;
    private String force_linkage;
    private String enabled_or_disable_identifer;
    private String arming_or_disarming_identifer;
    private String linkage_device_mac_addr;
    private String linkage_device_road;
    private String linkage_device_data_type;
    private String linkage_device_data_range;
    private String linkage_time;
    private String linkage_delay_time;

    /**
     * scene_detail_id : 4
     * device_id : 1
     * device_state1 : 1
     * device_state2 : 0
     * device_state3 : 0
     * other_status : 30
     */

    private List<SimpleSceneDetailInfo> scene_details;

    public int getScene_id() {
        return scene_id;
    }

    public void setScene_id(int scene_id) {
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

    public List<SimpleSceneDetailInfo> getScene_details() {
        return scene_details;
    }

    public void setScene_details(List<SimpleSceneDetailInfo> scene_details) {
        this.scene_details = scene_details;
    }

    public int getScene_type() {
        return scene_type;
    }

    public void setScene_type(int scene_type) {
        this.scene_type = scene_type;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public int getNeed_linkage() {
        return need_linkage;
    }

    public void setNeed_linkage(int need_linkage) {
        this.need_linkage = need_linkage;
    }

    public int getNeed_time_delay() {
        return need_time_delay;
    }

    public void setNeed_time_delay(int need_time_delay) {
        this.need_time_delay = need_time_delay;
    }

    public int getNeed_timing() {
        return need_timing;
    }

    public void setNeed_timing(int need_timing) {
        this.need_timing = need_timing;
    }

    public int getNeed_security_on() {
        return need_security_on;
    }

    public void setNeed_security_on(int need_security_on) {
        this.need_security_on = need_security_on;
    }

    public int getNeed_security_off() {
        return need_security_off;
    }

    public void setNeed_security_off(int need_security_off) {
        this.need_security_off = need_security_off;
    }

    public String getDelay_time() {
        return delay_time;
    }

    public void setDelay_time(String delay_time) {
        this.delay_time = delay_time;
    }

    public String getTiming_time() {
        return timing_time;
    }

    public void setTiming_time(String timing_time) {
        this.timing_time = timing_time;
    }

    public String getSub_command_identifer() {
        return sub_command_identifer;
    }

    public void setSub_command_identifer(String sub_command_identifer) {
        this.sub_command_identifer = sub_command_identifer;
    }

    public String getReserved_property() {
        return reserved_property;
    }

    public void setReserved_property(String reserved_property) {
        this.reserved_property = reserved_property;
    }

    public String getForce_linkage() {
        return force_linkage;
    }

    public void setForce_linkage(String force_linkage) {
        this.force_linkage = force_linkage;
    }

    public String getEnabled_or_disable_identifer() {
        return enabled_or_disable_identifer;
    }

    public void setEnabled_or_disable_identifer(String enabled_or_disable_identifer) {
        this.enabled_or_disable_identifer = enabled_or_disable_identifer;
    }

    public String getArming_or_disarming_identifer() {
        return arming_or_disarming_identifer;
    }

    public void setArming_or_disarming_identifer(String arming_or_disarming_identifer) {
        this.arming_or_disarming_identifer = arming_or_disarming_identifer;
    }

    public String getLinkage_device_mac_addr() {
        return linkage_device_mac_addr;
    }

    public void setLinkage_device_mac_addr(String linkage_device_mac_addr) {
        this.linkage_device_mac_addr = linkage_device_mac_addr;
    }

    public String getLinkage_device_road() {
        return linkage_device_road;
    }

    public void setLinkage_device_road(String linkage_device_road) {
        this.linkage_device_road = linkage_device_road;
    }

    public String getLinkage_device_data_type() {
        return linkage_device_data_type;
    }

    public void setLinkage_device_data_type(String linkage_device_data_type) {
        this.linkage_device_data_type = linkage_device_data_type;
    }

    public String getLinkage_device_data_range() {
        return linkage_device_data_range;
    }

    public void setLinkage_device_data_range(String linkage_device_data_range) {
        this.linkage_device_data_range = linkage_device_data_range;
    }

    public String getLinkage_time() {
        return linkage_time;
    }

    public void setLinkage_time(String linkage_time) {
        this.linkage_time = linkage_time;
    }

    public String getLinkage_delay_time() {
        return linkage_delay_time;
    }

    public void setLinkage_delay_time(String linkage_delay_time) {
        this.linkage_delay_time = linkage_delay_time;
    }

    @Override
    public String toString() {
        return "UpdateSceneRequestParam{" +
                "scene_id=" + scene_id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", scene_type=" + scene_type +
                ", serial_number='" + serial_number + '\'' +
                ", need_linkage=" + need_linkage +
                ", need_time_delay=" + need_time_delay +
                ", need_timing=" + need_timing +
                ", need_security_on=" + need_security_on +
                ", need_security_off=" + need_security_off +
                ", delay_time='" + delay_time + '\'' +
                ", timing_time='" + timing_time + '\'' +
                ", sub_command_identifer='" + sub_command_identifer + '\'' +
                ", reserved_property='" + reserved_property + '\'' +
                ", force_linkage='" + force_linkage + '\'' +
                ", enabled_or_disable_identifer='" + enabled_or_disable_identifer + '\'' +
                ", arming_or_disarming_identifer='" + arming_or_disarming_identifer + '\'' +
                ", linkage_device_mac_addr='" + linkage_device_mac_addr + '\'' +
                ", linkage_device_road='" + linkage_device_road + '\'' +
                ", linkage_device_data_type='" + linkage_device_data_type + '\'' +
                ", linkage_device_data_range='" + linkage_device_data_range + '\'' +
                ", linkage_time='" + linkage_time + '\'' +
                ", linkage_delay_time='" + linkage_delay_time + '\'' +
                ", scene_details=" + scene_details +
                '}';
    }
}
