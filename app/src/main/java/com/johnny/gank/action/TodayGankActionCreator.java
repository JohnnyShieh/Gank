package com.johnny.gank.action;
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

import com.johnny.gank.core.http.GankService;
import com.johnny.gank.data.GankType;
import com.johnny.gank.data.response.DateData;
import com.johnny.gank.data.response.DayData;
import com.johnny.gank.data.ui.GankGirlImageItem;
import com.johnny.gank.data.ui.GankHeaderItem;
import com.johnny.gank.data.ui.GankItem;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.util.SubscriptionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class TodayGankActionCreator extends RxActionCreator {

    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    @Inject
    public TodayGankActionCreator(Dispatcher dispatcher,
        SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    public void getTodayGank() {
        final RxAction rxAction = newRxAction(ActionType.GET_TODAY_GANK);
        if(hasRxAction(rxAction)) {
            return;
        }

        addRxAction(rxAction, GankService.Factory.getGankService()
            .getDateHistory()
            .filter(new Func1<DateData, Boolean>() {
                @Override
                public Boolean call(DateData dateData) {
                    return (null != dateData && null != dateData.results && dateData.results.size() > 0);
                }
            })
            .map(new Func1<DateData, Calendar>() {
                @Override
                public Calendar call(DateData dateData) {
                    Calendar calendar = Calendar.getInstance(Locale.CHINA);
                    try {
                        calendar.setTime(sDataFormat.parse(dateData.results.get(0)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        calendar = null;
                    }
                    return calendar;
                }
            })
            .filter(new Func1<Calendar, Boolean>() {
                @Override
                public Boolean call(Calendar calendar) {
                    return null != calendar;
                }
            })
            .flatMap(new Func1<Calendar, Observable<DayData>>() {
                @Override
                public Observable<DayData> call(Calendar calendar) {
                    return GankService.Factory.getGankService()
                        .getDayGank(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                }
            })
            .map(new Func1<DayData, List<GankItem>>() {
                @Override
                public List<GankItem> call(DayData dayData) {
                    return getGankList(dayData);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<GankItem>>() {
                @Override
                public void call(List<GankItem> gankList) {
                    rxAction.getData().put(Key.DAY_GANK, gankList);
                    postRxAction(rxAction);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    postError(rxAction, throwable);
                }
            }));
    }

    private List<GankItem> getGankList(DayData dayData) {
        if(null == dayData || null == dayData.results) {
            return null;
        }
        List<GankItem> gankList = new ArrayList<>(10);
        if(null != dayData.results.welfareList && dayData.results.welfareList.size() > 0) {
            gankList.add(GankGirlImageItem.newImageItem(dayData.results.welfareList.get(0)));
        }
        if(null != dayData.results.androidList && dayData.results.androidList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.ANDROID));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.androidList));
        }
        if(null != dayData.results.iosList && dayData.results.iosList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.IOS));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.iosList));
        }
        if(null != dayData.results.frontEndList && dayData.results.frontEndList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.FRONTEND));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.frontEndList));
        }
        if(null != dayData.results.extraList && dayData.results.extraList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.EXTRA));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.extraList));
        }
        if(null != dayData.results.casualList && dayData.results.casualList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.CASUAL));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.casualList));
        }
        if(null != dayData.results.appList && dayData.results.appList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.APP));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.appList));
        }
        if(null != dayData.results.videoList && dayData.results.videoList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.VIDEO));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.videoList));
        }

        return gankList;
    }
}
