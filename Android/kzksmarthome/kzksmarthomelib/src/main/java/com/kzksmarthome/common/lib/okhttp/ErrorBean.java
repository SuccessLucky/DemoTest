package com.kzksmarthome.common.lib.okhttp;

/**
 * @Title: ErrorBean
 * @Description: Rest请求ErrorBean
 * @author laixj
 * @date 2016/10/4 15:15
 * @version V1.0
 */
public class ErrorBean {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
