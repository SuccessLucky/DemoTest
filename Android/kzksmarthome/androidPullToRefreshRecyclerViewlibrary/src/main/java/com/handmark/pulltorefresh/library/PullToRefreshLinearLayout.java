/*******************************************************************************
 * Copyright 2014 Dean Ding.
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

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 
 * Support ViewGroup
 * 
 * @author Dean.Ding
 * 
 */
public class PullToRefreshLinearLayout extends PullToRefreshBase<RefreshLinearLayout> {

    public PullToRefreshLinearLayout(Context context) {
        super(context);
    }

    public PullToRefreshLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshLinearLayout(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshLinearLayout(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RefreshLinearLayout createRefreshableView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);
        int resId = 0;
        if (a.hasValue(R.styleable.PullToRefresh_ptrViewGroupId)) {
            resId = a.getResourceId(R.styleable.PullToRefresh_ptrViewGroupId, 0);
        }
        if (resId == 0) {
            RefreshLinearLayout view = new RefreshLinearLayout(context);
            return view;
        }
        RefreshLinearLayout viewGroup = (RefreshLinearLayout) LayoutInflater.from(context).inflate(resId, null);
        a.recycle();
        return viewGroup;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return true;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return true;
    }

}
