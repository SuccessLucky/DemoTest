package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSceneRights;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetSceneListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: SceneRightsSelectFragment
 * @Description: 场景权限列表
 * @date 2016/9/16 14:44
 */
public class SceneRightsSelectFragment extends BaseRequestFragment implements SceneRightsSelectListAdapter.OnSceneItemClick,RequestCallback {

    @BindView(R.id.scene_rights_recycle_list)
    RecyclerView sceneRightsRecycleList;

    private List<SceneInfo> sceneList = new ArrayList<SceneInfo>();

    private SceneRightsSelectListAdapter adapter = null;
    /**
     * 已选中的场景模式
     */
    List<SceneInfo> originSelected = new ArrayList<SceneInfo>();

    private Map<SceneInfo, SceneInfo> originSelectedMap = new HashMap<SceneInfo, SceneInfo>();

    @Override
    public void onRightBtnClick() {
        List<SceneInfo> selected = new ArrayList<SceneInfo>();
        for (SceneInfo sceneInfo : adapter.getSceneList()) {
            if (sceneInfo.isSelected()) {
                selected.add(sceneInfo);
            }
        }
        EventOfResultSceneRights event = new EventOfResultSceneRights();
        event.sceneList = selected;
        GjjEventBus.getInstance().post(event);
        super.onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_scene_rights_select_layout, container, false);
        RestRequestApi.getSceneList(getActivity(), -1, this);
        ButterKnife.bind(this, mRootView);
        initData();
        initViews();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        Bundle bundle = getArguments();
        originSelected = (List<SceneInfo>) bundle.getSerializable("selected");
        if (null != originSelected) {
            for (SceneInfo scene : originSelected) {
                originSelectedMap.put(scene, scene);
            }
        }
        /*sceneList.add(new SceneInfo(1, "回家模式"));
        sceneList.add(new SceneInfo(2, "离家模式"));
        sceneList.add(new SceneInfo(3, "联动模式"));
        sceneList.add(new SceneInfo(4, "就餐模式"));
        sceneList.add(new SceneInfo(5, "睡眠模式"));
        sceneList.add(new SceneInfo(6, "音乐模式"));*/
    }

    private void initSelected(){
        for(SceneInfo scene : sceneList){
            if(originSelectedMap.containsKey(scene)){
                scene.setSelected(true);
            }
        }
    }

    private void initViews() {
        sceneRightsRecycleList.setLayoutManager(new LinearLayoutManager(sceneRightsRecycleList.getContext()));
        adapter = new SceneRightsSelectListAdapter(getActivity(), sceneList);
        //adapter.setOnSceneItemClickListener(this);
        sceneRightsRecycleList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSceneItemClick(int position, SceneInfo sceneInfo) {
        // TODO: 2016/9/16  
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        SmartHomeApp.showToast("请检查网络");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_SCENE_LIST_URL)) {
            if (response != null) {
                GetSceneListResponse param = (GetSceneListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            sceneList = param.getResult();
                            adapter.setSceneList(sceneList);
                            initSelected();
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("请求失败");
                }
            }
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
    }
}
