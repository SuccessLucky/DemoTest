package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: RoomAddDialogIconAdapter
 * @Description: 房间添加/编辑房间图标适配器
 * @date 2016/9/17 15:50
 */
public class RoomAddDialogIconAdapter extends BaseAdapter {

    private Activity mContext;
    private LayoutInflater mInflater;

    //protected ImageLoader imageLoader = ImageLoader.getInstance();
    //private in.srain.cube.image.ImageLoader  mImageLoader;
    private int selected = -1;

    public interface OnRoomTypeClick {
        void onRoomTypeClick(int position, ImageInfo roomTypeEnums);
    }

    private OnRoomTypeClick mListener;

    //private List<RoomTypeInfo> typeList = new ArrayList<RoomTypeInfo>();
    private List<ImageInfo> typeList = new ArrayList<ImageInfo>();

    public RoomAddDialogIconAdapter(Activity context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        //typeList = EnumUtil.toList(RoomTypeEnums.class);
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context,R.drawable.translucent);
    }

    @Override
    public int getCount() {
        return typeList == null ? 0 : typeList.size();
    }

    @Override
    public Object getItem(int position) {
        return typeList == null ? null : typeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.smart_room_add_type_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageInfo typeInfo = typeList.get(position);
        if (null != typeInfo) {
            if (selected > -1) {
                if (selected == position) {
                    viewHolder.smartRoomTypeItemRed.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.smartRoomTypeItemRed.setVisibility(View.GONE);
                }
            } else {
                viewHolder.smartRoomTypeItemRed.setVisibility(View.GONE);
            }
            //imageLoader.displayImage(typeInfo.getBase_url()+typeInfo.getName()+"."+typeInfo.getImage_type(), viewHolder.smartRoomTypeItemIv);
            //viewHolder.smartRoomTypeItemIv.loadImage(mImageLoader, typeInfo.getBase_url()+typeInfo.getName()+"@2x."+typeInfo.getImage_type());
            Glide
                    .with(mContext)
                    .load(typeInfo.getBase_url()+typeInfo.getName()+"@2x."+typeInfo.getImage_type())
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.smartRoomTypeItemIv);
        }
        return convertView;
    }

    public void setOnRoomTypeClickListener(OnRoomTypeClick lis) {
        mListener = lis;
    }

    public List<ImageInfo> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<ImageInfo> typeList) {
        this.typeList = typeList;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    static class ViewHolder {
        @BindView(R.id.smart_room_type_item_iv)
        ImageView smartRoomTypeItemIv;
        @BindView(R.id.smart_room_type_item_red)
        TextView smartRoomTypeItemRed;
        @BindView(R.id.smart_room_type_item_rl)
        RelativeLayout smartRoomTypeItemRl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
