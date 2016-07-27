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
import com.johnny.gank.dispatcher.RxViewDispatch;
import com.johnny.gank.ui.adapter.CategoryGankAdapter;
import com.johnny.gank.ui.widget.HeaderViewRecyclerAdapter;
import com.johnny.gank.ui.widget.LoadMoreView;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public abstract class CategoryGankFragment extends BaseFragment implements RxViewDispatch, SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.refresh_layout) SwipeRefreshLayout vRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView vWelfareRecycler;
    protected LoadMoreView vLoadMore;

    protected LinearLayoutManager mLayoutManager;

    protected CategoryGankAdapter mAdapter;

    protected boolean mLoadingMore = false;

    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View contentView = inflater.inflate(R.layout.fragment_refresh_recycler, container, false);
        ButterKnife.bind(this, contentView);

        vRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        vRefreshLayout.setOnRefreshListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        vWelfareRecycler.setLayoutManager(mLayoutManager);
        vWelfareRecycler.setHasFixedSize(true);
        vWelfareRecycler.addOnScrollListener(mScrollListener);
        mAdapter = new CategoryGankAdapter();

        vLoadMore = (LoadMoreView) inflater.inflate(R.layout.load_more, vWelfareRecycler, false);
        HeaderViewRecyclerAdapter adapter = new HeaderViewRecyclerAdapter(mAdapter);
        adapter.setLoadingView(vLoadMore);
        vWelfareRecycler.setAdapter(adapter);

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
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected abstract void refreshList();

    protected abstract void loadMore();

    @Override
    public void onRefresh() {
        refreshList();
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
                mLoadingMore = true;
                loadMore();
            }
        }
    };
}
