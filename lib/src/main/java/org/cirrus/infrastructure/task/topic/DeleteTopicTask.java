package org.cirrus.infrastructure.task.topic;

import java.util.function.Consumer;
import org.cirrus.infrastructure.task.resource.DeleteResourceTask;
import org.cirrus.infrastructure.task.resource.ResourceType;

public final class DeleteTopicTask extends DeleteResourceTask {

  private static final ResourceType TYPE = ResourceType.TOPIC;
  private static final Consumer<Throwable> logger = DaggerTopicComponent.create().newLogger();

  public DeleteTopicTask() {
    super(TYPE, logger);
  }

  @Override
  public void deleteResource(String topicId) {
    DeleteTopic.create(topicId).run();
  }
}