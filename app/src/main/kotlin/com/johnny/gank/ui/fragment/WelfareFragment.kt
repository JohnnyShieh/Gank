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
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.gank.R
import com.johnny.gank.action.ActionType
import com.johnny.gank.action.WelfareActionCreator
import com.johnny.gank.data.ui.GankNormalItem
import com.johnny.gank.di.component.WelfareFragmentComponent
import com.johnny.gank.stat.StatName
import com.johnny.gank.store.NormalGankStore
import com.johnny.gank.ui.activity.MainActivity
import com.johnny.gank.ui.activity.PictureActivity
import com.johnny.gank.ui.adapter.WelfareAdapter
import com.johnny.gank.ui.widget.HeaderViewRecyclerAdapter
import com.johnny.gank.ui.widget.LoadMoreView
import com.johnny.rxflux.StoreObserver
import kotlinx.android.synthetic.main.fragment_refresh_recycler.*
import javax.inject.Inject

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
        @JvmStatic
        fun newInstance() = WelfareFragment()
    }

    private lateinit var vLoadMore: LoadMoreView

    private lateinit var mLayoutManager: GridLayoutManager

    private lateinit var mComponent: WelfareFragmentComponent

    private lateinit var mAdapter: WelfareAdapter

    lateinit var mStore: NormalGankStore
        @Inject set

    lateinit var mActionCreator: WelfareActionCreator
        @Inject set

    private var loadingMore = false

    override var statPageName = StatName.PAGE_WELFARE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    private fun initInjector() {
        mComponent = (activity as MainActivity).component
                .welfareFragmentComponent()
                .build()
        mComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val contentView = inflater.inflate(R.layout.fragment_refresh_recycler, container, false)
        vLoadMore = inflater.inflate(R.layout.load_more, recycler_view, false) as LoadMoreView

        mStore.register(ActionType.GET_WELFARE_LIST)
        mStore.setObserver(object : StoreObserver {
            override fun onChange(actionType: String) {
                loadDataSuccess()
            }

            override fun onError(actionType: String) {
                loadDataFail()
            }

        })
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
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val reachBottom = mLayoutManager.findLastCompletelyVisibleItemPosition() >= mLayoutManager.itemCount - 1
                if(!loadingMore && reachBottom) {
                    loadingMore = true
                    loadMore()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
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
                startActivity(PictureActivity.newIntent(mComponent.activity, item.page, item.gank._id))
            }
        }
        val adapter = HeaderViewRecyclerAdapter(mAdapter)
        adapter.loadingView = vLoadMore
        recycler_view.adapter = adapter
        refresh_layout.post {
            refresh_layout.isRefreshing = true
            refreshList()
        }
    }

    override fun onDestroyView() {
        mStore.unRegister()
        super.onDestroyView()
    }

    private fun refreshList() {
        mActionCreator.getWelfareList(1)
    }

    private fun loadMore() {
        vLoadMore.status = LoadMoreView.STATUS_LOADING
        mActionCreator.getWelfareList(mAdapter.curPage + 1)
    }

    override fun onRefresh() {
        refreshList()
    }

    private fun loadDataSuccess() {
        if (1 == mStore.page) {
            refresh_layout.isRefreshing = false
        }
        mAdapter.updateData(mStore.page, mStore.gankList)
        loadingMore = false
        vLoadMore.status = LoadMoreView.STATUS_INIT
    }

    private fun loadDataFail() {
        refresh_layout.isRefreshing = false
        loadingMore = false
        vLoadMore.status = LoadMoreView.STATUS_FAIL
    }
}
