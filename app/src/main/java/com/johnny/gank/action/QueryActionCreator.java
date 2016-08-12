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
import com.johnny.gank.data.response.GankData;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.util.SubscriptionManager;

import java.util.List;

import javax.inject.Inject;

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
public class QueryActionCreator extends RxActionCreator {

    private static final int DEFAULT_COUNT = 27;
    private static final int DEFAULT_PAGE = 1;

    @Inject
    public QueryActionCreator(Dispatcher dispatcher,
        SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    public void query(String queryText) {
        final RxAction rxAction = newRxAction(ActionType.QUERY_GANK);
        if(hasRxAction(rxAction)) {
            return;
        }

        addRxAction(rxAction, GankService.Factory.getGankService()
            .queryGank(queryText, DEFAULT_COUNT, DEFAULT_PAGE)
            .map(new Func1<GankData, List<GankNormalItem>>() {
                @Override
                public List<GankNormalItem> call(GankData gankData) {
                    if(null == gankData || null == gankData.results || 0 == gankData.results.size()) {
                        return null;
                    }
                    return GankNormalItem.newGankList(gankData.results);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<GankNormalItem>>() {
                @Override
                public void call(List<GankNormalItem> items) {
                    rxAction.getData().put(Key.QUERY_RESULT, items);
                    postRxAction(rxAction);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    postError(rxAction, throwable);
                }
            })
        );
    }
}
