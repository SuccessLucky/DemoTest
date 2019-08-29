package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceButtonInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceTools;
import com.kzksmarthome.SmartHouseYCT.util.DeviceStatusEnums;
import com.kzksmarthome.SmartHouseYCT.util.DeviceTypeEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: SceneEditAdapter
 * @Description: 场景编辑/添加界面场景详情适配器
 * @date 2016/10/16 7:47
 */
public class SceneEditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private Activity mContext;
	private LayoutInflater mLayoutInflater;
	//private ImageLoader imageLoader = ImageLoader.getInstance();
	//private in.srain.cube.image.ImageLoader mImageLoader;
	private String baseSceneImgUrl = "";
	private String baseDeviceImgUrl = "";

	private List<SceneDetailInfo> datailList = new ArrayList<SceneDetailInfo>();
	/**
	 * 网关地址
	 */
	private String iotMac;

	public SceneEditAdapter(Activity context) {
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		//mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context, R.drawable.translucent);
		baseSceneImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, "");
		baseDeviceImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_DEVICE_BASEURL, "");
		com.kzksmarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
		iotMac = userInfo.gateway;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View view = mLayoutInflater.inflate(R.layout.smart_scene_edit_item, viewGroup, false);
		SceneDeviceViewHolder sceneDeviceViewHolder = new SceneDeviceViewHolder(view);
		return sceneDeviceViewHolder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		SceneDeviceViewHolder sceneDeviceViewHolder = (SceneDeviceViewHolder) viewHolder;
		SceneDetailInfo deviceInfo = datailList.get(position);
		if (null != deviceInfo) {
			sceneDeviceViewHolder.smartSceneEditDeviceName.setText(deviceInfo.getDevice_name());
			sceneDeviceViewHolder.smartSceneEditDeviceLc.setText(deviceInfo.getFloor_name() + deviceInfo.getRoom_name());
			DeviceInfo deviceInfo1 = new DeviceInfo(deviceInfo);
			int deviceType = DeviceTools.getDeviceType(deviceInfo1);
			if (DeviceTypeEnums.TABLELAMP.getCode() == deviceType || DeviceTypeEnums.DROPLIGHT.getCode() == deviceType) {
				String way = deviceInfo.getOther_status();
				if (!TextUtils.isEmpty(way)) {
					int way_int = Integer.valueOf(way);
					if (way_int == 1) {
						updateDeviceIm(sceneDeviceViewHolder, deviceInfo1, deviceInfo.getDevice_state1());
						updateOpenOrCloseIm(sceneDeviceViewHolder, deviceInfo.getDevice_state1());
					} else if (way_int == 2) {
						updateDeviceIm(sceneDeviceViewHolder, deviceInfo1, deviceInfo.getDevice_state2());
						updateOpenOrCloseIm(sceneDeviceViewHolder, deviceInfo.getDevice_state2());
					} else if (way_int == 3) {
						updateDeviceIm(sceneDeviceViewHolder, deviceInfo1, deviceInfo.getDevice_state3());
						updateOpenOrCloseIm(sceneDeviceViewHolder, deviceInfo.getDevice_state3());
					}
				}
			} else if (DeviceTypeEnums.INFRARED.getCode() == deviceType) {
				updateDeviceIm(sceneDeviceViewHolder, deviceInfo1, deviceInfo.getDevice_state2());
				updateOpenOrCloseIm(sceneDeviceViewHolder, deviceInfo.getDevice_state2());
			} else {
				updateDeviceIm(sceneDeviceViewHolder, deviceInfo1, deviceInfo.getDevice_state1());
				updateOpenOrCloseIm(sceneDeviceViewHolder, deviceInfo.getDevice_state1());
			}
			sceneDeviceViewHolder.on_off_im.setVisibility(View.VISIBLE);
			if(deviceInfo.getDevice_OD().equals("0F BE")){
				sceneDeviceViewHolder.on_off_im.setVisibility(View.INVISIBLE);
			}else if(deviceInfo.getDevice_OD().equals("0F AA")) {
				String type = deviceInfo.getDevice_type();
				if (type.equals("81") || type.equals("82") || type.equals("83")) {
					sceneDeviceViewHolder.on_off_im.setVisibility(View.INVISIBLE);
				}
			}
			//sceneDeviceViewHolder.smartSceneEditDeviceIm.setTag(position);
			//imageLoader.displayImage(baseImgUrl + "Un_" + deviceInfo.getDevice_image() + "@2x.png", sceneDeviceViewHolder.smartSceneEditDeviceIm);
		}
	}

	/**
	 * 更新设备片
	 *
	 * @param sceneDeviceViewHolder
	 * @param deviceInfo
	 * @param state
	 */
	private void updateDeviceIm(SceneDeviceViewHolder sceneDeviceViewHolder, DeviceInfo deviceInfo, int state) {
		Log.d("laixj", "设备图片-->" + baseDeviceImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png");
		if (state == 1) {
			//sceneDeviceViewHolder.smartSceneEditDeviceIm.loadImage(mImageLoader, baseDeviceImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png");
			Glide
					.with(mContext)
					.load(baseDeviceImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png")
					.centerCrop()
					.placeholder(R.drawable.default_img)
					.crossFade()
					//.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(sceneDeviceViewHolder.smartSceneEditDeviceIm);
		} else {
			//sceneDeviceViewHolder.smartSceneEditDeviceIm.loadImage(mImageLoader, baseDeviceImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png");
			Glide
					.with(mContext)
					.load(baseDeviceImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png")
					.centerCrop()
					.placeholder(R.drawable.default_img)
					.crossFade()
					//.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(sceneDeviceViewHolder.smartSceneEditDeviceIm);
		}
	}

	/**
	 * 更新开关按钮
	 *
	 * @param sceneDeviceViewHolder
	 * @param state
	 */
	private void updateOpenOrCloseIm(SceneDeviceViewHolder sceneDeviceViewHolder, int state) {
		if (state == DeviceStatusEnums.ON.getCode()) {
			sceneDeviceViewHolder.on_off_im.setImageResource(R.drawable.smart_check_on);
		} else {
			sceneDeviceViewHolder.on_off_im.setImageResource(R.drawable.smart_check_off);
		}
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Activity mContext) {
		this.mContext = mContext;
	}

	public List<SceneDetailInfo> getDetailList() {
		return datailList;
	}

	public void setDetailList(List<SceneDetailInfo> detailList) {
		this.datailList = detailList;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return datailList == null ? 0 : datailList.size();
	}

	class SceneDeviceViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.top_line)
		View topLine;
		@BindView(R.id.smart_scene_edit_device_im)
		ImageView smartSceneEditDeviceIm;
		@BindView(R.id.smart_scene_edit_device_name)
		TextView smartSceneEditDeviceName;
		@BindView(R.id.smart_scene_edit_device_lc)
		TextView smartSceneEditDeviceLc;
		@BindView(R.id.smart_scene_edit_device_layout)
		RelativeLayout smartSceneEditDeviceLayout;
		@BindView(R.id.on_off_im)
		ImageView on_off_im;

		SceneDeviceViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}

		@OnLongClick(R.id.smart_scene_edit_device_layout)
		boolean onLong(){
			SceneDetailInfo sceneDetail = datailList.get(getPosition());
			if (null == sceneDetail) {
				return false;
			}
			DeviceInfo deviceInfo = new DeviceInfo(sceneDetail);
			if(DeviceTypeEnums.COLORFULBULB.getCode() == DeviceTools.getDeviceType(deviceInfo)){//多彩灯泡){
				Bundle bundle = new Bundle();
				bundle.putParcelable("deviceInfo",deviceInfo);
				PageSwitcher.switchToTopNavPage(mContext, ColorLightSceneFragment.class, bundle, "", deviceInfo.getDevice_name(), "");
			}
			return false;
		}

		@OnClick(R.id.smart_scene_edit_device_layout)
		void onClick() {
			try {
				Log.d("laixj", "点击场景设备");
				SceneDetailInfo sceneDetail = datailList.get(getPosition());
				if (null == sceneDetail) {
					return;
				}
				DeviceInfo deviceInfo = new DeviceInfo(sceneDetail);
				if (null != deviceInfo) {
					if (DeviceTypeEnums.DROPLIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo)
							|| DeviceTypeEnums.TABLELAMP.getCode() == DeviceTools.getDeviceType(deviceInfo)
							) {
					   /* Bundle bundle = new Bundle();
                        bundle.putParcelable("deviceInfo", deviceInfo);
                        bundle.putInt("deviceType", DeviceTypeEnums.TABLELAMP.getCode());
                        Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                        PageSwitcher.switchToTopNavPage((Activity) mContext, LightControlFragment.class, bundle, "", deviceInfo.getDevice_name(), "");*/
						String way = deviceInfo.getOther_status();
						if (TextUtils.isEmpty(way)) {
							return;
						}
						byte way_byte = Byte.valueOf(way);
						int state1 = deviceInfo.getDevice_state1();
						int state2 = deviceInfo.getDevice_state2();
						int state3 = deviceInfo.getDevice_state3();
						if (way_byte == 1) {
							if (1 == state1) {
								//RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
								sceneDetail.setDevice_state1(2);
							} else {
								//RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
								sceneDetail.setDevice_state1(1);
							}
						} else if (way_byte == 2) {
							if (1 == state2) {
								//RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
								sceneDetail.setDevice_state2(2);
							} else {
								//RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
								sceneDetail.setDevice_state2(1);
							}
						} else if (way_byte == 3) {
							if (1 == state3) {
								//RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
								sceneDetail.setDevice_state3(2);
							} else {
								//RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
								sceneDetail.setDevice_state3(1);
							}
						}
						notifyDataSetChanged();
					} else if (DeviceTypeEnums.LIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						if (1 == deviceInfo.getDevice_state1()) {
							//RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
							sceneDetail.setDevice_state1(2);
						} else {
							//RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					} else if (DeviceTypeEnums.MOBILEOUTLET.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						if (1 == deviceInfo.getDevice_state1()) {
							//RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
							sceneDetail.setDevice_state1(2);
						} else {
							//RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					} else if (DeviceTypeEnums.CURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                       /* SmartHomeApp.showToast("窗帘");
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("deviceInfo", deviceInfo);
                        Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                        PageSwitcher.switchToTopNavPage((Activity) mContext, SmartCurtainControlFragment.class, bundle, "", "智能窗帘", "");*/
					} else if (DeviceTypeEnums.AIRCONDITION.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						//发送设备型号
						//RestRequestApi.setAirConditionerType(iotMac, deviceInfo.getMac_address(), Integer.valueOf(deviceInfo.getDevice_type()));//发送空调型号适配
						int flag = sceneDetail.getDevice_state1();
						if (flag == DeviceStatusEnums.ON.getCode()) {
							//RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_OPEN_CLOSE, (byte) 0x00});
							sceneDetail.setDevice_state1(DeviceStatusEnums.OFF.getCode());
						} else {
							//RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_OPEN_CLOSE, (byte) 0xff});
							sceneDetail.setDevice_state1(DeviceStatusEnums.ON.getCode());
						}
						notifyDataSetChanged();
					} else if (DeviceTypeEnums.INFRARED.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						List<DeviceButtonInfo> list = deviceInfo.getDevice_buttons();
						if (!Util.isListEmpty(list)) {
							int size = list.size();
							for (int i = 0; i < size; i++) {
								if (mContext.getString(R.string.device_switch).equals(list.get(i).getName())) {
									int keyNumber = Tools.hexStr2Byte(list.get(i).getInstruction_code());
									//RestRequestApi.sendRedOrder(iotMac, deviceInfo.getMac_address(), keyNumber);//发送红外命令
									sceneDetail.setDevice_state1(keyNumber);
									if (sceneDetail.getDevice_state2() == 1) {
										sceneDetail.setDevice_state2(2);
									} else {
										sceneDetail.setDevice_state2(1);
									}
									notifyDataSetChanged();
									return;
								}
							}
							SmartHomeApp.showToast(R.string.add_power_button_hint);
						} else {
							SmartHomeApp.showToast(R.string.add_power_button_hint);
						}
					} else if (DeviceTypeEnums.SMOKESENSOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						SmartHomeApp.showToast("烟雾感应器");
					} else if (DeviceTypeEnums.GASSENSOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						SmartHomeApp.showToast("燃气感应器");
					} else if (DeviceTypeEnums.WARTERCONTROLER.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						SmartHomeApp.showToast("水浸控制器");
					} else if (DeviceTypeEnums.DOORANDWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						SmartHomeApp.showToast("门窗磁");
					} else if (DeviceTypeEnums.WINDRAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						SmartHomeApp.showToast("风雨感应器");
					} else if (DeviceTypeEnums.INFRARED.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						SmartHomeApp.showToast("红外探测器");
					} else if (DeviceTypeEnums.TRANSLATWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						if (deviceInfo.getDevice_state1() == 1) {
							sceneDetail.setDevice_state1(2);
						} else {
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					} else if (DeviceTypeEnums.SOUNDANDLIGHTALARM.getCode() == DeviceTools.getDeviceType(deviceInfo)||
							DeviceTypeEnums.OUTLET.getCode() == DeviceTools.getDeviceType(deviceInfo)||
							DeviceTypeEnums.METERINGSWITCH.getCode() == DeviceTools.getDeviceType(deviceInfo) ||
							DeviceTypeEnums.METERINGSOCKET10.getCode() == DeviceTools.getDeviceType(deviceInfo) ||
							DeviceTypeEnums.METERINGSOCKET16.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						if (deviceInfo.getDevice_state1() == 1) {
							sceneDetail.setDevice_state1(2);
						} else {
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					} else if (DeviceTypeEnums.PUSHWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						if (deviceInfo.getDevice_state1() == 1) {
							sceneDetail.setDevice_state1(2);
						} else {
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					}else if (DeviceTypeEnums.PROJECTIONFRAME.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
							if (deviceInfo.getDevice_state1() == 1) {
								sceneDetail.setDevice_state1(2);
							} else {
								sceneDetail.setDevice_state1(1);
							}
							notifyDataSetChanged();
					} else if (DeviceTypeEnums.THECURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						if (deviceInfo.getDevice_state1() == 1) {
							sceneDetail.setDevice_state1(2);
						} else {
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					}else if (DeviceTypeEnums.MANIPULATOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						if (deviceInfo.getDevice_state1() == 1) {
							sceneDetail.setDevice_state1(2);
						} else {
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					} else if (DeviceTypeEnums.TEMPANDHUMIDITY.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						SmartHomeApp.showToast("温湿度");
					} else if (DeviceTypeEnums.WINDOWOPENER.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//协议转发开窗器
						if (deviceInfo.getDevice_state1() == 1) {
							sceneDetail.setDevice_state1(2);
						} else {
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					} else if (DeviceTypeEnums.ELECTRICCURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//电动窗帘
						if (deviceInfo.getDevice_state1() == 1) {
							sceneDetail.setDevice_state1(2);
						} else {
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					} else if (DeviceTypeEnums.CONTROLBOX.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
						if (deviceInfo.getDevice_state1() == 1) {
							sceneDetail.setDevice_state1(2);
						} else {
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					}else if(DeviceTypeEnums.COLORFULBULB.getCode() == DeviceTools.getDeviceType(deviceInfo)){//多彩灯泡
						if (deviceInfo.getDevice_state1() == 1) {
							sceneDetail.setDevice_state1(2);
						} else {
							sceneDetail.setDevice_state1(1);
						}
						notifyDataSetChanged();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
