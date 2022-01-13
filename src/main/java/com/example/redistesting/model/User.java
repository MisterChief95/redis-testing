package com.example.redistesting.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = User.Builder.class)
public class User {
    private final String id;
    private final String name;
    private final int age;

    private User(User.Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.age = builder.age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public static final class Builder {
        private String id;
        private String name;
        private int age;

        @JsonSetter("id")
        public Builder withId(String id){
            this.id = id;
            return this;
        }

        @JsonSetter("name")
        public Builder withName(String name){
            this.name = name;
            return this;
        }

        @JsonSetter("age")
        public Builder withAge(int age){
            this.age = age;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
