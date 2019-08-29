package com.project.bean;

/**
 * Created by xieyanhao on 16/10/28.
 */
public class AlarmBeanResp {

    private int alarm_id;
    private String create_date;
    private String alarm_msg;

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getAlarm_msg() {
        return alarm_msg;
    }

    public void setAlarm_msg(String alarm_msg) {
        this.alarm_msg = alarm_msg;
    }
}
