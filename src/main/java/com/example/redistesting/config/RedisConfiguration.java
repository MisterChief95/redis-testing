package com.example.redistesting.config;

import com.example.redistesting.model.LettuceProperties;
import com.example.redistesting.model.User;
import com.example.redistesting.util.UserCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.RedisCodec;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Duration;

@Configuration
@Import(CommonBeans.class)
public class RedisConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "redis.lettuce")
    public LettuceProperties properties(){
        return new LettuceProperties();
    }

    @Bean
    public RedisURI uri(LettuceProperties lettuceProperties){
        return RedisURI.builder()
                .withHost(lettuceProperties.getHost())
                .withPort(lettuceProperties.getPort())
                .withTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Bean
    public RedisClient client(RedisURI uri){
        return RedisClient.create(uri);
    }

    @Bean
    public RedisCodec<String, User> codec(ObjectMapper objectMapper){
        return new UserCodec(objectMapper);
    }

    @Bean
    public StatefulRedisConnection<String, User> connection(RedisClient client, RedisCodec<String, User> codec) {
        return client.connect(codec);
    }

    @Bean
    public RedisAsyncCommands<String, User> asyncCommands(StatefulRedisConnection<String, User> connection){
        return connection.async();
    }
}
