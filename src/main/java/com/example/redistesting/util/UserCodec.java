/* (C)2022 Brendan Lackey */
package com.example.redistesting.util;

import com.example.redistesting.model.User;
import io.lettuce.core.codec.RedisCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class UserCodec implements RedisCodec<String, User> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserCodec.class);

  private final UncheckedObjectMapper objectMapper;

  public UserCodec(UncheckedObjectMapper objectMapper) {
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

    return objectMapper.readValue(bArray, User.class);
  }

  @Override
  public ByteBuffer encodeKey(String s) {
    return StandardCharsets.UTF_8.encode(s);
  }

  @Override
  public ByteBuffer encodeValue(User user) {
      var byteBuffer = ByteBuffer.wrap(objectMapper.writeValueAsBytes(user));
      LOGGER.debug("Value to Encode: {}", new String(byteBuffer.array()));
      return byteBuffer;
  }
}
