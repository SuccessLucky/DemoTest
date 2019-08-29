package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.base.TopNavSubActivity;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfColorLight;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultAddScene;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceScene;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultUpdateScene;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceTools;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddSceneResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetImageListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateSceneResponse;
import com.kzksmarthome.SmartHouseYCT.util.DeviceStatusEnums;
import com.kzksmarthome.SmartHouseYCT.util.DeviceTypeEnums;
import com.kzksmarthome.SmartHouseYCT.util.ImageTypeEnums;
import com.kzksmarthome.SmartHouseYCT.widget.wheel.WheelView;
import com.kzksmarthome.SmartHouseYCT.widget.wheel.adapters.ArrayWheelAdapter;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.IOTConfig;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.user.UserInfo;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

/**
 * @author laixj
 * @version V1.0
 * @Title: SceneEditFragment
 * @Description: 场景添加/编辑界面
 * @date 2016/10/16 7:09
 */
public class SceneEditFragment extends BaseRequestFragment implements RequestCallback, SceneImgSelectDialog.OnImgItemClick {

    @BindView(R.id.smart_scene_edit_im)
    ImageView smartSceneEditIm;
    @BindView(R.id.smart_scene_edit_name_tv)
    EditText smartSceneEditNameTv;
    @BindView(R.id.smart_scene_edit_name_im)
    ImageView smartSceneEditNameIm;
    @BindView(R.id.smart_scene_edit_recycler)
    RecyclerView smartSceneEditRecycler;
    @BindView(R.id.smart_scene_favorite_ib)
    ImageButton smartSceneFavoriteIb;
    @BindView(R.id.smart_scene_favorite_tv)
    TextView smartSceneFavoriteTv;
    @BindView(R.id.add_ld_title_layout)
    RelativeLayout addLdTitleLayout;
    @BindView(R.id.smart_scene_ld_edit_recycler)
    RecyclerView smartSceneLdEditRecycler;
    @BindView(R.id.add_kz_title_layout)
    RelativeLayout addKzTitleLayout;
    @BindView(R.id.smart_scene_ld_tv)
    TextView smartSceneLdTv;
    @BindView(R.id.smart_scene_ld_ib)
    ImageButton smartSceneLdIb;
    @BindView(R.id.smart_scene_ys_tv)
    TextView smartSceneYsTv;
    @BindView(R.id.smart_scene_ys_ib)
    ImageButton smartSceneYsIb;
    @BindView(R.id.smart_scene_ys_ed)
    TextView smartSceneYsEd;
    @BindView(R.id.smart_scene_ds_tv)
    TextView smartSceneDsTv;
    @BindView(R.id.smart_scene_ds_ib)
    ImageButton smartSceneDsIb;
    @BindView(R.id.smart_scene_ds_ed)
    TextView smartSceneDsEd;
    @BindView(R.id.smart_scene_bf_tv)
    TextView smartSceneBfTv;
    @BindView(R.id.smart_scene_bf_ib)
    ImageButton smartSceneBfIb;
    @BindView(R.id.smart_scene_cf_tv)
    TextView smartSceneCfTv;
    @BindView(R.id.smart_scene_cf_ib)
    ImageButton smartSceneCfIb;
    @BindView(R.id.layout_ld_control)
    LinearLayout layoutLdControl;
    @BindView(R.id.icon_scene_ld_title)
    ImageView iconSceneLdTitle;
    @BindView(R.id.icon_scene_ld_title_add)
    ImageView iconSceneLdTitleAdd;
    @BindView(R.id.scene_ld_layout)
    LinearLayout sceneLdLayout;
    @BindView(R.id.icon_scene_kz_title)
    ImageView iconSceneKzTitle;
    @BindView(R.id.icon_scene_kz_title_add)
    ImageView iconSceneKzTitleAdd;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_right_tv)
    TextView titleRightTv;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;


    private SceneInfo sceneInfo;
    private List<SceneDetailInfo> detailList = new ArrayList<SceneDetailInfo>();
    private List<SceneDetailInfo> detailLDList = new ArrayList<SceneDetailInfo>();
    private List<ImageInfo> sceneImgList = new ArrayList<ImageInfo>();
    private SceneEditAdapter editAdapter;
    /**
     * 条目移动帮组类
     */
    private ItemTouchHelper itemTouchHelper;

    //private ImageLoader imageLoader = ImageLoader.getInstance();
    //private in.srain.cube.image.ImageLoader mImageLoader;

    private String baseImgUrl = null;
    private SceneImgSelectDialog imgSelectDialog;

    private int position = -1;
    /**
     * 是否为常用场景
     */
    private boolean isFavorite;
    /**
     * 网关mac地址
     */
    private String iotMac;
    /**
     * 场景编号
     */
    private byte sceneNum = 1;
    /**
     * 命令数
     */
    private byte sceneOrderSum;
    /**
     * 传感器地址
     */
    private String dstAddr;
    /**
     * 是否添加了燃气传感器
     */
    boolean isRQ = false;
    /**
     * 是否在场景中配置了传感器
     */
    boolean haveSensor = false;
    /**
     * 联动场景设备信息
     */
    private SceneDetailInfo mLDSceneDetailInfo;
    /**
     * 联动适配器
     */
    private SceneEditAdapter mLDadapter;

    /**
     * 定时框
     */
    private PopupWindow mDsPopupWindow;

    /**
     * 延时框
     */
    private PopupWindow mYsPopupWindow;
    /**
     * 小时
     */
    private String[] mAddHHArray = new String[24];
    /**
     * 分钟
     */
    private String[] mAddMMArray = new String[60];
    /**
     * 投放时间
     */
    private String[] mAddYsArray = new String[12];
    /**
     * 顶部activity
     */
    TopNavSubActivity activity;
    /**
     * 多彩灯命令
     */
    private HashMap<String,byte[]> mColorLightOrderMap = new HashMap<String, byte[]>();
    /**
     * 添加的场景id防止重复提交
     */
    public int mAddSceneId = -100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_scene_eidt_layout, container, false);
        ButterKnife.bind(this, mRootView);
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader(this, R.drawable.translucent);
        Bundle bundle = getArguments();
        if (bundle != null) {
            sceneNum = bundle.getByte("sceneNum");
        }
        initData();
        initView();
        GjjEventBus.getInstance().register(mDeviceSelectedEvent);
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (userInfo != null) {
            iotMac = userInfo.gateway;
        }
        itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(smartSceneEditRecycler);
        initAddData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        RestRequestApi.getImageList(getActivity(), ImageTypeEnums.SCENE.getCode(), this);
        Bundle bundle = getArguments();
        sceneInfo = (SceneInfo) bundle.getSerializable("sceneInfo");
        position = bundle.getInt("position");
        baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, "");

        if (null != sceneInfo && null != sceneInfo.getScene_details()) {
            detailList = sceneInfo.getScene_details();
            sortList();
            Log.d("laixj", "场景详情-->" + sceneInfo.getScene_details());
            if (1 == sceneInfo.getScene_type()) {
                isFavorite = true;
            } else {
                isFavorite = false;
            }
            if (isFavorite) {
                smartSceneFavoriteIb.setImageResource(R.drawable.smart_check_on);
            } else {
                smartSceneFavoriteIb.setImageResource(R.drawable.smart_check_off);
            }
        }
    }

    /**
     * 排序
     */
    public void sortList() {
        ArrayList<SceneDetailInfo> senceDeviceList = new ArrayList<SceneDetailInfo>();
        ArrayList<SceneDetailInfo> nomalDeviceList = new ArrayList<SceneDetailInfo>();
        for (SceneDetailInfo device : detailList) {
            if (device.getDevice_OD().equals("0F BE")) {
                if (!device.getDevice_type().equals("02")) {//去掉锁
                    senceDeviceList.add(device);
                }
            } else if (device.getDevice_OD().equals("0F AA")) {
                String type = device.getDevice_type();
                if (type.equals("81") || type.equals("82") || type.equals("83")) {
                    senceDeviceList.add(device);
                } else {
                    nomalDeviceList.add(device);
                }
            } else {
                nomalDeviceList.add(device);
            }
        }
        detailList.clear();
        detailLDList.addAll(senceDeviceList);
        if (detailLDList.size() > 0) {
            mLDSceneDetailInfo = detailLDList.get(0);
        }
        detailList.addAll(nomalDeviceList);
    }

    private void initView() {
        activity = (TopNavSubActivity) getActivity();
        activity.getTopRl().setVisibility(View.GONE);
        smartSceneEditRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        editAdapter = new SceneEditAdapter(getActivity());
        editAdapter.setDetailList(detailList);
        mLDadapter = new SceneEditAdapter(getActivity());
        mLDadapter.setDetailList(detailLDList);
        smartSceneLdEditRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        smartSceneLdEditRecycler.setAdapter(mLDadapter);
        smartSceneEditRecycler.setAdapter(editAdapter);
        if (-1 == sceneInfo.getScene_id()) {
            smartSceneEditNameIm.setVisibility(View.INVISIBLE);
            smartSceneEditNameTv.setEnabled(true);
            smartSceneEditNameTv.setText("");
        } else {
            //imageLoader.displayImage(baseImgUrl + "Pr_" + sceneInfo.getImage() + "@2x.png", smartSceneEditIm);
            String imageUrl = baseImgUrl + "Pr_" + sceneInfo.getImage() + "@2x.png";
            //smartSceneEditIm.loadImage(mImageLoader, imageUrl.trim().replace(" ",""));
            Glide
                    .with(SceneEditFragment.this)
                    .load(imageUrl.trim().replace(" ", ""))
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(smartSceneEditIm);
            smartSceneEditNameIm.setVisibility(View.VISIBLE);
            smartSceneEditNameTv.setEnabled(false);
            smartSceneEditNameTv.setText(sceneInfo.getName());

        }
    }

    /**
     * 更新开关
     */
    public void updateChoice() {
        if (!TextUtils.isEmpty(sceneInfo.getDelay_time())) {
            smartSceneYsEd.setText(sceneInfo.getDelay_time());
        }
        if (!TextUtils.isEmpty(sceneInfo.getTiming_time())) {
            smartSceneDsEd.setText(sceneInfo.getTiming_time());
        }
        if (sceneInfo.getNeed_linkage() == 1) {
            smartSceneLdIb.setBackgroundResource(R.drawable.smart_check_on);
            if (sceneInfo.getNeed_security_on() == 1) {
                smartSceneBfIb.setBackgroundResource(R.drawable.smart_check_on);
            } else if (sceneInfo.getNeed_security_on() == 0) {
                smartSceneBfIb.setBackgroundResource(R.drawable.smart_check_off);
            }
            if (sceneInfo.getNeed_security_off() == 1) {
                smartSceneCfIb.setBackgroundResource(R.drawable.smart_check_on);
            } else if (sceneInfo.getNeed_security_off() == 0) {
                smartSceneCfIb.setBackgroundResource(R.drawable.smart_check_off);
            }
            layoutLdControl.setVisibility(View.VISIBLE);
            sceneLdLayout.setVisibility(View.VISIBLE);
        } else if (sceneInfo.getNeed_linkage() == 0) {
            smartSceneLdIb.setBackgroundResource(R.drawable.smart_check_off);
            layoutLdControl.setVisibility(View.GONE);
            sceneLdLayout.setVisibility(View.GONE);
        }

        if (sceneInfo.getNeed_time_delay() == 1) {
            smartSceneYsIb.setBackgroundResource(R.drawable.smart_check_on);
        } else if (sceneInfo.getNeed_time_delay() == 0) {
            smartSceneYsIb.setBackgroundResource(R.drawable.smart_check_off);
        }

        if (sceneInfo.getNeed_timing() == 1) {
            smartSceneDsIb.setBackgroundResource(R.drawable.smart_check_on);
        } else if (sceneInfo.getNeed_timing() == 0) {
            smartSceneDsIb.setBackgroundResource(R.drawable.smart_check_off);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            activity.getTopRl().setVisibility(View.VISIBLE);
        } else {
            activity.getTopRl().setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(mDeviceSelectedEvent);
    }

    @OnClick({R.id.smart_scene_edit_im, R.id.smart_scene_edit_name_im, R.id.smart_scene_favorite_ib, R.id.add_ld_title_layout, R.id.add_kz_title_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_ld_title_layout://联动设备
                Bundle bundle = new Bundle();
                bundle.putSerializable("selected", (Serializable) sceneInfo.getScene_details());
                bundle.putBoolean("isLD", true);
                PageSwitcher.switchToTopNavPage(getActivity(), SceneDeviceSelectFragment.class, bundle, "", "联动设备列表", getString(R.string.bt_ok_str));
                break;
            case R.id.add_kz_title_layout://控制设备
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("selected", (Serializable) sceneInfo.getScene_details());
                bundle2.putBoolean("isLD", false);
                PageSwitcher.switchToTopNavPage(getActivity(), SceneDeviceSelectFragment.class, bundle2, "", "控制设备列表", getString(R.string.bt_ok_str));
                break;
            case R.id.smart_scene_edit_im:
                if (null == imgSelectDialog) {
                    imgSelectDialog = new SceneImgSelectDialog(getActivity(), sceneImgList);
                    imgSelectDialog.setOnImgItemClickListener(this);
                    imgSelectDialog.setCancelable(true);
                    imgSelectDialog.setCanceledOnTouchOutside(true);
                }
                imgSelectDialog.show();
                break;
            case R.id.smart_scene_edit_name_im:
                boolean isEnable = smartSceneEditNameTv.isEnabled();
                if (!isEnable) {
                    smartSceneEditNameTv.setEnabled(true);
                    smartSceneEditNameTv.setSelection(smartSceneEditNameTv.getText().toString().length());
                } else {
                    smartSceneEditNameTv.setEnabled(false);
                }
                break;
            case R.id.smart_scene_favorite_ib:
                isFavorite = !isFavorite;
                if (isFavorite) {
                    sceneInfo.setScene_type(1);
                    smartSceneFavoriteIb.setImageResource(R.drawable.smart_check_on);
                } else {
                    sceneInfo.setScene_type(0);
                    smartSceneFavoriteIb.setImageResource(R.drawable.smart_check_off);
                }
                break;
        }
    }

    /**
     * 点击完成
     */
    public void onRightBtnClick() {
        isRQ = false;
        haveSensor = false;
        if (TextUtils.isEmpty(sceneInfo.getImage())) {
            showToast(R.string.scene_img_empty_str);
            return;
        }
        String name = smartSceneEditNameTv.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast(R.string.scene_name_empty_str);
            return;
        }
        sceneInfo.setName(name);

        List<SceneDetailInfo> dataList = sceneInfo.getScene_details();
        if (Util.isListEmpty(dataList)) {
            showToast(R.string.scene_no_device_str);
            return;
        }
        if (dataList.size() > 30) {
            showToast(R.string.scene_is_max_hint);
        }
        if (sceneInfo.getNeed_linkage() == 1 && mLDSceneDetailInfo == null) {
            showToast(R.string.scene_need_add_link_device);
            return;
        }

        showLoadingDialog(R.string.loading, true);
        if (mLDSceneDetailInfo != null) {
            if ("0F BE".equals(mLDSceneDetailInfo.getDevice_OD())) {
                haveSensor = true;
                String type = mLDSceneDetailInfo.getDevice_type();
                String category = mLDSceneDetailInfo.getCategory();
                dstAddr = mLDSceneDetailInfo.getMac_address();
                //判断是否是燃气传感器
                if (type != null && category != null && ("03".equals(type) && "02".equals(category) || "07".equals(type) && "02".equals(category))) {
                    isRQ = true;
                }
            } else if (mLDSceneDetailInfo.getDevice_OD().equals("0F AA")) {
                String type = mLDSceneDetailInfo.getDevice_type();
                if (type.equals("81") || type.equals("82") || type.equals("83")) {
                    haveSensor = true;
                    dstAddr = mLDSceneDetailInfo.getMac_address();
                    String category = mLDSceneDetailInfo.getCategory();
                    if ((type.equals("81") && category.equals("03")) || (type.equals("82") && category.equals("02"))) {
                        isRQ = true;
                    }
                }
            }
        }
        sceneOrderSum = (byte) dataList.size();
        //开始发送删除配置命令
        RestRequestApi.sendDelSceneOrder(iotMac, sceneNum);
    }

    /**
     * 提交编辑场景
     */
    public void summitEditScene() {
        try {
            if(mAddSceneId == sceneInfo.getScene_id()){
                return;
            }
            mAddSceneId = sceneInfo.getScene_id();
            if (-1 == sceneInfo.getScene_id()) {

                List<SimpleSceneDetailInfo> simpleDeviceList = new ArrayList<SimpleSceneDetailInfo>();
                if (mLDSceneDetailInfo != null) {
                    SimpleSceneDetailInfo device = new SimpleSceneDetailInfo();
                    device.setScene_detail_id(0);
                    device.setDevice_id(mLDSceneDetailInfo.getDevice_id());
                    device.setDevice_state1(mLDSceneDetailInfo.getDevice_state1());
                    device.setDevice_state2(mLDSceneDetailInfo.getDevice_state2());
                    device.setDevice_state3(mLDSceneDetailInfo.getDevice_state3());
                    device.setOther_status(mLDSceneDetailInfo.getOther_status());
                    simpleDeviceList.add(device);
                }
                if (null != sceneInfo.getScene_details() && sceneInfo.getScene_details().size() > 0) {
                    for (SceneDetailInfo info : sceneInfo.getScene_details()) {
                        SimpleSceneDetailInfo device = new SimpleSceneDetailInfo();
                        device.setScene_detail_id(0);
                        device.setDevice_id(info.getDevice_id());
                        device.setDevice_state1(info.getDevice_state1());
                        device.setDevice_state2(info.getDevice_state2());
                        device.setDevice_state3(info.getDevice_state3());
                        device.setOther_status(info.getOther_status());
                        simpleDeviceList.add(device);
                    }
                }
                showLoadingDialog(R.string.loading_str, false);
                RestRequestApi.addScene(getActivity(), Tools.byte2HexStr(sceneNum), 0, sceneInfo.getScene_type(), sceneInfo.getName(), sceneInfo.getImage(), sceneInfo, simpleDeviceList, this);
            } else {
                List<SimpleSceneDetailInfo> simpleDeviceList = new ArrayList<SimpleSceneDetailInfo>();
                if (mLDSceneDetailInfo != null) {
                    SimpleSceneDetailInfo device = new SimpleSceneDetailInfo();
                    device.setScene_detail_id(0);
                    device.setDevice_id(mLDSceneDetailInfo.getDevice_id());
                    device.setDevice_state1(mLDSceneDetailInfo.getDevice_state1());
                    device.setDevice_state2(mLDSceneDetailInfo.getDevice_state2());
                    device.setDevice_state3(mLDSceneDetailInfo.getDevice_state3());
                    device.setOther_status(mLDSceneDetailInfo.getOther_status());
                    simpleDeviceList.add(device);
                }
                if (null != sceneInfo.getScene_details() && sceneInfo.getScene_details().size() > 0) {
                    for (SceneDetailInfo info : sceneInfo.getScene_details()) {
                        SimpleSceneDetailInfo device = new SimpleSceneDetailInfo();
                        if (info.getScene_detail_id() > 0) {
                            device.setScene_detail_id(info.getScene_detail_id());
                        } else {
                            device.setScene_detail_id(0);
                        }
                        device.setDevice_id(info.getDevice_id());
                        device.setDevice_state1(info.getDevice_state1());
                        device.setDevice_state2(info.getDevice_state2());
                        device.setDevice_state3(info.getDevice_state3());
                        device.setOther_status(info.getOther_status());
                        simpleDeviceList.add(device);
                    }
                }
                showLoadingDialog(R.string.loading_str, false);
                RestRequestApi.updateScene(getActivity(), Tools.byte2HexStr(sceneNum), sceneInfo.getScene_id(), sceneInfo.getScene_type(), sceneInfo.getName(), sceneInfo.getImage(), sceneInfo, simpleDeviceList, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        L.d("loginFail");
        if (getActivity() == null) {
            return;
        }
        if (url.startsWith(ApiHost.ADD_SCENE_URL)) {
            mAddSceneId = -100;
        } else if (url.startsWith(ApiHost.UPDATE_SCENE_URL)) {
            mAddSceneId = -100;
        }
        dismissLoadingDialog();
        SmartHomeApp.showToast("请检查网络");
        if (url.equals(ApiHost.ADD_SCENE_URL)) {
            //sceneNum --;
        }
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.SCENE.getCode()))) {
            if (response != null) {
                GetImageListResponse param = (GetImageListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            if (param.getResult().getImages().size() > 0) {
                                ImageInfo imageInfo = param.getResult().getImages().get(0);
                                if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.DEVICE.getCode()))) {
                                    saveImgBaseUrl(ImageTypeEnums.DEVICE.getCode(), imageInfo.getBase_url());
                                } else if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.ROOM.getCode()))) {
                                    saveImgBaseUrl(ImageTypeEnums.ROOM.getCode(), imageInfo.getBase_url());
                                } else if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.SCENE.getCode()))) {
                                    saveImgBaseUrl(ImageTypeEnums.SCENE.getCode(), imageInfo.getBase_url());
                                }
                                sceneImgList = param.getResult().getImages();
                            }
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            Log.d("laixj", "场景图片->" + "fail");
                            SmartHomeApp.showToast("拉取场景图片失败");
                        }
                    }
                } else {
                    Log.d("laixj", "场景图片->" + "(param == null)");
                    SmartHomeApp.showToast("拉取场景图片失败");
                }
            }
        } else if (url.equals(ApiHost.ADD_SCENE_URL)) {
            if (response != null) {
                AddSceneResponse param = (AddSceneResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            Log.d("laixj", "添加场景返回->" + param.getResult().toString());
                            SceneInfo sceneInfo = param.getResult();
                            if (null != sceneInfo) {
                                EventOfResultAddScene event = new EventOfResultAddScene();
                                event.sceneInfo = sceneInfo;
                                GjjEventBus.getInstance().post(event);
                                dismissLoadingDialog();
                                getActivity().onBackPressed();
                            }
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            Log.d("laixj", "添加场景->" + "fail");
                            SmartHomeApp.showToast("添加场景失败");
                        }
                    }
                } else {
                    Log.d("laixj", "添加场景->" + "(param == null)");
                    SmartHomeApp.showToast("添加场景失败");
                }
            }
            mAddSceneId = -100;
        } else if (url.equals(ApiHost.UPDATE_SCENE_URL)) {
            if (response != null) {
                UpdateSceneResponse param = (UpdateSceneResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            Log.d("laixj", "修改场景返回->" + param.getResult().toString());
                            SceneInfo sceneInfo = param.getResult();
                            if (null != sceneInfo) {
                                EventOfResultUpdateScene event = new EventOfResultUpdateScene();
                                event.sceneInfo = sceneInfo;
                                event.position = position;
                                GjjEventBus.getInstance().post(event);
                                dismissLoadingDialog();
                                getActivity().onBackPressed();
                            }
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            Log.d("laixj", "添加场景->" + "fail");
                            SmartHomeApp.showToast("添加场景失败");
                        }
                    }
                } else {
                    Log.d("laixj", "添加场景->" + "(param == null)");
                    SmartHomeApp.showToast("添加场景失败");
                }
            }
            mAddSceneId = -100;
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.ADD_SCENE_URL)) {
            mAddSceneId = -100;
        } else if (url.startsWith(ApiHost.UPDATE_SCENE_URL)) {
            mAddSceneId = -100;
        }
    }

    private void saveImgBaseUrl(int category, String baseUrl) {
        SharedPreferences.Editor editor = SmartHomeAppLib.getInstance().getPreferences().edit();
        if (category == ImageTypeEnums.DEVICE.getCode()) {
            editor.putString(SharePrefConstant.PREFS_KEY_DEVICE_BASEURL, baseUrl);
        } else if (category == ImageTypeEnums.ROOM.getCode()) {
            editor.putString(SharePrefConstant.PREFS_KEY_ROOM_BASEURL, baseUrl);
        } else if (category == ImageTypeEnums.SCENE.getCode()) {
            editor.putString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, baseUrl);
        }
        editor.commit();
    }

    @Override
    public void onImgItemClick(ImageInfo imageInfo) {
        if (null != imageInfo) {
            //imageLoader.displayImage(imageInfo.getBase_url() + "Pr_" + imageInfo.getName() + "." + imageInfo.getImage_type(), smartSceneEditIm);
            //smartSceneEditIm.loadImage(mImageLoader, imageInfo.getBase_url() + "Pr_" + imageInfo.getName() + "." + imageInfo.getImage_type());
            Glide
                    .with(SceneEditFragment.this)
                    .load(imageInfo.getBase_url() + "Pr_" + imageInfo.getName() + "." + imageInfo.getImage_type())
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(smartSceneEditIm);
            sceneInfo.setImage(imageInfo.getName());
        }
    }

    /**
     * 请求回调
     */
    public Object mDeviceSelectedEvent = new Object() {
        public void onEventMainThread(EventOfColorLight eventOfColorLight) {
            if(eventOfColorLight != null) {
                ColorLightOrderModel colorLightOrderModel = eventOfColorLight.getColorLightOrderModel();
                if(colorLightOrderModel != null) {
                    mColorLightOrderMap.put(colorLightOrderModel.getMac_address(),colorLightOrderModel.getOrderArray());
                }
            }
        }

        public void onEventMainThread(EventOfResultDeviceScene data) {
            if (data == null) {
                return;
            }
            List<SceneDetailInfo> selected = (List<SceneDetailInfo>) data.deviceList;
            if (null == selected) {
                selected = new ArrayList<SceneDetailInfo>();
            }
            if (data.isLD) {
                if (selected.size() > 0) {
                    mLDSceneDetailInfo = selected.get(0);
                    mLDadapter.setDetailList(selected);
                }
            } else {
                List<SceneDetailInfo> selected2 = editAdapter.getDetailList();
                for (SceneDetailInfo sceneDetailInfo : editAdapter.getDetailList()) {//还原设备状态
                    for (SceneDetailInfo sceneDetailInfoNew : selected) {
                        if (sceneDetailInfo.getDevice_id() == sceneDetailInfoNew.getDevice_id()) {
                            DeviceInfo deviceInfo = new DeviceInfo(sceneDetailInfo);
                            int deviceType = DeviceTools.getDeviceType(deviceInfo);
                            if (DeviceTypeEnums.TABLELAMP.getCode() == deviceType || DeviceTypeEnums.DROPLIGHT.getCode() == deviceType) {
                                String way = sceneDetailInfoNew.getOther_status();
                                int way_int = 0;
                                if (!TextUtils.isEmpty(way)) {
                                    way_int = Integer.valueOf(way);
                                }
                                if (way_int == 1) {
                                    sceneDetailInfoNew.setDevice_state1(sceneDetailInfo.getDevice_state1());
                                } else if (way_int == 2) {
                                    sceneDetailInfoNew.setDevice_state2(sceneDetailInfo.getDevice_state2());
                                } else if (way_int == 3) {
                                    sceneDetailInfoNew.setDevice_state3(sceneDetailInfo.getDevice_state3());
                                }
                            } else {
                                sceneDetailInfoNew.setOther_status(sceneDetailInfo.getOther_status());
                                sceneDetailInfoNew.setDevice_state1(sceneDetailInfo.getDevice_state1());
                            }
                        }
                    }
                }
                detailList = selected;
                sceneInfo.setScene_details(detailList);
                editAdapter.setDetailList(detailList);
            }
        }

        //控制回调
        public void onEventMainThread(EventOfTcpResult eventOfTcpResult) {
            if (getActivity() == null) {
                return;
            }
            try {
                if (eventOfTcpResult != null) {
                    DeviceState deviceState = eventOfTcpResult.deviceState;
                    byte[] od = deviceState.deviceOD;
                    if (deviceState.srcAddr != null && deviceState.cmdId == 0x50) {//场景命令反馈
                        switch (deviceState.sceneOrderType) {
                            case 0x01://设置场景
                                if (deviceState.result_data_01 == 0x00) {//初始化场景成功
                                    //配置成功发起载入命令
                                    RestRequestApi.sendLoadSceneOrder(iotMac, sceneNum, sceneOrderSum, (byte) 1, getConttolOrder(1));
                                } else {
                                    dismissLoadingDialog();
                                    showToast(R.string.scene_set_fail_hint);
                                }
                                break;
                            case 0x02://载入场景
                                if (deviceState.result_data_01 == 0x00 && deviceState.sceneNum == sceneNum && deviceState.sceneOrderSum == sceneOrderSum) {//载入成功
                                    if (deviceState.sceneOrderNum == deviceState.sceneOrderSum) {
                                        //sceneNum++;
                                        summitEditScene();
                                    } else {
                                        byte sceneOrderNum = (byte) (deviceState.sceneOrderNum + 1);
                                        RestRequestApi.sendLoadSceneOrder(iotMac, sceneNum, sceneOrderSum, sceneOrderNum, getConttolOrder(sceneOrderNum));
                                    }
                                } else {
                                    dismissLoadingDialog();
                                    showToast(R.string.scene_set_fail_hint);
                                }
                                break;
                            case 0x07://删除场景
                                if (deviceState.result_data_01 == 0x00) {//删除场景成功
                                    if (haveSensor) {
                                        RestRequestApi.sendSetInitSceneOrder(iotMac, dstAddr, sceneNum, sceneOrderSum, isRQ, sceneInfo.getDelay_time(), sceneInfo.getNeed_security_off());
                                    } else {
                                        RestRequestApi.sendSetInitBaseSceneOrder(iotMac, sceneNum, sceneOrderSum, sceneInfo.getTiming_time(), sceneInfo.getDelay_time());
                                    }
                                } else {
                                    dismissLoadingDialog();
                                    showToast(R.string.scene_set_fail_hint);
                                }
                                break;
                        }

                    } else if (deviceState.srcAddr == null && deviceState.dstAddr != null) {//操作设备返回


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 获取控制命令
     *
     * @param index 编号
     */
    public byte[] getConttolOrder(int index) {
        List<SceneDetailInfo> dataList = sceneInfo.getScene_details();
        if (dataList == null || dataList.size() < index) {
            return null;
        }
        SceneDetailInfo info = dataList.get(index - 1);
        byte[] orderArray = null;
        DeviceInfo deviceInfo = new DeviceInfo(info);
        if (DeviceTypeEnums.DROPLIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo)
                || DeviceTypeEnums.TABLELAMP.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
            String way = deviceInfo.getOther_status();
            if (TextUtils.isEmpty(way)) {
                return orderArray;
            }
            byte way_byte = Byte.valueOf(way);
            int state1 = deviceInfo.getDevice_state1();
            int state2 = deviceInfo.getDevice_state2();
            int state3 = deviceInfo.getDevice_state3();
            if (way_byte == 1) {
                if (1 == state1) {
                    orderArray = RestRequestApi.getContorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
                } else {
                    orderArray = RestRequestApi.getContorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
                }
            } else if (way_byte == 2) {
                if (1 == state2) {
                    orderArray = RestRequestApi.getContorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x1, (byte) 0x00);
                } else {
                    orderArray = RestRequestApi.getContorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
                }
            } else if (way_byte == 3) {
                if (1 == state3) {
                    orderArray = RestRequestApi.getContorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x01, (byte) 0x00);
                } else {
                    orderArray = RestRequestApi.getContorMoreWay(iotMac, deviceInfo.getMac_address(), way_byte, (byte) 0x01, (byte) 0x02, (byte) 0x00);
                }
            }
        } else if (DeviceTypeEnums.CURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
        } else if (DeviceTypeEnums.DOOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
        } else if (DeviceTypeEnums.AIRCONDITION.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
            int flag = deviceInfo.getDevice_state1();
            if (flag == DeviceStatusEnums.OFF.getCode()) {
                orderArray = RestRequestApi.getSetAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_OPEN_CLOSE, 0x00});
            } else if (flag == DeviceStatusEnums.ON.getCode()) {
                orderArray = RestRequestApi.getSetAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_OPEN_CLOSE, (byte) 0xff});
            }
        } else if (DeviceTypeEnums.INFRARED.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
            orderArray = RestRequestApi.getSendRedOrder(iotMac, deviceInfo.getMac_address(), 0);//发送红外命令
        } else if (DeviceTypeEnums.LIGHT.getCode() == DeviceTools.getDeviceType(deviceInfo) ||
                DeviceTypeEnums.SOUNDANDLIGHTALARM.getCode() == DeviceTools.getDeviceType(deviceInfo) ||
                DeviceTypeEnums.OUTLET.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
            int flag1 = deviceInfo.getDevice_state1();
            if (flag1 == DeviceStatusEnums.OFF.getCode()) {
                orderArray = RestRequestApi.getContorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
            } else if (flag1 == DeviceStatusEnums.ON.getCode()) {
                orderArray = RestRequestApi.getContorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
            }
        } else if (DeviceTypeEnums.MOBILEOUTLET.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
            int flag1 = deviceInfo.getDevice_state1();
            if (flag1 == DeviceStatusEnums.OFF.getCode()) {
                orderArray = RestRequestApi.getContorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
            } else if (flag1 == DeviceStatusEnums.ON.getCode()) {
                orderArray = RestRequestApi.getContorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
            }
        } else if (DeviceTypeEnums.DOORANDWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
        } else if (DeviceTypeEnums.LOCK.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
        } else if (DeviceTypeEnums.WINDOWOPENER.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//协议转发开窗器
            if (deviceInfo.getDevice_state1() == 2) {
                orderArray = RestRequestApi.getSendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.WINDOW_OPENER_FZ);
            } else {
                orderArray = RestRequestApi.getSendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.WINDOW_OPENER_ZZ);
            }
        } else if (DeviceTypeEnums.ELECTRICCURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//电动窗帘
            if (deviceInfo.getDevice_state1() == 2) {
                orderArray = RestRequestApi.getSendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.ELECTRIC_CURTAIN_ZZ);
            } else {
                orderArray = RestRequestApi.getSendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.ELECTRIC_CURTAIN_FZ);
            }
        } else if (DeviceTypeEnums.CONTROLBOX.getCode() == DeviceTools.getDeviceType(deviceInfo)
                || DeviceTypeEnums.PUSHWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)
                || DeviceTypeEnums.PROJECTIONFRAME.getCode() == DeviceTools.getDeviceType(deviceInfo)
                || DeviceTypeEnums.THECURTAIN.getCode() == DeviceTools.getDeviceType(deviceInfo)
                || DeviceTypeEnums.TRANSLATWINDOW.getCode() == DeviceTools.getDeviceType(deviceInfo)
                || DeviceTypeEnums.MANIPULATOR.getCode() == DeviceTools.getDeviceType(deviceInfo)) {
            int flag = deviceInfo.getDevice_state1();
            if (flag == DeviceStatusEnums.OFF.getCode()) {
                orderArray = RestRequestApi.getContorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
            } else if (flag == DeviceStatusEnums.ON.getCode()) {
                orderArray = RestRequestApi.getContorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
            }
        } else if (DeviceTypeEnums.COLORFULBULB.getCode() == DeviceTools.getDeviceType(deviceInfo)) {//多彩灯泡
            byte[] colorLightOrderArray = mColorLightOrderMap.get(deviceInfo.getMac_address());
            if (colorLightOrderArray != null) {
                orderArray = colorLightOrderArray;
            } else {
                if (deviceInfo.getDevice_state1() == 2) {
                    orderArray = RestRequestApi.getSendColorLightLight(iotMac, deviceInfo.getMac_address(), (byte) 0, (byte) 2);
                } else {
                    orderArray = RestRequestApi.getSendColorLightLight(iotMac, deviceInfo.getMac_address(), (byte) 240, (byte) 1);

                }
            }
        } else if (DeviceTypeEnums.METERINGSWITCH.getCode() == DeviceTools.getDeviceType(deviceInfo) ||
                DeviceTypeEnums.METERINGSOCKET10.getCode() == DeviceTools.getDeviceType(deviceInfo) ||
                DeviceTypeEnums.METERINGSOCKET16.getCode() == DeviceTools.getDeviceType(deviceInfo)) {

            if (deviceInfo.getDevice_state1() == 2) {
                orderArray = RestRequestApi.getcontorl4040(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
            } else {
                orderArray = RestRequestApi.getcontorl4040(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
            }
        }
        return orderArray;
    }

    /**
     * 条目拖动监听
     */
    private ItemTouchHelper.Callback itemTouchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0;
            int swipeFlags = 0;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFlags = UP | DOWN | LEFT | RIGHT;
            } else {
                dragFlags = UP | DOWN;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
            int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(detailList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(detailList, i, i - 1);
                }
            }
            editAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
        }
    };

    @OnClick({R.id.smart_scene_ld_ib, R.id.smart_scene_ys_ib, R.id.smart_scene_ds_ib, R.id.smart_scene_bf_ib, R.id.smart_scene_cf_ib, R.id.smart_scene_ys_ed, R.id.smart_scene_ds_ed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.smart_scene_ld_ib:
                if (sceneInfo.getNeed_linkage() == 1) {
                    sceneInfo.setNeed_linkage(0);
                } else if (sceneInfo.getNeed_linkage() == 0) {
                    sceneInfo.setNeed_linkage(1);
                }
                break;
            case R.id.smart_scene_ys_ib:
                if (sceneInfo.getNeed_time_delay() == 1) {
                    sceneInfo.setNeed_time_delay(0);
                } else if (sceneInfo.getNeed_time_delay() == 0) {
                    sceneInfo.setNeed_time_delay(1);
                }
                break;
            case R.id.smart_scene_ds_ib:
                if (sceneInfo.getNeed_timing() == 1) {
                    sceneInfo.setNeed_timing(0);
                } else if (sceneInfo.getNeed_timing() == 0) {
                    sceneInfo.setNeed_timing(1);
                }
                break;
            case R.id.smart_scene_bf_ib:
                if (sceneInfo.getNeed_security_on() == 1) {
                    sceneInfo.setNeed_security_on(0);
                } else if (sceneInfo.getNeed_security_on() == 0) {
                    sceneInfo.setNeed_security_on(1);
                    sceneInfo.setNeed_security_off(0);
                }
                break;
            case R.id.smart_scene_cf_ib:
                if (sceneInfo.getNeed_security_off() == 1) {
                    sceneInfo.setNeed_security_off(0);
                } else if (sceneInfo.getNeed_security_off() == 0) {
                    sceneInfo.setNeed_security_off(1);
                    sceneInfo.setNeed_security_on(0);
                }
                break;
            case R.id.smart_scene_ys_ed:
                if (sceneInfo.getNeed_time_delay() == 1) {
                    showYsWindow();
                }
                break;
            case R.id.smart_scene_ds_ed:
                if (sceneInfo.getNeed_timing() == 1) {
                    showDsWindow();
                }
                break;
        }
        updateChoice();
    }


    /**
     * 显示添加布局
     */
    private void showDsWindow() {
        //dismissConstructNoticeWindow();
        View contentView = null;
        PopupWindow popupWindow = mDsPopupWindow;
        LinearLayout control_add_layout = null;
        if (popupWindow == null) {
            contentView = LayoutInflater.from(getActivity()).inflate(R.layout.sence_time_ds_layout, null);
            control_add_layout = (LinearLayout) contentView.findViewById(R.id.control_add_layout);
            final WheelView id_time_hh = (WheelView) contentView.findViewById(R.id.id_time_hh);
            final WheelView id_time_mm = (WheelView) contentView.findViewById(R.id.id_time_mm);
            TextView control_add_btn_ok = (TextView) contentView.findViewById(R.id.control_add_btn_save);
            id_time_hh.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mAddHHArray));
            id_time_mm.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mAddMMArray));
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, false);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setAnimationStyle(R.style.popwin_anim_style);
            // mPickUpPopWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            //setPopupWindowTouchModal(popupWindow, false);// 设置popupwindow外的点击事件可以传递到activity
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
            mDsPopupWindow = popupWindow;
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDsPopupWindow.isShowing()) {
                        mDsPopupWindow.dismiss();
                    }
                }
            });
            contentView.setTag(control_add_layout);
            control_add_layout.clearAnimation();
            control_add_btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String addHHstr = mAddHHArray[id_time_hh.getCurrentItem()];
                    String addMMstr = mAddMMArray[id_time_mm.getCurrentItem()];
                    String dsTime = addHHstr + ":" + addMMstr;
                    smartSceneDsEd.setText(dsTime);
                    sceneInfo.setTiming_time(dsTime);
                    dismissControlDsWindow();
                }
            });

        } else {
            contentView = mDsPopupWindow.getContentView();
            control_add_layout = (LinearLayout) contentView.getTag();
            control_add_layout.clearAnimation();
        }

        //判读window是否显示，消失了就执行动画
        if (!popupWindow.isShowing()) {
            Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.effect_bg_show);
            contentView.startAnimation(animation2);
        }

        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        //gridView 展示动画
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_window_down_show);
        control_add_layout.startAnimation(animation);
    }

    /**
     * 取消添加弹框
     *
     * @return
     */
    private void dismissControlDsWindow() {
        PopupWindow pickUpPopWindow = mDsPopupWindow;
        if (null != pickUpPopWindow && pickUpPopWindow.isShowing()) {
            pickUpPopWindow.dismiss();
        }
    }


    /**
     * 显示添加布局
     */
    private void showYsWindow() {
        //dismissConstructNoticeWindow();
        View contentView = null;
        PopupWindow popupWindow = mYsPopupWindow;
        LinearLayout control_add_layout = null;
        if (popupWindow == null) {
            contentView = LayoutInflater.from(getActivity()).inflate(R.layout.sence_time_ys_layout, null);
            control_add_layout = (LinearLayout) contentView.findViewById(R.id.control_add_layout);
            final WheelView id_time = (WheelView) contentView.findViewById(R.id.id_time);
            TextView control_add_btn_ok = (TextView) contentView.findViewById(R.id.control_add_btn_save);
            id_time.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mAddYsArray));
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, false);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setAnimationStyle(R.style.popwin_anim_style);
            // mPickUpPopWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            //setPopupWindowTouchModal(popupWindow, false);// 设置popupwindow外的点击事件可以传递到activity
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
            mYsPopupWindow = popupWindow;
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mYsPopupWindow.isShowing()) {
                        mYsPopupWindow.dismiss();
                    }
                }
            });
            contentView.setTag(control_add_layout);
            control_add_layout.clearAnimation();
            control_add_btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ysTime = mAddYsArray[id_time.getCurrentItem()];
                    smartSceneYsEd.setText(ysTime);
                    sceneInfo.setDelay_time(ysTime);
                    dismissControlYsWindow();
                }
            });

        } else {
            contentView = mYsPopupWindow.getContentView();
            control_add_layout = (LinearLayout) contentView.getTag();
            control_add_layout.clearAnimation();
        }

        //判读window是否显示，消失了就执行动画
        if (!popupWindow.isShowing()) {
            Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.effect_bg_show);
            contentView.startAnimation(animation2);
        }

        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        //gridView 展示动画
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_window_down_show);
        control_add_layout.startAnimation(animation);
    }

    /**
     * 取消添加弹框
     *
     * @return
     */
    private void dismissControlYsWindow() {
        PopupWindow pickUpPopWindow = mYsPopupWindow;
        if (null != pickUpPopWindow && pickUpPopWindow.isShowing()) {
            pickUpPopWindow.dismiss();
        }
    }

    /**
     * 初始化时间数据
     */
    private void initAddData() {
        for (int i = 0; i < mAddHHArray.length; i++) {
            if (i < 10) {
                mAddHHArray[i] = "0" + i;
            } else {
                mAddHHArray[i] = Integer.toString(i);
            }
        }
        for (int i = 0; i < mAddMMArray.length; i++) {
            if (i < 10) {
                mAddMMArray[i] = "0" + i;
            } else {
                mAddMMArray[i] = Integer.toString(i);
            }
        }
        for (int i = 0; i < mAddYsArray.length; i++) {
            if (i == 0) {
                mAddYsArray[i] = "5";
            } else {
                mAddYsArray[i] = Integer.toString(Integer.valueOf(mAddYsArray[i - 1]) + 5);
            }
        }
    }

    @OnClick(R.id.title_right_tv)
    public void onViewClicked() {
        onRightBtnClick();
    }

}
