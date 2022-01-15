package com.example.redistesting.contract;

import com.example.redistesting.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CacheRepository {

  Flux<User> getAll();

  Mono<User> getById(String id);

  Mono<Boolean> create(User user);

  Mono<Boolean> update(User user);

  Mono<Boolean> delete(String id);
}
