package com.project.utils.push;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.google.gson.Gson;
import com.project.common.config.GlobalConfiguration;

/**
 * Created by xieyanhao on 16/10/29.
 */

public class GTUtil {

    private static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    public static void push(Push.PushBean pushBean) {
        String appkey = GlobalConfiguration.getGTAppkey();
        String appId = GlobalConfiguration.getGTAppId();
        String master = GlobalConfiguration.getGTMaster();

        IGtPush push = new IGtPush(host, appkey, master);

        TransmissionTemplate template = transmissionTemplate(pushBean, appId, appkey);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        message.setPushNetWorkType(0); //可选。判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。
        Target target = new Target();

        target.setAppId(appId);
        target.setClientId(pushBean.to);

        IPushResult ret;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
    }

    private static TransmissionTemplate transmissionTemplate(Push.PushBean pushBean, String appId, String appkey) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appkey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        Gson gson = new Gson();
        template.setTransmissionContent(gson.toJson(pushBean));

        // ios专用
        APNPayload payload = new APNPayload();
        payload.setBadge(1);
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory(pushBean.category);
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(pushBean.message));
//        payload.setAlertMsg(getAlertMsg(pushBean));

        template.setAPNInfo(payload);
        return template;
    }

    private static APNPayload.DictionaryAlertMsg getAlertMsg(Push.PushBean pushBean) {

        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();

        alertMsg.setTitle(pushBean.title);
        alertMsg.setBody(pushBean.message);
        alertMsg.setActionLocKey("");
        alertMsg.setLaunchImage("");
        alertMsg.setLocKey("");
        alertMsg.setTitleLocKey("");

        return alertMsg;
    }

}
