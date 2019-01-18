package com.johnny.gank.action
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
import com.johnny.gank.core.http.GankService
import com.johnny.gank.data.ui.GankNormalItem
import com.johnny.rxflux.Action
import com.johnny.rxflux.postAction
import com.johnny.rxflux.postError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class QueryActionCreator
    @Inject constructor() {

    private var hasAction = false

    lateinit var mGankService: GankService
        @Inject set

    @SuppressLint("CheckResult")
    fun query(queryText: String) {
        if(hasAction) {
            return
        }

        hasAction = true
        mGankService.queryGank(queryText, DEFAULT_COUNT, DEFAULT_PAGE)
                .filter { it.results.isNotEmpty() }
                .map { GankNormalItem.newGankList(it.results) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ items ->
                    hasAction = false
                    postAction(ActionType.QUERY_GANK, items)
                }, { throwable ->
                    hasAction = false
                    postError(ActionType.QUERY_GANK, throwable)
                })
    }

    companion object {
        private const val DEFAULT_COUNT = 27
        private const val DEFAULT_PAGE = 1

    }
}
