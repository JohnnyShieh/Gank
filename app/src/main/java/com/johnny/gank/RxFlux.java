package com.johnny.gank;


import com.johnny.gank.dispatcher.Dispatcher;
import com.johnny.gank.dispatcher.RxBus;
import com.johnny.gank.util.LogLevel;
import com.johnny.gank.util.SubscriptionManager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by marcel on 09/09/15.
 * Main class, the init method of this class must be called onCreate of the Application and must
 * be called just once. This class will automatically track the lifecycle of the application and
 * unregister all the remaining subscriptions for each activity.
 */
public class RxFlux implements Application.ActivityLifecycleCallbacks {

  public static String TAG = "RxFlux";
  public static LogLevel LOG_LEVEL = LogLevel.NONE;

  private static RxFlux instance;
  private final RxBus rxBus;
  private final Dispatcher dispatcher;
  private final SubscriptionManager subscriptionManager;
  private int activityCounter;

  private RxFlux(Application application) {
    this.rxBus = RxBus.getInstance();
    this.dispatcher = Dispatcher.getInstance(rxBus);
    this.subscriptionManager = SubscriptionManager.getInstance();
    activityCounter = 0;
    application.registerActivityLifecycleCallbacks(this);
  }

  public static RxFlux init(Application application) {
    if (instance != null) throw new IllegalStateException("Init was already called");
    return instance = new RxFlux(application);
  }

  public static void shutdown() {
    if (instance == null) return;
    instance.subscriptionManager.clear();
    instance.dispatcher.unsubscribeAll();
  }

  /**
   * @return the instance of the RxBus in case you want to reused for something else
   */
  public RxBus getRxBus() {
    return rxBus;
  }

  /**
   * @return the instance of the dispatcher
   */
  public Dispatcher getDispatcher() {
    return dispatcher;
  }

  /**
   * @return the instance of the subscription manager in case you want to reuse for something else
   */
  public SubscriptionManager getSubscriptionManager() {
    return subscriptionManager;
  }

  @Override public void onActivityCreated(Activity activity, Bundle bundle) {
    activityCounter++;
  }

  @Override public void onActivityStarted(Activity activity) {
  }

  @Override public void onActivityResumed(Activity activity) {
  }

  @Override public void onActivityPaused(Activity activity) {
  }

  @Override public void onActivityStopped(Activity activity) {
  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

  }

  @Override public void onActivityDestroyed(Activity activity) {
    activityCounter--;
    if (activityCounter == 0) {
      RxFlux.shutdown();
    }
  }
}
