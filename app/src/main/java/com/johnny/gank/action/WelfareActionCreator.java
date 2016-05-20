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
import com.johnny.gank.data.response.GankData;
import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.util.SubscriptionManager;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class WelfareActionCreator extends RxActionCreator {

    private static final int WELFARE_PAGE_COUNT = 10;

    @Inject
    public WelfareActionCreator(Dispatcher dispatcher,
        SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    public void getWelfareList(final int page) {
        final RxAction rxAction = newRxAction(ActionType.GET_WELFARE_LIST);
        if(hasRxAction(rxAction)) {
            return;
        }

        addRxAction(rxAction, GankService.Factory.getGankService()
            .getGank(GankType.WELFARE, WELFARE_PAGE_COUNT, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<GankData>() {
                @Override
                public void call(GankData gankData) {
                    RxAction action = RxAction.type(ActionType.GET_WELFARE_LIST)
                        .bundle(Key.WELFARE, gankData)
                        .bundle(Key.PAGE, page)
                        .build();
                    postRxAction(action);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    postError(rxAction, throwable);
                }
            }));
    }

}
