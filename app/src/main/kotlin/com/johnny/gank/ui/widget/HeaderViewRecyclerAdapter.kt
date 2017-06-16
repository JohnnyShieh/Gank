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
class HeaderViewRecyclerAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : WrapperRecyclerAdapter(adapter) {

    var headerView: View? = null
        set(value) {
            val changed = field === value
            field = value
            if (changed) notifyDataSetChanged()
        }

    var footerView: View? = null
        set(value) {
            val changed = field === value
            field = value
            if (changed) notifyDataSetChanged()
        }

    var loadingView: View? = null
        set(value) {
            val changed = field === value
            field = value
            if (changed) notifyDataSetChanged()
        }

    fun getHeaderViewCount(): Int {
        return if (null == headerView) 0 else 1
    }

    fun getFooterViewCount(): Int {
        return if (null == footerView) 0 else 1
    }

    fun getLoadingViewCount(): Int {
        return if (null == loadingView) 0 else 1
    }

    private fun isFullSpanType(type: Int): Boolean {
        return (type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER || type == VIEW_TYPE_LOADING)
    }

    private fun setGridHeaderSpanSize(layoutManager: RecyclerView.LayoutManager) {
        val gridLayoutManager = layoutManager as GridLayoutManager
        if(gridLayoutManager.spanSizeLookup is GridLayoutManager.DefaultSpanSizeLookup) {
            val spanCount = gridLayoutManager.spanCount
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if(isFullSpanType(getItemViewType(position))) {
                        return spanCount
                    }
                    return 1
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if(recyclerView.layoutManager is GridLayoutManager) {
            setGridHeaderSpanSize(recyclerView.layoutManager)
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        if(holder is HeaderViewHolder) {
            val lp = holder.itemView.layoutParams
            if(lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        var itemView: View? = null
        when (viewType) {
            VIEW_TYPE_HEADER -> itemView = headerView
            VIEW_TYPE_FOOTER -> itemView = footerView
            VIEW_TYPE_LOADING -> itemView = loadingView
        }
        if(null != itemView) {
            return HeaderViewHolder(itemView)
        }
        return wrappedAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType != VIEW_TYPE_HEADER && viewType != VIEW_TYPE_FOOTER && viewType != VIEW_TYPE_LOADING) {
            wrappedAdapter.onBindViewHolder(holder, position - getHeaderViewCount())
        }
    }

    override fun getItemCount(): Int {
        return wrappedAdapter.itemCount + getHeaderViewCount() + getFooterViewCount() + getLoadingViewCount()
    }

    override fun getItemViewType(position: Int): Int {
        val headerViewCount = getHeaderViewCount()
        if(1 == headerViewCount && 0 == position) {
            return VIEW_TYPE_HEADER
        }
        val actualCount = wrappedAdapter.itemCount
        if(1 == getLoadingViewCount() && itemCount - 1 == position) {
            return VIEW_TYPE_LOADING
        }
        val footerViewCount = getFooterViewCount()
        if(1 == footerViewCount && (actualCount + headerViewCount + footerViewCount - 1) == position) {
            return VIEW_TYPE_FOOTER
        }
        return wrappedAdapter.getItemViewType(position)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        const val VIEW_TYPE_HEADER = 1 shl 7
        const val VIEW_TYPE_FOOTER = 1 shl 7 + 1
        const val VIEW_TYPE_LOADING = 1 shl 7 + 2
    }
}
