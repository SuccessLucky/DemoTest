package com.kzksmarthome.common.biz.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

import com.kzksmarthome.lib.R;

public class PaperStyleExpandableListView extends ExpandableListView {

    private View mHeaderView;
    private View mFooterView;

    public PaperStyleExpandableListView(Context context) {
        this(context, null, 0);
    }

    public PaperStyleExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaperStyleExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderFooter();
    }

    private void initHeaderFooter() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mHeaderView = inflater.inflate(R.layout.list_top_empty_view, null);
        setTopSpaceViewLineVisibility(View.VISIBLE);
        mFooterView = inflater.inflate(R.layout.list_bottom_shadow_view, null);
        addHeaderView(mHeaderView);
        addFooterView(mFooterView);
    }

    public void setTopSpaceViewLineVisibility(int visibility) {
        mHeaderView.findViewById(R.id.empty_bottom_line).setVisibility(visibility);
    }

    public void setBottomShadowVisibility(int visibility) {
        mFooterView.findViewById(R.id.imageView1).setVisibility(visibility);
    }

    public void setBottomLineVisibility(int visibility) {
        mFooterView.findViewById(R.id.shadow_top_line).setVisibility(visibility);
    }
    
    private void checkIfShowHeaderFooter() {
        final ListAdapter adapter = getAdapter();
        if (adapter != null && adapter.getCount() - 2 != 0) {
            mHeaderView.setVisibility(View.VISIBLE);
            mFooterView.setVisibility(View.VISIBLE);
        } else {
            mHeaderView.setVisibility(View.GONE);
            mFooterView.setVisibility(View.GONE);
        }
    }

    final DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfShowHeaderFooter();
        }
    };

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        final ExpandableListAdapter oldAdapter = (ExpandableListAdapter) getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerDataSetObserver(observer);
        }
        checkIfShowHeaderFooter();
    }

}
