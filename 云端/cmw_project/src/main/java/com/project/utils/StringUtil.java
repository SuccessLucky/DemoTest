package com.project.utils;

import com.google.common.base.Strings;
import com.project.controller.rest.PushController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static Log logger = LogFactory.getLog(StringUtil.class);

    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String userAgent(HttpServletRequest request) {
        String from = "";
        String agent = request.getHeader("User-Agent");
        if (agent == null) {
            from = "Unknown";
        } else if (agent.contains("iPhone")) {
            from = "iPhone";
        } else if (agent.contains("iPad")) {
            from = "iPad";
        } else if (agent.contains("iPod")) {
            from = "iPod";
        } else if (agent.contains("Android")) {
            from = "Android";
        } else if (agent.contains("Macintosh") && agent.contains("Mac")) {
            from = "Mac";
        } else if (agent.contains("Windows")) {
            from = "Windows";
        } else if (agent.contains("MicroMessenger")) {
            from = "WeChat";
        } else {
            logger.warn("未知 User-Agent : " + agent);
            from = "Unknown";
        }
        return from;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        String ipArrayString = request.getHeader("X-Forwarded-For");
        if (ipArrayString != null && !ipArrayString.isEmpty() && ipArrayString.length() > 0) {
            String[] ipArray = ipArrayString.split(",");
            if (ipArray.length > 0) {
                ip = ipArray[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unkonwn".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 生成字母和数字的随机数
     *
     * @param length 生成随机数的长度
     * @return
     */
    public static String getCharAndNumr(int length) {
        String val = "";

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

            if ("char".equalsIgnoreCase(charOrNum)) // 字符串
            {
                int choice = random.nextInt(2) % 2 == 0 ? 97 : 65; // 取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) // 数字
            {
                val += String.valueOf(random.nextInt(10));
            }
        }

        val = val.toUpperCase();
        boolean isAllNum = val.matches("^[0-9]*$");
        boolean isAllStr = val.matches("^[A-Z]*$");
        if (isAllNum || isAllStr) {
            return getCharAndNumr(5); // 使用递归排除单纯数字和字母随机数
        }
        return val;
    }

    /**
     * 字符串转换unicode
     */
    public static String stringToUnicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicodeToString(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    public static boolean isIP(String str) {

        if (str == null) {
            return Boolean.FALSE;
        }

        Pattern pattern = Pattern.compile("((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|[1-9])");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isEmail(String email) {
        if (Strings.isNullOrEmpty(email)) {
            return false;
        }

        if (email.endsWith("@klook.com")) {
            return true;
        }

        String check = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    public static boolean containsChinese(String s){
        if (null == s || "".equals(s.trim())) return false;
        for (int i = 0; i < s.length(); i++) {
            if (isChinese(s.charAt(i))) return true;
        }
        return false;
    }

    public static boolean isChinese(char a) {
        int v = (int)a;
        return (v >=19968 && v <= 171941);
    }

}
