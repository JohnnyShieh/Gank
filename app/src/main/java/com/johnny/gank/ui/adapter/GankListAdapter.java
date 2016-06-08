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

import com.bumptech.glide.Glide;
import com.johnny.gank.R;
import com.johnny.gank.data.ui.GankGirlImageItem;
import com.johnny.gank.data.ui.GankHeaderItem;
import com.johnny.gank.data.ui.GankItem;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.ui.widget.RatioImageView;
import com.johnny.gank.util.AppUtil;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
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
public class GankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_HEADER = 2;
    private static final int VIEW_TYPE_GIRL_IMAGE = 3;

    private Fragment mFragment;
    private List<GankItem> mItems;

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onClickNormalItem(View view, GankNormalItem normalItem);
        void onClickGirlItem(View view, GankGirlImageItem girlItem);
    }

    public GankListAdapter(Fragment fragment) {
        mFragment = fragment;
    }

    public void swapData(List<GankItem> list) {
        mItems = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mItemClickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new CategoryHeaderViewHolder(parent);
            case VIEW_TYPE_NORMAL:
                return new NormalViewHolder(parent);
            case VIEW_TYPE_GIRL_IMAGE:
                return new GirlImageViewHolder(parent);
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
            final GankNormalItem normalItem = (GankNormalItem) mItems.get(position);
            normalHolder.title.setText(getGankTitleStr(normalItem.desc, normalItem.who));
            normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mItemClickListener) {
                        mItemClickListener.onClickNormalItem(v, normalItem);
                    }
                }
            });
            return;
        }
        if(holder instanceof GirlImageViewHolder) {
            GirlImageViewHolder girlHolder = (GirlImageViewHolder) holder;
            final GankGirlImageItem girlItem = (GankGirlImageItem) mItems.get(position);
            Glide.with(mFragment)
                .load(girlItem.imgUrl)
                .placeholder(R.color.imageColorPlaceholder)
                .centerCrop()
                .into(girlHolder.girl_image);
            girlHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mItemClickListener) {
                        mItemClickListener.onClickGirlItem(v, girlItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        GankItem gankItem = mItems.get(position);
        if(gankItem instanceof GankHeaderItem) {
            return VIEW_TYPE_HEADER;
        }
        if(gankItem instanceof GankGirlImageItem) {
            return VIEW_TYPE_GIRL_IMAGE;
        }
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return null == mItems ? 0 : mItems.size();
    }

    private CharSequence getGankTitleStr(String desc, String who) {
        if(TextUtils.isEmpty(who)) {
            return desc;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(desc);
        SpannableString spannableString = new SpannableString(" (" + who + ")");
        spannableString.setSpan(new TextAppearanceSpan(AppUtil.getAppContext(), R.style.SummaryTextAppearance), 0, spannableString.length(), 0);
        builder.append(spannableString);
        return builder;
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

        public NormalViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_gank, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }

    public static class GirlImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.girl_image) RatioImageView girl_image;

        public GirlImageViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_girl_imge, parent, false));
            ButterKnife.bind(this, itemView);
            girl_image.setRatio(1.618f);
        }
    }
}
