package com.johnny.gank.rxflux;
/*
 * Copyright (C) 2017 Johnny Shieh Open Source Project
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



import com.johnny.gank.rxflux.util.Logger;
import com.johnny.gank.rxflux.util.Observable;

import io.reactivex.disposables.Disposable;

/**
 * Flux store
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public abstract class Store<T extends Store.StoreChangeEvent> extends Observable<T> {

    private Disposable mDisposable;

    void setDisposable(Disposable disposable) {
        if(null != mDisposable && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = disposable;
    }

    public void unRegister() {
        Logger.logUnregisterStore(getClass().getSimpleName());
        setDisposable(null);
        deleteObservers();
    }

    public abstract void onAction(Action action);

    public abstract void onError(Action action, Throwable throwable);

    protected void postChange(T t) {
        Logger.logPostStoreChange(getClass().getSimpleName(), t);
        notifyChange(t);
    }

    protected void postError(T t) {
        Logger.logPostStoreError(getClass().getSimpleName(), t);
        notifyError(t);
    }

    public static abstract class StoreChangeEvent {}
}
