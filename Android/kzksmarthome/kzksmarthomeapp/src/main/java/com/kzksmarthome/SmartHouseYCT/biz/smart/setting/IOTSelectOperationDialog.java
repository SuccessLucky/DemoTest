package com.kzksmarthome.SmartHouseYCT.biz.smart.setting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;

import java.util.ArrayList;

/**
 * @Title: IOTSelectOperationDialog
 * @Description: 网关选择对话框
 * @author jack
 * @date 2016/10/17 14:08
 * @version V1.0
 */
public class IOTSelectOperationDialog extends Dialog implements AdapterView.OnItemClickListener{


    public interface OnIOTSelectClickListener {
        void onIOTSelectClick(int position);
    }

    private ListView mListView;
    private Context context;

    public IOTSelectAdapter mAdapter;

    public OnIOTSelectClickListener mListener;

    public void setOnRedDeviceTypeClickListener(OnIOTSelectClickListener listener){
        this.mListener = listener;
    }

    private IOTSelectOperationDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private IOTSelectOperationDialog(Context context, int defStyle) {
        super(context, defStyle);
        View contentView = View.inflate(context, R.layout.dialog_brand_type, null);
        this.context = context;
        TextView tv_cmd_add_title_tv = (TextView) contentView.findViewById(R.id.tv_cmd_add_title_tv);
        tv_cmd_add_title_tv.setText("选择网关");
        mListView = (ListView) contentView.findViewById(R.id.brand_type_list);
        mAdapter = new IOTSelectAdapter(context);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        super.setContentView(contentView);
    }

    public IOTSelectOperationDialog(Context context ,ArrayList<IOTSelectInfo> dataList) {
        this(context, R.style.center_dialog);//center_dialog
        mAdapter.setData(dataList);
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



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onIOTSelectClick(position);
    }
}