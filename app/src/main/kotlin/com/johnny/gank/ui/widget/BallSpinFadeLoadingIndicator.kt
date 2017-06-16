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

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
class BallSpinFadeLoadingIndicator : BaseLoadingIndicator() {

    private val scaleFloats = arrayOf(
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE)

    private val alphas = arrayListOf(
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA)


    override fun draw(canvas: Canvas, paint: Paint) {
        val radius = getWidth() / 10
        for (i in 1..7) {
            canvas.save()
            val point = circleAt(getWidth(), getHeight(), (getWidth() / 2 - radius).toFloat(), i * (Math.PI / 4))
            canvas.translate(point.x, point.y)
            canvas.scale(scaleFloats[i], scaleFloats[i])
            paint.alpha = alphas[i]
            canvas.drawCircle(0f, 0f, radius.toFloat(), paint)
            canvas.restore()
        }
    }

    /**
     * 圆O的圆心为(a,b),半径为R,点A与到X轴的为角α.
     *则点A的坐标为(a+R*cosα,b+R*sinα)
     */
    private fun circleAt(width: Int, height: Int, radius: Float, angle: Double): Point{
        val x = (width / 2 + radius * (Math.cos(angle))).toFloat()
        val y = (height / 2 + radius * (Math.sin(angle))).toFloat()
        return Point(x, y)
    }

    override fun createAnimation(): List<Animator> {
        val animators = mutableListOf<Animator>()
        val delays = arrayOf(0, 120, 240, 360, 480, 600, 720, 780, 840)
        for (i in 1..7) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.4f, 1f)
            scaleAnim.duration = 100
            scaleAnim.repeatCount = -1
            scaleAnim.startDelay = delays[i].toLong()
            scaleAnim.addUpdateListener { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            scaleAnim.start()

            val alphaAnim = ValueAnimator.ofInt(255, 77, 255)
            alphaAnim.duration = 1000
            alphaAnim.repeatCount = -1
            alphaAnim.startDelay = delays[i].toLong()
            alphaAnim.addUpdateListener { animation ->
                alphas[i] = animation.animatedValue as Int
            }
            alphaAnim.start()
            animators.add(scaleAnim)
            animators.add(alphaAnim)
        }
        return animators
    }

    data class Point(val x: Float, val y: Float)

    companion object {
        const val SCALE = 1f
        const val ALPHA = 255
    }

}
