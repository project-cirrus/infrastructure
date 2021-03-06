package org.cirrus.infrastructure.handler.util;

import javax.inject.Inject;

public class ConsoleLogger implements Logger {

  @Inject
  public ConsoleLogger() {
    // no-op
  }

  @Override
  public void debug(String message) {
    System.out.println(message);
  }

  @Override
  public void info(String message) {
    System.out.println(message);
  }

  @Override
  public void warn(String message) {
    System.err.println(message);
  }

  @Override
  public void error(String message) {
    System.err.println(message);
  }
}
