package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kzksmarthome.SmartHouseYCT.R;

/**
 * @Title: BrandTypeOperationDialog
 * @Description: 空调类型控制
 * @author jack
 * @date 2016/10/17 14:08
 * @version V1.0
 */
public class BrandTypeOperationDialog extends Dialog implements AdapterView.OnItemClickListener{
    public interface OnBrandTypeClick {
        void onBrandTypeClick(int position, DeviceInfo deviceInfo);
    }
    private DeviceInfo deviceInfo;

    private ListView mListView;
    private Context context;

    private BrandTypeAdapter mAdapter;

    public OnBrandTypeClick mListener;

    public void setOnBrandTypeClickListener(OnBrandTypeClick listener){
        this.mListener = listener;
    }

    private BrandTypeOperationDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private BrandTypeOperationDialog(Context context, int defStyle,DeviceInfo deviceInfo) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_brand_type, null);
        this.deviceInfo = deviceInfo;
        this.context = context;

        mListView = (ListView) contentView.findViewById(R.id.brand_type_list);
        mAdapter = new BrandTypeAdapter(context);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        super.setContentView(contentView);
    }

    public BrandTypeOperationDialog(Context context,  DeviceInfo deviceInfo) {
        this(context, R.style.center_dialog, deviceInfo);//center_dialog
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(null != mListener){
            mListener.onBrandTypeClick(position,deviceInfo);
        }
        dismiss();
    }
}