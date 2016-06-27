package com.johnny.gank.ui.fragment;
/*
 * Copyright (C) 2015 Johnny Shieh Open Source Project
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

import com.johnny.gank.action.ActionType;
import com.johnny.gank.action.FrontEndActionCreator;
import com.johnny.gank.action.RxError;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.di.component.FrontEndFragmentComponent;
import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.store.FrontEndStore;
import com.johnny.gank.store.IOSStore;
import com.johnny.gank.store.RxStoreChange;
import com.johnny.gank.ui.activity.MainActivity;
import com.johnny.gank.ui.activity.WebviewActivity;
import com.johnny.gank.ui.adapter.CategoryGankAdapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class FrontEndFragment extends CategoryGankFragment {

    public static final String TAG = FrontEndFragment.class.getSimpleName();

    @Inject FrontEndStore mStore;
    @Inject FrontEndActionCreator mActionCreator;
    @Inject Dispatcher mDispatcher;

    private FrontEndFragmentComponent mComponent;

    private static FrontEndFragment sInstance;

    public static FrontEndFragment getInstance() {
        if(null == sInstance) {
            sInstance = new FrontEndFragment();
        }
        return sInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInjector();
    }

    private void initInjector() {
        mComponent = ((MainActivity)getActivity()).getMainActivityComponent().frontEndFragmentComponent();
        mComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View contentView = createView(inflater, container);
        mAdapter.setOnItemClickListener(new CategoryGankAdapter.OnItemClickListener() {
            @Override
            public void onClickNormalItem(View view, GankNormalItem normalItem) {
                WebviewActivity.openUrl(getActivity(), normalItem.url, normalItem.desc);
            }
        });

        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);
        return contentView;
    }

    @Override
    public void onDestroyView() {
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
        super.onDestroyView();
    }

    @Override
    protected void refreshList() {
        mActionCreator.getFrontEndList(1);
    }

    @Override
    protected void loadMore() {
        mActionCreator.getFrontEndList(mAdapter.getCurPage() + 1);
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case FrontEndStore.ID:
                if(1 == mStore.getPage()) {
                    vRefreshLayout.setRefreshing(false);
                }
                mAdapter.updateData(mStore.getPage(), mStore.getGankList());
                mLoadingMore = false;
                Snackbar.make(vWelfareRecycler, "Loaded Success!", Snackbar.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        switch (error.getAction().getType()) {
            case ActionType.GET_FRONT_END_LIST:
                vRefreshLayout.setRefreshing(false);
                mLoadingMore = false;
                break;
            default:
                break;
        }
    }
}
