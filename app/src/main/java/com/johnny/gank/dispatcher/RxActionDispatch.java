package com.johnny.gank.dispatcher;

import com.johnny.gank.action.RxAction;

/**
 * This interface must be implemented by the store
 */
public interface RxActionDispatch {

  void onRxAction(RxAction action);
}
