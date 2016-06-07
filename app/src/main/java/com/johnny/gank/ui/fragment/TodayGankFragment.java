package com.johnny.gank.ui.fragment;
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
import com.johnny.gank.action.ActionType;
import com.johnny.gank.action.RxError;
import com.johnny.gank.action.TodayGankActionCreator;
import com.johnny.gank.data.ui.GankGirlImageItem;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.di.component.TodayGankFragmentComponent;
import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.dispatcher.RxViewDispatch;
import com.johnny.gank.store.RxStoreChange;
import com.johnny.gank.store.TodayGankStore;
import com.johnny.gank.ui.activity.MainActivity;
import com.johnny.gank.ui.activity.WebviewActivity;
import com.johnny.gank.ui.adapter.TodayGankAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class TodayGankFragment extends Fragment implements RxViewDispatch, SwipeRefreshLayout.OnRefreshListener, TodayGankAdapter.OnItemClickListener{

    public static final String TAG = TodayGankFragment.class.getSimpleName();

    @Bind(R.id.refresh_layout) SwipeRefreshLayout vRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView vWelfareRecycler;

    private TodayGankFragmentComponent mComponent;

    public static TodayGankFragment sInstance;

    @Inject TodayGankStore mStore;
    @Inject TodayGankActionCreator mActionCreator;
    @Inject Dispatcher mDispatcher;

    private TodayGankAdapter mAdapter;

    public static TodayGankFragment getInstance() {
        if(null == sInstance) {
            sInstance = new TodayGankFragment();
        }
        return sInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInjector();
    }

    private void initInjector() {
        mComponent = ((MainActivity)getActivity()).getMainActivityComponent().todayGankFragmentComponent();
        mComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_refresh_recycler, null);
        ButterKnife.bind(this, contentView);

        vRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        vRefreshLayout.setOnRefreshListener(this);
        vWelfareRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        vWelfareRecycler.setHasFixedSize(true);
        mAdapter = new TodayGankAdapter(this);
        mAdapter.setOnItemClickListener(this);
        vWelfareRecycler.setAdapter(mAdapter);

        mDispatcher.subscribeRxStore(mStore);
        mDispatcher.subscribeRxView(this);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                vRefreshLayout.setRefreshing(true);
                refreshData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
        super.onDestroyView();
    }

    private void refreshData() {
        mActionCreator.getTodayGank();
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case TodayGankStore.ID:
                vRefreshLayout.setRefreshing(false);
                mAdapter.swapData(mStore.getItems());
                break;
            default:
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        switch (error.getAction().getType()) {
            case ActionType.GET_TODAY_GANK:
                vRefreshLayout.setRefreshing(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onClickNormalItem(View view, GankNormalItem normalItem) {
        if(null != normalItem && !TextUtils.isEmpty(normalItem.url)) {
            WebviewActivity.openUrl(mComponent.getActivity(), normalItem.url, normalItem.desc);
        }
    }

    @Override
    public void onClickGirlItem(View view, GankGirlImageItem girlItem) {

    }
}
