package com.johnny.gank.ui.activity
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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import com.johnny.gank.R
import com.johnny.gank.action.ActionType
import com.johnny.gank.action.QueryActionCreator
import com.johnny.gank.data.ui.GankNormalItem
import com.johnny.gank.store.SearchStore
import com.johnny.gank.ui.adapter.QueryGankAdapter
import com.johnny.rxflux.Store
import com.johnny.rxflux.StoreObserver
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import javax.inject.Inject

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class SearchActivity : BaseActivity(),
        StoreObserver {

    companion object {
        @JvmStatic
        fun newIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }

    lateinit var mStore: SearchStore
        @Inject set

    lateinit var mQueryActionCreator: QueryActionCreator
        @Inject set

    private val mAdapter = QueryGankAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        initSearchView()
        initRecyclerView()
        handleIntent(intent)
        initInjector()

        mStore.setObserver(this)
        mStore.register(ActionType.QUERY_GANK)
    }

    private fun initInjector() {
        getAppComponent()
            .searchActivityComponent()
            .activity(this)
            .build()
            .inject(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun initSearchView() {
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                queryGank(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun initRecyclerView() {
        mAdapter.onItemClickListener = object : QueryGankAdapter.OnItemClickListener {
            override fun onClickNormalItem(view: View, normalItem: GankNormalItem) {
                WebviewActivity.openUrl(this@SearchActivity, normalItem.gank.url, normalItem.gank.desc)
            }
        }
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mAdapter
    }

    private fun handleIntent(intent: Intent?) {
        search_view.requestFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        mStore.unRegister()
    }

    private fun queryGank(queryText: String) {
        mQueryActionCreator.query(queryText)
    }

    override fun onChange(store: Store?, actionType: String?) {
        mAdapter.updateData(mStore.gankList)
        empty_view.visibility = if (mStore.gankList.isEmpty()) View.VISIBLE else View.INVISIBLE
    }

    override fun onError(store: Store?, actionType: String?) {
        mAdapter.clearData()
        empty_view.visibility = View.VISIBLE
    }
}
