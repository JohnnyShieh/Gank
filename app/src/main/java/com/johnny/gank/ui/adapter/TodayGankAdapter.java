package com.johnny.gank.ui.adapter;
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
import com.johnny.gank.data.ui.GankHeaderItem;
import com.johnny.gank.data.ui.GankItem;
import com.johnny.gank.data.ui.GankNormalItem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class TodayGankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_HEADER = 2;

    private List<GankItem> mItems;

    public void swapData(List<GankItem> list) {
        mItems = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new CategoryHeaderViewHolder(parent);
            case VIEW_TYPE_NORMAL:
                return new NormalViewHolder(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CategoryHeaderViewHolder) {
            CategoryHeaderViewHolder headerHolder = (CategoryHeaderViewHolder) holder;
            headerHolder.title.setText(((GankHeaderItem)mItems.get(position)).name);
            return;
        }
        if(holder instanceof NormalViewHolder) {
            NormalViewHolder normalHolder = (NormalViewHolder) holder;
            GankNormalItem normalItem = (GankNormalItem) mItems.get(position);
            normalHolder.title.setText(normalItem.desc);
            normalHolder.author.setText(normalItem.source);
        }
    }

    @Override
    public int getItemViewType(int position) {
        GankItem gankItem = mItems.get(position);
        if(gankItem instanceof GankHeaderItem) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return null == mItems ? 0 : mItems.size();
    }

    public static class CategoryHeaderViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.category_title) TextView title;

        public CategoryHeaderViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_category_title, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title) TextView title;
        @Bind(R.id.author) TextView author;

        public NormalViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_gank, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }
}
