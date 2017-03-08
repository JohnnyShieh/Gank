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
import com.johnny.gank.rxflux.Action;
import com.johnny.gank.rxflux.Dispatcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class QueryActionCreator {

    private static final int DEFAULT_COUNT = 27;
    private static final int DEFAULT_PAGE = 1;

    private boolean hasAction = false;

    @Inject
    public QueryActionCreator() {}

    public void query(String queryText) {
        final Action action = Action.type(ActionType.QUERY_GANK).build();
        if(hasAction) {
            return;
        }

        hasAction = true;
        GankService.Factory.getGankService()
            .queryGank(queryText, DEFAULT_COUNT, DEFAULT_PAGE)
            .map(new Function<GankData, List<GankNormalItem>>() {
                @Override
                public List<GankNormalItem> apply(@NonNull GankData gankData) throws Exception {
                    if(null == gankData || null == gankData.results || 0 == gankData.results.size()) {
                        return null;
                    }
                    return GankNormalItem.newGankList(gankData.results);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<GankNormalItem>>() {
                @Override
                public void accept(@NonNull List<GankNormalItem> gankNormalItems) throws Exception {
                    hasAction = false;
                    action.getData().put(Key.QUERY_RESULT, gankNormalItems);
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
}
