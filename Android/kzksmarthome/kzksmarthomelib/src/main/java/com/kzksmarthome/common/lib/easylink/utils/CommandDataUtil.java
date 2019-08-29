package com.kzksmarthome.common.lib.easylink.utils;

import android.util.Log;

import com.kzksmarthome.common.lib.easylink.bean.GatewayWifi;
import com.kzksmarthome.common.lib.easylink.bean.Modle;
import com.kzksmarthome.common.lib.easylink.data.FinalData;
import com.kzksmarthome.common.lib.easylink.data.TempData;
import com.kzksmarthome.common.lib.easylink.modle.Modle_Login_Frame;
import com.kzksmarthome.common.lib.easylink.modle.Modle_Node_Zb_Unknow;
import com.kzksmarthome.common.lib.easylink.utils.news.CommandUtil;
import com.kzksmarthome.common.lib.easylink.utils.news.DataTypeUtil;

import java.util.List;


/**
 * Created by Administrator on 2016/8/28 0028.
 * 协议数据解析工具
 */
public class CommandDataUtil implements FinalData {
    //单例模式
    private static CommandDataUtil cdu;
    private CommandDataUtil() {
    }
    public static synchronized CommandDataUtil getIntance() {
        if (cdu == null)
            cdu = new CommandDataUtil();
        return cdu;
    }

    /**
     * easylink
     */
    //获取网关设备信息
    public GatewayWifi getGatewayByString(String data) {
        data = data.trim().toLowerCase();
        GatewayWifi gateway = null;
        if (data.contains("21005400010089ff")) {
            data = data.substring(16, data.length() - 4);
            String[] temp = new String[3];
            temp[0] = data.substring(0, 32);  //ip
            temp[1] = data.substring(32, 68);  //mac
            temp[2] = data.substring(68, 148);  //device_name
            //获取ip
            String[] temp_ip = temp[0].split("2e");
            StringBuffer ip = new StringBuffer("");
            for (int i = 0; i < temp_ip.length; i++) {
                if (i != temp_ip.length - 1)
                    ip.append(CommandUtil.getIpNum(temp_ip[i]));
                else ip.append(CommandUtil.getIpNum(CommandUtil.getDataEndWithoutZero((temp_ip[i]))));
                if (i != temp_ip.length - 1)
                    ip.append(".");
            }
            //获取mac
            String[] temp_mac = temp[1].split("2d");
            StringBuffer mac = new StringBuffer("");
            for (int i = 0; i < temp_mac.length; i++) {
                temp_mac[i] = temp_mac[i].substring(0, 4);
                mac.append((char) DataTypeUtil.hexStringToBytes(temp_mac[i].substring(0, 2))[0]).append((char) DataTypeUtil.hexStringToBytes(temp_mac[i].substring(2, 4))[0]);
                if (temp_mac.length - 1 != i)
                    mac.append(":");
            }
            //获取device_name
            StringBuffer device_name = new StringBuffer("");
            device_name.append(DataTypeUtil.hexStringToBytes(CommandUtil.getDataEndWithoutZero(temp[2])));

            gateway = new GatewayWifi(ip.toString(), mac.toString(), "0:0:0:0:0:0", TempData.SSID);
        }
        return gateway;
    }
    //获取登入帧信息
    public Modle_Login_Frame getLoginFrameByString(String data) {
        Modle_Login_Frame frame = null;
        try {
            String index = data.substring(30, 38);
            String system_state = data.substring(38, 40);
            String pattern = data.substring(40, 42);
            String user_pwd = data.substring(42, 54);
            String universal_pwd = data.substring(54, 66);
            String wifi_mac = data.substring(66, 82);
            String zb_mac = data.substring(82, 98);
            String product_id = data.substring(98, 110);
            String product_code = data.substring(110, 122);
            String software_version = data.substring(122, 130);
            String hardware_version = data.substring(130, 138);
            frame = new Modle_Login_Frame(index, system_state, pattern, user_pwd, universal_pwd, wifi_mac, zb_mac, product_id, product_code, software_version, hardware_version);
        } catch (Exception e) {
            Log.e("登入帧解析错误", e.getMessage());
        }
        return frame;
    }


