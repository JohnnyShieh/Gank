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
import com.johnny.gank.rxflux.Action;
import com.johnny.gank.rxflux.Dispatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class TodayGankActionCreator {

    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private boolean hasAction = false;

    @Inject
    public TodayGankActionCreator() {}

    public void getTodayGank() {
        final Action action = Action.type(ActionType.GET_TODAY_GANK).build();
        if(hasAction) {
            return;
        }

        hasAction = true;
        GankService.Factory.getGankService()
            .getDateHistory()
            .filter(new Predicate<DateData>() {
                @Override
                public boolean test(@NonNull DateData dateData) throws Exception {
                    return (null != dateData && null != dateData.results && dateData.results.size() > 0);
                }
            })
            .map(new Function<DateData, Calendar>() {
                @Override
                public Calendar apply(@NonNull DateData dateData) throws Exception {
                    Calendar calendar = Calendar.getInstance(Locale.CHINA);
                    try {
                        calendar.setTime(sDataFormat.parse(dateData.results.get(0)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        calendar = null;
                    }
                    return calendar;
                }
            }).filter(new Predicate<Calendar>() {
                @Override
                public boolean test(@NonNull Calendar calendar) throws Exception {
                    return null != calendar;
                }
            })
            .flatMap(new Function<Calendar, ObservableSource<DayData>>() {
                @Override
                public ObservableSource<DayData> apply(@NonNull Calendar calendar) throws Exception {
                    return GankService.Factory.getGankService()
                        .getDayGank(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                }
            })
            .map(new Function<DayData, List<GankItem>>() {
                @Override
                public List<GankItem> apply(@NonNull DayData dayData) throws Exception {
                    return getGankList(dayData);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<GankItem>>() {
                @Override
                public void accept(@NonNull List<GankItem> gankList) throws Exception {
                    hasAction = false;
                    action.getData().put(Key.DAY_GANK, gankList);
                    Dispatcher.get().postAction(action);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    hasAction = false;
                    Dispatcher.get().postError(action, throwable);
                }
            });
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
