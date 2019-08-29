package com.kzksmarthome.SmartHouseYCT.biz.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import butterknife.ButterKnife;
import butterknife.BindView;

import com.kzksmarthome.SmartHouseYCT.R;

public abstract class BasePaperStyleExpandAdapter<T extends BaseRecyclerItemData> extends BaseExpandableListAdapter {

    protected LayoutInflater mInflater;
    protected List<T> mGroupItems = null;
    protected boolean mIsShowTopSpace = false;

    public static final int VIEW_TYPE_LIST_TOP_SPACE = -1; // 顶部空view
    public static final int VIEW_TYPE_LIST_BOTTOM_SHADOW_VIEW = -2; // 带阴影间隔view

    public BasePaperStyleExpandAdapter(Context context, List<T> groupItems) {
        mInflater = LayoutInflater.from(context);
        this.mGroupItems = groupItems;
    }

    @Override
    public int getGroupType(int groupPosition) {
        T obj = mGroupItems.get(groupPosition);
        return obj.mViewType;
    }

    @Override
    public int getGroupCount() {
        return mGroupItems.size();
    }
    
    @Override
    public Object getGroup(int groupPosition) {
        return mGroupItems.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public T getData(int position) {
        return mGroupItems.get(position);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Object o = getChild(groupPosition, childPosition);
        if (o == null) {
            return childPosition;
        }
        return o.hashCode();
    }
    
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    
    public void setData(List<T> msg) {
        if (msg != mGroupItems) {
            if (mGroupItems != null) {
                mGroupItems.clear();
            }
            this.mGroupItems = msg;
            notifyDataSetChanged();
        }
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        switch (getGroupType(groupPosition)) {
            case VIEW_TYPE_LIST_TOP_SPACE: {
                ViewHolder_EMPTY holder;
                if (convertView != null && convertView.getTag() instanceof ViewHolder_EMPTY) {
                    holder = (ViewHolder_EMPTY)convertView.getTag();
                } else {
                    convertView = mInflater.inflate(R.layout.list_top_empty_view, null);
                    holder = new ViewHolder_EMPTY(convertView, VIEW_TYPE_LIST_TOP_SPACE);
                    convertView.setTag(holder);
                }
                
                if (mIsShowTopSpace) {
                    holder.line.setVisibility(View.VISIBLE);
                } else {
                    holder.line.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case VIEW_TYPE_LIST_BOTTOM_SHADOW_VIEW: {
                ViewHolder_SHADOW holder;
                if (convertView != null && convertView.getTag() instanceof ViewHolder_SHADOW) {
                    holder = (ViewHolder_SHADOW)convertView.getTag();
                } else {
                    convertView = mInflater.inflate(R.layout.list_bottom_shadow_view, null);
                    holder = new ViewHolder_SHADOW(convertView, VIEW_TYPE_LIST_BOTTOM_SHADOW_VIEW);
                    convertView.setTag(holder);
                }
                if (groupPosition == getGroupCount()) {
                    holder.line.setVisibility(View.INVISIBLE);
                } else {
                    holder.line.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
        return convertView;
    }
   /* @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
        case VIEW_TYPE_LIST_TOP_EMPTY:
            break;
        case VIEW_TYPE_LIST_BOTTOM_SHADOW_VIEW:
            break;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup vg, int viewtype) {

        ViewHolder viewHolder = null;
        View view;
        switch (viewtype) {
        case VIEW_TYPE_LIST_TOP_SPACE:
            view = mInflater.inflate(R.layout.list_top_empty_view, vg, false);
            viewHolder = new ViewHolder_EMPTY(view);
            break;
        case VIEW_TYPE_LIST_BOTTOM_SHADOW_VIEW:
            view = mInflater.inflate(R.layout.list_bottom_shadow_view, vg, false);
            viewHolder = new ViewHolder_SHADOW(view);
            break;
        }
        return viewHolder;
    }*/

    public static class ViewHolder_EMPTY {
        int viewType;

        @BindView(R.id.empty_bottom_line)
        View line;

        public ViewHolder_EMPTY(View view, int viewType) {
            ButterKnife.bind(this, view);
            this.viewType = viewType;
        }
    }

    public static class ViewHolder_SHADOW {
        int viewType;
        
        @BindView(R.id.shadow_top_line)
        View line;

        public ViewHolder_SHADOW(View view, int viewType) {
            ButterKnife.bind(this, view);
            this.viewType = viewType;
        }
    }
}
