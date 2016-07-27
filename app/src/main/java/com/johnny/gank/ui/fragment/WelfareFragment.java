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
import com.johnny.gank.action.WelfareActionCreator;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.di.component.WelfareFragmentComponent;
import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.dispatcher.RxViewDispatch;
import com.johnny.gank.stat.StatName;
import com.johnny.gank.store.RxStoreChange;
import com.johnny.gank.store.WelfareStore;
import com.johnny.gank.ui.activity.MainActivity;
import com.johnny.gank.ui.activity.PictureActivity;
import com.johnny.gank.ui.adapter.WelfareAdapter;
import com.johnny.gank.ui.widget.HeaderViewRecyclerAdapter;
import com.johnny.gank.ui.widget.LoadMoreView;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Show all welfare pic.
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class WelfareFragment extends BaseFragment implements RxViewDispatch, SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = WelfareFragment.class.getSimpleName();

    @Bind(R.id.refresh_layout) SwipeRefreshLayout vRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView vWelfareRecycler;
    private LoadMoreView vLoadMore;

    private GridLayoutManager mLayoutManager;

    private WelfareFragmentComponent mComponent;

    private WelfareAdapter mAdapter;

    @Inject WelfareStore mStore;
    @Inject WelfareActionCreator mActionCreator;
    @Inject Dispatcher mDispatcher;

    private boolean mLoadingMore = false;

    public static WelfareFragment newInstance() {
        return new WelfareFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initInjector() {
        mComponent = ((MainActivity)getActivity()).getMainActivityComponent().welfareFragmentComponent();
        mComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_refresh_recycler, container, false);
        ButterKnife.bind(this, contentView);
        initInjector();

        vRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        vRefreshLayout.setOnRefreshListener(this);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        vWelfareRecycler.setLayoutManager(mLayoutManager);
        vWelfareRecycler.setHasFixedSize(true);
        vWelfareRecycler.addOnScrollListener(mScrollListener);

        vLoadMore = (LoadMoreView) inflater.inflate(R.layout.load_more, vWelfareRecycler, false);
        mAdapter = new WelfareAdapter(this);
        mAdapter.setItemClickListener(mItemClickListener);
        HeaderViewRecyclerAdapter adapter = new HeaderViewRecyclerAdapter(mAdapter);
        adapter.setLoadingView(vLoadMore);
        vWelfareRecycler.setAdapter(adapter);

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
                refreshList();
            }
        });
    }

    @Override
    public void onDestroyView() {
        mDispatcher.unsubscribeRxStore(mStore);
        mDispatcher.unsubscribeRxView(this);
        super.onDestroyView();
    }

    private void refreshList() {
        mActionCreator.getWelfareList(1);
    }

    private void loadMore() {
        mLoadingMore = true;
        vLoadMore.setStatus(LoadMoreView.STATUS_LOADING);
        mActionCreator.getWelfareList(mAdapter.getCurPage() + 1);
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case WelfareStore.ID:
                if(1 == mStore.getPage()) {
                    vRefreshLayout.setRefreshing(false);
                }
                mAdapter.updateData(mStore.getPage(), mStore.getGankList());
                mLoadingMore = false;
                vLoadMore.setStatus(LoadMoreView.STATUS_INIT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        switch (error.getAction().getType()) {
            case ActionType.GET_WELFARE_LIST:
                vRefreshLayout.setRefreshing(false);
                mLoadingMore = false;
                vLoadMore.setStatus(LoadMoreView.STATUS_FAIL);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        // Refresh the first page data.
        refreshList();
    }

    @Override
    protected String getStatPageName() {
        return StatName.PAGE_WELFARE;
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            boolean reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition()
                >= mLayoutManager.getItemCount() - 1;
            if(newState == RecyclerView.SCROLL_STATE_IDLE && !mLoadingMore && reachBottom) {
                mLoadingMore = true;
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            boolean reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition()
                >= mLayoutManager.getItemCount() - 1;
            if(!mLoadingMore && reachBottom) {
                loadMore();
            }
        }
    };

    private WelfareAdapter.OnItemClickListener mItemClickListener = new WelfareAdapter.OnItemClickListener() {
        @Override
        public void onClickItem(View view, GankNormalItem item) {
            if(null != item) {
                startActivity(PictureActivity.newIntent(mComponent.getActivity(), item.page, item._id));
            }
        }
    };
}
