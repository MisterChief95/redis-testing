/* (C)2022 Brendan Lackey */
package com.example.redistesting.config;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.contract.CacheService;
import com.example.redistesting.model.User;
import com.example.redistesting.repository.CacheRepositoryImpl;
import com.example.redistesting.service.CacheServiceImpl;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RedisConfiguration.class, CommonBeans.class})
public class BootStrap {

  @Bean
  public CacheRepository cacheRepository(RedisReactiveCommands<String, User> commands) {
    return new CacheRepositoryImpl(commands);
  }

  @Bean
  public CacheService cacheService(CacheRepository cacheRepository) {
    return new CacheServiceImpl(cacheRepository);
  }
}
