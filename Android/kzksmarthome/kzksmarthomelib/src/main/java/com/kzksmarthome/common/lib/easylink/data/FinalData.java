package com.kzksmarthome.common.lib.easylink.data;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public interface FinalData {
    //设备类型4010-4070字符串
    public static final String FLAG_RECEIVE_NODE_4010="0faa";  //4010	0faa    多用途开关动作控制器
    public static final String FLAG_RECEIVE_NODE_4030="0fbe";  //4030	0fbe    休眠节点上报参数
    public static final String FLAG_RECEIVE_NODE_4040="0fc8";  //4040	0fc8    智能计量设备
    public static final String FLAG_RECEIVE_NODE_4070="0fe6";  //4070   0fe6    透传设备

    //设备类型4010-4070通用昵称
    public static final String NODE_4010="多用开关";
    public static final String NODE_4030="休眠设备";
    public static final String NODE_4040="计量设备";
    public static final String NODE_4070="透传设备";

    //其他参数
    public static final String FLAG_RECEIVE_NODE_1001="03e9";   //OD1001
    public static final String FLAG_RECEIVE_NODE_1007="03ef";   //OD1007
    public static final String FLAG_RECEIVE_NODE_5060="13c4";   //OD5060
}
