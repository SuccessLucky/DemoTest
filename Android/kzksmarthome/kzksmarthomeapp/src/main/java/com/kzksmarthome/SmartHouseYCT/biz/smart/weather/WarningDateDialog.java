package com.kzksmarthome.SmartHouseYCT.biz.smart.weather;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.widget.datepicker.MonthDateView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: WarningDateDialog
 * @Description: 警告日期选择
 * @author jack
 * @date 2016/10/29 14:08
 * @version V1.0
 */
public class WarningDateDialog extends Dialog {
    public interface DateSelectClick {
        void onDateSelectClick(String date);
    }
    private DeviceInfo deviceInfo;
    private Context context;
    public DateSelectClick mListener;


    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private MonthDateView monthDateView;

    public void setDateSelectClickListener(DateSelectClick listener){
        this.mListener = listener;
    }

    private WarningDateDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private WarningDateDialog(Context context, int defStyle) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.warning_date_layout, null);
        this.context = context;

        List<Integer> list = new ArrayList<Integer>();
        list.add(10);
        list.add(12);
        list.add(15);
        list.add(16);
        iv_left = (ImageView) contentView.findViewById(R.id.iv_left);
        iv_right = (ImageView)  contentView.findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) contentView.findViewById(R.id.monthDateView);
        tv_date = (TextView)  contentView.findViewById(R.id.date_text);
        tv_week  =(TextView)  contentView.findViewById(R.id.week_text);
        tv_today = (TextView)  contentView.findViewById(R.id.tv_today);
        monthDateView.setTextView(tv_date,tv_week);
        monthDateView.setDaysHasThingList(list);
        monthDateView.setDateClick(new MonthDateView.DateClick() {

            @Override
            public void onClickOnDate() {
                mListener.onDateSelectClick(monthDateView.getmSelYear()+"-"+monthDateView.getmSelMonth()+"-"+monthDateView.getmSelDay());
                dismiss();
            }
        });
        setOnlistener();
        super.setContentView(contentView);
    }

    private void setOnlistener(){
        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.onLeftClick();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.onRightClick();
            }
        });

        tv_today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.setTodayToView();
            }
        });
    }

    public WarningDateDialog(Context context) {
        this(context, R.style.center_dialog);//center_dialog
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


    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}