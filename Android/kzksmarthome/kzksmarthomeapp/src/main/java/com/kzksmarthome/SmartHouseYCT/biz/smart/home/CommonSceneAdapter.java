package com.kzksmarthome.SmartHouseYCT.biz.smart.home;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 场景适配器
 * Created by jack on 2016/9/6.
 */
public class CommonSceneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnSceneItemClick {
        void onSceneItemClick(int position);
    }

    private OnSceneItemClick mListener;

    private Activity mContext;
    private LayoutInflater mInflater;
    //private ImageLoader imageLoader = ImageLoader.getInstance();
    //private ImageLoader mImageLoader;
    private String baseImgUrl = "";

    private List<SceneInfo> sceneList;
    /**
     * 网关
     */
    private String iotMac;

    public CommonSceneAdapter(Activity context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, "");
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) context, R.drawable.translucent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.smart_common_scene_item, viewGroup, false);
        CommonSceneViewHolder viewHolder = new CommonSceneViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CommonSceneViewHolder sceneMainViewHolder = (CommonSceneViewHolder) viewHolder;
        SceneInfo sceneInfo = sceneList.get(position);
        if (null != sceneInfo) {
            //sceneMainViewHolder.indexSceneIv.loadImage(mImageLoader, baseImgUrl + "Un_" + sceneInfo.getImage() + ".png");
            Glide
                    .with(mContext)
                    .load(baseImgUrl + "Un_" + sceneInfo.getImage() + "@2x.png")
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(sceneMainViewHolder.indexSceneIv);
            sceneMainViewHolder.indexSceneTv.setText(sceneInfo.getName());
        }
        if (position == (getItemCount() - 1)) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) sceneMainViewHolder.indexSceneRl.getLayoutParams();
            layoutParams.rightMargin = 30;
            sceneMainViewHolder.indexSceneRl.setLayoutParams(layoutParams);
        }
    }

    public List<SceneInfo> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<SceneInfo> sceneList) {
        this.sceneList = sceneList;
    }

    @Override
    public int getItemCount() {
        return sceneList == null ? 0 : sceneList.size();
    }

    public void setOnSceneItemClickListener(OnSceneItemClick lis) {
        mListener = lis;
    }

    class CommonSceneViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.index_scene_iv)
        ImageView indexSceneIv;
        @BindView(R.id.index_scene_tv)
        TextView indexSceneTv;
        @BindView(R.id.index_scene_rl)
        LinearLayout indexSceneRl;

        @OnClick({R.id.index_scene_rl})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.index_scene_rl:
                    if (null != mListener) {
                        mListener.onSceneItemClick(getPosition());
                    }
                    break;
            }
        }

        CommonSceneViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
