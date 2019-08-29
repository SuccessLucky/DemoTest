package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.util.DeviceTypeEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnLongClick;

/**
 * 设备适配器
 * Created by jack on 2016/9/5.
 */
public class DeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    @BindView(R.id.smart_device_room_rl)
    LinearLayout smartDeviceRoomRl;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();

    //private ImageLoader imageLoader = ImageLoader.getInstance();
    //private in.srain.cube.image.ImageLoader mImageLoader;
    private String baseImgUrl = "";

    public interface OnDeviceItemClick {
        void onDeviceItemClick(int position, DeviceInfo deviceInfo);
    }

    private OnDeviceItemClick mListener;
    /**
     * 计量设备信息
     */
    private DeviceInfo mJLDeviceInfo;
    /**
     * BaseFragment
     */
    private BaseFragment baseFragment;


    /**
     * 网关地址
     */
    private String iotMac;

    public DeviceAdapter(Activity context, BaseFragment baseFragment) {
        this.mContext = context;
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context, R.drawable.translucent);
        mLayoutInflater = LayoutInflater.from(context);
        baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_DEVICE_BASEURL, "");
        com.kzksmarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        iotMac = userInfo.gateway;
        this.baseFragment = baseFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_device_room_item, viewGroup, false);
        DeviceViewHolder viewHolder = new DeviceViewHolder(view);
        viewHolder.smartDeviceRoomIm.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        try {
            DeviceViewHolder deviceViewHolder = (DeviceViewHolder) viewHolder;
            if (position == (getItemCount() - 1)) {
                deviceViewHolder.smartBottomView.setVisibility(View.VISIBLE);
            } else {
                deviceViewHolder.smartBottomView.setVisibility(View.GONE);
            }
            DeviceInfo deviceInfo = deviceList.get(position);
            if (null != deviceInfo) {
                deviceViewHolder.smartDeviceRoomTv.setText(deviceInfo.getDevice_name());
                int deviceType = DeviceTools.getDeviceType(deviceInfo);
                if (deviceInfo.getDevice_OD().equals("0F AA")  && (DeviceTypeEnums.TABLELAMP.getCode() == deviceType || DeviceTypeEnums.DROPLIGHT.getCode() == deviceType)) {
                    String way = deviceInfo.getOther_status();
                    if (!TextUtils.isEmpty(way)) {
                        int way_int = Integer.valueOf(way);
                        if (way_int == 1) {
                            updateDeviceIm(deviceViewHolder, deviceInfo, deviceInfo.getDevice_state1());
                        } else if (way_int == 2) {
                            updateDeviceIm(deviceViewHolder, deviceInfo, deviceInfo.getDevice_state2());
                        } else if (way_int == 3) {
                            updateDeviceIm(deviceViewHolder, deviceInfo, deviceInfo.getDevice_state3());
                        }
                    }
                } else {
                    updateDeviceIm(deviceViewHolder, deviceInfo, deviceInfo.getDevice_state1());
                }
                deviceViewHolder.smartDeviceRoomIm.setTag(R.id.device_position_id, position);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 更新设备片
     *
     * @param deviceViewHolder
     * @param deviceInfo
     * @param state
     */
    private void updateDeviceIm(DeviceViewHolder deviceViewHolder, DeviceInfo deviceInfo, int state) {
        if (state == 1) {
            //imageLoader.displayImage(baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png", deviceViewHolder.smartDeviceRoomIm);
            //deviceViewHolder.smartDeviceRoomIm.loadImage(mImageLoader, baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png");
            Glide
                    .with(mContext)
                    .load(baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png")
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(deviceViewHolder.smartDeviceRoomIm);
        } else {
            //imageLoader.displayImage(baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png", deviceViewHolder.smartDeviceRoomIm);
            //deviceViewHolder.smartDeviceRoomIm.loadImage(mImageLoader, baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png");
            Glide
                    .with(mContext)
                    .load(baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png")
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(deviceViewHolder.smartDeviceRoomIm);
        }
    }

    @Override
    public int getItemCount() {
        return deviceList == null ? 0 : deviceList.size();
    }

    public List<DeviceInfo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceInfo> deviceList) {
        this.deviceList = deviceList;
    }

    public void setOnDeviceItemClickListener(OnDeviceItemClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        try {
            Integer position = (Integer) view.getTag(R.id.device_position_id);
            if (position == null) {
                return;
            }
            DeviceInfo deviceInfo = deviceList.get(position);
            if (null != deviceInfo) {
                if (DeviceTypeEnums.DROPLIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo) || DeviceTypeEnums.TABLELAMP.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    String way = deviceInfo.getOther_status();
                    if (TextUtils.isEmpty(way)) {
                        return;
                    }
                    ImageView imageView = (ImageView) view;
                    byte way_byte = Byte.valueOf(way);
                    int state1 = deviceInfo.getDevice_state1();
                    int state2 = deviceInfo.getDevice_state2();
                    int state3 = deviceInfo.getDevice_state3();
                    if (way_byte == 1) {
                        if (1 == state1) {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
                            deviceInfo.setDevice_state1(2);
                            //imageLoader.displayImage(baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png", imageView);
                            //imageView.loadImage(mImageLoader, baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png");
                            Glide
                                    .with(mContext)
                                    .load(baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png")
                                    .centerCrop()
                                    .placeholder(R.drawable.default_img)
                                    .crossFade()
                                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageView);
                        } else {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
                            deviceInfo.setDevice_state1(1);
                            //imageLoader.displayImage(baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png", imageView);
                            //imageView.loadImage(mImageLoader, baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png");
                            Glide
                                    .with(mContext)
                                    .load(baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png")
                                    .centerCrop()
                                    .placeholder(R.drawable.default_img)
                                    .crossFade()
                                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageView);
                        }
                    } else if (way_byte == 2) {
                        if (1 == state2) {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
                            deviceInfo.setDevice_state2(2);
                            //imageView.loadImage(mImageLoader, baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png");
                            Glide
                                    .with(mContext)
                                    .load(baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png")
                                    .centerCrop()
                                    .placeholder(R.drawable.default_img)
                                    .crossFade()
                                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageView);
                        } else {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
                            deviceInfo.setDevice_state2(1);
                            //imageView.loadImage(mImageLoader, baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png");
                            Glide
                                    .with(mContext)
                                    .load(baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png")
                                    .centerCrop()
                                    .placeholder(R.drawable.default_img)
                                    .crossFade()
                                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageView);
                        }
                    } else if (way_byte == 3) {
                        if (1 == state3) {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
                            deviceInfo.setDevice_state3(2);
                            //imageView.loadImage(mImageLoader, baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png");
                            Glide
                                    .with(mContext)
                                    .load(baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png")
                                    .centerCrop()
                                    .placeholder(R.drawable.default_img)
                                    .crossFade()
                                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageView);
                        } else {
                            RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
                            deviceInfo.setDevice_state3(1);
                            //imageView.loadImage(mImageLoader, baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png");
                            Glide
                                    .with(mContext)
                                    .load(baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png")
                                    .centerCrop()
                                    .placeholder(R.drawable.default_img)
                                    .crossFade()
                                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageView);
                        }
                    }
                } else if (DeviceTypeEnums.CURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("deviceInfo", deviceInfo);
                    Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                    PageSwitcher.switchToTopNavPage((Activity) mContext, SmartCurtainControlFragment.class, bundle, "", "智能窗帘", "");
                } else if (DeviceTypeEnums.DOOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    /*Bundle bundle = new Bundle();
                    bundle.putParcelable("deviceInfo", deviceInfo);
                    Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                    com.smarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
                    if (UserRoleEnums.ADMIN.getCode() == userInfo.role) {
                        PageSwitcher.switchToTopNavPage((Activity) mContext, DoorLockControlFragment.class, bundle, "", "门锁", "联系人", -1, null, R.drawable.icon_add);
                    } else if (UserRoleEnums.USER.getCode() == userInfo.role) {
                        PageSwitcher.switchToTopNavPage((Activity) mContext, DoorLockControlFragment.class, bundle, "", "门锁", "");
                    }*/
                    if (null != mListener) {
                        mListener.onDeviceItemClick(position, deviceInfo);
                    }
                } else if (DeviceTypeEnums.AIRCONDITION.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    SmartHomeApp.showToast("空调");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("deviceInfo", deviceInfo);
                    Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                    PageSwitcher.switchToTopNavPage((Activity) mContext, AirConditionCtrFragment.class, bundle, "", "空调", "");
                } else if (DeviceTypeEnums.INFRARED.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    SmartHomeApp.showToast(deviceInfo.getDevice_name());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("deviceInfo", deviceInfo);
                    Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                    //PageSwitcher.switchToTopNavPage((Activity) mContext, TvControlFragment.class, bundle, "", "电视", "");
                    PageSwitcher.switchToTopNavPage((Activity) mContext, TvControlFragment.class, bundle, "", deviceInfo.getDevice_name(), "", -1, null, R.drawable.icon_add);
                } else if (DeviceTypeEnums.LIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    ImageView imageView = (ImageView) view;
                    if (1 == deviceInfo.getDevice_state1()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
                        deviceInfo.setDevice_state1(2);
                        //imageView.loadImage(mImageLoader, baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png");
                        Glide
                                .with(mContext)
                                .load(baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png")
                                .centerCrop()
                                .placeholder(R.drawable.default_img)
                                .crossFade()
                                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);

                    } else {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
                        deviceInfo.setDevice_state1(1);
                        //imageView.loadImage(mImageLoader, baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png");
                        Glide
                                .with(mContext)
                                .load(baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png")
                                .centerCrop()
                                .placeholder(R.drawable.default_img)
                                .crossFade()
                                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);
                    }
                }else if(DeviceTypeEnums.MOBILEOUTLET.getCode() == DeviceTools.getDeviceType(deviceInfo)){//移动插座
                    ImageView imageView = (ImageView) view;
                    if (1 == deviceInfo.getDevice_state1()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
                        deviceInfo.setDevice_state1(2);
                        //imageView.loadImage(mImageLoader, baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png");
                        Glide
                                .with(mContext)
                                .load(baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png")
                                .centerCrop()
                                .placeholder(R.drawable.default_img)
                                .crossFade()
                                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);

                    } else {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
                        deviceInfo.setDevice_state1(1);
                        //imageView.loadImage(mImageLoader, baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png");
                        Glide
                                .with(mContext)
                                .load(baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png")
                                .centerCrop()
                                .placeholder(R.drawable.default_img)
                                .crossFade()
                                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);
                    }
                } else if (DeviceTypeEnums.SMOKESENSOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.GASSENSOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.WARTERCONTROLER.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.DOORANDWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.WINDRAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.SOUNDANDLIGHTALARM.getCode() == DeviceTools.getDeviceType(deviceInfo)||DeviceTypeEnums.OUTLET.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    ImageView imageView = (ImageView) view;
                    if (1 == deviceInfo.getDevice_state1()) {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
                        deviceInfo.setDevice_state1(2);
                        Glide
                                .with(mContext)
                                .load(baseImgUrl + "Un_" + deviceInfo.getImage() + "@2x.png")
                                .centerCrop()
                                .placeholder(R.drawable.default_img)
                                .crossFade()
                                .into(imageView);
                    } else {
                        RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
                        deviceInfo.setDevice_state1(1);

                        Glide
                                .with(mContext)
                                .load(baseImgUrl + "Pr_" + deviceInfo.getImage() + "@2x.png")
                                .centerCrop()
                                .placeholder(R.drawable.default_img)
                                .crossFade()
                                .into(imageView);
                    }
                } else if (DeviceTypeEnums.LOCK.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    if (null != mListener) {
                        mListener.onDeviceItemClick(position, deviceInfo);
                    }
                } else if (DeviceTypeEnums.PUSHWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)
                        ||DeviceTypeEnums.PROJECTIONFRAME.getCode() == DeviceTools.getDeviceType(deviceInfo)
                        ||DeviceTypeEnums.THECURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)
                        ||DeviceTypeEnums.TRANSLATWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)
                        ||DeviceTypeEnums.MANIPULATOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("deviceInfo", deviceInfo);
                    PageSwitcher.switchToTopNavPage((Activity) mContext, ControlBoxFragment.class, bundle, "", deviceInfo.getDevice_name(), "");
                } else if (DeviceTypeEnums.TEMPANDHUMIDITY.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                } else if (DeviceTypeEnums.WINDOWOPENER.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//协议转发开窗器
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("deviceInfo", deviceInfo);
                    Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                    PageSwitcher.switchToTopNavPage((Activity) mContext, SmartCurtainControlFragment.class, bundle, "", deviceInfo.getDevice_name(), "");
                } else if (DeviceTypeEnums.ELECTRICCURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//电动窗帘
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("deviceInfo", deviceInfo);
                    Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                    PageSwitcher.switchToTopNavPage((Activity) mContext, SmartCurtainControlFragment.class, bundle, "", deviceInfo.getDevice_name(), "");
                } else if (DeviceTypeEnums.CONTROLBOX.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("deviceInfo", deviceInfo);
                    Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                    PageSwitcher.switchToTopNavPage((Activity) mContext, ControlBoxFragment.class, bundle, "", deviceInfo.getDevice_name(), "");
                } else if (DeviceTypeEnums.COLORFULBULB.getCode() == DeviceTools.getDeviceType(deviceInfo) || DeviceTypeEnums.COLORFULLAMP.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
					SmartHomeApp.showToast(deviceInfo.getDevice_name());
					Bundle bundle = new Bundle();
					bundle.putParcelable("deviceInfo", deviceInfo);
					Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
					PageSwitcher.switchToTopNavPage((Activity) mContext, ColorLightControlFragment.class, bundle, "", "多彩灯泡", "");
				}else if(DeviceTypeEnums.METERINGSWITCH.getCode() == DeviceTools.getDeviceType(deviceInfo)||
                        DeviceTypeEnums.METERINGSOCKET10.getCode() == DeviceTools.getDeviceType(deviceInfo)||
                        DeviceTypeEnums.METERINGSOCKET16.getCode() == DeviceTools.getDeviceType(deviceInfo)){//计量控制盒
                    mJLDeviceInfo = deviceInfo;
                    DeviceFragment deviceFragment = (DeviceFragment) baseFragment;
                    String[] jlArray = deviceFragment.getJlArray();
                    if (jlArray != null && jlArray.length > 0) {
                        deviceFragment.show4040Dialog(jlArray);
                    } else {
                        RestRequestApi.getAll4010NodeInfo(iotMac);
                        deviceFragment.setControl(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_device_room_im)
        ImageView smartDeviceRoomIm;
        @BindView(R.id.smart_device_room_tv)
        TextView smartDeviceRoomTv;
        @BindView(R.id.smart_bottom_view)
        View smartBottomView;
        @BindView(R.id.smart_device_room_rl)
        LinearLayout smartDeviceRoomRl;

        @OnLongClick(R.id.smart_device_room_im)
        public boolean onLongClick() {
            DeviceInfo deviceInfo = deviceList.get(getPosition());
            if (null != deviceInfo) {
                if (DeviceTypeEnums.LIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo) || DeviceTypeEnums.TABLELAMP.getCode() == DeviceTools.getDeviceType(deviceInfo) || DeviceTypeEnums.DROPLIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("deviceInfo", deviceInfo);
                    Log.d("laixj", "设备信息跳转：" + deviceInfo.toString());
                    PageSwitcher.switchToTopNavPage((Activity) mContext, LightControlFragment.class, bundle, "", deviceInfo.getDevice_name(), "");
                }
            }
            return true;
        }

        DeviceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public DeviceInfo getmJLDeviceInfo() {
        return mJLDeviceInfo;
    }

    public void setmJLDeviceInfo(DeviceInfo mJLDeviceInfo) {
        this.mJLDeviceInfo = mJLDeviceInfo;
    }

    public String getIotMac() {
        return iotMac;
    }

    public void setIotMac(String iotMac) {
        this.iotMac = iotMac;
    }
}
