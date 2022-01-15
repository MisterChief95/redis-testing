package com.example.redistesting.util;

public final class Preconditions {

  public static String requireNotBlank(String string){
    return requireNotBlank(string, "string cannot be blank or null");
  }

  public static String requireNotBlank(String string, String message){
    if (string.isBlank()) {
      throw new IllegalArgumentException(message);
    }
    return string;
  }

  public static void checkArgument(Boolean condition) {
    checkArgument(condition, "Illegal argument supplied");
  }

  public static void checkArgument(Boolean condition, String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  private Preconditions(){}
}
