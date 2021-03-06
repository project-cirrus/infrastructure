package org.cirrus.infrastructure.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.cirrus.infrastructure.util.Keys;
import org.cirrus.infrastructure.util.Preconditions;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateNodeResponse.class)
@JsonDeserialize(as = ImmutableCreateNodeResponse.class)
public abstract class CreateNodeResponse {

  public static Builder builder() {
    return ImmutableCreateNodeResponse.builder();
  }

  @JsonProperty(Keys.NODE_ID)
  public abstract String nodeId();

  @JsonProperty(Keys.FUNCTION_ID)
  public abstract String functionId();

  @JsonProperty(Keys.QUEUE_ID)
  public abstract String queueId();

  @JsonProperty(Keys.ARTIFACT_ID)
  public abstract String artifactId();

  @Value.Check
  protected void check() {
    Preconditions.checkNotNullOrEmpty(nodeId());
    Preconditions.checkNotNullOrEmpty(functionId());
    Preconditions.checkNotNullOrEmpty(queueId());
    Preconditions.checkNotNullOrEmpty(artifactId());
  }

  public abstract static class Builder {

    public abstract CreateNodeResponse build();

    public abstract Builder nodeId(String nodeId);

    public abstract Builder functionId(String functionId);

    public abstract Builder queueId(String queueId);

    public abstract Builder artifactId(String artifactId);
  }
}
