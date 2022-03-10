package org.cirrus.infrastructure.handler.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import org.cirrus.infrastructure.handler.exception.FailedResourceCreationException;
import org.cirrus.infrastructure.handler.exception.FailedResourceDeletionException;
import org.cirrus.infrastructure.handler.model.QueueConfig;
import org.cirrus.infrastructure.handler.util.Resources;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

public class SqsQueueService implements QueueService {

  private static final String TRUE = "true";
  private final SqsAsyncClient sqsClient;
  private final ServiceHelper helper;

  @Inject
  public SqsQueueService(SqsAsyncClient sqsClient, ServiceHelper helper) {
    this.sqsClient = sqsClient;
    this.helper = helper;
  }

  private static Map<QueueAttributeName, String> queueAttributes(QueueConfig config) {
    return Map.of(
        QueueAttributeName.DELAY_SECONDS,
        String.valueOf(config.delaySeconds()),
        QueueAttributeName.MAXIMUM_MESSAGE_SIZE,
        String.valueOf(config.maxMessageSizeBytes()),
        QueueAttributeName.MESSAGE_RETENTION_PERIOD,
        String.valueOf(config.messageRetentionPeriodSeconds()),
        QueueAttributeName.POLICY,
        "", // TODO
        QueueAttributeName.RECEIVE_MESSAGE_WAIT_TIME_SECONDS,
        String.valueOf(config.receiveMessageWaitTimeSeconds()),
        // TODO
        // https://docs.aws.amazon.com/lambda/latest/dg/best-practices.html#function-configuration
        // https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/using-messagededuplicationid-property.html#working-with-visibility-timeouts
        QueueAttributeName.VISIBILITY_TIMEOUT,
        String.valueOf(config.visibilityTimeoutSeconds()),
        QueueAttributeName.FIFO_QUEUE,
        TRUE,
        QueueAttributeName.CONTENT_BASED_DEDUPLICATION,
        TRUE);
  }

  @Override
  public CompletableFuture<String> createQueue(QueueConfig config) {
    String queueId = Resources.createRandomId();
    Map<QueueAttributeName, String> attributes = queueAttributes(config);
    return helper.getOrThrow(
        sqsClient.createQueue(builder -> builder.queueName(queueId).attributes(attributes)),
        CreateQueueResponse::queueUrl,
        FailedResourceCreationException::new);
  }

  @Override
  public CompletableFuture<Void> deleteQueue(String queueId) {
    return helper.getOrThrow(
        sqsClient.deleteQueue(builder -> builder.queueUrl(queueId)),
        FailedResourceDeletionException::new);
  }
}
