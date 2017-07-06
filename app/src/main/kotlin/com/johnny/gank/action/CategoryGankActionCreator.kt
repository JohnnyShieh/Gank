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

import com.johnny.gank.core.http.GankService
import com.johnny.gank.data.ui.GankNormalItem
import com.johnny.rxflux.Action
import com.johnny.rxflux.RxFlux
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * The Action Creator used to pull a category gank data.

 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */
abstract class CategoryGankActionCreator {

    private var hasAction = false

    lateinit var mGankService: GankService
        @Inject set

    protected abstract val actionId: String

    protected val pageCount: Int
        get() = DEFAULT_PAGE_COUNT

    protected fun getGankList(category: String, page: Int) {
        val action = Action.type(actionId).build()
        if (hasAction) {
            return
        }

        hasAction = true
        mGankService.getGank(category, pageCount, page)
                .filter { it.results.isNotEmpty() }
                .map { GankNormalItem.newGankList(it.results, page) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ gankNormalItems ->
                    hasAction = false
                    action.data.put(Key.GANK_LIST, gankNormalItems)
                    action.data.put(Key.PAGE, page)
                    RxFlux.postAction(action)
                }) { throwable ->
                    hasAction = false
                    RxFlux.postError(action, throwable)
                }
    }

    companion object {
        private val DEFAULT_PAGE_COUNT = 17
    }
}
