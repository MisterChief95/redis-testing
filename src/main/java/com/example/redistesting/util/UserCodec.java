/* (C)2022 Brendan Lackey */
package com.example.redistesting.util;

import com.example.redistesting.model.User;
import io.lettuce.core.codec.RedisCodec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class UserCodec implements RedisCodec<String, User> {

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
    return objectMapper.readValue(bArray, User.class);
  }

  @Override
  public ByteBuffer encodeKey(String s) {
    return StandardCharsets.UTF_8.encode(s);
  }

  @Override
  public ByteBuffer encodeValue(User user) {
    return ByteBuffer.wrap(objectMapper.writeValueAsBytes(user));
  }
}