    /**
     * 对象获取
     */

    //获取休眠未知设备
    public Modle getNodeZb_Others_ByString(String data){
        Modle zb = null;
        try {
            String mac = data.substring(6, 22);
            String ucDeviceType =data.substring(26, 28);
            String ucSensorType =data.substring(28, 30);
            String ucWorkMode ="00";
            String ucWorkState ="00";
            String ucOD_Upload_Interval_0 = "00";
            String ucOD_Upload_Interval_1 = "00";
            String ucOD_Area ="--";
            String ucOD_Func = "00";
            String uiBATVoltage ="0000";
            String ucRSSI ="00";
            String ucLQI ="00";
            String others="";
            zb = new Modle_Node_Zb_Unknow(mac,ucDeviceType, ucSensorType, ucWorkMode, ucWorkState, ucOD_Upload_Interval_0, ucOD_Upload_Interval_1, ucOD_Area, ucOD_Func, uiBATVoltage, ucRSSI, ucLQI,others);
            zb.setFlag(data.substring(22, 26));
            zb.setModle_name(CommandUtil.getModleName(zb.getFlag(),zb.getUcDeviceType(),zb.getUcSensorType()));
            zb.setTime(CommandUtil.getTime());
            zb.setFatherMac(TempData.MAC);
            zb.setCustom_name("");
            zb.setSqlflag(0);
        } catch (Exception e) {
            Log.e("节点设备解析错误", e.getMessage());
        }
        return zb;
    }
    //获取设备
    public Modle getNodeZb_Unknow_ByString(String data) {
        Modle zb = null;
        try {
            String mac = data.substring(6, 22);
            String ucDeviceType = data.substring(38, 40);
            String ucSensorType = data.substring(40, 42);
            String ucWorkMode = data.substring(42, 44);
            String ucWorkState = data.substring(44, 46);
            String ucOD_Upload_Interval_0 = data.substring(46, 48);
            String ucOD_Upload_Interval_1 = data.substring(48, 50);
            String ucOD_Area = data.substring(50, 52);
            String ucOD_Func = data.substring(52, 54);
            String uiBATVoltage = data.substring(54, 58);
            String ucRSSI = data.substring(58, 60);
            String ucLQI = data.substring(60, 62);
            String others=(data.length()<=66?"":data.substring(62,data.length()-4));
            zb = new Modle_Node_Zb_Unknow(mac, ucDeviceType, ucSensorType, ucWorkMode, ucWorkState, ucOD_Upload_Interval_0, ucOD_Upload_Interval_1, ucOD_Area, ucOD_Func, uiBATVoltage, ucRSSI, ucLQI,others);
            zb.setFlag(data.substring(22, 26));
            zb.setModle_name(CommandUtil.getModleName(zb.getFlag(),zb.getUcDeviceType(),zb.getUcSensorType()));
            zb.setTime(CommandUtil.getTime());
            zb.setCustom_name("");
            zb.setFatherMac(TempData.MAC);
            zb.setSqlflag(0);
        } catch (Exception e) {
            Log.e("节点设备解析错误", e.getMessage());
        }
        return zb;
    }

