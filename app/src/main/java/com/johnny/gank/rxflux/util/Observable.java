package com.johnny.gank.rxflux.util;
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

import java.util.Vector;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class Observable<T> {

    private Vector<Observer<T>> mObservers = new Vector<>(1);

    public synchronized void addObserver(Observer<T> observer) {
        if(null == observer) {
            throw new NullPointerException();
        }else {
            if(!mObservers.contains(observer)) {
                mObservers.addElement(observer);
            }
        }
    }

    public synchronized void deleteObserver(Observer<T> observer) {
        mObservers.removeElement(observer);
    }

    public void notifyChange(T t) {
        Object[] array = mObservers.toArray();
        for(int i = array.length - 1; i >= 0; i --) {
            ((Observer<T>)array[i]).onChange(t);
        }
    }

    public void notifyError(T t) {
        Object[] array = mObservers.toArray();
        for(int i = array.length - 1; i >= 0; i --) {
            ((Observer<T>)array[i]).onError(t);
        }
    }

    public synchronized void deleteObservers() {
        mObservers.removeAllElements();
    }
}
