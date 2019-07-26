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

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.johnny.gank.R
import com.johnny.gank.base.BaseFragment
import com.johnny.gank.network.GankService
import com.johnny.gank.model.GankType
import com.johnny.gank.model.response.DayData
import com.johnny.gank.model.ui.GankGirlImageItem
import com.johnny.gank.model.ui.GankHeaderItem
import com.johnny.gank.model.ui.GankItem
import com.johnny.gank.model.ui.GankNormalItem
import com.johnny.gank.model.StatName
import com.johnny.gank.adapter.GankListAdapter
import com.johnny.rxflux.RxFlux
import com.johnny.rxflux.Store
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_refresh_recycler.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class TodayGankFragment : BaseFragment(),
    SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val TAG = "TodayGankFragment"
    }

    val mStore: TodayGankStore by viewModel()

    val mActionCreator: TodayGankActionCreator by inject()

    private lateinit var mAdapter: GankListAdapter

    override var statPageName = StatName.PAGE_TODAY

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_refresh_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_layout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent
        )
        refresh_layout.setOnRefreshListener(this)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
        mAdapter = GankListAdapter(this)
        mAdapter.onItemClickListener = object : GankListAdapter.OnItemClickListener {
            override fun onClickNormalItem(view: View, normalItem: GankNormalItem) {
                if (!normalItem.gank.url.isEmpty()) {
                    WebviewActivity.openUrl(activity!!, normalItem.gank.url, normalItem.gank.desc)
                }
            }

            override fun onClickGirlItem(view: View, girlImageItem: GankGirlImageItem) {
                if (girlImageItem.imgUrl.isNotEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.transitionName = girlImageItem.imgUrl
                    }
                    startActivity(
                        PictureActivity.newIntent(
                            activity!!,
                            girlImageItem.imgUrl,
                            girlImageItem.publishedAt
                        )
                    )
                }
            }
        }
        recycler_view.adapter = mAdapter
        refresh_layout.post {
            refresh_layout.isRefreshing = true
            refreshData()
        }

        mStore.refreshFinish.observe(this, Observer {
            refresh_layout.isRefreshing = false
        })
        mStore.items.observe(this, Observer {
            mAdapter.swapData(it)
        })
    }

    private fun refreshData() {
        mActionCreator.getTodayGank(mStore)
    }

    override fun onRefresh() {
        refreshData()
    }
}

private val GET_TODAY_GANK = RxFlux.newActionType<List<GankItem>>("today_gank_page_get_today_gank")

class TodayGankActionCreator(private val gankService: GankService) {

    private var requesting = false

    @SuppressLint("CheckResult")
    fun getTodayGank(store: Store) {
        if (requesting) {
            return
        }
        requesting = true
        gankService.getTodayGank()
            .map { getGankList(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ gankList ->
                requesting = false
                RxFlux.postAction(GET_TODAY_GANK, store, gankList)
            }, { throwable ->
                requesting = false
                RxFlux.postAction(GET_TODAY_GANK, store, throwable)
            })
    }

    private fun getGankList(dayData: DayData?): List<GankItem> {
        if (null == dayData) {
            return arrayListOf()
        }
        val gankList = ArrayList<GankItem>(10)
        if (dayData.results.welfareList.isNotEmpty()) {
            gankList.add(GankGirlImageItem.newImageItem(dayData.results.welfareList[0]))
        }
        if (dayData.results.androidList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.ANDROID))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.androidList))

        }
        if (dayData.results.iosList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.IOS))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.iosList))
        }
        if (dayData.results.frontEndList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.FRONTEND))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.frontEndList))
        }
        if (dayData.results.extraList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.EXTRA))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.extraList))
        }
        if (dayData.results.casualList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.CASUAL))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.casualList))
        }
        if (dayData.results.appList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.APP))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.appList))
        }
        if (dayData.results.videoList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.VIDEO))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.videoList))
        }
        return gankList
    }
}

class TodayGankStore : Store() {

    val refreshFinish = MutableLiveData<Unit>()

    val items = MutableLiveData<List<GankItem>>()

    init {
        register(
            GET_TODAY_GANK,
            { value ->
                refreshFinish.value = Unit
                items.value = value
            },
            {
                refreshFinish.value = Unit
            }
        )
    }
}
