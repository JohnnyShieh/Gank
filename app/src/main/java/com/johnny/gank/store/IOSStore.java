package com.johnny.gank.store;
/*
 * Copyright (C) 2015 Johnny Shieh Open Source Project
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

import com.johnny.gank.action.ActionType;
import com.johnny.gank.action.Key;
import com.johnny.gank.action.RxAction;
import com.johnny.gank.data.ui.GankNormalItem;
import com.johnny.gank.dispatcher.Dispatcher;

import java.util.List;

import javax.inject.Inject;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class IOSStore extends RxStore {

    public static final String ID = "IOSStore";

    private int mPage;
    private List<GankNormalItem> mGankList;

    @Inject
    public IOSStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case ActionType.GET_IOS_LIST:
                mPage = action.get(Key.PAGE);
                mGankList = action.get(Key.GANK_LIST);
                break;
            default:
                return;
        }
        postChange(new RxStoreChange(ID, action));
    }

    public int getPage() {
        return mPage;
    }

    public List<GankNormalItem> getGankList() {
        return mGankList;
    }
}
