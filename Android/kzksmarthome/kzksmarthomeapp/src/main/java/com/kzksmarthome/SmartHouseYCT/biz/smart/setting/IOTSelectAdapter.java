package com.kzksmarthome.SmartHouseYCT.biz.smart.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * 红外转发类型选择
 * Created by jack on 2016/10/17.
 */
public class IOTSelectAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public  List<IOTSelectInfo> mDataList;


    public IOTSelectAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 设置数据
     * @param dataList
     */
    public void setData( List<IOTSelectInfo> dataList){
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.brand_type_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        IOTSelectInfo iotSelectInfo = mDataList.get(position);
        holder.brandTypeItemTv.setText("IP:"+iotSelectInfo.getHostIp()+"\nMac:"+iotSelectInfo.getIot_wifi_mac());
        return convertView;
    }

     class ViewHolder {
        @BindView(R.id.brand_type_item_tv)
        TextView brandTypeItemTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
