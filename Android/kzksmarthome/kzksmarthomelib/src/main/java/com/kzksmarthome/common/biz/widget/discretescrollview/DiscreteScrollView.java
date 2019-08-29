package com.kzksmarthome.common.biz.widget.discretescrollview;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.kzksmarthome.common.biz.widget.discretescrollview.transform.DiscreteScrollItemTransformer;


/**
 * Created by yarolegovich on 18.02.2017.
 */
@SuppressWarnings("unchecked")
public class DiscreteScrollView extends RecyclerView {

    private DiscreteScrollLayoutManager layoutManager;

    private ScrollStateChangeListener scrollStateChangeListener;
    private CurrentItemChangeListener currentItemChangeListener;

    public DiscreteScrollView(Context context) {
        super(context);
    }

    public DiscreteScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscreteScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        layoutManager = new DiscreteScrollLayoutManager(getContext());
        layoutManager.setScrollStateListener(new DiscreteScrollLayoutManager.ScrollStateListener() {
            @Override
            public void onIsBoundReachedFlagChange(boolean isBoundReached) {
                setOverScrollMode(isBoundReached ? OVER_SCROLL_ALWAYS : OVER_SCROLL_NEVER);
            }

            @Override
            public void onScrollStart() {
                if (scrollStateChangeListener != null) {
                    int current = layoutManager.getCurrentPosition();
                    ViewHolder holder = getViewHolder(current);
                    scrollStateChangeListener.onScrollStart(holder, current);
                }
            }

            @Override
            public void onScrollEnd() {
                ViewHolder holder = null;
                int current = layoutManager.getCurrentPosition();
                if (scrollStateChangeListener != null) {
                    holder = getViewHolder(current);
                    scrollStateChangeListener.onScrollEnd(holder, current);
                }
                if (currentItemChangeListener != null) {
                    if (holder == null) {
                        holder = getViewHolder(current);
                    }
                    currentItemChangeListener.onCurrentItemChanged(holder, current);
                }
            }

            @Override
            public void onScroll(float currentViewPosition) {
                if (scrollStateChangeListener != null) {
                    scrollStateChangeListener.onScroll(currentViewPosition);
                }
            }

            @Override
            public void onCurrentViewFirstLayout() {
                if (currentItemChangeListener != null) {
                    int current = layoutManager.getCurrentPosition();
                    currentItemChangeListener.onCurrentItemChanged(getViewHolder(current), current);
                }
            }
        });
        setLayoutManager(layoutManager);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof DiscreteScrollLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new IllegalArgumentException(
                    "You should not set LayoutManager on DiscreteScrollView.class" +
                            "instance. Library uses a special one. Just don't call the method.");
        }
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        boolean isFling = super.fling(velocityX, velocityY);
        if (isFling) {
            layoutManager.onFling(velocityX);
        } else {
            layoutManager.returnToCurrentPosition();
        }
        return isFling;
    }

    public ViewHolder getViewHolder(int position) {
        return getChildViewHolder(layoutManager.findViewByPosition(position));
    }

    public void setItemTransformer(DiscreteScrollItemTransformer transformer) {
        layoutManager.setItemTransformer(transformer);
    }

    public void setItemTransitionTimeMillis(@IntRange(from = 10) int millis) {
        layoutManager.setTimeForItemSettle(millis);
    }

    public void setScrollStateChangeListener(ScrollStateChangeListener<?> scrollStateChangeListener) {
        this.scrollStateChangeListener = scrollStateChangeListener;
    }

    public void setCurrentItemChangeListener(CurrentItemChangeListener<?> currentItemChangeListener) {
        this.currentItemChangeListener = currentItemChangeListener;
    }

    /**
     * @return adapter position of the current item or -1 if nothing is selected
     */
    public int getCurrentItem() {
        return layoutManager.getCurrentPosition();
    }

    public interface ScrollStateChangeListener<T extends ViewHolder> {

        void onScrollStart(T currentItemHolder, int adapterPosition);

        void onScrollEnd(T currentItemHolder, int adapterPosition);

        void onScroll(float scrollPosition);
    }

    public interface CurrentItemChangeListener<T extends ViewHolder> {
        /*
         * This method will be also triggered when view appears on the screen for the first time.
         */
        void onCurrentItemChanged(T viewHolder, int adapterPosition);
    }

}
