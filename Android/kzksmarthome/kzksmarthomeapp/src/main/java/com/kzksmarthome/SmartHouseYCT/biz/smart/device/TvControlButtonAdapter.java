package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: TvControlButtonAdapter
 * @Description: 设备控制命令适配器
 * @date 2016/9/11 10:15
 */
public class TvControlButtonAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<DeviceButtonInfo> cmdList = new ArrayList<DeviceButtonInfo>();

    public TvControlButtonAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cmdList == null ? 0 : cmdList.size();
    }

    @Override
    public Object getItem(int position) {
        return cmdList == null ? null : cmdList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("laixj", "红外按键getView");
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.smart_tv_button_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DeviceButtonInfo cmdInfo = cmdList.get(position);
        if (null != cmdInfo) {
            viewHolder.smartTvButtonItemTv.setText(cmdInfo.getName());

        }
        return convertView;
    }

    public List<DeviceButtonInfo> getCmdList() {
        return cmdList;
    }

    public void setCmdList(List<DeviceButtonInfo> cmdList,int deviceId) {
        if (null == cmdList) {
            cmdList = new ArrayList<DeviceButtonInfo>();
            cmdList.add(new DeviceButtonInfo(deviceId, "开/关", "-1"));
        } else if (null != cmdList && cmdList.size() == 0){
            cmdList.add(new DeviceButtonInfo(deviceId, "开/关", "-1"));
        }
        this.cmdList = cmdList;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    class ViewHolder {
        @BindView(R.id.smart_tv_button_item_tv)
        TextView smartTvButtonItemTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
