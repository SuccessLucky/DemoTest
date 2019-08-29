package com.kzksmarthome.common.lib.tools;


/**
 * @Description: 配置文件
 */
public class IOTConfig {
    /**
     * 消息序号值
     */
    public static int SEQNO = 1;
    /**
     * 电动窗帘正转Data byte[]
     */
    public static final byte[] ELECTRIC_CURTAIN_ZZ = {(byte) 0xFF, 0x00, 0x00, 0x3D, 0x4F, 0x50};

    /**
     * 电动窗帘反转Data byte[]
     */
    public static final byte[] ELECTRIC_CURTAIN_FZ = {(byte) 0xFF, 0x00, 0x00, 0x3D, 0x43, 0x4C};
    /**
     * 电动窗帘停止Data byte[]
     */
    public static final byte[] ELECTRIC_CURTAIN_STOP = {(byte) 0xFF, 0x00, 0x00, 0x3D, 0x53, 0x54};
    /**
     * 电动窗帘重启Data byte[]
     */
    public static final byte[] ELECTRIC_CURTAIN_RESTART = {(byte) 0xFF, 0x00, 0x00, 0x3D, 0x52, 0x53};
    /**
     * 电动窗帘状态读取Data byte[]
     */
    public static final byte[] ELECTRIC_CURTAIN_STATE = {(byte) 0xFF, 0x00, 0x00, 0x3D, 0x50, 0x4F};

    /**
     * 平移开窗器正转Data byte[]
     */
    public static final byte[] WINDOW_OPENER_ZZ = {(byte) 0xFF, 0x01, 0x00, 0x3D, 0x4F, 0x50};

    /**
     * 平移开窗器反转Data byte[]
     */
    public static final byte[] WINDOW_OPENER_FZ = {(byte) 0xFF, 0x01, 0x00, 0x3D, 0x43, 0x4C};
    /**
     * 平移开窗器停止Data byte[]
     */
    public static final byte[] WINDOW_OPENER_STOP = {(byte) 0xFF, 0x01, 0x00, 0x3D, 0x53, 0x54};
    /**
     * 平移开窗器重启Data byte[]
     */
    public static final byte[] WINDOW_OPENER_RESTART = {(byte) 0xFF, 0x01, 0x00, 0x3D, 0x52, 0x53};
    /**
     * 平移开窗器状态读取Data byte[]
     */
    public static final byte[] WINDOW_OPENER_STATE = {(byte) 0xFF, 0x01, 0x00, 0x3D, 0x50, 0x4F};
    /**
     * 设置网关电话号码
     */
    public static final byte[] SETTING_IOT_PHONE = {0x00, 0x00, 0x00 ,0x10};
    /**
     * 设置网关报警电话号码
     */
    public static final byte[] SETTING_IOT_PHONE_BJ = {0x00, 0x00, 0x00 ,0x20};
    
    /**
     * 空调控制命令类型--开关(FF： 开，00：关)
     */
    public static final byte  KT_ORDER_OPEN_CLOSE = 0x04;
    /**
     * 空调控制命令类型--模式(00：自动  01：制冷   02：除湿    03：送风   04：制暖)
     */
    public static final byte  KT_ORDER_MODEL = 0x05;
    /**
     * 空调控制命令类型--温度(10H - 1EH （16-31度）)
     */
    public static final byte  KT_ORDER_WD = 0x06;
    /**
     * 空调控制命令类型--风速（00 = 自动  01=1档   02=2档    03=3档）
     */
    public static final byte  KT_ORDER_SPEED = 0x07;
    /**
     * 空调控制命令类型--风向（00 = 自动摆风  01手动摆风 ）
     */
    public static final byte  KT_ORDER_WIND_WAY = 0x08;

    /**
     * 空调控制命令类型--休眠（0xFF 必须为0xFF）
     */
    public static final byte  KT_ORDER_IC_SLEEP = 0x01;

    /**
     * 空调控制命令类型--初始化启动（0xAA 必须是0xAA）
     */
    public static final byte  KT_ORDER_INIT_START = (byte) 0xaa;

    /**
     * 空调控制命令类型--初始化结束（0xCC 必须是0xCC）
     */
    public static final byte  KT_ORDER_INIT_END = (byte) 0xcc;


    /**
     * TAG
     */
    public static final String TAG = "DataPackage";
    /**
     * 当前的网关地址
     */
    public static byte[] SRCADDR;
    /**
     * 空调类型
     */
    public static int[] KT_TYPE= {0,};
    /**
     * 红外设备类型
     */
    public static String RED_DEVICE_TYPE = "{\n" +
            "    \"title\": \"红外设备类型\",\n" +
            "    \"redDeviceList\": [\n" +
            "        {\n" +
            "            \"type\": \"5\",\n" +
            "            \"name\": \"电视\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"19\",\n" +
            "            \"name\": \"DVD\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"20\",\n" +
            "            \"name\": \"机顶盒\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"21\",\n" +
            "            \"name\": \"投影仪\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"22\",\n" +
            "            \"name\": \"音响\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"23\",\n" +
            "            \"name\": \"风扇\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"24\",\n" +
            "            \"name\": \"背景音乐\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}
