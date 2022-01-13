/* (C)2022 Brendan Lackey */
package com.example.redistesting.config;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.model.User;
import com.example.redistesting.repository.CacheRepositoryImpl;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@Import({RedisConfiguration.class, CommonBeans.class})
public class BootStrap {

  @Bean
  public CacheRepository cacheRepository(RedisAsyncCommands<String, User> commands) {
    return new CacheRepositoryImpl(commands);
  }

  @Bean
  public CommonsRequestLoggingFilter logFilter() {
    var filter = new CommonsRequestLoggingFilter();
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(true);
    filter.setMaxPayloadLength(10000);
    filter.setIncludeHeaders(false);
    filter.setAfterMessagePrefix("REQUEST DATA : ");
    return filter;
  }
}
