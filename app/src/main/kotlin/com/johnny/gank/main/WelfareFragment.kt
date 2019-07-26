package com.johnny.gank.main
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.johnny.gank.R
import com.johnny.gank.base.BaseFragment
import com.johnny.gank.model.GankType
import com.johnny.gank.model.ui.GankNormalItem
import com.johnny.gank.model.StatName
import com.johnny.gank.adapter.WelfareAdapter
import com.johnny.gank.widget.HeaderViewRecyclerAdapter
import com.johnny.gank.widget.LoadMoreView
import kotlinx.android.synthetic.main.fragment_refresh_recycler.*
import kotlinx.android.synthetic.main.recycler_item_welfare.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Show all welfare pic.
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class WelfareFragment : BaseFragment(),
        SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val TAG = "WelfareFragment"
    }

    private lateinit var vLoadMore: LoadMoreView

    private lateinit var mLayoutManager: GridLayoutManager

    private lateinit var mAdapter: WelfareAdapter

    val mStore: GankStore by viewModel()

    val mActionCreator: GankActionCreator by inject()

    private var loadingMore = false

    override var statPageName = StatName.PAGE_WELFARE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val contentView = inflater.inflate(R.layout.fragment_refresh_recycler, container, false)
        vLoadMore = inflater.inflate(R.layout.load_more, recycler_view, false) as LoadMoreView
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        refresh_layout.setOnRefreshListener(this)
        mLayoutManager = GridLayoutManager(activity, 2)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.setHasFixedSize(true)
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition() >= mLayoutManager.itemCount - 1
                if(!loadingMore && reachBottom) {
                    loadingMore = true
                    loadMore()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition() >= mLayoutManager.itemCount - 1
                if(newState == RecyclerView.SCROLL_STATE_IDLE && !loadingMore && reachBottom) {
                    loadingMore = true
                    loadMore()
                }
            }
        })

        mAdapter = WelfareAdapter(this)
        mAdapter.onItemClickListener = object : WelfareAdapter.OnItemClickListener {
            override fun onClickItem(view: View, item: GankNormalItem) {
                startActivity(PictureActivity.newIntent(activity!!, item.page, item.gank._id, item.gank.url))
            }
        }
        val adapter = HeaderViewRecyclerAdapter(mAdapter)
        adapter.loadingView = vLoadMore
        recycler_view.adapter = adapter
        refresh_layout.post {
            refresh_layout.isRefreshing = true
            refreshList()
        }

        mStore.isSwipeRefreshing.observe(this, Observer { refresh_layout.isRefreshing = false })
        mStore.isLoadingMore.observe(this, Observer {
            loadingMore = false
            vLoadMore.status = LoadMoreView.STATUS_INIT
        })
        mStore.gankList.observe(this, Observer { mAdapter.updateData(mStore.page, it) })
    }

    private fun refreshList() {
        mActionCreator.getGankList(mStore, GankType.WELFARE, 1)
    }

    private fun loadMore() {
        vLoadMore.status = LoadMoreView.STATUS_LOADING
        mActionCreator.getGankList(mStore, GankType.WELFARE, mAdapter.curPage + 1)
    }

    override fun onRefresh() {
        refreshList()
    }
}
