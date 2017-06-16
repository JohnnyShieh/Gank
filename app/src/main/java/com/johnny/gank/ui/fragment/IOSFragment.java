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
import com.johnny.gank.action.IOSActionCreator;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.di.component.IOSFragmentComponent;
import com.johnny.gank.stat.StatName;
import com.johnny.gank.store.NormalGankStore;
import com.johnny.gank.ui.activity.MainActivity;
import com.johnny.gank.ui.activity.WebviewActivity;
import com.johnny.gank.ui.adapter.CategoryGankAdapter;
import com.johnny.gank.ui.widget.LoadMoreView;
import com.johnny.rxflux.Store;
import com.johnny.rxflux.StoreObserver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * description
 *
 * @author Johnny Shieh
 * @version 1.0
 */
public class IOSFragment extends CategoryGankFragment implements
    StoreObserver {

    public static final String TAG = IOSFragment.class.getSimpleName();

    @Inject
    NormalGankStore mStore;

    @Inject
    IOSActionCreator mActionCreator;

    protected IOSFragmentComponent mComponent;

    public static IOSFragment newInstance() {
        return new IOSFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    private void initInjector() {
        mComponent = ((MainActivity)getActivity()).getMainActivityComponent()
            .iosFragmentComponent()
            .build();
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
                WebviewActivity.openUrl(getActivity(), normalItem.getGank().getUrl(), normalItem.getGank().getDesc());
            }
        });

        mStore.register(ActionType.GET_IOS_LIST);
        mStore.setObserver(this);
        return contentView;
    }

    @Override
    public void onDestroyView() {
        mStore.unRegister();
        super.onDestroyView();
    }

    @Override
    protected void refreshList() {
        mActionCreator.getIOSList(1);
    }

    @Override
    protected void loadMore() {
        vLoadMore.setStatus(LoadMoreView.STATUS_LOADING);
        mActionCreator.getIOSList(mAdapter.getCurPage() + 1);
    }

    @Override
    protected String getStatPageName() {
        return StatName.PAGE_IOS;
    }

    @Override
    public void onChange(Store store, String actionType) {
        if(1 == mStore.getPage()) {
            vRefreshLayout.setRefreshing(false);
        }
        vLoadMore.setStatus(LoadMoreView.STATUS_INIT);
        mAdapter.updateData(mStore.getPage(), mStore.getGankList());
        mLoadingMore = false;
    }

    @Override
    public void onError(Store store, String actionType) {
        vRefreshLayout.setRefreshing(false);
        mLoadingMore = false;
        vLoadMore.setStatus(LoadMoreView.STATUS_FAIL);
    }
}
