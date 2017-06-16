package com.johnny.gank.ui.widget

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

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View

/**
 * description

 * @author Johnny Shieh
 * *
 * @version 1.0
 */
class RatioImageView
    @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

     var ratio = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (ratio > 0) {
            var width = View.MeasureSpec.getSize(widthMeasureSpec)
            var height = View.MeasureSpec.getSize(heightMeasureSpec)

            if (width > 0) {
                height = (width.toFloat() / ratio).toInt()
            } else if (height > 0) {
                width = (height.toFloat() * ratio).toInt()
            }

            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
