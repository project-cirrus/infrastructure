package org.cirrus.infrastructure.handler.topic;

import dagger.Module;
import dagger.Provides;
import java.util.Map;
import java.util.function.Supplier;
import javax.inject.Singleton;
import org.cirrus.infrastructure.handler.util.Logger;
import org.cirrus.infrastructure.handler.util.ResourceUtil;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;

@Module
final class CreateTopicModule {

  private static final Map<String, String> ATTRIBUTES = Map.of();

  private CreateTopicModule() {}

  @Provides
  public static Supplier<CreateTopicRequest> provideCreateRequester() {
    return () ->
        CreateTopicRequest.builder()
            .name(ResourceUtil.createRandomId())
            .attributes(ATTRIBUTES)
            .build();
  }

  @Provides
  @Singleton
  public static Logger provideLogger() {
    return Logger.of("CreateTopic");
  }
}