package com.johnny.gank.store;
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

import com.johnny.gank.action.Key;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.rxflux.Action;
import com.johnny.rxflux.Store;

import java.util.List;

import javax.inject.Inject;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class SearchStore extends Store {

    private List<GankNormalItem> mGankList;

    @Inject
    public SearchStore() {}

    public List<GankNormalItem> getGankList() {
        return mGankList;
    }

    @Override
    protected boolean onAction(Action action) {
        mGankList = action.get(Key.QUERY_RESULT);
        return true;
    }

    @Override
    protected boolean onError(Action action, Throwable throwable) {
        return true;
    }
}
