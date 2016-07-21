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
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.johnny.gank.R;
import com.johnny.gank.data.entity.Gank;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.ui.widget.RatioImageView;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class WelfareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment mFragment;
    private List<GankNormalItem> mWelfareList;

    private int mCurPage = 0;

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onClickItem(View view, GankNormalItem item);
    }

    public WelfareAdapter(Fragment fragment) {
        mFragment = fragment;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void updateData(int page, List<GankNormalItem> list) {
        if(null == list || 0 == list.size()) {
            return;
        }
        if(page - mCurPage > 1) {
            return;
        }
        if(page <= 1) {
            if(null == mWelfareList || !mWelfareList.containsAll(list)) {
                mWelfareList = list;
                notifyDataSetChanged();
                mCurPage = page;
            }
        }else if(1 == page - mCurPage && null != mWelfareList) {
            int size = mWelfareList.size();
            mWelfareList.addAll(size, list);
            notifyItemRangeInserted(size, list.size());
            mCurPage = page;
        }
    }

    public int getCurPage() {
        return mCurPage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_welfare, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if(null != tag && tag instanceof Integer) {
                    int position = (Integer) tag;
                    if(null != mItemClickListener) {
                        mItemClickListener.onClickItem(v, mWelfareList.get(position));
                    }
                }
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        holder.itemView.setTag(position);
        GankNormalItem welfare = mWelfareList.get(position);
        Glide.with(mFragment)
            .load(welfare.url)
            .centerCrop()
            .placeholder(R.color.imageColorPlaceholder)
            .into(vh.vGirlImage);
    }

    @Override
    public int getItemCount() {
        return null == mWelfareList ? 0 : mWelfareList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.girl_image) RatioImageView vGirlImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // set the ratio to golden ratio.
            vGirlImage.setRatio(0.618f);
        }
    }
}
