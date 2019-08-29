/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {

	public PullToRefreshScrollView(Context context) {
		super(context);
	}

	public PullToRefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshScrollView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshScrollView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
		ScrollView scrollView;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			scrollView = new InternalScrollViewSDK9(context, attrs);
		} else {
			scrollView = new ObservableScrollView(context, attrs);
		}

		scrollView.setId(R.id.scrollview);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}
		return false;
	}

	@TargetApi(9)
	final class InternalScrollViewSDK9 extends ObservableScrollView {

		public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
			int newScrollX = scrollX + deltaX;
			int newScrollY = scrollY + deltaY;

			final int top = 0;
			final int bottom = maxOverScrollY + scrollRangeY;
			// Clamp values if at the limits and record
			final int left = -maxOverScrollX;
			final int right = maxOverScrollX + scrollRangeX;

			boolean clampedX = false;
			if (newScrollX > right) {
				newScrollX = right;
				clampedX = true;
			} else if (newScrollX < left) {
				newScrollX = left;
				clampedX = true;
			}

			boolean clampedY = false;
			if (newScrollY > bottom) {
				newScrollY = bottom;
				clampedY = true;
			} else if (newScrollY < top) {
				newScrollY = top;
				clampedY = true;
			}
			onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);

			boolean returnValue = clampedX || clampedY;

//			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
//					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
			// Does all of the hard work...
//			OverscrollHelper.overScrollBy(PullToRefreshScrollView.this, deltaX, scrollX, deltaY, scrollY,
//					getScrollRange(), isTouchEvent);
			return returnValue;
		}

		/**
		 * Taken from the AOSP ScrollView source
		 */
		private int getScrollRange() {
			int scrollRange = 0;
			if (getChildCount() > 0) {
				View child = getChildAt(0);
				scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
			}
			return scrollRange;
		}
	}

	@Override
	protected void onPullToRefresh() {
		super.onPullToRefresh();
		((ObservableScrollView)getRefreshableView()).onScrollHeader(true);
	}

	@Override
	protected void onReset() {
		super.onReset();
		((ObservableScrollView)getRefreshableView()).onScrollHeader(false);
	}

	@Override
	protected void onReleaseToRefresh() {
		super.onReleaseToRefresh();
		((ObservableScrollView)getRefreshableView()).onScrollHeader(true);
	}

	@Override
	protected void onRefreshing(boolean doScroll) {
		super.onRefreshing(doScroll);
		((ObservableScrollView)getRefreshableView()).onScrollHeader(true);
	}
}
