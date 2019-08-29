package com.project.utils.push;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.project.entity.PushEntity;
import com.project.service.PushEntityService;
import com.project.utils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/29.
 * new Push().toMerchant(id).newOrder().send();
 */
public class Push {
    private static Log logger = LogFactory.getLog(Push.class);

    private PushBean pushBean = new PushBean();
    private List<PushEntity> pushEntities = new ArrayList<>();
    private PushEntityService pushEntityService;

    public Push() {
        try {
            this.pushEntityService = new BeanUtils().getBean("pushEntityService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User toUser(int id) {
        try {

            this.pushEntities = pushEntityService.readPushEntities(id);
            pushBean.title = "[您有一条报警消息]";
        } catch (Exception e) {
            logger.error("user id :" + id);
            e.printStackTrace();
        }
        return new User(this);
    }

    public class User implements IUser {
        private Push push;

        public User(Push push) {
            this.push = push;
            pushBean.userType = "3";
        }

        @Override
        public Push newAlarm(int deviceId, String message) {
            pushBean.message = message;
            pushBean.category = String.valueOf(deviceId);
            pushBean.action = "1";
            return push;
        }

        @Override
        public Push test(String action, String category, String message) {
            pushBean.message = Strings.isNullOrEmpty(message) ? "测试推送,请忽略!" : message;
            pushBean.action = Strings.isNullOrEmpty(action) ? "2" : action;
            pushBean.category = Strings.isNullOrEmpty(category) ? "2" : category;
            return push;
        }
    }

    public void send() {
        operation();
    }

    public String sandbox() {
        operation();
        return new Gson().toJson(pushBean);
    }

    private void operation() {
        try {
            for (PushEntity pushEntity : pushEntities) {

                logger.warn(pushEntity.toString());
                logger.warn(pushBean.toString());

                String toUUID = pushEntity.getUuid();

                if (Strings.isNullOrEmpty(toUUID)) {
                    continue;
                }

                pushBean.to = toUUID;
                GTUtil.push(pushBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class PushBean {
        String message = "";
        String title = "";
        String action = "";
        String to = "";
        String category = "";
        String userType = "-1";
        String type = "";

        @Override
        public String toString() {
            return "PushBean{" +
                    "message='" + message + '\'' +
                    ", title='" + title + '\'' +
                    ", action='" + action + '\'' +
                    ", to='" + to + '\'' +
                    ", category='" + category + '\'' +
                    ", userType='" + userType + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    interface IUser {

        Push newAlarm(int deviceId, String deviceName);

        Push test(String action, String category, String message);

    }

}
