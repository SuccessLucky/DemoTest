package com.kzksmarthome.SmartHouseYCT.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.PushResponse;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.util.GsonHelper;

public class GetuiPushReceiver extends BroadcastReceiver {

	private static final String TAG = "GetuiPushReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		try {
			Log.d(TAG, "onReceive() action=" + bundle.getInt("action"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(bundle == null){
			return;
		}
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");
			if (payload != null) {
				String message = new String(payload);
				Log.d(TAG, "Got Payload:" + (message == null ? "" : message));
				// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
				if (!TextUtils.isEmpty(message)) {
					try {
						PushResponse pushData = GsonHelper.getGson().fromJson(message, PushResponse.class);
						Log.i(TAG, "新消息-->"+pushData.toString());
						if (null != pushData) {
							SmartHomeApp.getInstance().showNotic(pushData.getMessage());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String cid = bundle.getString("clientid");
			Log.d(TAG, "获取cid--->" + cid);
			if(SmartHomeAppLib.getUserMgr().isLogin()){
				RestRequestApi.pushReport(context, cid, null);
			}
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
			 * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
			 * actionid); Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
			break;
		default:
			break;
		}
	}

	/**
	 * 处理消息
	 * 
	 * @param context
	 * @param msgid
	 * @param topage
	 * @param jumptype
	 * @param title
	 * @param message
	 * @param showposition
	 */
	private void handleMessage(Context context, final int msgid,
							   final String topage, final String jumptype, final String title,
							   final String message, final String showposition,
							   final String srcsystem, final String datingId,
							   final String datatype, final String uid, final String datingtype) {
		Log.d("uuuj", "处理新消息");
		// TODO: 2016/9/5
	}
}
