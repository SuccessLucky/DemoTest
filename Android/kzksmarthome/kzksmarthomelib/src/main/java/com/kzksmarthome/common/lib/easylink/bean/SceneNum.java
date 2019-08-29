package com.kzksmarthome.common.lib.easylink.bean;


/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class SceneNum {
    protected int id;
    protected String mac; //对应网关
    protected String bh;  //场景编号
    protected int flag;  //判断该编号是否已经被使用   0:未被使用   1:已被使用

    public SceneNum() {
    }

    public SceneNum(String mac, String bh, int flag) {
        this.mac = mac;
        this.bh = bh;
        this.flag = flag;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
