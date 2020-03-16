package com.example.redistraining.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class RedisServiceImpl {

  @Autowired
  private ReactiveRedisTemplate<Object, Object> redisTemplate;

  public Mono<Boolean> setValue(String key, Object value) {
    return redisTemplate.opsForValue().set(key, value);
  }

  public Mono<Object> getValue(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public Mono<Long> incrementValue(String key) {
    return redisTemplate.opsForValue().increment(key);
  }

  public Mono<Boolean> deleteKey(String key) {
    return redisTemplate.opsForValue().delete(key);
  }

  public Mono<Boolean> expireKey(String key, long seconds) {
    return redisTemplate.expire(key, Duration.ofSeconds(seconds));
  }
}