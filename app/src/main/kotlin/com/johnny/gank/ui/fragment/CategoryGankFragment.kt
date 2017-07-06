package com.johnny.gank.ui.fragment
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

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.gank.R
import com.johnny.gank.data.ui.GankNormalItem
import com.johnny.gank.ui.activity.WebviewActivity
import com.johnny.gank.ui.adapter.CategoryGankAdapter
import com.johnny.gank.ui.widget.HeaderViewRecyclerAdapter
import com.johnny.gank.ui.widget.LoadMoreView
import kotlinx.android.synthetic.main.fragment_refresh_recycler.*

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
abstract class CategoryGankFragment : BaseFragment(),
        SwipeRefreshLayout.OnRefreshListener {

    protected lateinit var vLoadMore: LoadMoreView

    protected lateinit var layoutManager: LinearLayoutManager

    protected var wrappedAdapter = CategoryGankAdapter()

    protected var loadingMore = false

    protected fun createView(inflater: LayoutInflater, container: ViewGroup): View {
        val contentView = inflater.inflate(R.layout.fragment_refresh_recycler, container, false)

        vLoadMore = inflater.inflate(R.layout.load_more, recycler_view, false) as LoadMoreView
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        refresh_layout.setOnRefreshListener(this)
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)
        wrappedAdapter.onItemClickListener = object : CategoryGankAdapter.OnItemClickListener {
            override fun onClickNormalItem(view: View, normalItem: GankNormalItem) {
                WebviewActivity.openUrl(activity, normalItem.gank.url, normalItem.gank.desc)
            }
        }
        val adapter = HeaderViewRecyclerAdapter(wrappedAdapter)
        adapter.loadingView = vLoadMore
        recycler_view.adapter = adapter
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val reachBottom = layoutManager.findLastCompletelyVisibleItemPosition() >= (layoutManager.itemCount - 1)
                if(!loadingMore && reachBottom) {
                    loadingMore = true
                    loadMore()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val reachBottom = layoutManager.findLastCompletelyVisibleItemPosition() >= (layoutManager.itemCount - 1)
                if(newState == RecyclerView.SCROLL_STATE_IDLE && !loadingMore && reachBottom) {
                    loadingMore = true
                    loadMore()
                }
            }
        })
        refresh_layout.post {
            refresh_layout.isRefreshing = true
            refreshList()
        }
    }

    protected abstract fun refreshList()

    protected abstract fun loadMore()

    protected abstract fun loadDataSuccess()

    protected abstract fun loadDataFail()

    override fun onRefresh() {
        refreshList()
    }
}
