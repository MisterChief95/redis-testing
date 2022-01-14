/* (C)2022 Brendan Lackey */
package com.example.redistesting.repository;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.model.User;
import io.lettuce.core.api.async.RedisAsyncCommands;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CacheRepositoryImpl implements CacheRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(CacheRepositoryImpl.class);
  private static final String USER_SET_KEY = "user_set";

  private final RedisAsyncCommands<String, User> commands;

  public CacheRepositoryImpl(RedisAsyncCommands<String, User> commands) {
    this.commands = commands;
  }

  @Override
  public CompletableFuture<Collection<User>> getAll() {
    return commands.hgetall(USER_SET_KEY).thenApply(Map::values).toCompletableFuture();
  }

  @Override
  public CompletableFuture<User> getById(String id) {
    return commands.hget(USER_SET_KEY, id).toCompletableFuture();
  }

  @Override
  public CompletableFuture<Boolean> create(User user) {
    return update(user);
  }

  @Override
  public CompletableFuture<Boolean> update(User user) {
    return commands.hset(USER_SET_KEY, user.getId(), user).toCompletableFuture();
  }

  @Override
  public CompletableFuture<Boolean> delete(String id) {
    return commands.hdel(USER_SET_KEY, id).thenApply(this::longToBoolean).toCompletableFuture();
  }

  private Boolean longToBoolean(Long l) {
    return l >= 1;
  }
}
