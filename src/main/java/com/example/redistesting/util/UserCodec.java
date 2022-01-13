package com.example.redistesting.util;

import com.example.redistesting.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.codec.RedisCodec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class UserCodec implements RedisCodec<String, User> {

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
        try {
            return objectMapper.readValue(byteBuffer.array(), User.class);
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
            return ByteBuffer.wrap(objectMapper.writeValueAsBytes(user));
        } catch (IOException e) {
            throw new RuntimeException("Unable to encode User for redis", e);
        }
    }
}
