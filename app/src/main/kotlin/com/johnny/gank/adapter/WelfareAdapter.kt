package com.johnny.gank.adapter
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

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.johnny.gank.R
import com.johnny.gank.model.ui.GankNormalItem
import kotlinx.android.synthetic.main.recycler_item_welfare.view.*

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class WelfareAdapter(private val fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<GankNormalItem>()

    var curPage = 0
        private set

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClickItem(view: View, item: GankNormalItem)
    }

    fun updateData(page: Int, list: List<GankNormalItem>?) {
        if (null == list || list.isEmpty()) return
        if (page - curPage > 1) return
        if (page <= 1 && !items.containsAll(list)) {
            items.clear()
            items.addAll(list)
            notifyDataSetChanged()

        } else if (1 == page - curPage) {
            val oldSize = items.size
            items.addAll(oldSize, list)
            notifyItemRangeInserted(oldSize, list.size)
        }
        curPage = page
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_welfare, parent, false)
        itemView.setOnClickListener { view ->
            val tag = view.tag
            if (null != tag && tag is Int) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.girl_image.transitionName = items[tag].gank.url
                }
                onItemClickListener?.onClickItem(view, items[tag])
            }
        }
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.itemView.tag = position
            val item = items[position]
            Glide.with(fragment)
                .load(item.gank.url)
                .apply(RequestOptions()
                    .placeholder(R.color.imageColorPlaceholder)
                    .centerCrop()
                ).into(holder.itemView.girl_image)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
