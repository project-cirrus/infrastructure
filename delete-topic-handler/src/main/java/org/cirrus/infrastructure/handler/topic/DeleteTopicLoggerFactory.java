package org.cirrus.infrastructure.handler.topic;

import org.cirrus.infrastructure.handler.util.Logger;

public final class DeleteTopicLoggerFactory {

  private static final DeleteTopicComponent component = DaggerDeleteTopicComponent.create();

  private DeleteTopicLoggerFactory() {
    // No-op
  }

  public static Logger create() {
    return component.getLogger();
  }
}
