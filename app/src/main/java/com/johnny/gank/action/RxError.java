package com.johnny.gank.action;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

/**
 * Special RxAction used for Errors, use the RxError.newRxError(action, throwable) to create a
 * new instance
 */
public class RxError extends RxAction {

  public static final String ERROR_TYPE = "RxError_Type";
  public static final String ERROR_ACTION = "RxError_Action";
  public static final String ERROR_THROWABLE = "RxError_Throwable";

  private RxError(String type, ArrayMap<String, Object> data) {
    super(type, data);
  }

  public static RxError newRxError(@NonNull RxAction action, Throwable throwable) {
    ArrayMap<String, Object> data = new ArrayMap<>();
    data.put(ERROR_ACTION, action);
    data.put(ERROR_THROWABLE, throwable);
    return new RxError(ERROR_TYPE, data);
  }

  public RxAction getAction() {
    return (RxAction) getData().get(ERROR_ACTION);
  }

  public Throwable getThrowable() {
    return (Throwable) getData().get(ERROR_THROWABLE);
  }
}
