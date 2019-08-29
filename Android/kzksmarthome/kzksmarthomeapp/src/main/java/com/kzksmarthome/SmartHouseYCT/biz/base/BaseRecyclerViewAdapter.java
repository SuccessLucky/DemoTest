package com.kzksmarthome.SmartHouseYCT.biz.base;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.BindView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;

public class BaseRecyclerViewAdapter<T extends BaseRecyclerItemData> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> items = null;
    protected boolean isShowTopSpace = false;
    public static final int VIEW_TYPE_TITLE = -1; // 标题
    public static final int VIEW_TYPE_NO_DATA = -2; // 没数据

    public static final int VIEW_TYPE_LIST_TOP_SPACE = -3; // 顶部空view
    public static final int VIEW_TYPE_LIST_BOTTOM_SHADOW_VIEW = -4; // 带阴影间隔view
    public static final int VIEW_TYPE_EMPTY_48 = -5; // 空view
    /**
     * 图片
     */
    public static final int VIEW_TYPE_PHOTO=-6;

    public BaseRecyclerViewAdapter(Context context, List<T> items) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (null != items) {
            T obj = items.get(position);
            if (null != obj) {
                return obj.mViewType;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (null == items) {
            return 0;
        }
        return items.size();

    }

    public T getData(int position) {
        if (null == items) {
            return null;
        }
        return items.get(position);
    }

    public void setData(List<T> msg) {
        if (msg != items) {
            if (items != null) {
                items.clear();
            }
            this.items = msg;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup vg, int viewtype) {

        ViewHolder viewHolder = null;
        View view;
        switch (viewtype) {
        case VIEW_TYPE_LIST_TOP_SPACE:
            view = mInflater.inflate(R.layout.list_top_empty_view, vg, false);
            viewHolder = new ViewHolderEmpty(view);
            break;
        case VIEW_TYPE_LIST_BOTTOM_SHADOW_VIEW:
            view = mInflater.inflate(R.layout.list_bottom_shadow_view, vg, false);
            viewHolder = new ViewHolderShadow(view);
            break;
        case VIEW_TYPE_EMPTY_48:
            view = mInflater.inflate(R.layout.list_margin_empty_view, vg, false);
            viewHolder = new ViewHolderEmpty_2(view);
            break;
        }
        return viewHolder;
    }

    public class ViewHolderEmpty extends ViewHolder {

        @BindView(R.id.empty_bottom_line)
        View line;

        public ViewHolderEmpty(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (isShowTopSpace) {
                line.setVisibility(View.VISIBLE);
            } else {
                line.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class ViewHolderEmpty_2 extends ViewHolder {

        public ViewHolderEmpty_2(View view) {
            super(view);
            view.setBackgroundColor(SmartHomeApp.getInstance().getResources().getColor(R.color.transparent));
        }
    }

    public class ViewHolderShadow extends ViewHolder {

        public ViewHolderShadow(View view) {
            super(view);
        }
    }
}
