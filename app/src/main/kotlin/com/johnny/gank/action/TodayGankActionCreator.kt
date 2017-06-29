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

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * description

 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */
class TodayGankActionCreator
    @Inject constructor() {

    private var hasAction = false

    lateinit internal var mGankService: GankService
        @Inject set

    fun getTodayGank() {
        val action = Action.type(ActionType.GET_TODAY_GANK).build()
        if (hasAction) {
            return
        }

        hasAction = true
        mGankService.getDateHistory()
                .filter { null != it && it.results.isNotEmpty() }
                .map { (results) ->
                    var calendar: Calendar? = Calendar.getInstance(Locale.CHINA)
                    try {
                        calendar!!.time = sDataFormat.parse(results[0])
                    } catch (e: ParseException) {
                        e.printStackTrace()
                        calendar = null
                    }

                    calendar
                }.filter { null != it }
                .flatMap { calendar ->
                    mGankService
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
        if (null == dayData) {
            return null
        }
        val gankList = ArrayList<GankItem>(10)
        if (dayData.results.welfareList.size > 0) {
            gankList.add(GankGirlImageItem.newImageItem(dayData.results.welfareList[0]))
        }
        if (dayData.results.androidList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.ANDROID))
            GankNormalItem.newGankList(dayData.results.androidList)?.let { gankList.addAll(it) }
        }
        if (dayData.results.iosList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.IOS))
            GankNormalItem.newGankList(dayData.results.iosList)?.let { gankList.addAll(it) }
        }
        if (dayData.results.frontEndList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.FRONTEND))
            GankNormalItem.newGankList(dayData.results.frontEndList)?.let { gankList.addAll(it) }
        }
        if (dayData.results.extraList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.EXTRA))
            GankNormalItem.newGankList(dayData.results.extraList)?.let { gankList.addAll(it) }
        }
        if (dayData.results.casualList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.CASUAL))
            GankNormalItem.newGankList(dayData.results.casualList)?.let { gankList.addAll(it) }
        }
        if (dayData.results.appList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.APP))
            GankNormalItem.newGankList(dayData.results.appList)?.let { gankList.addAll(it) }
        }
        if (dayData.results.videoList.isNotEmpty()) {
            gankList.add(GankHeaderItem(GankType.VIDEO))
            GankNormalItem.newGankList(dayData.results.videoList)?.let { gankList.addAll(it) }
        }
        return gankList
    }

    companion object {
        private val sDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    }
}
