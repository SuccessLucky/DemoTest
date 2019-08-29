package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kzksmarthome.SmartHouseYCT.R;

/**
 * @author laixj
 * @version V1.0
 * @Title: TvCtrOperationSelectDialog
 * @Description: 电视控制操作选择Dialog
 * @date 2016/9/11 14:08
 */
public class RightsTypeSelectDialog extends Dialog implements
        View.OnClickListener {

    public interface OnRightsTypeItemClick {
        void onSceneClick();

        void onDeviceClick();
    }

    private OnRightsTypeItemClick mListener;

    private RightsTypeSelectDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private RightsTypeSelectDialog(Context context, int defStyle) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_rights_type_select, null);

        contentView.findViewById(R.id.rights_type_select_scene_rl).setOnClickListener(this);
        contentView.findViewById(R.id.rights_type_select_device_rl).setOnClickListener(this);
        contentView.findViewById(R.id.rights_type_select_cancel_rl).setOnClickListener(this);

        super.setContentView(contentView);
    }

    public RightsTypeSelectDialog(Context context) {
        this(context, R.style.tv_operation_select_dialog);
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

    public void setOnRightsTypeClickListener(OnRightsTypeItemClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rights_type_select_scene_rl:
                if (mListener != null) {
                    mListener.onSceneClick();
                }
                dismiss();
                break;
            case R.id.rights_type_select_device_rl:
                if (mListener != null) {
                    mListener.onDeviceClick();
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }

    }
}