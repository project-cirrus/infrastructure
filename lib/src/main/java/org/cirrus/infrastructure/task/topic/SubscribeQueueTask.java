package org.cirrus.infrastructure.task.topic;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.function.Consumer;
import org.cirrus.infrastructure.task.resource.ResourceType;
import org.cirrus.infrastructure.task.util.Mapping;

public final class SubscribeQueueTask implements RequestHandler<String, String> {

  private static final ResourceType TOPIC = ResourceType.TOPIC;
  private static final ResourceType QUEUE = ResourceType.QUEUE;
  private static final Consumer<Throwable> logger = DaggerTopicComponent.create().newLogger();

  @Override
  public String handleRequest(String input, Context context) {
    SubscribeQueueInput mappedInput = mapInput(input);
    String topicId = getTopicId(mappedInput);
    String queueId = getQueueId(mappedInput);
    subscribeQueue(topicId, queueId);
    SubscribeQueueOutput output = createOutput(mappedInput);
    return mapOutput(output);
  }

  private SubscribeQueueInput mapInput(String input) {
    return Mapping.read(input, SubscribeQueueInput.class, logger);
  }

  private String getTopicId(SubscribeQueueInput input) {
    return getResourceId(input, TOPIC);
  }

  private String getQueueId(SubscribeQueueInput input) {
    return getResourceId(input, QUEUE);
  }

  private void subscribeQueue(String topicId, String queueId) {
    SubscribeQueue.create(topicId, queueId).run();
  }

  private SubscribeQueueOutput createOutput(SubscribeQueueInput input) {
    return SubscribeQueueOutput.newBuilder().addAllOutputs(input.getOutputs()).build();
  }

  private String mapOutput(SubscribeQueueOutput output) {
    return Mapping.write(output, logger);
  }

  private String getResourceId(SubscribeQueueInput input, ResourceType type) {
    return input.getTypedOutputs().get(type).getResourceId();
  }
}