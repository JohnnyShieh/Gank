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

import com.johnny.gank.R

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * The view show loading more animation.

 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */
class LoadingIndicatorView
    @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint()
    private val mIndicator = BallSpinFadeLoadingIndicator()

    init {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        mPaint.color = typedValue.data
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true

        mIndicator.setTarget(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec)
        val height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun dp2px(dpValue: Int): Int {
        return context.resources.displayMetrics.density.toInt() * dpValue
    }

    private fun measureDimension(defaultSize: Int, measureSpec: Int): Int {
        var result = defaultSize
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize)
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mIndicator.draw(canvas, mPaint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        applyAnimation()
    }

    override fun setVisibility(visibility: Int) {
        if (visibility != getVisibility()) {
            super.setVisibility(visibility)
            if (visibility == View.GONE || visibility == View.INVISIBLE) {
                mIndicator.setAnimationStatus(BaseLoadingIndicator.STATUS_END)
            } else {
                mIndicator.setAnimationStatus(BaseLoadingIndicator.STATUS_START)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (visibility == View.VISIBLE) {
            mIndicator.setAnimationStatus(BaseLoadingIndicator.STATUS_START)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mIndicator.setAnimationStatus(BaseLoadingIndicator.STATUS_CANCEL)
    }

    private fun applyAnimation() {
        mIndicator.initAnimation()
    }

    companion object {
        private const val DEFAULT_SIZE = 45  // in DP
    }
}
