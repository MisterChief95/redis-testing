package com.example.redistesting.service;

import static java.util.Objects.requireNonNull;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.contract.CacheService;
import com.example.redistesting.model.User;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

public class CacheServiceImpl implements CacheService {

  @Value("${rest.controller.user.timeout}")
  private Duration timeout;

  private final CacheRepository cacheRepository;

  public CacheServiceImpl(CacheRepository cacheRepository) {
    this.cacheRepository = requireNonNull(cacheRepository, "cacheRepository cannot be null");
  }

  @Override
  public Mono<List<User>> getAll() {
    return cacheRepository.getAll().collectList().timeout(timeout);
  }

  @Override
  public Mono<User> getById(String id) {
    return cacheRepository.getById(id).timeout(timeout);
  }

  @Override
  public Mono<Boolean> create(User user) {
    return cacheRepository.create(user).timeout(timeout);
  }

  @Override
  public Mono<Boolean> update(User user) {
    return cacheRepository.update(user).timeout(timeout);
  }

  @Override
  public Mono<Boolean> delete(String id) {
    return cacheRepository.delete(id).timeout(timeout);
  }
}
