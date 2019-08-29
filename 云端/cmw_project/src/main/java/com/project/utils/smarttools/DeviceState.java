package com.project.utils.smarttools;

import java.io.Serializable;

/**
 * 设备状态
 * Created by jack on 2016/9/17.
 */
public class DeviceState implements Serializable {

    /**
     * 网关地址
     */
    public byte[] srcAddr;
    /**
     * 目的地址/设备地址
     */
    public byte[] dstAddr;
    /**
     * 第一路返回数据（非三路开关默认使用）
     */
    public byte result_data_01;
    /**
     * 第二路返回数据
     */
    public byte result_data_02;
    /**
     * 第三路返回数据
     */
    public byte result_data_03;
    /**
     * 版本信息
     */
    public byte[] version;
    /**
     * 硬件版本信息
     */
    public byte[] hardware;
    /**
     * 设备索引OD
     */
    public byte[] deviceOD;
    /**
     * 设备类型
     */
    public byte deviceType;
    /**
     * 设备分类
     */
    public byte deviceProduct;
    /**
     * 红外转发状态 0:红外转发上报，1：学习或控制成功，2：学习或控制失败
     */
    public int redSendState;
    /**
     * 子索引
     */
    public byte[] sindex;
    /**
     * 子索引长度/协议转发设备子类型
     */
    public byte sindex_length;
    /**
     * cmdId
     */
    public byte cmdId;
    /**
     * 远程服务器登记
     */
    public boolean remoteAddFlag = true;
    /**
     * 指纹锁控制类型
     */
    public byte lockOperateType;
    /**
     * 锁的数据
     */
    public int lockState;
    /**
     * 命令类型 07:删除场景、09:启动场景、02:载入场景、01:配置场景
     */
    public byte sceneOrderType;
    /**
     * 场景编号
     */
    public byte sceneNum;
    /**
     * 场景命令数
     */
    public byte sceneOrderSum;
    /**
     * 当前成功命令编号
     */
    public byte sceneOrderNum;
    /**
     * 计量设备数据
     */
    public String[] jlArray;

    /**
     * 格式：用户ID|ID类型|锁状态|
     * ID:
     * XXX（注册时的ID号）
     * ID类型：
     * 管理指纹：1
     * 普通指纹：2
     * 客人指纹：3
     * 挟持指纹：4
     * 遥控：5
     * 门铃：6
     * 普通密码：7
     * 劫持密码：8
     * 指纹加密码：9
     * 网络开启：10
     * 门卡：11
     * 指纹加卡：12
     * 锁状态：
     * 0（关）/1（开）
     */
    public byte[] lockData;

    /**
     * 数据上报状态 00 自动上报
     */
    public byte uploadState;
}