    /**
     * 通用控制指令获取
     */
//    //获取设置区域指令
//    public String getSetAreaCommand(String mac1,Modle zb,String area){
//        String s="0201"+mac1+zb.getMac()+"03ef"+"08"+"01"+area;
//        String check = DataTypeUtil.getAddCheck(s); //校验码
//        s = DataTypeUtil.decimalToHex(s.length() / 2) + s;
//        s = "2a" + s + check + "23";
//        return s;
//    }
//    //设置场景指令  cmds-要执行设备的指令集合, mac-网关下zb的mac, num-队列编号, stateNum-场景编号，time-延时时间参数,startTime-定时时间
//    public String[] getSetStateCommand(List<String> cmds,String mac,String num,String stateNum,String time,String startTime){
//        String[] s=new String[cmds.size()+1];
//        String temp="01"+"0000000000000000000000000000000000000000"+"01"+num+"00"+DataTypeUtil.decimalToHex(cmds.size())+stateNum+"ffff"+"ffffff"+"ffffffffffffffff"+"ffffff"+"ffffffffffffffff"+startTime+time;
//        s[0]="5000"+mac+DataTypeUtil.decimalToHex(temp.length()/2)+temp;
//        s[0]="2a"+DataTypeUtil.decimalToHex(s[0].length()/2)+s[0]+DataTypeUtil.getAddCheck(s[0])+"23";
//        for(int i=1;i<s.length;i++){
//            s[i]="02"+num+DataTypeUtil.decimalToHex(cmds.size())+DataTypeUtil.decimalToHex(i)+cmds.get(i-1);
//            s[i]="5000"+mac+DataTypeUtil.decimalToHex(s[i].length()/2)+s[i];
//            s[i]="2a"+DataTypeUtil.decimalToHex(s[i].length()/2)+s[i]+DataTypeUtil.getAddCheck(s[i])+"23";
//        }
//        return s;
//    }
//    //删除场景指令
//    public String getDelStateCommand(List<String> cmds,String mac,String num,String stateNum,String time,String startTime){
//        String s="";
//        String temp="01"+"0000000000000000000000000000000000000000"+"01"+num+"ff"+DataTypeUtil.decimalToHex(cmds.size())+stateNum+"ffff"+"ffffff"+"ffffffffffffffff"+"ffffff"+"ffffffffffffffff"+startTime+time;
//        s="5000"+mac+DataTypeUtil.decimalToHex(temp.length()/2)+temp;
//        s="2a"+DataTypeUtil.decimalToHex(s.length()/2)+s+DataTypeUtil.getAddCheck(s)+"23";
//        return s;
//    }

    //设置联动指令   数据通道channel、数据类型dataType、比较类型compdata
    public String[] getSetState2Command(List<String> cmds, String mac, String num, String force, String bcf, String mac2, String channel, String dataType, String compType, String dataDown, String dataUp, String time){
        String[] s=new String[cmds.size()+1];
        String temp="01"+"0000000000000000000000000000000000000000"+"02"+num+"00"+DataTypeUtil.decimalToHex(cmds.size())+"ffffff"+force+"01"+bcf+mac2+channel+dataType+compType+"0018"+dataDown+dataUp+time;
        s[0]="5000"+mac+DataTypeUtil.decimalToHex(temp.length()/2)+temp;
        s[0]="2a"+DataTypeUtil.decimalToHex(s[0].length()/2)+s[0]+DataTypeUtil.getAddCheck(s[0])+"23";
        for(int i=1;i<s.length;i++){
            s[i]="02"+num+DataTypeUtil.decimalToHex(cmds.size())+DataTypeUtil.decimalToHex(i)+cmds.get(i-1);
            s[i]="5000"+mac+DataTypeUtil.decimalToHex(s[i].length()/2)+s[i];
            s[i]="2a"+DataTypeUtil.decimalToHex(s[i].length()/2)+s[i]+DataTypeUtil.getAddCheck(s[i])+"23";
        }
        return s;
    }
//    //删除联动指令
//    public String getDelState2Command(List<String> cmds,String mac,String num,String force,String bcf,String mac2,String channel,String dataType,String compType,String dataDown,String dataUp,String time){
//        String s="";
//        String temp="01"+"0000000000000000000000000000000000000000"+"02"+num+"ff"+DataTypeUtil.decimalToHex(cmds.size())+"ffffff"+force+"01"+bcf+mac2+channel+dataType+compType+"0018"+dataDown+dataUp+time;
//        s="5000"+mac+DataTypeUtil.decimalToHex(temp.length()/2)+temp;
//        s="2a"+DataTypeUtil.decimalToHex(s.length()/2)+s+DataTypeUtil.getAddCheck(s)+"23";
//        return s;
//    }
//    //触发场景或联动指令
//    public String getStateAllCommand(String mac,String num){
//        String s="5000"+mac+"0209"+num;
//        s="2a"+DataTypeUtil.decimalToHex(s.length()/2)+s+DataTypeUtil.getAddCheck(s)+"23";
//        return s;
//    }

}
