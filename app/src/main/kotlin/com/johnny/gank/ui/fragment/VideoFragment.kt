package com.johnny.gank.ui.fragment
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

import com.johnny.gank.action.ActionType
import com.johnny.gank.action.VideoActionCreator
import com.johnny.gank.stat.StatName
import com.johnny.gank.ui.widget.LoadMoreView
import javax.inject.Inject

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class VideoFragment : CategoryGankFragment() {

    companion object {
        const val TAG = "VideoFragment"
        @JvmStatic
        fun newInstance() = VideoFragment()
    }

    lateinit var mActionCreator: VideoActionCreator
        @Inject set

    override val statPageName = StatName.PAGE_VIDEO

    override val actionType = ActionType.GET_VIDEO_LIST

    override fun refreshList() {
        mActionCreator.getVideoList(1)
    }

    override fun loadMore() {
        vLoadMore.status = LoadMoreView.STATUS_LOADING
        mActionCreator.getVideoList(wrappedAdapter.curPage + 1)
    }

}
