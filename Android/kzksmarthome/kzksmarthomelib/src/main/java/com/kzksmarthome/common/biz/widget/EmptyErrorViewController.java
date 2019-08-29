package com.kzksmarthome.common.biz.widget;

import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.kzksmarthome.common.lib.task.MainTaskExecutor;

/**
 * 
 * 根据是否有本地数据，网络数据，更新空白view，错误view，数据view的状态
 *        状态                                                是否展示EmptyView     是否展示ErrorView     是否展示数据View
 *       加载中                                                        N                     N                 Y
 *  网络请求失败，本地有缓存数据                           N                     N                 Y
 *  网络请求失败，本地无缓存数据                           N                     Y                 N
 *  网络请求成功，后台返回有数据                           N                     N                 Y
 *  网络请求成功，后台返回无数据                           Y                     N                 N
 * @author panrq
 * @createDate 2015-3-26
 *
 */
public class EmptyErrorViewController {
    private View mEmptyView;
    private View mErrorView;
    private View mContentView;
    private AdapterWrapper mAdapter;
    private Animation mShowAnim;

    public EmptyErrorViewController(View emptyView, View errorView, View contentView, AdapterWrapper adapter) {
        this.mEmptyView = emptyView;
        this.mErrorView = errorView;
        this.mContentView = contentView;
        this.mAdapter = adapter;
    }

    private Animation getShowAnim() {
        if (mShowAnim == null) {
            mShowAnim = new AlphaAnimation(0.5f, 1.0f);
            mShowAnim.setDuration(500);
        }
        return mShowAnim;
    }

    public void onLoading() {
        setEmptyViewVisible(false);
        setErrorViewVisible(false);
        setContentViewVisible(true);
    }
    
    public void onRequestError() {
        MainTaskExecutor.scheduleTaskOnUiThread(300, new Runnable() {
            
            @Override
            public void run() {
                boolean haveLocalData = mAdapter != null && mAdapter.getCount() > 0;
                if (haveLocalData) {
                    setEmptyViewVisible(false);
                    setErrorViewVisible(false);
                    setContentViewVisible(true);
                } else {
                    setEmptyViewVisible(false);
                    setErrorViewVisible(true);
                    setContentViewVisible(false);
                }
            }
        });
    }
    
    public void onRequestFinish(final boolean hasRemoteData) {
        MainTaskExecutor.scheduleTaskOnUiThread(300, new Runnable() {
            
            @Override
            public void run() {
                if (hasRemoteData) {
                    setEmptyViewVisible(false);
                    setErrorViewVisible(false);
                    setContentViewVisible(true);
                } else {
                    setEmptyViewVisible(true);
                    setErrorViewVisible(false);
                    setContentViewVisible(false);
                }
            }
        });
        
    }
    
    private void setEmptyViewVisible(boolean visible) {
        if (mEmptyView != null) {
            if (visible) {
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyView.startAnimation(getShowAnim());
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        }
    }
    
    private void setErrorViewVisible(boolean visible) {
        if (mErrorView != null) {
            if (visible) {
                mErrorView.setVisibility(View.VISIBLE);
                mErrorView.startAnimation(getShowAnim());
            } else {
                mErrorView.setVisibility(View.GONE);
            }
        }
    }
    
    private void setContentViewVisible(boolean visible) {
        if (mContentView != null) {
            if (visible) {
                mContentView.setVisibility(View.VISIBLE);
            } else {
                mContentView.setVisibility(View.GONE);
            }
        }
    }

    public static class AdapterWrapper {
        android.support.v7.widget.RecyclerView.Adapter mRecyclerAdapter;
        android.widget.Adapter mAdapter;
        android.widget.ExpandableListAdapter mExpandableListAdapter;
        
        public AdapterWrapper(android.support.v7.widget.RecyclerView.Adapter recyclerAdapter) {
            mRecyclerAdapter = recyclerAdapter;
        }
        
        public AdapterWrapper(android.widget.Adapter adapter) {
            mAdapter = adapter;
        }
        
        public AdapterWrapper(android.widget.ExpandableListAdapter adapter) {
            mExpandableListAdapter = adapter;
        }
        
        public int getCount() {
            if (mRecyclerAdapter != null) {
                return mRecyclerAdapter.getItemCount();
            }
            if (mAdapter != null) {
                return mAdapter.getCount();
            }
            if (mExpandableListAdapter != null) {
                return mExpandableListAdapter.getGroupCount();
            }
            return 0;
        }
    }
}
