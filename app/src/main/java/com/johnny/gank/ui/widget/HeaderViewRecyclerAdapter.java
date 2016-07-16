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

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class HeaderViewRecyclerAdapter extends WrapperRecyclerAdapter {

    public static final int VIEW_TYPE_HEADER = 1 << 7;
    public static final int VIEW_TYPE_FOOTER = 1 << 7 + 1;
    public static final int VIEW_TYPE_LOADING = 1 << 7 + 2;

    private View mHeaderView;
    private View mFooterView;
    private View mLoadingView;

    public HeaderViewRecyclerAdapter(
        RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        super(adapter);
    }

    public int getHeaderViewCount() {
        return (null == mHeaderView ? 0 : 1);
    }

    public int getFooterViewCount() {
        return (null == mFooterView ? 0 : 1);
    }

    public int getLoadingViewCount() {
        return (null == mLoadingView ? 0 : 1);
    }

    public void setHeaderView(View view) {
        if(null == view) {
            return;
        }
        mHeaderView = view;
        notifyDataSetChanged(); // TODO
    }

    public void setFooterView(View view) {
        if(null == view) {
            return;
        }
        mFooterView = view;
        notifyDataSetChanged();
    }

    public void setLoadingView(View view) {
        if(null == view) {
            return;
        }
        mLoadingView = view;
        notifyDataSetChanged();
    }

    public void removeHeaderView() {
        if(null != mHeaderView) {
            mHeaderView = null;
            notifyDataSetChanged();
        }
    }

    public void removeFooterView() {
        if(null != mFooterView) {
            mFooterView = null;
            notifyDataSetChanged();
        }
    }

    public void removeLoadingView() {
        if(null != mLoadingView) {
            mLoadingView = null;
            notifyDataSetChanged();
        }
    }

    private boolean isFullSpanType(int type) {
        return (type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER || type == VIEW_TYPE_LOADING);
    }

    private void setGridHeaderSpanSize(RecyclerView.LayoutManager layoutManager) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        if(gridLayoutManager.getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
            final int spanCount = gridLayoutManager.getSpanCount();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(isFullSpanType(getItemViewType(position))) {
                        return spanCount;
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            setGridHeaderSpanSize(recyclerView.getLayoutManager());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(holder instanceof HeaderViewHolder) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if(lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) lp;
                layoutParams.setFullSpan(true);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                itemView = mHeaderView;
                break;
            case VIEW_TYPE_FOOTER:
                itemView = mFooterView;
                break;
            case VIEW_TYPE_LOADING:
                itemView = mLoadingView;
                break;
            default:
                break;
        }
        if(null != itemView) {
            return new HeaderViewHolder(itemView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
            case VIEW_TYPE_FOOTER:
            case VIEW_TYPE_LOADING:
                break;
            default:
                mAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + getHeaderViewCount() + getFooterViewCount() + getLoadingViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        int headerViewCount = getHeaderViewCount();
        if(1 == headerViewCount && 0 == position) {
            return VIEW_TYPE_HEADER;
        }
        int actualCount = mAdapter.getItemCount();
        if(1 == getLoadingViewCount() && getItemCount() - 1 == position) {
            return VIEW_TYPE_LOADING;
        }
        int footerViewCount = getFooterViewCount();
        if(1 == footerViewCount && (actualCount + headerViewCount + footerViewCount - 1) == position ) {
            return VIEW_TYPE_FOOTER;
        }
        return mAdapter.getItemViewType(position);
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
