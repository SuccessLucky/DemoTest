package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.text.TextUtils;

import com.kzksmarthome.SmartHouseYCT.util.DeviceTypeEnums;
import com.kzksmarthome.common.lib.tools.Tools;

/**
 * 设备类型
 * Created by jack on 2016/10/15.
 */
public class DeviceTools {
    /**
     * 获取设备类型
     * @param deviceInfo
     * @return
     */
    public static short getDeviceType(DeviceInfo deviceInfo) {
        short result_Type = -1;
        try {
            byte[] od = Tools.hexStr2Bytes(deviceInfo.getDevice_OD());
            int type_int = Integer.decode("0x"+deviceInfo.getDevice_type());
            byte type = (byte)type_int;
            String cmdId_str = deviceInfo.getCmdId();
            byte cmdId = 1;
            if(!TextUtils.isEmpty(cmdId_str)){
                cmdId = Byte.valueOf(cmdId_str);
            }
            int productSrc_int = Integer.decode("0x"+deviceInfo.getCategory());
            byte productSrc = (byte)productSrc_int;
            if (od[0] == 0x03 && od[1] == (byte) 0xea) {// 当前时间1002

            } else if (od[0] == 0x03 && od[1] == (byte) 0xeb) {// 获取邻居表1003

            } else if (od[0] == 0x03 && od[1] == (byte) 0xec) {// coo设备参数1005

            } else if (od[0] == 0x03 && od[1] == (byte) 0xed) {// 节点设备配置1006

            } else if (od[0] == 0x0f && od[1] == (byte) 0xaa) {// 开关控制4010
                byte deviceType = type;// 设备类型
                byte product = productSrc;// 产品类型
                switch (deviceType) {
                    case 0x01:
                        if (product == 0x02) {// 普通电动窗帘
                            result_Type = DeviceTypeEnums.CURTAIN.getCode();
                        }
                        break;
                    case 0x02:
                        switch (product) {
                            case 0x01://强弱电控制器(控制盒)
                                result_Type = DeviceTypeEnums.CONTROLBOX.getCode();
                                break;
                            case 0x02:// 电动幕布
                                result_Type = DeviceTypeEnums.THECURTAIN.getCode();
                                break;
                            case 0x10:// 投影架
                                result_Type = DeviceTypeEnums.PROJECTIONFRAME.getCode();
                                break;
                            case 0x11:// 推拉开窗器
                                result_Type = DeviceTypeEnums.PUSHWINDOW.getCode();
                                break;
                            case 0x12:// 平推开窗器
                                result_Type = DeviceTypeEnums.TRANSLATWINDOW.getCode();
                                break;
                            case 0x13:// 机械手控制器
                                result_Type = DeviceTypeEnums.MANIPULATOR.getCode();
                                break;

                        }
                        break;
                    case 0x05:
                        switch (product) {
                            case 0x02:// 一路灯开关
                                result_Type = DeviceTypeEnums.LIGHT.getCode();
                                break;
                            case 0x03://电动玻璃
                                result_Type = DeviceTypeEnums.ELECTRICGLASS.getCode();
                                break;
                            case 0x10:// 86插座
                                result_Type = DeviceTypeEnums.OUTLET.getCode();
                                break;
                            case 0x11:// 移动插座
                                result_Type = DeviceTypeEnums.MOBILEOUTLET.getCode();
                                break;
                        }
                        break;
                    case 0x06:// 二路灯开关
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.TABLELAMP.getCode();
                        }
                        break;
                    case 0x07:// 三路灯开关
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.DROPLIGHT.getCode();
                        }
                        break;
                    case 0x08:// 1路调光灯开关
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.LIGHTMODULATIONLAMP.getCode();
                        }
                        break;
                    case 0x09:// 声光报警器
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.SOUNDANDLIGHTALARM.getCode();
                        }
                        break;
                    case 0x0b:// 多彩灯泡
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.COLORFULBULB.getCode();
                        }
                        break;
                    case 0x0e:// 多彩冷暖灯
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.COLORFULLAMP.getCode();
                        }
                        break;
                    case (byte) 0x81:// 人体红外感应
                        switch (product){
                            case 0x02:// 人体红外感应-new
                                result_Type = DeviceTypeEnums.PYROELECTRICINFRARED.getCode();
                                break;
                            case 0x03:// 一氧化碳-new
                                result_Type = DeviceTypeEnums.GASSENSOR.getCode();
                                break;
                            case 0x04:// 烟雾传感器-new
                                result_Type = DeviceTypeEnums.SMOKESENSOR.getCode();
                                break;
                            case 0x05:// SOS报警器
                                result_Type = DeviceTypeEnums.SOSBUTTON.getCode();
                                break;
                        }
                        break;
                    case (byte) 0x8a:// 场景控制器
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.SCENECONTROL.getCode();
                        }
                        break;
                    case (byte) 0xc1:// 六路面板
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.SIXWAYPANEL.getCode();
                        }
                        break;
                    default:
                        break;
                }

            } else if (od[0] == 0x0f && od[1] == (byte) 0xb4) {// 多用途规约控制器
                // 4020

            } else if (od[0] == 0x0f && od[1] == (byte) 0xbe) {// 多用途休眠设备控制器4030
                byte deviceType = type;// 设备类型
                byte product = productSrc;// 产品类型
                switch (deviceType) {
                    case 0x01:
                        if (product == 0x02) {// 门磁设备1-开；2-关；3-由关到开；4-由开到关；门磁
                            result_Type = DeviceTypeEnums.DOORANDWINDOW.getCode();
                        }
                        break;
                    case 0x02:
                        if (product == 0x02 || product == 0x03) {//指纹锁
                            result_Type = DeviceTypeEnums.LOCK.getCode();
                        }
                        break;
                    case 0x03:
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.GASSENSOR.getCode();
                        }
                        break;
                    case 0x04:
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.NFRAREDINDUCTION.getCode();
                        }
                        break;
                    case 0x05:
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.WATERIMMERSIONSENSOR.getCode();
                        }
                        break;
                    case 0x07:// 烟雾传感器-new
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.SMOKESENSOR.getCode();
                        }
                        break;
                    case (byte) 0x81:
                        if (product == 0x02) {//门磁-new
                            result_Type = DeviceTypeEnums.DOORANDWINDOW.getCode();
                        }else if(product == 0x03){//窗磁-new
                            result_Type = DeviceTypeEnums.MAGNETICWINDOW.getCode();
                        }
                        break;
                    case (byte) 0x83:// 水浸传感器-new
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.WATERIMMERSIONSENSOR.getCode();
                        }
                        break;

                    case (byte) 0x86:// 人体红外感应-new
                        if (product == 0x02) {
                            result_Type = DeviceTypeEnums.NFRAREDINDUCTION.getCode();
                        }
                        break;
                    default:
                        break;
                }
            } else if (od[0] == 0x0f && od[1] == (byte) 0xc8) {// 智能计量设备4040
                //result_Type = DeviceTypeEnums.JLOUTLET.getCode();
                byte deviceType = type;// 设备类型
                byte product = productSrc;// 产品类型
                switch (deviceType) {
                    case 0x01:
                        if (product == 0x02) {//单相电表-new
                            result_Type = DeviceTypeEnums.SINGLEMETER.getCode();
                        }
                        break;
                    case 0x02:
                        if (product == 0x02) {//计量控制盒-new
                            result_Type = DeviceTypeEnums.METERINGSWITCH.getCode();
                        }
                        break;
                    case 0x03:
                        if (product == 0x02) {//三厢电表-new
                            result_Type = DeviceTypeEnums.THREECOMPARTMENTAMMETER.getCode();
                        }
                        break;
                    case 0x04:
                        if (product == 0x02) {//计量插座（10A）-new
                            result_Type = DeviceTypeEnums.METERINGSOCKET10.getCode();
                        } else if (product == 0x03) {//计量插座（16A）-new
                            result_Type = DeviceTypeEnums.METERINGSOCKET16.getCode();
                        }
                        break;
                }
            } else if (od[0] == 0x0f && od[1] == (byte) 0xe6) {// 协议转发4070
                byte deviceType = type;// 设备类型
                byte product = productSrc;// 产品类型
                if(deviceType == 0x02){
                    switch (product){
                        case 0x02://红外转发器-new
                            String type_red = deviceInfo.getSindex_length();
                            if(!TextUtils.isEmpty(type_red)) {
                                result_Type = Short.valueOf(type_red);
                            }
                            break;
                        case 0x03://音乐背景器-new

                            break;
                        case 0x10://电动窗帘-new
                            result_Type = DeviceTypeEnums.ELECTRICCURTAIN.getCode();
                            break;
                        case 0x11://平移开窗器-new
                            result_Type = DeviceTypeEnums.WINDOWOPENER.getCode();
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

            } else if (od[0] == 0x82 && od[1] == 0x01) {//设置网关手机号码、报警号码、报警短信

            }/*else if (cmdId == 0x07) {// 协议转发
                byte zfDeviceType = Tools.hexStr2Byte(deviceInfo.getDevice_type());
                byte childType = 0;
                if(deviceInfo.getSindex_length() != null) {
                    childType = Tools.hexStr2Byte(deviceInfo.getSindex_length());
                }
                switch (zfDeviceType) {
                    case (byte) 0xa1://指纹锁

                        break;
                    case (byte) 0xff://开窗器01
                        if (childType == 0x01) {// 开窗器

                        } else  { // 电动窗帘00

                        }
                        break;
                    case (byte) 0x86:
                        if (childType == 0x06) {// 电动床
                            result_Type = DeviceTypeEnums.ELECTRICBED.getCode();
                        } else if (childType == 0x10) {// 新风
                            result_Type = DeviceTypeEnums.FRESHAIR.getCode();
                        }
                        break;
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_Type;
    }

    /**
     * 获取设备类型名称
     * @param deviceInfo
     * @return
     */
    public static String getDeviceTypeName(DeviceInfo deviceInfo) {
        String result_TypeName = "";
        try {
            byte[] od = Tools.hexStr2Bytes(deviceInfo.getDevice_OD());
            int type_int = Integer.decode("0x"+deviceInfo.getDevice_type());
            byte type = (byte)type_int;
            String cmdId_str = deviceInfo.getCmdId();
            byte cmdId = 1;
            if(!TextUtils.isEmpty(cmdId_str)){
                cmdId = Byte.valueOf(cmdId_str);
            }
            int productSrc_int = Integer.decode("0x"+deviceInfo.getCategory());
            byte productSrc = (byte)productSrc_int;
            if (od[0] == 0x03 && od[1] == (byte) 0xea) {// 当前时间1002

            } else if (od[0] == 0x03 && od[1] == (byte) 0xeb) {// 获取邻居表1003

            } else if (od[0] == 0x03 && od[1] == (byte) 0xec) {// coo设备参数1005

            } else if (od[0] == 0x03 && od[1] == (byte) 0xed) {// 节点设备配置1006

            } else if (od[0] == 0x0f && od[1] == (byte) 0xaa) {// 开关控制4010
                byte deviceType = type;// 设备类型
                byte product = productSrc;// 产品类型
                switch (deviceType) {
                    case 0x01:
                        if (product == 0x02) {// 普通电动窗帘
                            result_TypeName = DeviceTypeEnums.CURTAIN.getName();
                        }
                        break;
                    case 0x02:
                        switch (product) {
                            case 0x01://强弱电控制器(控制盒)
                                result_TypeName = DeviceTypeEnums.CONTROLBOX.getName();
                                break;
                            case 0x02:// 电动幕布
                                result_TypeName = DeviceTypeEnums.THECURTAIN.getName();
                                break;
                            case 0x10:// 投影架
                                result_TypeName = DeviceTypeEnums.PROJECTIONFRAME.getName();
                                break;
                            case 0x11:// 推拉开窗器
                                result_TypeName = DeviceTypeEnums.PUSHWINDOW.getName();
                                break;
                            case 0x12:// 平推开窗器
                                result_TypeName = DeviceTypeEnums.TRANSLATWINDOW.getName();
                                break;
                            case 0x13:// 机械手控制器
                                result_TypeName = DeviceTypeEnums.MANIPULATOR.getName();
                                break;

                        }
                        break;
                    case 0x05:
                        switch (product) {
                            case 0x02:// 一路灯开关
                                result_TypeName = DeviceTypeEnums.LIGHT.getName();
                                break;
                            case 0x03://电动玻璃
                                result_TypeName = DeviceTypeEnums.ELECTRICGLASS.getName();
                                break;
                            case 0x10:// 86插座
                                result_TypeName = DeviceTypeEnums.OUTLET.getName();
                                break;
                            case 0x11:// 移动插座
                                result_TypeName = DeviceTypeEnums.MOBILEOUTLET.getName();
                                break;
                        }
                        break;
                    case 0x06:// 二路灯开关
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.TABLELAMP.getName();
                        }
                        break;
                    case 0x07:// 三路灯开关
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.DROPLIGHT.getName();
                        }
                        break;
                    case 0x08:// 1路调光灯开关
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.LIGHTMODULATIONLAMP.getName();
                        }
                        break;
                    case 0x09:// 声光报警器
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.SOUNDANDLIGHTALARM.getName();
                        }
                        break;
                    case 0x0b:// 多彩灯泡
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.COLORFULBULB.getName();
                        }
                        break;
                    case 0x0e:// 多彩冷暖灯
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.COLORFULLAMP.getName();
                        }
                        break;
                    case (byte) 0x81:// 人体红外感应
                        switch (product){
                            case 0x02:// 人体红外感应-new
                                result_TypeName = DeviceTypeEnums.PYROELECTRICINFRARED.getName();
                                break;
                            case 0x03:// 一氧化碳-new
                                result_TypeName = DeviceTypeEnums.GASSENSOR.getName();
                                break;
                            case 0x04:// 烟雾传感器-new
                                result_TypeName = DeviceTypeEnums.SMOKESENSOR.getName();
                                break;
                            case 0x05:// SOS报警器
                                result_TypeName = DeviceTypeEnums.SOSBUTTON.getName();
                                break;
                        }
                        break;
                    case (byte) 0x8a:// 场景控制器
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.SCENECONTROL.getName();
                        }
                        break;
                    case (byte) 0xc1:// 六路面板
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.SIXWAYPANEL.getName();
                        }
                        break;
                    default:
                        break;
                }

            } else if (od[0] == 0x0f && od[1] == (byte) 0xb4) {// 多用途规约控制器
                // 4020

            } else if (od[0] == 0x0f && od[1] == (byte) 0xbe) {// 多用途休眠设备控制器4030
                byte deviceType = type;// 设备类型
                byte product = productSrc;// 产品类型
                switch (deviceType) {
                    case 0x01:
                        if (product == 0x02) {// 门磁设备1-开；2-关；3-由关到开；4-由开到关；门磁
                            result_TypeName = DeviceTypeEnums.DOORANDWINDOW.getName();
                        }
                        break;
                    case 0x02:
                        if (product == 0x02) {//指纹锁
                            result_TypeName = DeviceTypeEnums.LOCK.getName();
                        }
                        break;
                    case 0x03:
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.GASSENSOR.getName();
                        }
                        break;
                    case 0x04:
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.NFRAREDINDUCTION.getName();
                        }
                        break;
                    case 0x05:
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.WATERIMMERSIONSENSOR.getName();
                        }
                        break;
                    case 0x07:// 烟雾传感器-new
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.SMOKESENSOR.getName();
                        }
                        break;
                    case (byte) 0x81:
                        if (product == 0x02) {//门磁-new
                            result_TypeName = DeviceTypeEnums.DOORANDWINDOW.getName();
                        }else if(product == 0x03){//窗磁-new
                            result_TypeName = DeviceTypeEnums.MAGNETICWINDOW.getName();
                        }
                        break;
                    case (byte) 0x83:// 水浸传感器-new
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.WATERIMMERSIONSENSOR.getName();
                        }
                        break;
                    case (byte) 0x86:// 人体红外感应-new
                        if (product == 0x02) {
                            result_TypeName = DeviceTypeEnums.NFRAREDINDUCTION.getName();
                        }
                        break;
                    default:
                        break;
                }
            } else if (od[0] == 0x0f && od[1] == (byte) 0xc8) {// 智能计量设备4040
                //result_TypeName = DeviceTypeEnums.JLOUTLET.getName();
                byte deviceType = type;// 设备类型
                byte product = productSrc;// 产品类型
                switch (deviceType) {
                    case 0x01:
                        if (product == 0x02) {//单相电表-new
                            result_TypeName = DeviceTypeEnums.SINGLEMETER.getName();
                        }
                        break;
                    case 0x02:
                        if (product == 0x02) {//计量控制盒-new
                            result_TypeName = DeviceTypeEnums.METERINGSWITCH.getName();
                        }
                        break;
                    case 0x03:
                        if (product == 0x02) {//三厢电表-new
                            result_TypeName = DeviceTypeEnums.THREECOMPARTMENTAMMETER.getName();
                        }
                        break;
                    case 0x04:
                        if (product == 0x02) {//计量插座（10A）-new
                            result_TypeName = DeviceTypeEnums.METERINGSOCKET10.getName();
                        } else if (product == 0x03) {//计量插座（16A）-new
                            result_TypeName = DeviceTypeEnums.METERINGSOCKET16.getName();
                        }
                        break;
                }
            } else if (od[0] == 0x0f && od[1] == (byte) 0xe6) {// 协议转发4070
                byte deviceType = type;// 设备类型
                byte product = productSrc;// 产品类型
                if(deviceType == 0x02){
                    switch (product){
                        case 0x02://红外转发器-new
                            result_TypeName = DeviceTypeEnums.INFRARED.getName();
                            break;
                        case 0x03://音乐背景器-new

                            break;
                        case 0x10://电动窗帘-new
                            result_TypeName = DeviceTypeEnums.ELECTRICCURTAIN.getName();
                            break;
                        case 0x11://平移开窗器-new
                            result_TypeName = DeviceTypeEnums.WINDOWOPENER.getName();
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

            } else if (od[0] == 0x82 && od[1] == 0x01) {//设置网关手机号码、报警号码、报警短信

            }/*else if (cmdId == 0x07) {// 协议转发
                byte zfDeviceType = Tools.hexStr2Byte(deviceInfo.getDevice_type());
                byte childType = 0;
                if(deviceInfo.getSindex_length() != null) {
                    childType = Tools.hexStr2Byte(deviceInfo.getSindex_length());
                }
                switch (zfDeviceType) {
                    case (byte) 0xa1://指纹锁

                        break;
                    case (byte) 0xff://开窗器01
                        if (childType == 0x01) {// 开窗器

                        } else  { // 电动窗帘00

                        }
                        break;
                    case (byte) 0x86:
                        if (childType == 0x06) {// 电动床
                            result_TypeName = DeviceTypeEnums.ELECTRICBED.getName();
                        } else if (childType == 0x10) {// 新风
                            result_Type = DeviceTypeEnums.FRESHAIR.getName();
                        }
                        break;
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_TypeName;
    }
}
