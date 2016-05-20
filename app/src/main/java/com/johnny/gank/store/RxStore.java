package com.johnny.gank.store;


import com.johnny.gank.action.RxActionCreator;
import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.dispatcher.RxActionDispatch;

/**
 * This class must be extended by each store of the app in order to recieve the actions dispatched
 * by the {@link RxActionCreator}
 */
public abstract class RxStore implements RxActionDispatch {

  private final Dispatcher dispatcher;

  public RxStore(Dispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  public void register() {
    dispatcher.subscribeRxStore(this);
  }

  public void unregister() {
    dispatcher.unsubscribeRxStore(this);
  }

  protected void postChange(RxStoreChange change) {
    dispatcher.postRxStoreChange(change);
  }
}
