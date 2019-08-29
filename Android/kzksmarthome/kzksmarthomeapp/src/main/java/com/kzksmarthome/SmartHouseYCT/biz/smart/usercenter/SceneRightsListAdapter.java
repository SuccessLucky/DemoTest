package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: SceneRightsListAdapter
 * @Description: 用户场景权限列表适配器
 * @date 2016/9/14 15:56
 */
public class SceneRightsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SceneInfo> sceneList = new ArrayList<SceneInfo>();

    private String baseImgUrl = "";

    public interface OnSceneItemClick {
        void onSceneItemClick(int position, SceneInfo sceneInfo);
    }

    private OnSceneItemClick mListener;

    public List<SceneInfo> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<SceneInfo> sceneList) {
        this.sceneList = sceneList;
    }

    public SceneRightsListAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        baseImgUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, "");
    }

    public SceneRightsListAdapter(Context context, List<SceneInfo> sceneList) {
        this(context);
        this.sceneList = sceneList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.smart_scene_rights_list_item, viewGroup, false);
        SceneViewHolder viewHolder = new SceneViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SceneViewHolder sceneViewHolder = (SceneViewHolder) viewHolder;
        SceneInfo sceneInfo = sceneList.get(position);
        if (null != sceneInfo) {
            sceneViewHolder.smartSceneRightsListNameTv.setText(sceneInfo.getName());
            Glide
                    .with(mContext)
                    .load(baseImgUrl + "Pr_"+sceneInfo.getImage() + "@2x.png")
                    .centerCrop()
                    .placeholder(R.drawable.default_img)
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(sceneViewHolder.smartSceneRightsListIconIv);
        }
    }

    @Override
    public int getItemCount() {
        return sceneList == null ? 0 : sceneList.size();
    }

    public void setOnSceneItemClickListener(OnSceneItemClick lis) {
        mListener = lis;
    }

    class SceneViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.smart_scene_rights_list_icon_iv)
        ImageView smartSceneRightsListIconIv;
        @BindView(R.id.smart_scene_rights_list_name_tv)
        TextView smartSceneRightsListNameTv;
        @BindView(R.id.smart_scene_rights_list_item_rl)
        RelativeLayout smartSceneRightsListItemRl;

        SceneViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.smart_scene_rights_list_item_rl)
        public void onClick() {
            if (mListener != null) {
                mListener.onSceneItemClick(getPosition(), sceneList.get(getPosition()));
            }
        }
    }
}
