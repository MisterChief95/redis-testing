/* (C)2022 Brendan Lackey */
package com.example.redistesting.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UncheckedObjectMapper extends ObjectMapper {

  @Override
  public <T> T readValue(byte[] src, Class<T> valueType) {
    try {
      return super.readValue(src, valueType);
    } catch (Exception ex) {
      throw new RuntimeException("Failed to convert bytes to " + valueType.getSimpleName(), ex);
    }
  }

  @Override
  public byte[] writeValueAsBytes(Object value) {
    try {
      return super.writeValueAsBytes(value);
    } catch (Exception ex) {
      throw new RuntimeException(
          "Failed to convert " + value.getClass().getSimpleName() + " to bytes", ex);
    }
  }
}
