package com.johnny.gank.action;
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

import com.johnny.gank.data.GankType;
import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.util.SubscriptionManager;

import javax.inject.Inject;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class VideoActionCreator extends CategoryGankActionCreator {

    @Inject
    public VideoActionCreator(Dispatcher dispatcher,
        SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    @Override
    protected String getActionId() {
        return ActionType.GET_VIDEO_LIST;
    }

    public void getVideoList(final int page) {
        getGankList(GankType.VIDEO, page);
    }
}
