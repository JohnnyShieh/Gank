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
import com.johnny.gank.data.GankType
import com.johnny.gank.data.response.DateData
import com.johnny.gank.data.response.DayData
import com.johnny.gank.data.ui.GankGirlImageItem
import com.johnny.gank.data.ui.GankHeaderItem
import com.johnny.gank.data.ui.GankItem
import com.johnny.gank.data.ui.GankNormalItem
import com.johnny.rxflux.Action
import com.johnny.rxflux.Dispatcher

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

import javax.inject.Inject

import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers

/**
 * description

 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */
class TodayGankActionCreator @Inject
constructor() {

    private var hasAction = false

    internal var mGankService: GankService? = null
        @Inject set

    fun getTodayGank() {
        val action = Action.type(ActionType.GET_TODAY_GANK).build()
        if (hasAction) {
            return
        }

        hasAction = true
        mGankService!!.dateHistory
                .filter { null != it && null != it.results && it.results.size > 0 }
                .map { dateData ->
                    var calendar: Calendar? = Calendar.getInstance(Locale.CHINA)
                    try {
                        calendar!!.time = sDataFormat.parse(dateData.results[0])
                    } catch (e: ParseException) {
                        e.printStackTrace()
                        calendar = null
                    }

                    calendar
                }.filter { null != it }
                .flatMap { calendar ->
                    mGankService!!
                            .getDayGank(calendar!!.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
                }
                .map { getGankList(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ gankList ->
                    hasAction = false
                    action.data.put(Key.DAY_GANK, gankList)
                    Dispatcher.get().postAction(action)
                }, { throwable ->
                    hasAction = false
                    Dispatcher.get().postError(action, throwable)
                })
    }

    private fun getGankList(dayData: DayData?): List<GankItem>? {
        if (null == dayData || null == dayData.results) {
            return null
        }
        val gankList = ArrayList<GankItem>(10)
        if (null != dayData.results.welfareList && dayData.results.welfareList.size > 0) {
            gankList.add(GankGirlImageItem.newImageItem(dayData.results.welfareList[0]))
        }
        if (null != dayData.results.androidList && dayData.results.androidList.size > 0) {
            gankList.add(GankHeaderItem(GankType.ANDROID))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.androidList))
        }
        if (null != dayData.results.iosList && dayData.results.iosList.size > 0) {
            gankList.add(GankHeaderItem(GankType.IOS))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.iosList))
        }
        if (null != dayData.results.frontEndList && dayData.results.frontEndList.size > 0) {
            gankList.add(GankHeaderItem(GankType.FRONTEND))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.frontEndList))
        }
        if (null != dayData.results.extraList && dayData.results.extraList.size > 0) {
            gankList.add(GankHeaderItem(GankType.EXTRA))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.extraList))
        }
        if (null != dayData.results.casualList && dayData.results.casualList.size > 0) {
            gankList.add(GankHeaderItem(GankType.CASUAL))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.casualList))
        }
        if (null != dayData.results.appList && dayData.results.appList.size > 0) {
            gankList.add(GankHeaderItem(GankType.APP))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.appList))
        }
        if (null != dayData.results.videoList && dayData.results.videoList.size > 0) {
            gankList.add(GankHeaderItem(GankType.VIDEO))
            gankList.addAll(GankNormalItem.newGankList(dayData.results.videoList))
        }

        return gankList
    }

    companion object {
        private val sDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    }
}
