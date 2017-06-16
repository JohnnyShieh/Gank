package com.johnny.gank.ui.widget

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

import android.content.Context
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.johnny.gank.R
import kotlinx.android.synthetic.main.load_more_content.view.*

/**
 * description

 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */
class LoadMoreView
    @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(STATUS_INIT, STATUS_LOADING, STATUS_FAIL, STATUS_NO_MORE)
    annotation class LoadStatus

    var status = STATUS_INIT
        set(@LoadStatus status) {
            field = status
            when (status) {
                STATUS_INIT -> visibility = View.INVISIBLE
                STATUS_LOADING -> {
                    loading_indicator.visibility = View.VISIBLE
                    load_tip.visibility = View.INVISIBLE
                    visibility = View.VISIBLE
                }
                STATUS_FAIL -> {
                    loading_indicator.visibility = View.INVISIBLE
                    load_tip.setText(R.string.load_fail)
                    load_tip.visibility = View.VISIBLE
                    visibility = View.VISIBLE
                }
                STATUS_NO_MORE -> {
                    loading_indicator.visibility = View.INVISIBLE
                    load_tip.setText(R.string.load_no_more)
                    load_tip.visibility = View.VISIBLE
                    visibility = View.VISIBLE
                }
            }
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.load_more_content, this)
    }

    companion object {
        const val STATUS_INIT = 0L
        const val STATUS_LOADING = 1L
        const val STATUS_FAIL = 2L
        const val STATUS_NO_MORE = 3L
    }
}
