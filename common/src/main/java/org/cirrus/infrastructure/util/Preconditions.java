package org.cirrus.infrastructure.util;

public final class Preconditions {

  private Preconditions() {
    // no-op
  }

  public static void checkState(boolean expression) {
    if (!expression) {
      throw new IllegalStateException();
    }
  }

  public static <T> T checkNotNull(T value) {
    if (value == null) {
      throw new NullPointerException();
    }
    return value;
  }

  public static double checkInRangeClosed(double value, double lower, double upper) {
    checkState(value >= lower && value <= upper);
    return value;
  }

  public static String checkNotNullOrEmpty(String string) {
    checkNotNull(string);
    checkState(!string.isEmpty());
    return string;
  }
}
