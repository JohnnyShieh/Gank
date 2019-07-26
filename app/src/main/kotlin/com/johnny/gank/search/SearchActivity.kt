package com.johnny.gank.search
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
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnny.gank.R
import com.johnny.gank.model.ui.GankNormalItem
import com.johnny.gank.network.GankService
import com.johnny.gank.main.WebviewActivity
import com.johnny.gank.base.BaseActivity
import com.johnny.gank.model.StatName
import com.johnny.gank.adapter.getGankTitleStr
import com.johnny.rxflux.RxFlux
import com.johnny.rxflux.Store
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import kotlinx.android.synthetic.main.recycler_item_gank.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class SearchActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun newIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }

    override val pageName = StatName.PAGE_SEARCH

    val mStore: SearchStore by viewModel()

    val mQueryActionCreator: QueryActionCreator by inject()

    private val mAdapter = QueryGankAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        initSearchView()
        initRecyclerView()
        handleIntent(intent)

        mStore.showEmptyView.observe(this, Observer {
            empty_view.visibility = View.VISIBLE
            recycler_view.visibility = View.INVISIBLE
        })
        mStore.gankList.observe(this, Observer {
            empty_view.visibility = View.INVISIBLE
            recycler_view.visibility = View.VISIBLE
            mAdapter.updateData(it)
        })
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
                WebviewActivity.openUrl(
                    this@SearchActivity,
                    normalItem.gank.url,
                    normalItem.gank.desc
                )
            }
        }
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mAdapter
    }

    private fun handleIntent(intent: Intent?) {
        search_view.requestFocus()
    }

    private fun queryGank(queryText: String) {
        mQueryActionCreator.query(mStore, queryText)
    }
}

class QueryGankAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<GankNormalItem>()

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClickNormalItem(view: View, normalItem: GankNormalItem)
    }

    fun updateData(list: List<GankNormalItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NormalViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            val item = items[position]
            holder.itemView.title.text = getGankTitleStr(
                item.gank.desc,
                item.gank.who,
                item.gank.type
            )
            holder.itemView.title.setOnClickListener { view -> onItemClickListener?.onClickNormalItem(view, item) }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class NormalViewHolder
    constructor(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_gank, parent, false)) {
        init {
            itemView.align_pic.visibility = View.GONE
        }
    }
}

private val QUERY_GANK = RxFlux.newActionType<List<GankNormalItem>>("search_page_query_gank")

class QueryActionCreator(private val gankService: GankService) {

    private var querying = false

    @SuppressLint("CheckResult")
    fun query(store: Store, queryText: String) {
        if(querying) {
            return
        }

        querying = true
        gankService.queryGank(
            queryText,
            DEFAULT_COUNT,
            DEFAULT_PAGE
        ).map { GankNormalItem.newGankList(it.results) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ items ->
                querying = false
                RxFlux.postAction(QUERY_GANK, store, items)
            }, { throwable ->
                querying = false
                RxFlux.postError(QUERY_GANK, store, throwable)
            })
    }

    companion object {
        private const val DEFAULT_COUNT = 27
        private const val DEFAULT_PAGE = 1

    }
}

class SearchStore : Store() {

    val showEmptyView = MutableLiveData<Unit>()

    val gankList = MutableLiveData<List<GankNormalItem>>()

    init {
        register(
            QUERY_GANK,
            { value ->
                if (value.isEmpty()) {
                    showEmptyView.value = Unit
                } else {
                    gankList.value = value
                }
            },
            {
                showEmptyView.value = Unit
            }
        )
    }
}
