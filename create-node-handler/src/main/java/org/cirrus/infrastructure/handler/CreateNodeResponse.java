package org.cirrus.infrastructure.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cirrus.infrastructure.util.Keys;
import org.immutables.value.Value;

@Value.Immutable
public abstract class CreateNodeResponse {

  public static Builder newBuilder() {
    return ImmutableCreateNodeResponse.newBuilder();
  }

  @JsonProperty(Keys.FUNCTION_KEY)
  public abstract String getFunctionId();

  @JsonProperty(Keys.QUEUE_KEY)
  public abstract String getQueueId();

  public interface Builder {

    CreateNodeResponse build();

    Builder setFunctionId(String functionId);

    Builder setQueueId(String queueId);
  }
}
