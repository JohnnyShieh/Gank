package com.johnny.gank.ui.widget
/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.animation.Animator
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.IntDef
import android.view.View
import java.lang.ref.WeakReference

/**
 * Created by Jack on 2015/10/15.
 */
abstract class BaseLoadingIndicator {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(STATUS_START, STATUS_END, STATUS_CANCEL)
    annotation class AnimStatus

    private var mTarget: WeakReference<View>? = null

    private var mAnimators: List<Animator>? = null


    fun setTarget(target: View) {
        mTarget = WeakReference<View>(target)
    }

    fun getTarget(): View? {
        return mTarget?.get()
    }


    fun getWidth(): Int {
        return mTarget?.get()?.width ?: 0
    }

    fun getHeight(): Int {
        return mTarget?.get()?.height ?: 0
    }

    fun postInvalidate() {
        getTarget()?.postInvalidate()
    }

    /**
     * draw indicator
     * @param canvas
     * @param paint
     */
    abstract fun draw(canvas: Canvas, paint: Paint)

    /**
     * create animation or animations
     */
    abstract fun createAnimation(): List<Animator>

    fun initAnimation() {
        mAnimators = createAnimation()
    }

    /**
     * make animation to start or end when target
     * view was be Visible or Gone or Invisible.
     * make animation to cancel when target view
     * be onDetachedFromWindow.
     * @param animStatus
     */
    fun setAnimationStatus(@AnimStatus animStatus: Long){
        if (mAnimators == null){
            return
        }
        val count = mAnimators!!.size
        for (i in 0 until count) {
            val animator = mAnimators!![i]
            val isRunning = animator.isRunning
            when (animStatus) {
                STATUS_START -> if (!isRunning) animator.start()
                STATUS_END -> if (isRunning) animator.end()
                STATUS_CANCEL -> if (isRunning) animator.cancel()
            }
        }
    }

    companion object {
        const val STATUS_START = 1L
        const val STATUS_END = 2L
        const val STATUS_CANCEL = -1L
    }
}
