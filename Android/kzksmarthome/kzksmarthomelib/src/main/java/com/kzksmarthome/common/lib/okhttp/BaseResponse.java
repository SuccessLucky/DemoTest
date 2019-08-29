package com.kzksmarthome.common.lib.okhttp;

/**
 * @Title: BaseResponse
 * @Description: 网络请求返回基类
 * @author laixj
 * @date 2016/10/23 17:05
 * @version V1.0
 */
public class BaseResponse {

    private ErrorBean error;

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "error=" + error +
                '}';
    }
}
