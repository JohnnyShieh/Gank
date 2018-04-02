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

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.johnny.gank.R
import com.johnny.gank.data.ui.GankGirlImageItem
import com.johnny.gank.data.ui.GankHeaderItem
import com.johnny.gank.data.ui.GankItem
import com.johnny.gank.data.ui.GankNormalItem
import kotlinx.android.synthetic.main.recycler_item_category_title.view.*
import kotlinx.android.synthetic.main.recycler_item_gank.view.*
import kotlinx.android.synthetic.main.recycler_item_girl_imge.view.*

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class GankListAdapter(private val fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        const val VIEW_TYPE_NORMAL = 1
        const val VIEW_TYPE_HEADER = 2
        const val VIEW_TYPE_GIRL_IMAGE = 3
    }

    interface OnItemClickListener {
        fun onClickNormalItem(view: View, normalItem: GankNormalItem)
        fun onClickGirlItem(view: View, girlImageItem: GankGirlImageItem)
    }

    private val items = mutableListOf<GankItem>()

    var onItemClickListener: OnItemClickListener? = null

    fun swapData(list: List<GankItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        when (viewType) {
            VIEW_TYPE_HEADER -> return CategoryHeaderViewHolder(parent)
            VIEW_TYPE_NORMAL -> return NormalViewHolder(parent)
            VIEW_TYPE_GIRL_IMAGE -> return GirlImageViewHolder(parent)
            else -> return null
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryHeaderViewHolder -> holder.itemView.category_title.text = (items[position] as GankHeaderItem).name
            is NormalViewHolder -> {
                val normalItem = items[position] as GankNormalItem
                holder.itemView.title.text = getGankTitleStr(normalItem.gank.desc, normalItem.gank.who)
                holder.itemView.title.setOnClickListener { view -> onItemClickListener?.onClickNormalItem(view, normalItem) }
            }
            is GirlImageViewHolder -> {
                val girlItem = items[position] as GankGirlImageItem
                Glide.with(fragment)
                        .load(girlItem.imgUrl)
                        .placeholder(R.color.imageColorPlaceholder)
                        .centerCrop()
                        .into(holder.itemView.girl_image)
                holder.itemView.setOnClickListener { view -> onItemClickListener?.onClickGirlItem(view, girlItem) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (items[position]) {
            is GankHeaderItem -> return VIEW_TYPE_HEADER
            is GankGirlImageItem -> return VIEW_TYPE_GIRL_IMAGE
            else -> return VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CategoryHeaderViewHolder
        constructor(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_category_title, parent, false))


    class NormalViewHolder
        constructor(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_gank, parent, false))

    class GirlImageViewHolder
        constructor(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_girl_imge, parent, false)) {
        init {
            itemView.girl_image.ratio = 1.618f
        }
    }

}
