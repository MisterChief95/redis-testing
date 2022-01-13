package com.example.redistesting.model;

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

    private static final class Builder {
        private String id;
        private String name;
        private int age;

        public Builder withId(String id){
            this.id = id;
            return this;
        }

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withAge(int age){
            this.age = age;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
