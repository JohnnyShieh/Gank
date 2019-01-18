package com.johnny.gank.store
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

import android.arch.lifecycle.MutableLiveData
import com.johnny.gank.action.Key
import com.johnny.gank.data.ui.GankItem
import com.johnny.rxflux.Action
import com.johnny.rxflux.Store
import javax.inject.Inject

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class TodayGankStore : Store() {

    val isSwipeRefreshing = MutableLiveData<Boolean>()

    val items = MutableLiveData<List<GankItem>>()

    @Suppress("UNCHECKED_CAST")
    override fun onAction(action: Action) {
        isSwipeRefreshing.value = false
        items.value = action.singleData as List<GankItem>
    }

    override fun onError(action: Action) {
        isSwipeRefreshing.value = false
    }
}
