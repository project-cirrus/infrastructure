package org.cirrus.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UncheckedIOException;
import javax.inject.Inject;
import org.cirrus.infrastructure.logging.Logger;

public class JacksonMapper implements Mapper {

  private final ObjectMapper mapper;
  private final Logger logger;

  @Inject
  public JacksonMapper(ObjectMapper mapper, Logger logger) {
    this.mapper = mapper;
    this.logger = logger;
  }

  public <T> T read(String content, Class<T> cls) {
    try {
      return mapper.readValue(content, cls);
    } catch (JsonProcessingException exception) {
      logger.error(exception.getLocalizedMessage());
      throw new UncheckedIOException(exception);
    }
  }

  public String write(Object value) {
    try {
      return mapper.writeValueAsString(value);
    } catch (JsonProcessingException exception) {
      logger.error(exception.getLocalizedMessage());
      throw new UncheckedIOException(exception);
    }
  }
}
