package com.johnny.gank.dispatcher;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Rx version of an EventBus
 */
public class RxBus {

  private static RxBus instance;

  private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

  private RxBus() {
  }

  public synchronized static RxBus getInstance() {
    if (instance == null) {
      instance = new RxBus();
    }
    return instance;
  }

  public void send(Object o) {
    bus.onNext(o);
  }

  public Observable<Object> get() {
    return bus;
  }

  public boolean hasObservers() {
    return bus.hasObservers();
  }
}
