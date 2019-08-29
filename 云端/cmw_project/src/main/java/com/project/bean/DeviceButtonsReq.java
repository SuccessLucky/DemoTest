package com.project.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/9/21.
 */
public class DeviceButtonsReq {

    private Integer device_id;
    private List<DeviceButtonBean> device_buttons = new ArrayList<>();

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public List<DeviceButtonBean> getDevice_buttons() {
        return device_buttons;
    }

    public void setDevice_buttons(List<DeviceButtonBean> device_buttons) {
        this.device_buttons = device_buttons;
    }

    public static class DeviceButtonBean {

        private int button_id;
        private String name; // 按键名称
        private String instruction_code; // 指令编码

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
