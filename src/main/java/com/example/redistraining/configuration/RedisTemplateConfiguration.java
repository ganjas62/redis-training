package com.example.redistraining.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisTemplateConfiguration {

  @Bean
  public ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate
      (ReactiveRedisConnectionFactory connectionFactory) {
    return new ReactiveRedisTemplate<>(connectionFactory,
        RedisSerializationContext.java());
  }
}
