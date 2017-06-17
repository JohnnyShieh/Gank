package com.johnny.gank.ui.adapter
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

import android.support.annotation.IntDef
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.johnny.gank.R
import com.johnny.gank.data.ui.GankNormalItem
import kotlinx.android.synthetic.main.pager_item_picture.view.*
import javax.inject.Inject

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class PicturePagerAdapter
    @Inject constructor() : PagerAdapter() {

    companion object {
        const val ADD_FRONT = -1L
        const val ADD_END = 1L
        const val ADD_NONE = 0L
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(ADD_FRONT, ADD_END, ADD_NONE)
    annotation class ADD_STATUS

    private val items = mutableListOf<GankNormalItem>()

    fun initList(list: List<GankNormalItem>) {
        items.addAll(list)
    }

    fun appendList(page: Int, list: List<GankNormalItem>): Long   {
        if (0 == count) return ADD_NONE
        if (page == items[0].page - 1) {
            items.addAll(0, list)
            return ADD_FRONT
        } else if (page == items.last().page + 1) {
            items.addAll(items.size, list)
            return ADD_END
        }
        return ADD_NONE
    }

    fun getItem(position: Int): GankNormalItem? {
        if (position !in 0..(count - 1)) return null
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.pager_item_picture, container, false)
        val item = items[position]
        Glide.with(container.context)
                .load(item.gank.url)
                .dontAnimate()
                .centerCrop()
                .into(view.pic)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) {
        if (`object` is View) container.removeView(`object`)
    }

    override fun getItemPosition(`object`: Any?): Int {
        return POSITION_NONE
    }
}
