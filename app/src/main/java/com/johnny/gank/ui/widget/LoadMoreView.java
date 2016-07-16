package com.johnny.gank.ui.widget;
/*
 * Copyright (C) 2016 Johnny Shieh Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.johnny.gank.R;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class LoadMoreView extends FrameLayout {

    public static final int STATUS_INIT = 0;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_FAIL = 2;
    public static final int STATUS_NO_MORE = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_INIT, STATUS_LOADING, STATUS_FAIL, STATUS_NO_MORE})
    public @interface LoadStatus{}

    private @LoadStatus int mStatus = STATUS_INIT;

    @Bind(R.id.loading_indicator) LoadingIndicatorView vLoadingIndicator;
    @Bind(R.id.load_tip) TextView vLoadTip;

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.load_more_content, this);

        ButterKnife.bind(this);
        setStatus(mStatus);
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(@LoadStatus int status) {
        mStatus = status;
        switch (status) {
            case STATUS_INIT:
                setVisibility(INVISIBLE);
                break;
            case STATUS_LOADING:
                vLoadingIndicator.setVisibility(VISIBLE);
                vLoadTip.setVisibility(INVISIBLE);
                setVisibility(VISIBLE);
                break;
            case STATUS_FAIL:
                vLoadingIndicator.setVisibility(INVISIBLE);
                vLoadTip.setText(R.string.load_fail);
                vLoadTip.setVisibility(VISIBLE);
                setVisibility(VISIBLE);
                break;
            case STATUS_NO_MORE:
                vLoadingIndicator.setVisibility(INVISIBLE);
                vLoadTip.setText(R.string.load_no_more);
                vLoadTip.setVisibility(VISIBLE);
                setVisibility(VISIBLE);
                break;
        }
    }
}
