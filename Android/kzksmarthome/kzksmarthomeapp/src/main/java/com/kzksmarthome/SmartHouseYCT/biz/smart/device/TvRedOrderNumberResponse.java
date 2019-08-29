package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * Created by jack on 2016/10/19.
 */
public class TvRedOrderNumberResponse extends BaseResponse {

    /**
     * success : true
     * result : [{"button_id":1,"name":"测试","instruction_code":"16"},{"button_id":2,"name":"按钮","instruction_code":"12"}]
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * button_id : 1
     * name : 测试
     * instruction_code : 16
     */

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

    public static class ErrorBean {
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
    }

    public static class ResultBean {
        private int button_id;
        private String name;
        private String instruction_code;

        public int getButton_id() {
            return button_id;
        }

        public void setButton_id(int button_id) {
            this.button_id = button_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInstruction_code() {
            return instruction_code;
        }

        public void setInstruction_code(String instruction_code) {
            this.instruction_code = instruction_code;
        }
    }
}
