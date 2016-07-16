package com.johnny.gank.ui.widget;
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

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Jack on 2015/10/15.
 */
public abstract class BaseLoadingIndicator {

    public static final int STATUS_START = 1;
    public static final int STATUS_END = 2;
    public static final int STATUS_CANCEL = -1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_START, STATUS_END, STATUS_CANCEL})
    public @interface AnimStatus{}

    private WeakReference<View> mTarget;

    private List<Animator> mAnimators;


    public void setTarget(View target){
        this.mTarget=new WeakReference<>(target);
    }

    public View getTarget(){
        return mTarget!=null?mTarget.get():null;
    }


    public int getWidth(){
        return getTarget()!=null?getTarget().getWidth():0;
    }

    public int getHeight(){
        return getTarget()!=null?getTarget().getHeight():0;
    }

    public void postInvalidate(){
        if (getTarget()!=null){
            getTarget().postInvalidate();
        }
    }

    /**
     * draw indicator
     * @param canvas
     * @param paint
     */
    public abstract void draw(Canvas canvas,Paint paint);

    /**
     * create animation or animations
     */
    public abstract List<Animator> createAnimation();

    public void initAnimation(){
        mAnimators=createAnimation();
    }

    /**
     * make animation to start or end when target
     * view was be Visible or Gone or Invisible.
     * make animation to cancel when target view
     * be onDetachedFromWindow.
     * @param animStatus
     */
    public void setAnimationStatus(@AnimStatus int animStatus){
        if (mAnimators==null){
            return;
        }
        int count=mAnimators.size();
        for (int i = 0; i < count; i++) {
            Animator animator=mAnimators.get(i);
            boolean isRunning=animator.isRunning();
            switch (animStatus){
                case STATUS_START:
                    if (!isRunning){
                        animator.start();
                    }
                    break;
                case STATUS_END:
                    if (isRunning){
                        animator.end();
                    }
                    break;
                case STATUS_CANCEL:
                    if (isRunning){
                        animator.cancel();
                    }
                    break;
            }
        }
    }
}
