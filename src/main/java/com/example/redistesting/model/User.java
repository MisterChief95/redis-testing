package com.example.redistesting.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.net.InetAddress;

@JsonDeserialize(builder = User.Builder.class)
public class User {
  private final InetAddress ipv4;
  private final String firstName;
  private final String id;
  private final String lastName;
  private final int age;

  private User(User.Builder builder) {
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.age = builder.age;

    try {
      this.ipv4 = InetAddress.getByName(builder.ipv4);
    } catch (Exception ex) {
      throw new RuntimeException("Unable to parse IPv4 String", ex);
    }

    if (builder.id.isBlank()) {
      this.id = String.valueOf((firstName + lastName + age).hashCode());
    } else {
      this.id = builder.id;
    }
  }

  public String getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public int getAge() {
    return age;
  }

  public InetAddress getIpv4() {
    return ipv4;
  }

  public String getLastName() {
    return lastName;
  }

  public static final class Builder {
    private String firstName;
    private String lastName;
    private String ipv4;
    private int age;
    private String id;

    @JsonSetter("id")
    public Builder withId(String id) {
      this.id = id;
      return this;
    }

    @JsonSetter("firstName")
    public Builder withFirstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    @JsonSetter("lastName")
    public Builder withLastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    @JsonSetter("age")
    public Builder withAge(int age) {
      this.age = age;
      return this;
    }

    @JsonSetter("ipv4")
    public Builder withIPv4(String ipv4) {
      this.ipv4 = ipv4;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }
}
