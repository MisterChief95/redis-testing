/* (C)2022 Brendan Lackey */
package com.example.redistesting.config;

import com.example.redistesting.util.UncheckedObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonBeans {

  @Bean
  public UncheckedObjectMapper objectMapper() {
    return new UncheckedObjectMapper();
  }
}
