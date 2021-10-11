package org.cirrus.infrastructure.workflow;

import software.amazon.awscdk.services.stepfunctions.TaskStateBase;
import software.constructs.Construct;

public final class TopicStateFactory {

  private static final String CREATE_TOPIC = "CreateTopic";
  private static final String CREATE_TOPIC_PATH = "../task/topic/CreateTopicTask";
  private static final String CREATE_TOPIC_COMMENT = "Creates an SNS topic";
  private static final String DELETE_TOPIC = "DeleteTopic";
  private static final String DELETE_TOPIC_PATH = "../task/topic/DeleteTopicTask";
  private static final String DELETE_TOPIC_COMMENT = "Deletes an SNS topic";
  private static final String SUBSCRIBE_QUEUE = "SubscribeQueue";
  private static final String SUBSCRIBE_QUEUE_PATH = ""; // TODO
  private static final String SUBSCRIBE_QUEUE_COMMENT = "Subscribes an SQS queue to an SNS topic";
  private final Construct scope;

  private TopicStateFactory(Construct scope) {
    this.scope = scope;
  }

  public static TopicStateFactory of(Construct scope) {
    return new TopicStateFactory(scope);
  }

  public TaskStateBase createTopic() {
    return LambdaStateBuilder.create(scope)
        .setFunctionName(CREATE_TOPIC)
        .setCodePath(CREATE_TOPIC_PATH)
        .setComment(CREATE_TOPIC_COMMENT)
        .build();
  }

  public TaskStateBase deleteTopic() {
    return LambdaStateBuilder.create(scope)
        .setFunctionName(DELETE_TOPIC)
        .setCodePath(DELETE_TOPIC_PATH)
        .setComment(DELETE_TOPIC_COMMENT)
        .build();
  }

  public TaskStateBase subscribeQueue() {
    return LambdaStateBuilder.create(scope)
        .setFunctionName(SUBSCRIBE_QUEUE)
        .setCodePath(SUBSCRIBE_QUEUE_PATH)
        .setComment(SUBSCRIBE_QUEUE_COMMENT)
        .build();
  }
}
