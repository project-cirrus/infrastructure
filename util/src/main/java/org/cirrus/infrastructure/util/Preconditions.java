package org.cirrus.infrastructure.util;

public final class Preconditions {

  private static final String EMPTY = "";

  private Preconditions() {
    // no-op
  }

  public static void checkState(boolean expression) {
    if (!expression) {
      throw new IllegalStateException();
    }
  }

  public static void inRangeClosed(double value, double upper, double lower) {
    checkState(value <= upper && value >= lower);
  }

  public static void notNullOrEmpty(String string) {
    checkState(string != null && !string.equals(EMPTY));
  }
}
