package com.kzksmarthome.common.lib.tools;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * @Description: 工具类
 */
public class Tools {
    /**
     * 红外命令消息序号
     */
    public static byte infrared_msgno = 0;


    /**
     * 获取计量数据
     * @param data
     * @return
     */
    public static String getJLData(byte[] data,int num){
        double result = 0;
        try {
            String resultStr = "";
            for (int i = (data.length - 1); i > -1; i--) {
                resultStr = resultStr + byte2HexStr(data[i]);
            }
            result = Double.valueOf(resultStr)/num;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Double.toString(result);
    }

    /**
     * int转化为byte数组
     *
     * @param n 整数
     * @return 字节数组
     */
    public static byte[] byteToByteArray(int n) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeByte(n);
            return out.toByteArray();
        } catch (IOException e) {
            Log.e(IOTConfig.TAG, "Tools-->34-->" + e.toString());
        }
        return null;
    }

    /**
     * int转化为byte数组
     *
     * @param n 整数
     * @return 字节数组
     */
    public static byte[] intToByteArray(int n) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeInt(n);
            return out.toByteArray();
        } catch (IOException e) {
            Log.e(IOTConfig.TAG, "Tools-->50-->" + e.toString());
        }
        return null;
    }

    /**
     * short转化为byte数组
     *
     * @param n 整数
     * @return 字节数组
     */
    public static byte[] shortToByteArray(int n) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeShort(n);
            return out.toByteArray();
        } catch (IOException e) {
            Log.e(IOTConfig.TAG, "Tools-->shortToByteArray-->" + e.toString());
        }
        return null;
    }

    /**
     * byte数组转化为byte
     *
     * @param byteArray 字节数组
     * @return 整数
     */
    public static int byteArrayToByte(byte[] byteArray) {
        ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
        DataInputStream dis = new DataInputStream(input);
        try {
            return dis.readByte() & 0xFF;
        } catch (IOException e) {
            Log.e(IOTConfig.TAG, "Tools-->byteArrayToByte-->" + e.toString());
        }
        return -1;
    }

    /**
     * byte数组转化为Short
     *
     * @param byteArray 字节数组
     * @return 整数
     */
    public static int byteArrayToShort(byte[] byteArray) {
        ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
        DataInputStream dis = new DataInputStream(input);
        try {
            return dis.readShort() & 0xFFFF;
        } catch (IOException e) {
            Log.e(IOTConfig.TAG, "Tools-->byteArrayToShort-->" + e.toString());
        }
        return -1;
    }

    /**
     * byte数组转化为int
     *
     * @param byteArray 字节数组
     * @return 整数
     */
    public static int byteArrayToInt(byte[] byteArray) {
        int iOutcome = 0;
        try {
            byte bLoop;
            byte[] newArray = new byte[byteArray.length];
            for (int i = 0; i < byteArray.length; i++) {
                newArray[i] = byteArray[byteArray.length - 1 - i];
            }
            for (int i = 0; i < newArray.length; i++) {
                bLoop = newArray[i];
                iOutcome += (bLoop & 0xFF) << (8 * i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return iOutcome;
    }

    /**
     * 生成消息序号
     */
    public static byte[] createMsgSeqNo() {
        if (IOTConfig.SEQNO > 65535) {
            IOTConfig.SEQNO = 1;
        }
        int intMesgSeqNo = IOTConfig.SEQNO++;
        byte[] msgSeqNo = new byte[2];
        msgSeqNo = Tools.shortToByteArray(intMesgSeqNo);
        return msgSeqNo;
    }

    /**
     * 将指定日期转换为"yyyy-MM-dd HH:mm:ss"格式
     *
     * @param date Date
     * @return String
     */
    public static String formatDateString1(Date date) {
        if (date == null) {
            return "";
        } else {
            String disp = "";
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            disp = bartDateFormat.format(date);
            return disp;
        }
    }

    /**
     * 将指定日期转换为"yyyyMMddHHmmss"格式
     *
     * @param date Date
     * @return String
     */
    public static String formatDateString2(Date date) {
        if (date == null) {
            return "";
        } else {
            String disp = "";
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            disp = bartDateFormat.format(date);
            return disp;
        }
    }

    /**
     * 将日期字符串转换为Date
     *
     * @param dateStr
     * @return
     */
    public static Date getDateFromString(String dateStr) {
        Date datetime = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            datetime = df.parse(dateStr);
        } catch (Exception ex) {
            datetime = null;
        }
        return datetime;
    }

    /**
     * 将日期字符串转换为Date
     *
     * @param dateStr
     * @return
     */
    public static Date getDateFromString2(String dateStr) {
        Date datetime = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            datetime = df.parse(dateStr);
        } catch (Exception ex) {
            datetime = null;
        }
        return datetime;
    }

    /**
     * 将日期字符串转换为Date
     *
     * @param dateStr
     * @return
     */
    public static Date getDateFromString3(String dateStr) {
        Date datetime = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            datetime = df.parse(dateStr);
        } catch (Exception ex) {
            datetime = null;
        }
        return datetime;
    }

    /**
     * 判断数组array里是否包含原始element
     *
     * @param array
     * @param element
     * @return
     */
    public static boolean isArrayContainsItem(int[] array, int element) {
        boolean isContain = false;
        for (int item : array) {
            if (item == element) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    /**
     * 判断数组array里是否包含原始element
     *
     * @param array
     * @param element
     * @return
     */
    public static boolean isArrayContainsByteItem(byte[] array, byte element) {
        boolean isContain = false;
        for (byte item : array) {
            if (item == element) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    /**
     * 判断两个数组的内容是否相同
     * laixj  2013-10-22
     * 修改者名字 修改日期
     * 修改内容
     *
     * @return void
     * @throws
     */
    public static boolean compareArray(byte[] array1, byte[] array2) {
        boolean isSame = true;
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                isSame = false;
                break;
            }
        }
        return isSame;
    }

    /**
     * 生成消息序号
     */
    public synchronized static byte createInfraredMsgNo() {
        if (infrared_msgno > 127) {
            infrared_msgno = 1;
        }
        byte intMesgSeqNo = infrared_msgno++;
        return intMesgSeqNo;
    }

    /**
     * 打印字节数组
     *
     * @param bytes
     * @param tag
     */
    public static void printByteArray(byte[] bytes, String tag, String detail) {
        String string = "";
        for (byte b : bytes) {
            string += toHexString(b) + " ";
        }
        Log.d(tag, detail + "-->" + string);
    }

    /**
     * 将字节以十六进制格式输出(如FF,08)
     *
     * @param b
     * @return
     */
    public static String toHexString(byte b) {
        String string = Integer.toHexString(b);
        int length = string.length();
        if (length == 1) {
            return "0" + string;
        } else if (length > 2) {
            return string.substring(length - 2);
        }
        return string;
    }

    public static void main(String[] args) {

    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) throws Exception {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * byte转字符串
     * @param b
     * @return
     * @throws Exception
     */
    public static String byte2HexStr(byte b) throws Exception{
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        stmp = Integer.toHexString(b & 0xFF);
        if(stmp != null && stmp.length() < 2){
            stmp = "0"+stmp;
        }
        return stmp;
    }

    /**
     * bytes字符串转换为Byte值
     *
     * @param String src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) throws Exception {
        src = src.trim().replaceAll(" ", "");
        int m = 0, n = 0;
        int l = src.length() / 2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int b = Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
            ret[i] = (byte) b;
        }
        return ret;
    }

    /**
     * 字符串转byte
     * @param src
     * @return
     * @throws Exception
     */
    public static byte hexStr2Byte(String src) throws Exception {
        src = src.trim().replaceAll(" ", "");
        int data = Integer.decode("0x" + src);
        byte ret = (byte) data;
        return ret;
    }

    /**
     *  16进制字符串转int
     * @param src
     * @return
     * @throws Exception
     */
    public static int hexStr2Int(String src)throws Exception{
        src = src.trim().replaceAll(" ", "");
        int data = Integer.decode("0x" + src);
        return data;
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param String str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) throws Exception {
        str = str.trim().replaceAll(" ", "");
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 十六进制转换字符串
     *
     * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) throws Exception {
        hexStr = hexStr.trim().replaceAll(" ", "");
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * String的字符串转换成unicode的String
     *
     * @param String strText 全角字符串
     * @return String 每个unicode之间无分隔符
     * @throws Exception
     */
    public static String strToUnicode(String strText) throws Exception {
        strText = strText.trim().replaceAll(" ", "");
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
                str.append("\\u" + strHex);
            else // 低位在前面补00
                str.append("\\u00" + strHex);
        }
        return str.toString();
    }

    /**
     * unicode的String转换成String的字符串
     *
     * @param String hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex) throws Exception {
        hex = hex.trim().replaceAll(" ", "");
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }

    /**
     * 拆包工具
     * @return
     */
    public static ArrayList<byte[]> splitData(byte[] datas){
        ArrayList<byte[]> arrayList = null;
        if (datas != null) {
            arrayList = new ArrayList<byte[]>();
            for (int i = 0; i < datas.length; i++) {
                if (datas[i] == 0x2a) {//找到针头
                    int dataLength = datas[i + 1] + 4;
                    byte[] newData = new byte[dataLength];
                    System.arraycopy(datas, i, newData, 0, dataLength);
                    if (newData != null && newData[0] == 0x2a
                            && newData[newData.length - 1] == 0x23
                            && newData.length > 15) {// 判断是否是一条完整的数据
                        // 检验和是否一致
                        if (DataPack.checkSmartSum(newData)) {
                            arrayList.add(newData);
                            i = i + dataLength - 1;
                           /* try {
                                Log.d("AddDevice","拆包-----"+Tools.byte2HexStr(newData));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                        }
                    }
                }
            }
        }
        return arrayList;
    }
    /**
     * 检测是否需要拆包
     * @param bytes
     * @return
     */
    public static boolean isNeedSpilt(byte[] bytes) {
        if (bytes.length > 1) {
            if (bytes.length > (bytes[1] + 4)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取小蛮腰指纹锁校验位
     * @param data
     * @return
     */
    public static byte lockDataSum(byte[] data){
        byte sum = 0;
        if(data != null && data.length > 2){
            for(int i = 3; i< (data.length -1); i++) {
                sum ^= data[i];
            }
        }
        return sum;
    }
}
