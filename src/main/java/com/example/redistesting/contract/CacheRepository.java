package com.example.redistesting.contract;

import com.example.redistesting.model.User;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface CacheRepository {

  CompletableFuture<Collection<User>> getAll();

  CompletableFuture<User> getById(String id);

  CompletableFuture<Boolean> create(User user);

  CompletableFuture<Boolean> update(User user);

  CompletableFuture<Boolean> delete(String id);
}
