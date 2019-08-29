package com.kzksmarthome.common.lib.easylink.utils.news;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class OD_4030_Or_4070_Util {
    public static String CMD_FINGERPRINTLOCK_OPENDOOR="fefefefea1600102";  //指纹锁远程开锁
    public static String RECEVICE_FINGERPRINTLOCK_DISTANCE="00";  //远程开锁第三方指令(实际上是通用上行指令成功的反馈)
    public static String RECEVICE_FINGERPRINTLOCK_PASSWORD="51";  //密码开锁第三方指令
    public static String RECEVICE_FINGERPRINTLOCK_FINGERPR="50";  //指纹开锁第三方指令
    /**
     * 获取透传指令
     * @param mac1  网关mac
     * @param mac2  设备mac
     * @param cmd   第三方指令
     * @return
     */
    public static String getCmd_TransparentTransmission(String mac1, String mac2, String cmd){
        return CommandUtil.getCmd_TransparentTransmission(mac1,mac2,cmd);
    }
    /**
     * 用于检验指纹锁反馈指令是哪种
     */
    public static String getFINGERPRINTLOCK_ReceviceDataType(String data){
        String temp=data.substring(40,data.length()-4);
        temp=temp.replaceAll("fffefe","");
        String head=temp.substring(0,2);
        String cmdType=temp.substring(2,4);
        String content=temp.substring(4,temp.length()-2);
        String check=temp.substring(temp.length()-2,temp.length());
        if(!"a1".equals(head))
            return null;
        if(!DataTypeUtil.getAddCheck(head+cmdType+content).equals(check))
            return null;
        if(RECEVICE_FINGERPRINTLOCK_DISTANCE.equals(cmdType)){
            return "远程开锁";
        }
        if(RECEVICE_FINGERPRINTLOCK_PASSWORD.equals(cmdType)){
            return "密码开锁"+"\n序号："+content.substring(2,content.length());
        }
        if(RECEVICE_FINGERPRINTLOCK_FINGERPR.equals(cmdType)){
            return "指纹开锁"+"\n序号："+content.substring(2,content.length());
        }
        return null;
    }
}
