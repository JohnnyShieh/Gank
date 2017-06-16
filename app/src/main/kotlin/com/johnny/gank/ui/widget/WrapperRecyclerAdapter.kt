package com.johnny.gank.ui.widget
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

import android.support.v7.widget.RecyclerView;

/**
 * Recycler adapter that wraps another recycler adapter. The wrapped adapter can be retrieved
 * by calling {@link #getWrappedAdapter()}.
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
abstract class WrapperRecyclerAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val wrappedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> = adapter

    init {
        wrappedAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                notifyItemRangeChanged(positionStart, itemCount, payload)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }
        })
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        wrappedAdapter.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long {
        return wrappedAdapter.getItemId(position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        wrappedAdapter.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder?): Boolean {
        return wrappedAdapter.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        wrappedAdapter.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
        wrappedAdapter.onViewDetachedFromWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        wrappedAdapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        wrappedAdapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver?) {
        wrappedAdapter.registerAdapterDataObserver(observer)
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver?) {
        wrappedAdapter.unregisterAdapterDataObserver(observer)
    }

}
