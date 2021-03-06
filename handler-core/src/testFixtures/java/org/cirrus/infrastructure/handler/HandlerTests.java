package org.cirrus.infrastructure.handler;

import java.util.concurrent.CompletableFuture;
import org.cirrus.infrastructure.handler.exception.FailedEventSourceMappingException;
import org.cirrus.infrastructure.handler.exception.FailedResourceCreationException;
import org.cirrus.infrastructure.handler.exception.FailedResourceDeletionException;
import org.cirrus.infrastructure.handler.exception.FailedStorageWriteException;
import org.cirrus.infrastructure.handler.exception.NoSuchNodeException;
import org.cirrus.infrastructure.handler.model.FunctionConfig;
import org.cirrus.infrastructure.handler.model.NodeRecord;
import org.cirrus.infrastructure.handler.model.QueueConfig;

public final class HandlerTests {

  public static final String NODE_ID = "nodeId";
  public static final String FUNCTION_ID = "functionId";
  public static final String QUEUE_ID = "queueId";
  public static final String CODE_ID = "codeId";
  public static final String ARTIFACT_ID = "artifactId";
  public static final String UPLOAD_URL = "uploadUrl";
  public static final String EVENT_SOURCE_MAPPING_ID = "eventSourceMappingId";

  private HandlerTests() {
    // no-op
  }

  public static CompletableFuture<NodeRecord> nodeRecordStage() {
    return CompletableFuture.completedFuture(nodeRecord());
  }

  public static FunctionConfig functionConfig() {
    return FunctionConfig.builder().artifactId(ARTIFACT_ID).build();
  }

  public static QueueConfig queueConfig() {
    return QueueConfig.builder().build();
  }

  public static NodeRecord nodeRecord() {
    return NodeRecord.builder()
        .nodeId(NODE_ID)
        .functionId(FUNCTION_ID)
        .queueId(QUEUE_ID)
        .artifactId(CODE_ID)
        .build();
  }

  public static <T> CompletableFuture<T> noReturn() {
    return CompletableFuture.completedFuture(null);
  }

  public static CompletableFuture<String> function() {
    return CompletableFuture.completedFuture(FUNCTION_ID);
  }

  public static CompletableFuture<String> queue() {
    return CompletableFuture.completedFuture(QUEUE_ID);
  }

  public static CompletableFuture<String> failedResource() {
    return CompletableFuture.failedFuture(new FailedResourceCreationException());
  }

  public static CompletableFuture<String> eventSourceMappingId() {
    return CompletableFuture.completedFuture(EVENT_SOURCE_MAPPING_ID);
  }

  public static <T> CompletableFuture<T> failedStorageWrite() {
    return CompletableFuture.failedFuture(new FailedStorageWriteException());
  }

  public static <T> CompletableFuture<T> noSuchNode() {
    return CompletableFuture.failedFuture(new NoSuchNodeException());
  }

  public static <T> CompletableFuture<T> failedResourceDeletion() {
    return CompletableFuture.failedFuture(new FailedResourceDeletionException());
  }

  public static <T> CompletableFuture<T> failedEventSourceMapping() {
    return CompletableFuture.failedFuture(new FailedEventSourceMappingException());
  }
}
