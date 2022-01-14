/* (C)2022 Brendan Lackey */
package com.example.redistesting.contract;

import com.example.redistesting.model.User;
import java.util.List;
import reactor.core.publisher.Mono;

public interface CacheService {

  Mono<List<User>> getAll();

  Mono<User> getById(String id);

  Mono<Boolean> create(User user);

  Mono<Boolean> update(User user);

  Mono<Boolean> delete(String id);
}
