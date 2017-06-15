package com.johnny.gank.di.component;
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

import com.johnny.gank.di.ActivityScope;
import com.johnny.gank.ui.activity.PictureActivity;

import android.app.Activity;

import dagger.BindsInstance;
import dagger.Subcomponent;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
@ActivityScope
@Subcomponent
public interface PictureActivityComponent{

    void inject(PictureActivity pictureActivity);

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        Builder activity(Activity activity);

        PictureActivityComponent build();
    }

}
