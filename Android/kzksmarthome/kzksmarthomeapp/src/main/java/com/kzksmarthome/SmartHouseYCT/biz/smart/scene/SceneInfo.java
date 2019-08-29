package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * @Title: SceneInfo
 * @Description: 场景模式信息Bean
 * @author laixj
 * @date 2016/9/14 14:09
 * @version V1.0
 */
public class SceneInfo extends BaseModel implements Parcelable ,Serializable{
    /**
     * scene_id : 2
     * name : 回家
     * image : 0002.jpg
     * scene_details : [{"scene_detail_id":1,"device_id":1,"room_id":1,"device_name":"测试天天","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"30","mac_address":"192.168.1.1"},{"scene_detail_id":2,"device_id":2,"room_id":1,"device_name":"测试天天2","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"70","mac_address":"192.168.1.2"}]
     */

    private int scene_id;
    private String name;
    private String image;
    private int scene_type;
    /**
     * 场景编号
     */
    private String serial_number;
    private boolean selected;




    private List<SceneDetailInfo> scene_details;
    /**
     * need_linkage : 1
     * need_time_delay : 1
     * need_timing : 1
     * need_security_on : 1
     * need_security_off : 1
     * delay_time : 30
     * timing_time : 15:30
     * sub_command_identifer : null
     * reserved_property : null
     * force_linkage : null
     * enabled_or_disable_identifer : null
     * arming_or_disarming_identifer : null
     * linkage_device_mac_addr : null
     * linkage_device_road : null
     * linkage_device_data_type : null
     * linkage_device_data_range : null
     * linkage_time : null
     * linkage_delay_time : null
     */
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<SceneDetailInfo> getScene_details() {
        return scene_details;
    }

    public void setScene_details(List<SceneDetailInfo> scene_details) {
        this.scene_details = scene_details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SceneInfo sceneInfo = (SceneInfo) o;

        return scene_id == sceneInfo.scene_id;

    }

    @Override
    public int hashCode() {
        return scene_id;
    }

    @Override
    public String toString() {
        return "SceneInfo{" +
                "scene_id=" + scene_id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", scene_type=" + scene_type +
                ", serial_number='" + serial_number + '\'' +
                ", selected=" + selected +
                ", scene_details=" + scene_details +
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
                '}';
    }

    public SceneInfo() {
    }

    public SceneInfo(int scene_id, String name) {
        this.scene_id = scene_id;
        this.name = name;
    }

    public SceneInfo(int scene_id) {
        this.scene_id = scene_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.scene_id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeInt(this.scene_type);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.scene_details);
    }

    protected SceneInfo(Parcel in) {
        this.scene_id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
        this.scene_type = in.readInt();
        this.selected = in.readByte() != 0;
        this.scene_details = in.createTypedArrayList(SceneDetailInfo.CREATOR);
    }

    public static final Creator<SceneInfo> CREATOR = new Creator<SceneInfo>() {
        @Override
        public SceneInfo createFromParcel(Parcel source) {
            return new SceneInfo(source);
        }

        @Override
        public SceneInfo[] newArray(int size) {
            return new SceneInfo[size];
        }
    };

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
}
