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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.gank.R
import com.johnny.gank.action.ActionType
import com.johnny.gank.action.TodayGankActionCreator
import com.johnny.gank.data.ui.GankGirlImageItem
import com.johnny.gank.data.ui.GankNormalItem
import com.johnny.gank.di.component.TodayGankFragmentComponent
import com.johnny.gank.stat.StatName
import com.johnny.gank.store.TodayGankStore
import com.johnny.gank.ui.activity.MainActivity
import com.johnny.gank.ui.activity.PictureActivity
import com.johnny.gank.ui.activity.WebviewActivity
import com.johnny.gank.ui.adapter.GankListAdapter
import com.johnny.rxflux.Store
import com.johnny.rxflux.StoreObserver
import kotlinx.android.synthetic.main.fragment_refresh_recycler.*
import javax.inject.Inject

/**
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class TodayGankFragment : BaseFragment(),
        StoreObserver,
        SwipeRefreshLayout.OnRefreshListener {

    companion object {
        @JvmStatic
        fun newInstance() = TodayGankFragment()
    }

    private var mComponent: TodayGankFragmentComponent? = null

    var mStore: TodayGankStore? = null
        @Inject set

    var mActionCreator: TodayGankActionCreator ?= null
        @Inject set

    private var mAdapter: GankListAdapter? = null

    override var statPageName = StatName.PAGE_TODAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    private fun initInjector() {
        mComponent = (activity as MainActivity).mainActivityComponent
                .todayGankFragmentComponent()
                .build()
        mComponent!!.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val contentView = inflater.inflate(R.layout.fragment_refresh_recycler, container, false)
        refresh_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        refresh_layout.setOnRefreshListener(this)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
        mAdapter = GankListAdapter(this)
        mAdapter!!.onItemClickListener = object : GankListAdapter.OnItemClickListener {
            override fun onClickNormalItem(view: View, normalItem: GankNormalItem) {
                if (!normalItem.gank.url.isNullOrEmpty()) {
                    WebviewActivity.openUrl(mComponent!!.activity, normalItem.gank.url, normalItem.gank.desc)
                }
            }

            override fun onClickGirlItem(view: View, girlImageItem: GankGirlImageItem) {
                if (!girlImageItem.imgUrl.isNullOrEmpty()) {
                    startActivity(PictureActivity.newIntent(mComponent!!.activity, girlImageItem.imgUrl, girlImageItem.publishedAt))
                }
            }
        }
        recycler_view.adapter = mAdapter

        mStore!!.register(ActionType.GET_TODAY_GANK)
        mStore!!.setObserver(this)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_layout.post {
            refresh_layout.isRefreshing = true
            refreshData()
        }
    }

    override fun onDestroyView() {
        mStore!!.unRegister()
        super.onDestroyView()
    }

    private fun refreshData() {
        mActionCreator!!.getTodayGank()
    }

    override fun onRefresh() {
        refreshData()
    }

    override fun onChange(store: Store, actionType: String) {
        refresh_layout.isRefreshing = false
        mAdapter!!.swapData(mStore!!.items)
    }

    override fun onError(store: Store, actionType: String) {
        refresh_layout.isRefreshing = false
    }
}
