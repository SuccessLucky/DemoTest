package com.kzksmarthome.common.lib.tools;


import android.util.Log;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;

import java.util.Calendar;

/**
 * 酷庭业务操作类
 *
 * @author jack
 */
public class BusinessTool {
    private static BusinessTool mBusinessTool;

    public static BusinessTool getInstance() {
        if (mBusinessTool == null) {
            mBusinessTool = new BusinessTool();
        }
        return mBusinessTool;
    }

    private BusinessTool() {
    }

    ;

    /**
     * 读取mac地址
     */
    public byte[] readMAC() throws Exception {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, (byte) 0x00, null,
                new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
                new byte[]{0x03, (byte) 0xEB, 0x00});
        return sendMsg;
    }

    /**
     * 获取网关信息
     */
    public byte[] getIOTInfo() throws Exception {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, (byte) 0x00, null,
                new byte[]{(byte) 0xBB, (byte) 0x8A, (byte) 0x80, 0x01, 0x00,
                        0x6F, 0x0D, 0x00}, new byte[]{0x00, 0x00, 0x00});
        return sendMsg;
    }

    /**
     * 发送广播
     *
     * @param srcAddr 源地址：网关地址
     */
    public byte[] sendBoradCast(byte[] srcAddr) throws Exception {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x21, (byte) 0x00,
                srcAddr, null, null);
        return sendMsg;
    }

    /**
     * 获取节点信息 索引 索引描述 索引 子索引 子索引描述 数据类型 字节数 单位 000 节点信息 0000 0 0000全部参数 byte(2)
     * 0 　 0000 1 软件版本号 byte(4) 4 　 0000 2 硬件版本号 byte(4) 4 　 0000 3 OD版本号
     * byte(4) 4 　 0000 4 生产商 byte(8) 8 　 0000 5 行规标识 int 2 　 0000 6 保留 byte(22)
     * 22
     *
     * @param srcAddr 源地址：网关地址
     * @param dstAddr 目的地址：设备地址
     * @param sinde
     */
    public byte[] getNodeInfo(byte[] srcAddr, byte[] dstAddr, byte sinde) throws Exception {
        byte number = 0x00;
        if (dstAddr != null) {// 读取网关信息
            number = 0x01;// 读取其它设备信息
        }

        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, number, srcAddr,
                dstAddr, new byte[]{0x00, 0x00, sinde});

        return sendMsg;
    }

    /**
     * 获取节点网络信息
     * <p/>
     * 索引 索引描述 索引 子索引 子索引描述 数据类型 字节数 1001 节点网络参数 1001 0 0x03E9全部参数 byte(4) 4
     * 1001 1 当前无线通道 byte 1 1001 2 压缩使能 bool 1 1001 3 加密使能 bool 1 1001 4 发送功率
     * byte 1 1001 5 密码 byte(16) 16 1001 6 PAN ID int 2 1001 7 组ID byte 1 1001 8
     * telnet输出调试信息存TOKEN byte 1 1001 9 ExtendPanID byte(8) 8 1001 10 EEPROM使能
     * byte 1 1001 11 保留字段 byte(15) 15
     *
     * @param srcAddr 源地址：网关地址
     * @param dstAddr 目的地址：设备地址
     * @param od
     */
    public byte[] getNodeNetworkParam(byte[] srcAddr, byte[] dstAddr, byte[] od) throws Exception {
        byte number = 0x00;
        if (dstAddr != null) {// 读取网关信息
            number = 0x01;// 读取其它设备信息
        }
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, number, dstAddr,
                srcAddr, new byte[]{0x03, (byte) 0xe9, od[0], od[1]});
        return sendMsg;
    }

    /**
     * 发送控制命令
     *
     * @param srcAddr 源地址：网关地址
     * @param dstAddr 目的地址：设备地址
     */
    public byte[] set3SCContorl(byte[] srcAddr, byte[] dstAddr, byte[] od) throws Exception {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, (byte) 0x00,
                dstAddr, srcAddr,
                new byte[]{0x03, (byte) 0xe9, od[0], od[1]});
        return sendMsg;
    }

    /**
     * 读取网关信道
     *
     * @param srcAddr 源地址：网关地址
     */
    public byte[] readIOTChannel(byte[] srcAddr) throws Exception {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, (byte) 0x00,
                srcAddr, null, new byte[]{0x03, (byte) 0xE9, 0x01});
        return sendMsg;
    }

    /**
     * 修改网关信道
     *
     * @param num     信道号 (11-26可以选择)
     * @param srcAddr 源地址：网关地址
     */
    public byte[] modifyIOTChannel(byte[] srcAddr, byte num) throws Exception {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x00,
                srcAddr, null,
                new byte[]{0x03, (byte) 0xE9, 0x01, 0x01, num});
        return sendMsg;

    }

    /**
     * 读取网关pandID
     *
     * @param srcAddr 源地址：网关地址
     */
    public byte[] readIOTPanID(byte[] srcAddr) throws Exception {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, (byte) 0x00,
                srcAddr, null, new byte[]{0x03, (byte) 0xE9, 0x06});
        return sendMsg;
    }

    /**
     * 修改pandId
     *
     * @param pandId
     * @param srcAddr 源地址：网关地址
     */
    public byte[] modifyIOTPanID(byte[] srcAddr, int pandId) throws Exception {
        byte[] pandBytes = new byte[2];
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x00,
                srcAddr, null, new byte[]{0x03, (byte) 0xE9, 0x06, 0x02,
                        pandBytes[0], pandBytes[1]});
        return sendMsg;
    }

    /**
     * 多用途开关动作控制器
     *
     * @param srcAddr     源地址：网关地址
     * @param dstAddr     目的地址：设备地址
     * @param way         第几路 0x01、0x02、0x03
     * @param controlMode 操作模式 0-无操作； 1-直接操作； 2-渐渐操作； 3-延时操作
     * @param control     操作类型 0-无操作； 1-开； 2-关； 3-状态切换； 4-暂停
     * @param delayTime   延迟时间（秒）0-65535
     */
    public byte[] controlMsg(byte[] srcAddr, byte[] dstAddr, byte way,
                             byte controlMode, byte control, byte delayTime) throws Exception {
        byte[] way1 = new byte[]{0x00, 0x00, 0x00, 0x00};
        byte[] way2 = new byte[]{0x00, 0x00, 0x00, 0x00};
        byte[] way3 = new byte[]{0x00, 0x00, 0x00, 0x00};
        switch (way) {
            case 1:
                way1 = new byte[]{controlMode, control, delayTime, 0x00};
                break;
            case 2:
                way2 = new byte[]{controlMode, control, delayTime, 0x00};
                break;
            case 3:
                way3 = new byte[]{controlMode, control, delayTime, 0x00};
                break;
            default:
                break;
        }
        byte[] data = new byte[20];
        data[0] = 0x0f;
        data[1] = (byte) 0xaa;
        data[2] = (byte) 0xff;
        data[3] = 0x10;// 控制命令长度
        data[4] = 0x00;
        data[5] = (byte) 0xff;
        data[6] = (byte) 0x80;
        data[7] = 0x00;
        System.arraycopy(way1, 0, data, 8, way1.length);
        System.arraycopy(way2, 0, data, 8 + way1.length, way2.length);
        System.arraycopy(way3, 0, data, 8 + way1.length + way3.length,
                way3.length);

        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x01,
                srcAddr, dstAddr, data);
        String sendStr = Tools.byte2HexStr(sendMsg);
        System.out.println(sendStr);
        return sendMsg;
    }

    /**
     * 多用途开关动作控制器
     *
     * @param srcAddr     源地址：网关地址
     * @param dstAddr     目的地址：设备地址
     * @param way         第几路 0x01、0x02、0x03
     * @param controlMode 操作模式 0-无操作； 1-直接操作； 2-渐渐操作； 3-延时操作
     * @param control     操作类型 0-无操作； 1-开； 2-关； 3-状态切换； 4-暂停
     * @param delayTime   延迟时间（秒）0-65535
     */
    public byte[] control4010Msg(byte[] srcAddr, byte[] dstAddr, byte way,
                                 byte controlMode, byte control, byte delayTime) throws Exception {
        byte[] data = new byte[10];
        data[0] = 0x0f;
        data[1] = (byte) 0xaa;
        data[2] = (byte) 0xff;
        data[3] = 0x06;// 控制命令长度
        //子索引
        byte[] childIndex = getChildIndex(way);
        System.arraycopy(childIndex, 0, data, 4, childIndex.length);
        //控制模式
        data[8] = controlMode;
        data[9] = control;
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x01,
                srcAddr, dstAddr, data);
        String sendStr = Tools.byte2HexStr(sendMsg);
        Log.d("order", "send:" + sendStr);
        return sendMsg;
    }

    /**
     * 4040控制
     * @param srcAddr
     * @param dstAddr
     * @param control 1：开、2：关
     * @return
     * @throws Exception
     */
    public static byte[] control4040(byte[] srcAddr, byte[] dstAddr,byte control)throws Exception{
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x01,
                srcAddr, dstAddr, new byte[]{0x0f, (byte) 0xc8, (byte) 0xff,0x05,0x00,0x02,0x00,0x00,control});
        String sendStr = Tools.byte2HexStr(sendMsg);
        Log.d("order", "send:" + sendStr);
        return sendMsg;
    }

    /**
     * 获取子索引
     *
     * @param way 第几路
     * @return
     */
    public static byte[] getChildIndex(int way) {
        byte[] childIndex;
        int num1 = 0;
        int num2 = 0;
        int num = 0;
        switch (way) {
            case 0x01:
                num1 = 1 << 15;
                num2 = 1 << 16;
                break;
            case 0x02:
                num1 = 1 << 18;
                num2 = 1 << 19;
                break;
            case 0x03:
                num1 = 1 << 21;
                num2 = 1 << 22;
                break;
        }
        num = num1 + num2;
        childIndex = Tools.intToByteArray(num);
        return childIndex;
    }

   /* public static void main(String[] args){
        try {
            System.out.println(Tools.byte2HexStr(getChildIndex(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 布防或撤防
     *
     * @throws Exception
     */
    public byte[] buFangOrCheFang(byte[] srcAddr, byte type) throws Exception {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x50, (byte) 0x00,
                srcAddr, null, new byte[]{0x02, 0x05, type});
        return sendMsg;
    }

    /**
     * 解除告警
     *
     * @param srcAddr
     * @return
     */
    public byte[] liftAlarm(byte[] srcAddr) {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x50, (byte) 0x00,
                srcAddr, null, new byte[]{0x01, 0x08});
        return sendMsg;
    }

    /**
     * 网关对时
     * @param srcAddr
     * @param time
     * @return
     */
    public byte[] setIOTTime(byte[] srcAddr,byte[] time){
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x00,
                srcAddr, null, new byte[]{0x13, (byte) 0xb0,0x02,0x08,time[0],time[1],time[2], time[3],time[4],time[5],time[6],0x00});
        return sendMsg;
    }

    /**
     * 获取设定的时间
     * @param srcAddr
     * @return
     */
    public byte[] getIOTTime(byte[] srcAddr){
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, (byte) 0x00,
                srcAddr, null, new byte[]{0x13, (byte) 0xb0,0x02});
        return sendMsg;
    }

    /**
     * 红外学习
     *
     * @param srcAddr   源地址：网关地址
     * @param dstAddr   目的地址：设备地址
     * @param keyNumber 按键编号
     * @throws Exception
     */
    public byte[] redLinear(byte[] srcAddr, byte[] dstAddr, byte keyNumber) throws Exception {
        byte[] data = new byte[6];
        data[0] = 0x05;
        data[1] = (byte) 0x88;
        data[2] = keyNumber;
        data[3] = 0x00;
        data[4] = 0x00;
        data[5] = getXor(data);
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x07, (byte) 0x01,
                srcAddr, dstAddr, data);
        return sendMsg;
    }

    /**
     * 发送红外命令
     *
     * @param srcAddr   源地址：网关地址
     * @param dstAddr   目的地址：设备地址
     * @param keyNumber 按键编码
     * @throws Exception
     */
    public byte[] sendRedOrder(byte[] srcAddr, byte[] dstAddr, byte keyNumber) throws Exception {
        byte[] data = new byte[6];
        data[0] = 0x05;
        data[1] = (byte) 0x86;
        data[2] = keyNumber;
        data[3] = 0x00;
        data[4] = 0x00;
        data[5] = getXor(data);
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x07, (byte) 0x01,
                srcAddr, dstAddr, data);
        return sendMsg;
    }

    /**
     * 发送转发命令
     *
     * @param dstAddr
     * @param data
     * @return
     */
    public byte[] sendForward(byte[] dstAddr, byte[] data) throws Exception {
        byte[] sendData = new byte[data.length + 1];
        sendData[0] = (byte) data.length;
        System.arraycopy(data, 0, sendData, 1, data.length);
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x07, (byte) 0x01,
                null, dstAddr, sendData);
        return sendMsg;
    }

    /**
     * 发送锁命令
     *
     * @param srcAddr
     * @param dstAddr
     * @param data
     * @return
     */
    public byte[] sendLockOrder(byte[] srcAddr, byte[] dstAddr, byte[] data) {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x07, (byte) 0x01,
                srcAddr, dstAddr, data);
        return sendMsg;
    }

    /**
     * 获取调光灯参数
     *
     * @param srcAddr
     * @param dstAddr
     * @return
     */
    public byte[] getColorLightInfo(byte[] srcAddr, byte[] dstAddr) {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, (byte) 0x01,
                srcAddr, dstAddr, new byte[]{(byte) 0xff, (byte) 0xaa, 0x00});
        return sendMsg;
    }

    /**
     * 设置调光灯亮度
     *
     * @param srcAddr
     * @param dstAddr
     * @param light
     * @param openOrClose 1:开、2:关
     * @return
     */
    public byte[] sendColorLightLight(byte[] srcAddr, byte[] dstAddr, byte light, byte openOrClose) {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x01,
                srcAddr, dstAddr, new byte[]{(byte) 0x0f, (byte) 0xaa, (byte) 0xff, 0x0a, 0x1f, 0x00, 0x00, 0x00, 0x01, 0x02, 0x00, 0x05, openOrClose, light});
        return sendMsg;
    }

    /**
     * 设置调光灯颜色
     *
     * @param srcAddr
     * @param dstAddr
     * @param type 操作类型 01:直接、02:渐渐、03:延时
     * @param time 延时时间
     * @param colorR
     * @param colorG
     * @param colorB
     * @return
     */
    public byte[] sendColorLightColor(byte[] srcAddr, byte[] dstAddr,byte type,byte time, byte colorR, byte colorG, byte colorB) {
        if(type == 0x02){
            time = 0x05;
        }
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x01,
                srcAddr, dstAddr, new byte[]{0x0f, (byte) 0xaa, (byte) 0xff, 0x0c, 0x7f, 0x00, 0x00, 0x00, type, 0x03, 0x00, time, 0x01, colorR, colorG, colorB});
        return sendMsg;
    }

    /**
     * 七彩渐渐变
     * @param srcAddr
     * @param dstAddr
     * @return
     */
    public byte[] sendColorLightColorQCJB(byte[] srcAddr, byte[] dstAddr){
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x01,
                srcAddr, dstAddr, new byte[]{0x0f, (byte) 0xaa, (byte) 0xff, 0x0a, 0x7b, 0x00, 0x00, 0x00, 0x09, 0x01, 0x01, 0x00, 0x00, 0x00});
        return sendMsg;
    }

    /**
     * 呼吸灯
     * @param srcAddr
     * @param dstAddr
     * @return
     */
    public byte[] sendColorLightColorHXD(byte[] srcAddr, byte[] dstAddr,byte colorR,byte colorG,byte colorB){
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x01,
                srcAddr, dstAddr, new byte[]{0x0f, (byte) 0xaa, (byte) 0xff, 0x0a, 0x7b, 0x00, 0x00, 0x00, 0x0b, 0x03, 0x01, colorR, colorG, colorB});
        return sendMsg;
    }


    /**
     * 跳变
     * @param srcAddr
     * @param dstAddr
     * @return
     */
    public byte[] sendColorLightColorTB(byte[] srcAddr, byte[] dstAddr){
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x01,
                srcAddr, dstAddr, new byte[]{0x0f, (byte) 0xaa, (byte) 0xff, 0x0c, 0x7f, 0x00, 0x00, 0x00, 0x0a, 0x01, 0x00, 0x05, 0x01, 0x00,0x00,0x00});
        return sendMsg;
    }


    /**
     * 发送空调型号
     *
     * @param srcAddr
     * @param dstAddr
     * @param type    型号
     * @return
     */
    public byte[] setAirConditionerType(byte[] srcAddr, byte[] dstAddr, int type) {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        byte[] typeBytes = Tools.shortToByteArray(type);
        byte[] data = new byte[6];
        data[0] = 0x05;
        data[1] = 0x02;
        data[2] = typeBytes[0];
        data[3] = typeBytes[1];
        data[4] = 0x08;//(byte) minute;
        data[5] = getXor(data);
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x07, (byte) 0x01,
                srcAddr, dstAddr, data);
        return sendMsg;
    }

    /**
     * 配置联动场景命令
     *
     * @param srcAddr
     * @param dstAddr
     * @param sceneNum 情景模式编号
     * @param orderSum 设备个数或命令条数
     * @param isRQ     判断是否是燃气
     * @return
     */
    public byte[] setInitSceneOrder(byte[] srcAddr, byte[] dstAddr, byte sceneNum, byte orderSum, boolean isRQ,byte[] ysTime,byte bf) {
        byte qzld = 0x02;
        byte qd = 0x01;
        if (isRQ) {
            qzld = 0x01;
            qd = (byte) 0xff;
            bf = (byte) 0xff;
        }
        byte[] data = {0x36, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, sceneNum, 0x00, orderSum, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, qzld, qd, bf, dstAddr[0], dstAddr[1], dstAddr[2], dstAddr[3], dstAddr[4], dstAddr[5],
                dstAddr[6], dstAddr[7], 0x01, 0x01, 0x01, 0x00, 0x18, 0x00, 0x00, 0x00, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, ysTime[0], ysTime[1]};
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x50, (byte) 0x00,
                srcAddr, null, data);
        return sendMsg;
    }

    /**
     * 配置普通场景命令
     *
     * @param srcAddr
     * @param sceneNum
     * @param orderSum
     * @return
     */
    public byte[] setInitBaseSceneOrder(byte[] srcAddr, byte sceneNum, byte orderSum,byte[] dsTime,byte[] ysTime) {
        byte[] data = {0x36, 0x01,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
                , 0x01, sceneNum, 0x00, orderSum, sceneNum, dsTime[0],
                dsTime[1], (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, ysTime[0], ysTime[1]};
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x50, (byte) 0x00,
                srcAddr, null, data);
        return sendMsg;
    }


    /**
     * 删除场景配置命令
     *
     * @param srcAddr
     * @param sceneNum
     * @return
     */
    public byte[] delSceneOrder(byte[] srcAddr, byte sceneNum) {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x50, (byte) 0x00,
                srcAddr, null, new byte[]{0x02, 0x07, sceneNum});
        return sendMsg;
    }

    /**
     * 联动载入指令
     *
     * @param srcAddr
     * @param sceneNum
     * @param orderSum
     * @return
     */
    public byte[] loadSceneOrder(byte[] srcAddr, byte sceneNum, byte orderSum, byte orderNum, byte[] orderData) {
        byte[] dataBase = {0x2A, 0x02, sceneNum, orderSum, orderNum};
        byte[] data = new byte[orderData.length + dataBase.length];
        System.arraycopy(dataBase, 0, data, 0, dataBase.length);
        System.arraycopy(orderData, 0, data, dataBase.length, orderData.length);
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x50, (byte) 0x00,
                srcAddr, null, data);
        return sendMsg;
    }

    /**
     * 联动触发命令
     *
     * @param srcAddr
     * @param sceneNum
     * @return
     */
    public byte[] sendSceneOrder(byte[] srcAddr, byte sceneNum) {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x50, (byte) 0x00,
                srcAddr, null, new byte[]{0x02, 0x09, sceneNum});
        return sendMsg;
    }


    /**
     * 获取xor
     *
     * @param data
     * @return
     */
    public byte getXor(byte[] data) {
        if (data != null && data.length > 5) {
            byte xor = data[1];
            for (int i = 2; i < 5; i++) {
                xor = (byte) (xor ^ data[i]);
            }
            return xor;
        }
        return 0;
    }


    /**
     * 发送空调指令
     *
     * @param srcAddr
     * @param dstAddr
     * @param order   长度为2
     * @return
     * @throws Exception
     */
    public byte[] setAirConditionerOrder(byte[] srcAddr, byte[] dstAddr, byte[] order) throws Exception {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        byte[] data = new byte[6];
        data[0] = 0x05;
        data[1] = order[0];
        data[2] = order[1];
        data[3] = 0x08;//(byte) hour;
        data[4] = 0x08;//(byte) minute;
        data[5] = getXor(data);
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x07, (byte) 0x01,
                srcAddr, dstAddr, data);
        return sendMsg;
    }

    /**
     * 设置网关电话号码
     *
     * @param srcAddr 网关地址
     * @param zsy
     * @param phone
     * @return
     */
    public byte[] setIOTPhone(byte[] srcAddr, byte[] zsy, String phone) {
        byte[] phoneBytes = phone.getBytes();
        byte[] data = new byte[20];
        data[0] = (byte) 0x82;
        data[1] = 0x01;
        data[2] = (byte) 0xff;
        data[3] = 0x10;
        System.arraycopy(zsy, 0, data, 4, zsy.length);
        System.arraycopy(phoneBytes, 0, data, 4 + zsy.length, phoneBytes.length);
        data[19] = 0x46;
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x02, (byte) 0x00,
                srcAddr, null, data);
        return sendMsg;
    }

    /**
     * 发送协议转发命令
     *
     * @param srcAddr
     * @param dstAddr
     * @param order
     * @return
     */
    public byte[] sendForwardOrder(byte[] srcAddr, byte[] dstAddr, byte[] order) {
        byte[] data = new byte[order.length + 1];
        data[0] = (byte) order.length;
        for (int i = 0; i < order.length; i++) {
            data[i + 1] = order[i];
        }
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x07, (byte) 0x01,
                srcAddr, dstAddr, data);
        return sendMsg;
    }

   /* public byte[] sendLockOrder(byte[] srcAddr, byte[] dstAddr, byte[] order){

    }*/

    /**
     * 获取设备信息
     *
     * @param srcAddr
     * @return
     */
    public byte[] getDeviceInfo(byte[] srcAddr) {
        byte[] sendMsg = DataPack.joinSmartMsg((byte) 0x01, (byte) 0x00,
                srcAddr, null, new byte[]{0x03, (byte) 0xed, 0x0f});
        return sendMsg;
    }

    /**
     * 发送转发服务器手机信息登记
     *
     * @param srcAddr
     * @return
     */
    public byte[] sendRemoteOrder(byte[] srcAddr) {
        byte[] sendMsg = new byte[77];
        sendMsg[0] = 0x55;
        sendMsg[1] = 0x49;
        sendMsg[2] = 0x01;
        System.arraycopy(srcAddr, 0, sendMsg, 3, srcAddr.length);
        return sendMsg;
    }


    /**
     * 接收并解析数据
     *
     * @param receiverData
     */
    public DeviceState resolveData(byte[] receiverData) {
        DeviceState deviceState = new DeviceState();//返回设备状态
        byte[] dstAddr = new byte[8];//目的地址
        try {
            if (receiverData != null && receiverData[0] == 0x2a
                    && receiverData[receiverData.length - 1] == 0x23
                    && receiverData.length > 15) {// 判断是否是一条完整的数据
                // 计算数据长度是否一致
                if (receiverData[1] != DataPack
                        .getSmartDataLength(receiverData)) {
                    return null;
                }
                // 检查校验和
                if (!DataPack.checkSmartSum(receiverData)) {
                    return null;
                }
                // 获取OD
                byte[] od = new byte[2];
                od[0] = receiverData[11];
                od[1] = receiverData[12];
                byte cmdId = receiverData[2];
                byte zsy = receiverData[13];
                deviceState.deviceOD = od;
                deviceState.cmdId = cmdId;
                // 根据OD判断业务
                if (od[0] == 0x03 && od[1] == (byte) 0xeb) {// 获取网关MAC 1003
                    // 取出网关mac地址
                    byte[] srcAddr = new byte[8];
                    System.arraycopy(receiverData, 3, srcAddr, 0, 8);
                    deviceState.srcAddr = srcAddr;

                } else if (od[0] == 0x00 && od[1] == 0x00) {// 获取节点信息0000
                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                    deviceState.dstAddr = dstAddr;
                    byte[] version = new byte[4];
                    // 版本信息
                    System.arraycopy(receiverData, 15, version, 0, 4);
                    deviceState.version = version;
                    // 硬件版本号
                    byte[] hardware = new byte[4];
                    System.arraycopy(receiverData, 19, hardware, 0, 4);
                    deviceState.hardware = hardware;

                } else if (od[0] == 0x03 && od[1] == (byte) 0xe9) {// 获取节点或网关网络参数1001

                    switch (cmdId) {
                        case 0x01:
                            switch (zsy) {
                                case 0x01:// 读取通道号
                                    // 通道号
                                    byte channel = receiverData[15];
                                    break;
                                case 0x06:// 读取panId
                                    byte[] panId = new byte[2];
                                    System.arraycopy(receiverData, 15, panId, 0, 2);
                                    break;
                            }
                            break;
                        case 0x02:
                            switch (zsy) {
                                case 0x01:// 设置通道返回结果
                                    byte result = receiverData[14];
                                    if (result == 0x00) {
                                        // 成功
                                    }
                                    break;
                                case 0x06:// 设置panId返回结果
                                    byte resultpanId = receiverData[14];
                                    if (resultpanId == 0x00) {
                                        // 成功
                                    }
                                    break;
                            }

                            break;
                        default:
                            break;
                    }

                } else if (od[0] == 0x03 && od[1] == (byte) 0xea) {// 当前时间1002

                } else if (od[0] == 0x03 && od[1] == (byte) 0xeb) {// 获取邻居表1003

                } else if (od[0] == 0x03 && od[1] == (byte) 0xec) {// coo设备参数1005

                } else if (od[0] == 0x03 && od[1] == (byte) 0xed) {// 节点设备配置1006

                } else if (od[0] == 0x0f && od[1] == (byte) 0xaa) {// 开关控制4010
                    byte deviceType = receiverData[19];// 设备类型
                    byte product = receiverData[20];// 产品类型
                    deviceState.sindex_length = receiverData[14];
                    byte sindex = receiverData[13];
                    byte status_01 = 0;// 1路状态
                    byte status_02 = 0;// 2路状态
                    byte status_03 = 0;// 3路状态
                    if (receiverData.length > 32) {
                        status_01 = receiverData[31];
                    }
                    if (receiverData.length > 33) {
                        status_02 = receiverData[32];
                    }
                    if (receiverData.length > 34) {
                        status_03 = receiverData[33];
                    }
                    deviceState.deviceType = deviceType;
                    deviceState.deviceProduct = product;
                    switch (deviceType) {
                        case 0x01:
                            if (product == 0x02) {// 普通电动窗帘
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x02:
                            switch (product) {
                                case 0x01://强弱电控制器(控制盒)-disable
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x02:// 电动幕布(控制盒)
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x10:// 投影架(控制盒)
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x11:// 推拉开窗器(控制盒)
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x12:// 平推开窗器(控制盒)
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x13:// 机械手控制器(控制盒)
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;

                            }
                            break;
                        case 0x03:// 电动玻璃-disable
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x04:// 门禁开合-disable
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x05:
                            switch (product) {
                                case 0x02:// 一路灯开关
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x03:// 电动玻璃
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x10:// 86插座
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x11:// 移动插座
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                            }
                            break;
                        case 0x06:// 二路灯开关
                            if (product == 0x02) {
                                /*if (status_01 == 0x01) {
                                    System.out
											.println("二路开关：第1路 --开--" + status_01);
								} else if (status_01 == 0x02) {
									System.out.println("二路开关：第1路  --关--"
											+ status_01);
								}
								if (status_02 == 0x01) {
									System.out
											.println("二路开关：第2路 --开--" + status_02);
								} else if (status_02 == 0x02) {
									System.out.println("二路开关：第2路  --关--"
											+ status_02);
								}*/
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                                deviceState.result_data_02 = status_02;
                            }
                            break;
                        case 0x07:// 三路灯开关
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                                deviceState.result_data_02 = status_02;
                                deviceState.result_data_03 = status_03;
                                /*if (status_01 == 0x01) {
                                    System.out
											.println("三路开关：第1路 --开--" + status_01);
								} else if (status_01 == 0x02) {
									System.out.println("三路开关：第1路  --关--"
											+ status_01);
								}
								if (status_02 == 0x01) {
									System.out
											.println("三路开关：第2路 --开--" + status_02);
								} else if (status_02 == 0x02) {
									System.out.println("三路开关：第2路  --关--"
											+ status_02);
								}
								if (status_03 == 0x01) {
									System.out
											.println("三路开关：第3路 --开--" + status_03);
								} else if (status_03 == 0x02) {
									System.out.println("三路开关：第3路  --关--"
											+ status_03);
								}*/
                            }
                            break;
                        case 0x08:// 1路调光灯开关-disable
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x09:// 声光报警器-new
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x0a:// 场景控制器-disable
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                if (receiverData.length > 55) {
                                    deviceState.result_data_01 = receiverData[51];
                                    deviceState.sindex_length = receiverData[47];//直接、渐渐、延时
                                }
                            }
                            break;
                        case 0x0b://多彩球灯泡-new
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x0e://多彩冷暖灯泡-new
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case (byte) 0x81:
                            switch (product) {
                                case 0x02:// 人体红外感应-new
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;

								/*if (status_01 == 0x01) {// 开
                                    System.out.println("红外感应 ：感应到人" + status_01);
								} else if (status_01 == 0x02) {// 关
									System.out.println("红外感应 ：休眠状态或关" + status_01);
								}*/
                                    break;
                                case 0x03:// 一氧化碳-new
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x04:// 烟雾传感器-new
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    break;
                                case 0x05:// SOS报警器
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    deviceState.result_data_01 = status_01;
                                    deviceState.uploadState = receiverData[22];
                                    break;
                            }
                            break;
                        case (byte) 0x82:// 一氧化碳感应器-disable
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                                /*if (status_01 == 0x01) {
									System.out.println("燃气有报警：" + status_01);
								} else if (status_01 == 0x02) {
									System.out.println("燃气报警解除或无报警：" + status_01);
								}*/
                            }
                            break;
                        case (byte) 0x83:// 烟雾感应器-disable
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case (byte) 0x8a:// 场景控制器-new
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case (byte) 0xc1:// 六路面板-new
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        default:
                            break;
                    }

                } else if (od[0] == 0x0f && od[1] == (byte) 0xb4) {// 多用途规约控制器
                    // 4020

                } else if (od[0] == 0x0f && od[1] == (byte) 0xbe) {// 多用途休眠设备控制器4030
                    byte deviceType = receiverData[19];// 设备类型
                    byte product = receiverData[20];// 产品类型
                    if (cmdId == 0x07) {//休眠设备主动设备
                        deviceType = receiverData[13];// 设备类型
                        product = receiverData[14];// 产品类型
                    }
                    if (receiverData.length > 22) {
                        if (deviceType != 0x02 && product != 0x02 && receiverData[22] != 0x00) {
                            return null;
                        }
                    }
                    byte[] sindex = new byte[4];
                    sindex[0] = receiverData[15];
                    sindex[1] = receiverData[16];
                    sindex[2] = receiverData[17];
                    sindex[3] = receiverData[18];
                    deviceState.sindex = sindex;
                    byte status_01 = 0;// 1路状态
                    if (receiverData.length > 32) {
                        status_01 = receiverData[31];
                    }
                    byte status_02 = 0;// 2路状态
                    if (receiverData.length > 33) {
                        status_02 = receiverData[32];
                    }
                    byte status_03 = 0;// 3路状态
                    if (receiverData.length > 34) {
                        status_03 = receiverData[33];
                    }
                    deviceState.deviceType = deviceType;
                    deviceState.deviceProduct = product;
                    switch (deviceType) {
                        case 0x01:
                            if (product == 0x01) {// 门磁设备 1-开；2-关；3-由关到开；4-由开到关；门磁-disable
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;

				/*				switch (status_01) {
									case 0x01:
										System.out.println("门磁状态：开---" + status_01);
										break;
									case 0x02:
										System.out.println("门磁状态：关---" + status_01);
										break;
									case 0x03:
										System.out.println("门磁状态：由关到开---" + status_01);
										break;
									case 0x04:
										System.out.println("门磁状态：由开到关---" + status_01);
										break;
									default:
										break;
								}*/
                            } else if (product == 0x02) {//门磁设备-new
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x02:
                            if (product == 0x02) {// 指纹锁-new
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.lockOperateType = receiverData[21];
                                byte[] lockData = new byte[2];
                                lockData[0] = receiverData[23];
                                lockData[1] = receiverData[24];
                                deviceState.lockState = Tools.byteArrayToInt(lockData);
                            } else if (product == 0x01) {//红外感应-disable
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }else if(product == 0x03){//小蛮腰指纹锁
                                if(DataPack.lockDataCheck(receiverData)){
                                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                    deviceState.dstAddr = dstAddr;
                                    byte[] lockData = new byte[3];
                                    System.arraycopy(receiverData,24,lockData,0,3);
                                    deviceState.lockData = lockData;
                                    deviceState.lockState = lockData[0];
                                }
                            }
                            break;
                        case 0x03:
                            if (product == 0x02) {//燃气传感器
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            } else if (product == 0x01) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x04:
                            if (product == 0x02) {// 人体红外传感器
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x05:
                            if (product == 0x02) {//水浸传感器
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x06:// 亮度传感器-disable
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x07:// 烟雾传感器-new
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case (byte) 0x81:
                            if (product == 0x02) {//门磁-new
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            } else if (product == 0x03) {//窗磁-new
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case (byte) 0x83:// 水浸传感器-new
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case (byte) 0x86:// 人体红外感应-new
                            if (product == 0x02) {
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        default:
                            break;
                    }
                } else if (od[0] == 0x0f && od[1] == (byte) 0xc8) {// 智能计量设备4040
                    byte deviceType = receiverData[19];// 设备类型
                    byte product = receiverData[20];// 产品类型
                    byte status_01 = 0;// 1路状态
                    if (receiverData.length > 45) {
                        status_01 = receiverData[43];
                    }
                    deviceState.deviceType = deviceType;
                    deviceState.deviceProduct = product;
                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                    deviceState.dstAddr = dstAddr;
                    switch (deviceType) {
                        case 0x01:
                            if (product == 0x02) {//单相电表-new
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x02:
                            if (product == 0x02) {//计量控制盒-new
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                                String[] jlData = new String[4];
                                jlData[0] = Tools.getJLData(new byte[]{receiverData[31],receiverData[32]},10);
                                jlData[1] = Tools.getJLData(new byte[]{receiverData[33],receiverData[34],receiverData[35]},1000);
                                jlData[2] = Tools.getJLData(new byte[]{receiverData[36],receiverData[37],receiverData[38]},10);
                                jlData[3] = Tools.getJLData(new byte[]{receiverData[39],receiverData[40],receiverData[41],receiverData[42]},100);
                                deviceState.jlArray = jlData;
                            }
                            break;
                        case 0x03:
                            if (product == 0x02) {//三厢电表-new
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                            }
                            break;
                        case 0x04:
                            if (product == 0x02) {//计量插座（10A）-new
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                                String[] jlData = new String[4];
                                jlData[0] = Tools.getJLData(new byte[]{receiverData[31],receiverData[32]},10);
                                jlData[1] = Tools.getJLData(new byte[]{receiverData[33],receiverData[34],receiverData[35]},1000);
                                jlData[2] = Tools.getJLData(new byte[]{receiverData[36],receiverData[37],receiverData[38]},10);
                                jlData[3] = Tools.getJLData(new byte[]{receiverData[39],receiverData[40],receiverData[41],receiverData[42]},100);
                                deviceState.jlArray = jlData;
                            } else if (product == 0x03) {//计量插座（16A）-new
                                System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                                deviceState.dstAddr = dstAddr;
                                deviceState.result_data_01 = status_01;
                                String[] jlData = new String[4];
                                jlData[0] = Tools.getJLData(new byte[]{receiverData[31],receiverData[32]},10);
                                jlData[1] = Tools.getJLData(new byte[]{receiverData[33],receiverData[34],receiverData[35]},1000);
                                jlData[2] = Tools.getJLData(new byte[]{receiverData[36],receiverData[37],receiverData[38]},10);
                                jlData[3] = Tools.getJLData(new byte[]{receiverData[39],receiverData[40],receiverData[41],receiverData[42]},100);
                                deviceState.jlArray = jlData;
                            }
                            break;
                    }

                } else if (od[0] == 0x0f && od[1] == (byte) 0xe6) {// 协议转发4070
                    // System.arraycopy(receiverData, 3, DstAddr, 0, 8);//
                    // 测试保持红外转发器地址
                    byte deviceType = receiverData[19];// 设备类型
                    byte product = receiverData[20];// 产品类型
                    if (cmdId == 0x07) {//休眠设备主动设备
                        deviceType = receiverData[13];// 设备类型
                        product = receiverData[14];// 产品类型
                    }
                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                    deviceState.dstAddr = dstAddr;
                    deviceState.deviceType = deviceType;
                    deviceState.deviceProduct = product;
                    deviceState.redSendState = 0;
                   /* try {
                        Log.d("AddDevice","解包-----"+Tools.byte2HexStr(receiverData));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    if (deviceType == 0x02) {
                        switch (product) {
                            case 0x02://红外转发器-new
                                byte state = receiverData[receiverData.length - 3];
                                if (state == 0xe0) {//红外学习失败或红外控制失败
                                    deviceState.redSendState = 2;
                                } else if (state == 0x89) {//红外控制命令发送成功
                                    deviceState.redSendState = 1;
                                } else if (receiverData.length > 64) {//红外编码学习成功
                                    deviceState.redSendState = 1;
                                }
                                break;
                            case 0x03://音乐背景器-new

                                break;
                            case 0x10://电动窗帘-new
                                byte windowContorl_02 = receiverData[24];
                                deviceState.sindex_length = windowContorl_02;
                                break;
                            case 0x11://平移开窗器-new
                                byte windowContorl_01 = receiverData[24];
                                deviceState.sindex_length = windowContorl_01;
                                break;
                            case 0x12://电动床-new

                                break;
                            case 0x13://新风系统-new

                                break;
                            case 0x20://浴霸-new

                                break;
                        }
                    }
                } else if (od[0] == 0x13 && od[1] == (byte) 0x92) {// 网关登录信息数据帧5010

                } else if (od[0] == 0x13 && od[1] == (byte) 0x9c) {// 维持和主站通讯的心跳5020

                } else if (od[0] == 0x13 && od[1] == (byte) 0xa6) {// 手机号码信息数据帧,设置网关号码5030
                    byte result = receiverData[14];
                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                    deviceState.dstAddr = dstAddr;
                    deviceState.result_data_01 = result;
                } else if (od[0] == 0x82 && od[1] == 0x01) {//设置网关手机号码、报警号码、报警短信

                } else if (od[0] == 0x03 && od[1] == 0x05) {//布放撤防
                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                    deviceState.dstAddr = dstAddr;
                    deviceState.result_data_01 = receiverData[14];//布放撤防状态
                } else if (cmdId == 0x50 || cmdId == 0xd0) {//场景操作返回数据
                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                    deviceState.srcAddr = dstAddr;
                    deviceState.sceneOrderType = receiverData[12];//命令类型
                    deviceState.sceneNum = receiverData[13];//场景编号
                    deviceState.result_data_01 = receiverData[14];//是否成功 00表示成功
                    deviceState.sceneOrderSum = receiverData[15];//命令总条数
                    deviceState.sceneOrderNum = receiverData[16];//当前成功命令第几条
                }
                /*else if (cmdId == 0x07) {// 协议转发
                    System.arraycopy(receiverData, 3, dstAddr, 0, 8);
                    deviceState.dstAddr = dstAddr;
                    if (receiverData.length > 48) {//红外编码学习成功
                        deviceState.redSendState = 1;
                    }else if (receiverData.length >= 24) {
                        Log.d("AddDevice", "resolveData: "+Tools.byte2HexStr(receiverData));
                        byte zfDeviceType = receiverData[16];
                        deviceState.deviceType = zfDeviceType;
                        if(zfDeviceType == (byte) 0xa1){//指纹锁
                            deviceState.lockOperateType = receiverData[17];
                            byte[] lockData = new byte[2];
                            lockData[0] = receiverData[19];
                            lockData[1] = receiverData[20];
                            deviceState.lockState = Tools.byteArrayToInt(lockData);
                                 *//*switch (deviceState.lockOperateType){
                                     case 0x50://指纹开锁上报
                                         break;
                                     case 0x51://密码开锁上报
                                         break;
                                     case 0x60://远程开锁上报
                                         break;
                                 }*//*
                        }else if(zfDeviceType == (byte) 0xff){//开窗器01/窗帘
                            byte windowType = receiverData[17];
                            deviceState.sindex_length = windowType;
                            if (windowType == 0x01) {// 开窗器
                                byte windowContorl_01 = receiverData[20];
                                deviceState.result_data_01 = windowContorl_01;
							*//*		switch (windowContorl_01) {
										case 0x4f:// 正转

											break;
										case 0x43:// 反转

											break;
										case 0x53:// 停止

											break;
										case 0x52:// 重启

											break;
										case 0x50:// 状态读取

											break;

										default:
											// 进度范围 ：XX范围为16进制00~10
											if (windowContorl_01 >= 0
													&& windowContorl_01 <= 0x01) {

											}
											break;
									}*//*
                            } else { // 电动窗帘00
                                byte windowContorl_01 = receiverData[20];
                                deviceState.result_data_01 = windowContorl_01;
*//*									switch (windowContorl_01) {
										case 0x4f:// 正转

											break;
										case 0x43:// 反转

											break;
										case 0x53:// 停止

											break;
										case 0x52:// 重启

											break;
										case 0x50:// 状态读取

											break;

										default:
											// 进度范围 ：XX范围为16进制00~10
											if (windowContorl_01 >= 0
													&& windowContorl_01 <= 0x01) {

											}
											break;
									}*//*
                            }
                        }else if(zfDeviceType == (byte) 0x86){
                            byte type_86 = receiverData[15];
                            deviceState.sindex_length = type_86;
                            if (type_86 == 0x06) {// 电动床

                            } else if (type_86 == 0x10) {// 新风

                            }
                        }else{
                            deviceState.dstAddr = null;//废弃不正确的上报
                        }
                    } else if (receiverData.length > 18) {//红外编码学习或空调发送后返回的状态
                        byte state = receiverData[17];
                        if (state == 0xe0) {//红外学习失败或红外控制失败
                            deviceState.redSendState = 2;
                        } else if (state == 0x89) {//红外控制命令发送成功
                            deviceState.redSendState = 1;
                        }else{
                            deviceState.redSendState = 2;
                        }
                    }

                }*/
            } else if (receiverData[0] == 0x55 && receiverData[receiverData.length - 1] == (byte)0xaa) {//手机信息登记
                if (receiverData.length > 3) {
                    byte resultCode = receiverData[1];
                    byte result = receiverData[3];
                    if(resultCode == 0x01) {
                        if (result == 0x41) {
                            deviceState.remoteAddFlag = true;
                        } else if (result == 0x7f) {
                            deviceState.remoteAddFlag = false;
                        }
                    }else if(resultCode == 0x02){
                        if(result == 0x01){
                            showToast("网关连接异常！");
                        }else if(result == 0x02){
                            showToast("网络异常"+ErrorCode.ERROR_SERVER_CODE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceState;
    }

    /**
     * 显示提示
     * @param msg
     */
    public void showToast(final String msg){
        MainTaskExecutor.runTaskOnUiThread(new Runnable() {
            @Override
            public void run() {
                SmartHomeAppLib.showToast(msg);
            }
        });
    }



}
