package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 场景编辑适配
 * Created by jack on 2016/9/6.
 */
public class SceneMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnSceneItemClick {
        void onSceneDeleteClick(int position);

        void onSceneItemClick(int position);
    }

    private OnSceneItemClick mListener;

    private Activity mContext;
    private LayoutInflater mInflater;
    /**
     * 编辑状态
     */
    private boolean isEdit = false;
    //private ImageLoader imageLoader = ImageLoader.getInstance();
    //private in.srain.cube.image.ImageLoader  mImageLoader;
    private String baseImgUrl = "";

    private List<SceneInfo> sceneList;
    /**
     * 网关
     */
    private String iotMac;

    public SceneMainAdapter(Activity context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, "");
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context, R.drawable.translucent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.smart_scene_main_item, viewGroup, false);
        SceneMainViewHolder viewHolder = new SceneMainViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SceneMainViewHolder sceneMainViewHolder = (SceneMainViewHolder) viewHolder;
        SceneInfo sceneInfo = sceneList.get(position);
        if (null != sceneInfo) {
            if (isEdit) {
                if (-1 == sceneInfo.getScene_id()) {
                    //BitmapDrawable bitmapDrawable  = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.scene_add_icon);
                    //sceneMainViewHolder.smartSceneMainIm.loadImage(mImageLoader,"onLoadDrawable",bitmapDrawable);
                    sceneMainViewHolder.smartSceneMainIm.setImageResource(R.drawable.scene_add_im);
                    sceneMainViewHolder.smartSceneMainDelTv.setVisibility(View.INVISIBLE);
                    sceneMainViewHolder.smartSceneMainTv.setText(R.string.add_str);
                } else {
                    sceneMainViewHolder.smartSceneMainDelTv.setVisibility(View.VISIBLE);
                    //imageLoader.displayImage(baseImgUrl + "Un_" + sceneInfo.getImage() + "@2x.png", sceneMainViewHolder.smartSceneMainIm);
                    //sceneMainViewHolder.smartSceneMainIm.loadImage(mImageLoader, baseImgUrl + "Un_" + sceneInfo.getImage() + "@2x.png");
                    Glide
                            .with(mContext)
                            .load(baseImgUrl + "Un_" + sceneInfo.getImage() + "@2x.png")
                            .centerCrop()
                            .placeholder(R.drawable.default_img)
                            .crossFade()
                            //.diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(sceneMainViewHolder.smartSceneMainIm);
                    sceneMainViewHolder.smartSceneMainTv.setText(sceneInfo.getName());
                    //sceneMainViewHolder.smartSceneMainIm.setImageResource(R.drawable.cj_hj);
                }
            } else {
                sceneMainViewHolder.smartSceneMainDelTv.setVisibility(View.INVISIBLE);
                //imageLoader.displayImage(baseImgUrl + "Un_" + sceneInfo.getImage() + "@2x.png", sceneMainViewHolder.smartSceneMainIm);
                //sceneMainViewHolder.smartSceneMainIm.loadImage(mImageLoader, baseImgUrl + "Un_" + sceneInfo.getImage() + "@2x.png");
                Glide
                        .with(mContext)
                        .load(baseImgUrl + "Un_" + sceneInfo.getImage() + "@2x.png")
                        .centerCrop()
                        .placeholder(R.drawable.default_img)
                        .crossFade()
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(sceneMainViewHolder.smartSceneMainIm);
                sceneMainViewHolder.smartSceneMainTv.setText(sceneInfo.getName());
            }
        }
    }

    public List<SceneInfo> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<SceneInfo> sceneList) {
        this.sceneList = sceneList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sceneList == null ? 0 : sceneList.size();
    }

    public void setOnSceneItemClickListener(OnSceneItemClick lis) {
        mListener = lis;
    }

    class SceneMainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_scene_main_im)
        ImageView smartSceneMainIm;
        @BindView(R.id.smart_scene_main_del_tv)
        TextView smartSceneMainDelTv;
        @BindView(R.id.smart_scene_main_tv)
        TextView smartSceneMainTv;

        @OnClick({R.id.smart_scene_main_im, R.id.smart_scene_main_del_tv})
        void onClick(View view) {
            try {
            switch (view.getId()) {
                case R.id.smart_scene_main_im:
                    byte sceneNum = getSceneNum();
                    SceneInfo sceneInfo = sceneList.get(getPosition());
                    if (isEdit) {
                        if (-1 == sceneInfo.getScene_id()) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sceneInfo", new SceneInfo(-1));
                            bundle.putByte("sceneNum",sceneNum);
                            PageSwitcher.switchToTopNavPage((Activity) mContext, SceneEditFragment.class, bundle, "", "添加场景", "完成");
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sceneInfo", sceneInfo);
                            bundle.putInt("position", getPosition());
                            bundle.putByte("sceneNum",Tools.hexStr2Byte(sceneInfo.getSerial_number()));
                            PageSwitcher.switchToTopNavPage((Activity) mContext, SceneEditFragment.class, bundle, "", "编辑场景", "完成");
                        }
                    } else {
                        if (null != mListener) {
                            mListener.onSceneItemClick(getPosition());
                        }
                    }
                    break;
                case R.id.smart_scene_main_del_tv:
                    if (isEdit) {
                        if (null != mListener) {
                            mListener.onSceneDeleteClick(getPosition());
                        }
                    }
                    break;
            }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        SceneMainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 获取场景编号
     * @return
     */
    public byte getSceneNum() {
        byte sceneNum = 1;
        try {
            boolean haveSceneNum = false;
            for (byte i = 1; i < 44; i++) {
                haveSceneNum = false;
                for (SceneInfo info : sceneList) {
                    if (info.getSerial_number() != null && Tools.hexStr2Byte(info.getSerial_number()) == i) {
                        haveSceneNum = true;
                        break;
                    }
                }
                if(!haveSceneNum){
                    sceneNum = i;
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return sceneNum;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
