package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;
import com.kzksmarthome.SmartHouseYCT.biz.widget.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: SceneImgSelectDialog
 * @Description: 场景图片选择Dialog
 * @author laixj
 * @date 2016/10/16 13:01
 * @version V1.0
 */
public class SceneImgSelectDialog extends Dialog implements
        View.OnClickListener,SceneImgSelectAdapter.OnImgItemClick {

    private List<ImageInfo> sceneImgList = new ArrayList<ImageInfo>();

    @Override
    public void onImgItemClick(ImageInfo img) {
        if(null != mListener){
            mListener.onImgItemClick(img);
            dismiss();
        }
    }

    public interface OnImgItemClick {
        void onImgItemClick(ImageInfo img);
    }

    private OnImgItemClick mListener;

    private SceneImgSelectDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private SceneImgSelectDialog(Activity context, int defStyle, List<ImageInfo> sceneImgList) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_scene_img_select, null);
        this.sceneImgList = sceneImgList;
        RecyclerView recyclerView = (RecyclerView)contentView.findViewById(R.id.smart_scene_img_recycle);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SceneImgSelectAdapter mAdapter = new SceneImgSelectAdapter(context);
        mAdapter.setImageList(sceneImgList);
        recyclerView.setAdapter(mAdapter);
        contentView.findViewById(R.id.scene_img_select_cancel_tv).setOnClickListener(this);
        mAdapter.setmListener(this);
        super.setContentView(contentView);
    }

    public SceneImgSelectDialog(Activity context, List<ImageInfo> sceneImgList) {
        this(context, R.style.tv_operation_select_dialog, sceneImgList);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    public void setOnImgItemClickListener(OnImgItemClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scene_img_select_cancel_tv:
                dismiss();
                break;
            default:
                //dismiss();
                break;
        }

    }
}