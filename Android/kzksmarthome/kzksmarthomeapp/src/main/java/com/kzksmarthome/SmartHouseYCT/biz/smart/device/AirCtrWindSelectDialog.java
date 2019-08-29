package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.util.AirConditionWindEnums;

/**
 * @Title: AirCtrModeSelectDialog
 * @Description: 空调风速选择Dialog
 * @author laixj
 * @date 2016/9/12 22:40
 * @version V1.0
 */
public class AirCtrWindSelectDialog extends Dialog implements
        View.OnClickListener {

    public interface OnWindItemClick {
        void onWindItemClick(short wind);
    }

    private OnWindItemClick mListener;

    private AirCtrWindSelectDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private AirCtrWindSelectDialog(Context context, int defStyle) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_air_wind_select, null);

        contentView.findViewById(R.id.air_wind_cancel_tv).setOnClickListener(this);
        contentView.findViewById(R.id.air_wind_auto_rl).setOnClickListener(this);
        contentView.findViewById(R.id.air_wind_high_rl).setOnClickListener(this);
        contentView.findViewById(R.id.air_wind_middle_rl).setOnClickListener(this);
        contentView.findViewById(R.id.air_wind_low_rl).setOnClickListener(this);

        super.setContentView(contentView);
    }

    public AirCtrWindSelectDialog(Context context) {
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

    public void setOnWindItemClickListener(OnWindItemClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.air_wind_cancel_tv:
                dismiss();
                break;
            case R.id.air_wind_auto_rl:
                if (mListener != null) {
                    mListener.onWindItemClick(AirConditionWindEnums.AUTO.getCode());
                }
                dismiss();
                break;
            case R.id.air_wind_high_rl:
                if (mListener != null) {
                    mListener.onWindItemClick(AirConditionWindEnums.HIGH.getCode());
                }
                dismiss();
                break;
            case R.id.air_wind_middle_rl:
                if (mListener != null) {
                    mListener.onWindItemClick(AirConditionWindEnums.MIDDLE.getCode());
                }
                dismiss();
                break;
            case R.id.air_wind_low_rl:
                if (mListener != null) {
                    mListener.onWindItemClick(AirConditionWindEnums.LOW.getCode());
                }
                dismiss();
                break;
            default:
                dismiss();
                break;
        }

    }
}