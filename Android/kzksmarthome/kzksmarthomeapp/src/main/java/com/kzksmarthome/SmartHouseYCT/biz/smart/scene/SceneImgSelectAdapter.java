package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: SceneImgSelectAdapter
 * @Description: 场景图片选择适配器
 * @date 2016/10/16 15:36
 */
public class SceneImgSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnImgItemClick {
        void onImgItemClick(ImageInfo img);
    }

    private OnImgItemClick mListener;

    private Activity mContext;
    private LayoutInflater mInflater;
    //private ImageLoader imageLoader = ImageLoader.getInstance();
    //private in.srain.cube.image.ImageLoader  mImageLoader;
    //private String baseImgUrl = "";

    private List<ImageInfo> imageList;

    public SceneImgSelectAdapter(Activity context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        //baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, "");
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context,R.drawable.translucent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.smart_scene_img_item, viewGroup, false);
        SceneImgViewHolder viewHolder = new SceneImgViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SceneImgViewHolder sceneMainViewHolder = (SceneImgViewHolder) viewHolder;
        ImageInfo imageInfo = imageList.get(position);
        if (null != imageInfo) {
            //sceneMainViewHolder.smartSceneImgTv.setVisibility(View.VISIBLE);
            //imageLoader.displayImage(imageInfo.getBase_url() + "Pr_" + imageInfo.getName() + "." + imageInfo.getImage_type(), sceneMainViewHolder.smartSceneImgIm);
            //sceneMainViewHolder.smartSceneImgIm.loadImage(mImageLoader, imageInfo.getBase_url() + "Pr_" + imageInfo.getName() + "." + imageInfo.getImage_type());
            Glide
                    .with(mContext)
                    .load(imageInfo.getBase_url() + "Pr_" + imageInfo.getName() + "@2x." + imageInfo.getImage_type())
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(sceneMainViewHolder.smartSceneImgIm);
        }
    }

    public List<ImageInfo> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageInfo> imageList) {
        this.imageList = imageList;
    }

    public void setmListener(OnImgItemClick mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getItemCount() {
        return imageList == null ? 0 : imageList.size();
    }

    class SceneImgViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_scene_img_im)
        ImageView smartSceneImgIm;
        @BindView(R.id.smart_scene_img_tv)
        TextView smartSceneImgTv;

        @OnClick(R.id.smart_scene_img_im)
        public void onClick() {
            if (mListener != null) {
                mListener.onImgItemClick(imageList.get(getPosition()));
            }
        }

        SceneImgViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
