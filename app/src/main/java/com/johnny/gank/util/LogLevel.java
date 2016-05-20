package com.johnny.gank.util;

/**
 * Enum used to decide how much to log during RxFlux flow
 */
public enum LogLevel {
  /**
   * Log all events in the dispatcher
   */
  FULL,
  /**
   * Log all single event in the dispatcher (no duplicated post for different stores)
   */
  SIMPLIFY,
  /**
   * Do not log
   */
  NONE

}
