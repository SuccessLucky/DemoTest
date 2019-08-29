package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.smart.home.BrandType;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * 空调类型选择
 * Created by jack on 2016/10/17.
 */
public class BrandTypeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;


    public BrandTypeAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return SmartHomeApp.mBrandTypeArrayList == null ? 0 : SmartHomeApp.mBrandTypeArrayList.size();
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
        BrandType brandType = SmartHomeApp.mBrandTypeArrayList.get(position);
        if(brandType != null){
            holder.brandTypeItemTv.setText(brandType.brandName);
        }

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
