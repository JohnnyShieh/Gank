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
import com.johnny.gank.data.ui.GankNormalItem;

import android.support.annotation.IntDef;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class PicturePagerAdapter extends PagerAdapter {

    public static final int ADD_FRONT = -1;
    public static final int ADD_END = 1;
    public static final int ADD_NONE = 0;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ADD_FRONT, ADD_END, ADD_NONE})
    public @interface ADD_STATUS{}

    private List<GankNormalItem> mItems;

    @Inject
    public PicturePagerAdapter() {}

    public void initList(List<GankNormalItem> items) {
        mItems = items;
    }

    public @ADD_STATUS int appendList(int page, List<GankNormalItem> items) {
        if(0 == getCount()) {
            return ADD_NONE;
        }
        if(page == mItems.get(0).page - 1) {
            mItems.addAll(0, items);
            return ADD_FRONT;
        }else if(page == mItems.get(mItems.size() - 1).page + 1) {
            mItems.addAll(mItems.size(), items);
            return ADD_END;
        }
        return ADD_NONE;
    }

    public GankNormalItem getItem(int pos) {
        if(pos < 0 || pos >= getCount()) {
            return null;
        }
        return mItems.get(pos);
    }

    @Override
    public int getCount() {
        return (null == mItems ? 0 : mItems.size());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item_picture, container, false);
        ViewHolder holder = new ViewHolder(view);
        GankNormalItem item = mItems.get(position);
        Glide.with(container.getContext())
            .load(item.url)
            .dontAnimate()
            .centerCrop()
            .into(holder.vPic);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(object instanceof View) {
            container.removeView((View)object);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public static class ViewHolder {
        @Bind(R.id.pic) ImageView vPic;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
