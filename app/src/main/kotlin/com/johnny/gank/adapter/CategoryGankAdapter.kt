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

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.gank.R
import com.johnny.gank.model.ui.GankNormalItem
import kotlinx.android.synthetic.main.recycler_item_gank.view.*

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class CategoryGankAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<GankNormalItem>()

    var curPage = 0
        private set

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClickNormalItem(view: View, normalItem: GankNormalItem)
    }

    fun updateData(page: Int, list: List<GankNormalItem>?) {
        if (null == list || list.isEmpty()) return
        if (page - curPage > 1) return
        if (page <= 1) {
            if (!items.containsAll(list)) {
                items.clear()
                items.addAll(list)
                curPage = page
                notifyDataSetChanged()
            }
        } else if (1 == page - curPage) {
            val oldSize = items.size
            items.addAll(oldSize, list)
            notifyItemRangeInserted(oldSize, list.size)
            curPage = page
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NormalViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            val normalItem = items[position]
            holder.itemView.title.text =
                getGankTitleStr(normalItem.gank.desc, normalItem.gank.who)
            holder.itemView.title.setOnClickListener { view -> onItemClickListener?.onClickNormalItem(view, normalItem) }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class NormalViewHolder
        constructor(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_gank, parent, false)) {

        init {
            itemView.align_pic.visibility = View.GONE
        }
    }
}
