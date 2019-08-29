package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;

/**
 * @author laixj
 * @version V1.0
 * @Title: SceneDelConfirmDialog
 * @Description: 场景删除确认框
 * @date 2016/10/18 8:03
 */
public class SceneDelConfirmDialog extends Dialog implements
        View.OnClickListener {

    public interface OnEnsureClick {
        void onEnsureClick(int position, SceneInfo sceneInfo);
    }

    private OnEnsureClick mListener;

    private int position;

    private SceneInfo sceneInfo;

    private TextView mTvTitle;

    private Context context;

    private SceneDelConfirmDialog(Context context, boolean cancelable, OnCancelListener listener) {
        super(context, cancelable, listener);
    }

    private SceneDelConfirmDialog(Context context, int defStyle, final int position, SceneInfo sceneInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_scene_del_confirm, null);

        this.position = position;
        this.context = context;
        this.sceneInfo = sceneInfo;

        mTvTitle = (TextView) contentView.findViewById(R.id.scene_del_confirm_title_tv);
        if (null != sceneInfo) {
            mTvTitle.setText(String.format(context.getString(R.string.scene_del_title_str), sceneInfo.getName()));
        }
        contentView.findViewById(R.id.scene_del_confirm_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.scene_del_confirm_ensure).setOnClickListener(this);
        super.setContentView(contentView);
    }

    public SceneDelConfirmDialog(Context context, int position, SceneInfo sceneInfo) {
        this(context, R.style.center_dialog, position, sceneInfo);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.CENTER);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SceneInfo getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        this.sceneInfo = sceneInfo;
    }

    public void setOnEnsureClickListener(OnEnsureClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scene_del_confirm_ensure:
                if (mListener != null) {
                    mListener.onEnsureClick(getPosition(), sceneInfo);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}