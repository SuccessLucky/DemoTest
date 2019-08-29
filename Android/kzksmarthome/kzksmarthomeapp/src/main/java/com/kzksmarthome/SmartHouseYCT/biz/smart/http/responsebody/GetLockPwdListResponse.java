package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: GetDeviceByIdResponse
 * @Description: 获取指纹密码列表信息返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class GetLockPwdListResponse extends BaseResponse {
    /**
     * success : true
     * result : [{"device_id":1,"lock_id":2,"unlock_psw":"12345678","unlock_times":0},{"device_id":1,"lock_id":3,"unlock_psw":"12345678","unlock_times":0}]
     */
    private boolean success;
    private List<ResultBean> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * device_id : 1
         * lock_id : 2
         * unlock_psw : 12345678
         * unlock_times : 0
         */
        private int device_id;
        private int lock_id;
        private String unlock_psw;
        private int unlock_times;

        public int getDevice_id() {
            return device_id;
        }

        public void setDevice_id(int device_id) {
            this.device_id = device_id;
        }

        public int getLock_id() {
            return lock_id;
        }

        public void setLock_id(int lock_id) {
            this.lock_id = lock_id;
        }

        public String getUnlock_psw() {
            return unlock_psw;
        }

        public void setUnlock_psw(String unlock_psw) {
            this.unlock_psw = unlock_psw;
        }

        public int getUnlock_times() {
            return unlock_times;
        }

        public void setUnlock_times(int unlock_times) {
            this.unlock_times = unlock_times;
        }
    }
}
