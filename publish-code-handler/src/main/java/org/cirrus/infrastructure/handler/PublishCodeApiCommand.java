package org.cirrus.infrastructure.handler;

import javax.inject.Inject;
import org.cirrus.infrastructure.handler.api.ApiCommand;
import org.cirrus.infrastructure.handler.api.ApiRequest;
import org.cirrus.infrastructure.handler.api.ApiResponse;
import org.cirrus.infrastructure.handler.api.HttpStatus;
import org.cirrus.infrastructure.handler.exception.CirrusException;
import org.cirrus.infrastructure.handler.util.Mapper;

public final class PublishCodeApiCommand implements ApiCommand {

  private static final PublishCodeComponent component = DaggerPublishCodeComponent.create();
  private final Command<PublishCodeRequest, PublishCodeResponse> command;
  private final Mapper mapper;

  @Inject
  PublishCodeApiCommand(Command<PublishCodeRequest, PublishCodeResponse> command, Mapper mapper) {
    this.command = command;
    this.mapper = mapper;
  }

  public static PublishCodeApiCommand create() {
    return component.api();
  }

  @Override
  public ApiResponse run(ApiRequest request) {
    String body;
    int status;
    try {
      body = run(request.body());
      status = HttpStatus.CREATED;
    } catch (CirrusException exception) {
      body = exception.getMessage();
      status = HttpStatus.BAD_REQUEST;
    }
    return ApiResponse.of(body, status);
  }

  private String run(String body) {
    PublishCodeRequest request = mapper.read(body, PublishCodeRequest.class);
    PublishCodeResponse response = command.run(request);
    return mapper.write(response);
  }
}
