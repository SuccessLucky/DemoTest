package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.util.DeviceCtrTypeEnums;

/**
 * @author laixj
 * @version V1.0
 * @Title: AirCtrModeSelectDialog
 * @Description: 空调风速选择Dialog
 * @date 2016/9/12 22:40
 */
public class DeviceCtrTypeSelectDialog extends Dialog implements
        View.OnClickListener {

    public interface OnCtrTypeItemClick {
        void onCtrTypeItemClick(String ctrType);
    }

    private OnCtrTypeItemClick mListener;

    private DeviceCtrTypeSelectDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private DeviceCtrTypeSelectDialog(Context context, int defStyle) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_device_ctr_type_select, null);

        contentView.findViewById(R.id.ctr_type_cancel_tv).setOnClickListener(this);
        contentView.findViewById(R.id.ctr_type_normal_rl).setOnClickListener(this);
        contentView.findViewById(R.id.ctr_type_air_rl).setOnClickListener(this);
        contentView.findViewById(R.id.ctr_type_infrared_rl).setOnClickListener(this);

        super.setContentView(contentView);
    }

    public DeviceCtrTypeSelectDialog(Context context) {
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

    public void setOnCtrTypeItemClickListener(OnCtrTypeItemClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.air_wind_cancel_tv:
                dismiss();
                break;
            case R.id.ctr_type_normal_rl:
                if (mListener != null) {
                    mListener.onCtrTypeItemClick(DeviceCtrTypeEnums.NORMAL.getCode());
                }
                dismiss();
                break;
            case R.id.ctr_type_air_rl:
                if (mListener != null) {
                    mListener.onCtrTypeItemClick(DeviceCtrTypeEnums.AIRCONDITION.getCode());
                }
                dismiss();
                break;
            case R.id.ctr_type_infrared_rl:
                if (mListener != null) {
                    mListener.onCtrTypeItemClick(DeviceCtrTypeEnums.INFRARED.getCode());
                }
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}