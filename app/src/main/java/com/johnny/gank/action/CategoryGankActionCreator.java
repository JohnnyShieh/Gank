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
import com.johnny.rxflux.Action;
import com.johnny.rxflux.Dispatcher;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * The Action Creator used to pull a category gank data.
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
abstract class CategoryGankActionCreator{

    private static final int DEFAULT_PAGE_COUNT = 17;

    private boolean hasAction = false;

    protected abstract String getActionId();

    protected int getPageCount() {
        return DEFAULT_PAGE_COUNT;
    }

    protected void getGankList(final String category, final int page) {
        final Action action = Action.type(getActionId()).build();
        if(hasAction) {
            return;
        }

        hasAction = true;
        GankService.Factory.getGankService()
            .getGank(category, getPageCount(), page)
            .map(new Function<GankData, List<GankNormalItem>>() {
                @Override
                public List<GankNormalItem> apply(@NonNull GankData gankData) throws Exception {
                    if(null == gankData || null == gankData.results || 0 == gankData.results.size()) {
                        return null;
                    }
                    return GankNormalItem.newGankList(gankData.results, page);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<GankNormalItem>>() {
                @Override
                public void accept(@NonNull List<GankNormalItem> gankNormalItems) throws Exception {
                    hasAction = false;
                    action.getData().put(Key.GANK_LIST, gankNormalItems);
                    action.getData().put(Key.PAGE, page);
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
