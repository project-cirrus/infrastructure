package org.cirrus.infrastructure.handler.api;

import org.cirrus.infrastructure.util.Preconditions;
import org.immutables.value.Value;

@Value.Immutable
public abstract class ApiResponse {

  public static Builder builder() {
    return ImmutableApiResponse.builder();
  }

  public static ApiResponse of(String body, int status) {
    return ImmutableApiResponse.of(body, status);
  }

  @Value.Parameter
  public abstract String body();

  @Value.Parameter
  public abstract int status();

  @Value.Check
  protected void check() {
    Preconditions.checkState(HttpStatus.contains(status()));
  }

  interface Builder {

    Builder body(String body);

    Builder status(int status);

    ApiResponse build();
  }
}
