package com.kzksmarthome.common.lib.easylink.bean;


/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class CustomLearning {
    protected int id;
    protected String gateway_mac; //对应网关mac
    protected String node_mac; //对应设备mac
    protected String flag; //对应OD
    protected String type1; //对应设备类别
    protected String type2; //对应产品类型
    protected String name; //对应指令名
    protected String cmd; //对应指令内容

    public CustomLearning() {
    }

    public CustomLearning(String gateway_mac, String node_mac, String flag, String type1, String type2, String name, String cmd) {
        this.gateway_mac = gateway_mac;
        this.node_mac = node_mac;
        this.flag = flag;
        this.type1 = type1;
        this.type2 = type2;
        this.name = name;
        this.cmd = cmd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGateway_mac() {
        return gateway_mac;
    }

    public void setGateway_mac(String gateway_mac) {
        this.gateway_mac = gateway_mac;
    }

    public String getNode_mac() {
        return node_mac;
    }

    public void setNode_mac(String node_mac) {
        this.node_mac = node_mac;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
