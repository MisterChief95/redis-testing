/* (C)2022 Brendan Lackey */
package com.example.redistesting.repository;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.model.User;
import io.lettuce.core.KeyValue;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class CacheRepositoryImpl implements CacheRepository {

  private static final String USER_SET_KEY = "user_set";

  private final RedisReactiveCommands<String, User> commands;

  public CacheRepositoryImpl(RedisReactiveCommands<String, User> commands) {
    this.commands = commands;
  }

  @Override
  public Flux<User> getAll() {
    return commands.hgetall(USER_SET_KEY).map(KeyValue::getValue);
  }

  @Override
  public Mono<User> getById(String id) {
    return commands.hget(USER_SET_KEY, id);
  }

  @Override
  public Mono<Boolean> create(User user) {
    return update(user);
  }

  @Override
  public Mono<Boolean> update(User user) {
    return commands.hset(USER_SET_KEY, user.getId(), user);
  }

  @Override
  public Mono<Boolean> delete(String id) {
    return commands.hdel(USER_SET_KEY, id).map(this::longToBoolean);
  }

  private Boolean longToBoolean(Long l) {
    return l >= 1;
  }
}
