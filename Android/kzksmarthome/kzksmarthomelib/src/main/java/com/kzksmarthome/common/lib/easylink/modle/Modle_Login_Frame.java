package com.kzksmarthome.common.lib.easylink.modle;

/**
 * 登入帧获取信息对象
 * Created by Administrator on 2016/9/9 0009.
 */
public class Modle_Login_Frame {
    private String index;   //索引
    private String system_state;  //系统状态字
    private String pattern; //模式状态
    private String user_pwd;  //用户密码
    private String universal_pwd; //万能密码
    private String wifi_mac;  //wifi的mac地址
    private String zb_mac;  //zigbee的mac地址
    private String product_id; //产品序列号
    private String product_code; //产品验证码
    private String software_version; //软件版本号
    private String hardware_version; //硬件版本号

    public Modle_Login_Frame(String index, String system_state, String pattern, String user_pwd, String universal_pwd, String wifi_mac, String zb_mac, String product_id, String product_code, String software_version, String hardware_version) {
        this.index = index;
        this.system_state = system_state;
        this.pattern = pattern;
        this.user_pwd = user_pwd;
        this.universal_pwd = universal_pwd;
        this.wifi_mac = wifi_mac;
        this.zb_mac = zb_mac;
        this.product_id = product_id;
        this.product_code = product_code;
        this.software_version = software_version;
        this.hardware_version = hardware_version;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getSystem_state() {
        return system_state;
    }

    public void setSystem_state(String system_state) {
        this.system_state = system_state;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getUniversal_pwd() {
        return universal_pwd;
    }

    public void setUniversal_pwd(String universal_pwd) {
        this.universal_pwd = universal_pwd;
    }

    public String getWifi_mac() {
        return wifi_mac;
    }

    public void setWifi_mac(String wifi_mac) {
        this.wifi_mac = wifi_mac;
    }

    public String getZb_mac() {
        return zb_mac;
    }

    public void setZb_mac(String zb_mac) {
        this.zb_mac = zb_mac;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getSoftware_version() {
        return software_version;
    }

    public void setSoftware_version(String software_version) {
        this.software_version = software_version;
    }

    public String getHardware_version() {
        return hardware_version;
    }

    public void setHardware_version(String hardware_version) {
        this.hardware_version = hardware_version;
    }
}
