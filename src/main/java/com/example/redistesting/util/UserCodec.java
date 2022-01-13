/* (C)2022 Brendan Lackey */
package com.example.redistesting.util;

import com.example.redistesting.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.codec.RedisCodec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCodec implements RedisCodec<String, User> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserCodec.class);

  private final ObjectMapper objectMapper;

  public UserCodec(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public String decodeKey(ByteBuffer byteBuffer) {
    return StandardCharsets.UTF_8.decode(byteBuffer).toString();
  }

  @Override
  public User decodeValue(ByteBuffer byteBuffer) {
    var bArray = new byte[byteBuffer.remaining()];
    byteBuffer.get(bArray);

    LOGGER.debug("Value to Decode: {}", new String(bArray));

    try {
      return objectMapper.readValue(bArray, User.class);
    } catch (IOException e) {
      throw new RuntimeException("Unable to decode User from redis", e);
    }
  }

  @Override
  public ByteBuffer encodeKey(String s) {
    return StandardCharsets.UTF_8.encode(s);
  }

  @Override
  public ByteBuffer encodeValue(User user) {
    try {
      var byteBuffer = ByteBuffer.wrap(objectMapper.writeValueAsBytes(user));

      LOGGER.debug("Value to Encode: {}", new String(byteBuffer.array()));

      return byteBuffer;
    } catch (IOException e) {
      throw new RuntimeException("Unable to encode User for redis", e);
    }
  }
}
