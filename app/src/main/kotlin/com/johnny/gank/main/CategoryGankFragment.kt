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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.johnny.gank.R
import com.johnny.gank.base.BaseFragment
import com.johnny.gank.model.ui.GankNormalItem
import com.johnny.gank.model.GankType
import com.johnny.gank.model.StatName
import com.johnny.gank.adapter.CategoryGankAdapter
import com.johnny.gank.widget.HeaderViewRecyclerAdapter
import com.johnny.gank.widget.LoadMoreView
import kotlinx.android.synthetic.main.fragment_refresh_recycler.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

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

    val mActionCreator: GankActionCreator by inject()

    val mStore: GankStore by viewModel()

    abstract val category: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_refresh_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        refresh_layout.setOnRefreshListener(this)
        layoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false
        )
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)
        wrappedAdapter.onItemClickListener = object : CategoryGankAdapter.OnItemClickListener {
            override fun onClickNormalItem(view: View, normalItem: GankNormalItem) {
                WebviewActivity.openUrl(activity!!, normalItem.gank.url, normalItem.gank.desc)
            }
        }
        vLoadMore = layoutInflater.inflate(R.layout.load_more, recycler_view, false) as LoadMoreView
        val adapter = HeaderViewRecyclerAdapter(wrappedAdapter)
        adapter.loadingView = vLoadMore
        recycler_view.adapter = adapter
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val reachBottom = (wrappedAdapter.itemCount > 0) && layoutManager.findLastCompletelyVisibleItemPosition() >= (layoutManager.itemCount - 1)
                if(!loadingMore && reachBottom) {
                    loadingMore = true
                    loadMore()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val reachBottom = (wrappedAdapter.itemCount > 0) && layoutManager.findLastCompletelyVisibleItemPosition() >= (layoutManager.itemCount - 1)
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

        mStore.isSwipeRefreshing.observe(this, Observer {
            refresh_layout.isRefreshing = false
        })
        mStore.isLoadingMore.observe(this, Observer {
            loadingMore = false
            vLoadMore.status = LoadMoreView.STATUS_INIT
        })
        mStore.gankList.observe(this, Observer {
            wrappedAdapter.updateData(mStore.page, it)
        })
    }

    protected fun refreshList() = mActionCreator.getGankList(mStore, category, 1)

    protected fun loadMore() {
        vLoadMore.status = LoadMoreView.STATUS_LOADING
        mActionCreator.getGankList(mStore, category, mStore.page + 1)
    }

    override fun onRefresh() {
        refreshList()
    }
}

class AndroidFragment : CategoryGankFragment() {
    companion object {
        const val TAG = "AndroidFragment"
    }

    override val statPageName = StatName.PAGE_ANDROID

    override val category = GankType.ANDROID
}

class FrontEndFragment : CategoryGankFragment() {
    companion object {
        const val TAG = "FrontEndFragment"
    }

    override val statPageName = StatName.PAGE_FRONTEND

    override val category = GankType.FRONTEND
}

class IOSFragment : CategoryGankFragment() {
    companion object {
        const val TAG = "IOSFragment"
    }

    override val statPageName = StatName.PAGE_IOS

    override val category = GankType.IOS
}

class VideoFragment : CategoryGankFragment() {
    companion object {
        const val TAG = "VideoFragment"
    }

    override val statPageName = StatName.PAGE_VIDEO

    override val category = GankType.VIDEO
}