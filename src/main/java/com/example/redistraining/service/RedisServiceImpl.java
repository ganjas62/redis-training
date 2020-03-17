package com.example.redistraining.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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

  public Mono<Object> getValueWithTime(String key) throws InterruptedException {
    Mono<Object> cachedValue = getValue(key);
    return cachedValue.switchIfEmpty(process(key));
  }

  public Mono<Object> process(String key) throws InterruptedException {
    Thread.sleep(5000);
    String hasil = "Hasil method " + key;
    return setValue(key, hasil).then(Mono.just(hasil));
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

  public Mono<Long> rightPush(String key, Object value) {
    return redisTemplate.opsForList().rightPush(key, value);
  }

  public Mono<Long> leftPush(String key, Object value) {
    return redisTemplate.opsForList().leftPush(key, value);
  }

  public Flux<Object> listRange(String key, long startIndex, long lastIndex) {
    return redisTemplate.opsForList().range(key, startIndex, lastIndex);
  }
}
