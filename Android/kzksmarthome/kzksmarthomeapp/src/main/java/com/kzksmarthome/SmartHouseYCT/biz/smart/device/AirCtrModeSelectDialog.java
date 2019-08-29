package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.util.AirConditionModeEnums;

/**
 * @Title: AirCtrModeSelectDialog
 * @Description: 空调模式选择Dialog
 * @author laixj
 * @date 2016/9/12 22:40
 * @version V1.0
 */
public class AirCtrModeSelectDialog extends Dialog implements
        View.OnClickListener {

    public interface OnModeItemClick {
        void onModeItemClick(short mode);
    }

    private OnModeItemClick mListener;

    private AirCtrModeSelectDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private AirCtrModeSelectDialog(Context context, int defStyle) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_air_mode_select, null);

        contentView.findViewById(R.id.air_mode_cancel_tv).setOnClickListener(this);
        contentView.findViewById(R.id.air_mode_auto_rl).setOnClickListener(this);
        contentView.findViewById(R.id.air_mode_cold_rl).setOnClickListener(this);
        contentView.findViewById(R.id.air_mode_hot_rl).setOnClickListener(this);
        contentView.findViewById(R.id.air_mode_wind_rl).setOnClickListener(this);
        contentView.findViewById(R.id.air_mode_xeransis_rl).setOnClickListener(this);

        super.setContentView(contentView);
    }

    public AirCtrModeSelectDialog(Context context) {
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

    public void setOnModeItemClickListener(OnModeItemClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.air_mode_cancel_tv:
                dismiss();
                break;
            case R.id.air_mode_auto_rl:
                if (mListener != null) {
                    mListener.onModeItemClick(AirConditionModeEnums.AUTO.getCode());
                }
                dismiss();
                break;
            case R.id.air_mode_cold_rl:
                if (mListener != null) {
                    mListener.onModeItemClick(AirConditionModeEnums.COLD.getCode());
                }
                dismiss();
                break;
            case R.id.air_mode_hot_rl:
                if (mListener != null) {
                    mListener.onModeItemClick(AirConditionModeEnums.HOT.getCode());
                }
                dismiss();
                break;
            case R.id.air_mode_wind_rl:
                if (mListener != null) {
                    mListener.onModeItemClick(AirConditionModeEnums.WIND.getCode());
                }
                dismiss();
                break;
            case R.id.air_mode_xeransis_rl:
                if (mListener != null) {
                    mListener.onModeItemClick(AirConditionModeEnums.XERANSIS.getCode());
                }
                dismiss();
                break;
            default:
                dismiss();
                break;
        }

    }
}