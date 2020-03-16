package com.example.redistraining.service;

import com.example.redistraining.RedisTrainingApplication;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import redis.embedded.RedisServerBuilder;

import java.io.IOException;
import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = RedisTrainingApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RedisServiceImplTest {

  private static redis.embedded.RedisServer redisServer;

  @Autowired
  private RedisServiceImpl serviceImpl;

  @BeforeClass
  public static void startRedisServer() throws IOException {
    redisServer = new RedisServerBuilder().port(6379).setting("maxmemory 256M").build();
    redisServer.start();
  }

  @AfterClass
  public static void stopRedisServer() throws IOException {
    redisServer.stop();
  }

  @Test
  public void getValueTest() {
    Mono<Object> result = serviceImpl.setValue("stringKey", "value")
        .then(serviceImpl.getValue("stringKey"));
    StepVerifier.create(result).expectNext("value").verifyComplete();
  }

  @Test
  public void incrementTest() {
    Mono<Long> result = serviceImpl.incrementValue("incKey").log();
    StepVerifier.create(result).expectNext(Long.valueOf(1)).verifyComplete();
  }

  @Test
  public void deleteKeyTest() {
    Flux<Object> result = serviceImpl.setValue("testKey", "value")
        .then(serviceImpl.getValue("testKey"))
        .then(serviceImpl.expireKey("testKey", 5))
        .thenMany(Flux.range(0, 3)
            .delayElements(Duration.ofSeconds(2))
            .flatMap(i -> serviceImpl.getValue("testKey")));
    StepVerifier.create(result)
        .expectNext("value")
        .expectNext("value")
        .verifyComplete();
  }
}
