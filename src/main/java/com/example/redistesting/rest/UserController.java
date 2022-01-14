/* (C)2022 Brendan Lackey */
package com.example.redistesting.rest;

import static java.util.Objects.isNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.model.User;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  @Value("${rest.controller.user.timeout}")
  private long TIMEOUT = 100L;

  private final CacheRepository cacheRepository;

  public UserController(CacheRepository cacheRepository) {
    this.cacheRepository = cacheRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<Collection<User>>> getAll() {
    return cacheRepository
        .getAll()
        .thenApply(list -> ResponseEntity.ok().body(list))
        .completeOnTimeout(ResponseEntity.internalServerError().build(), TIMEOUT, MILLISECONDS);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<User>> get(@PathVariable String id) {
    return cacheRepository
        .getById(id)
        .thenApply(u -> new ResponseEntity<>(u, isNull(u) ? HttpStatus.NO_CONTENT : HttpStatus.OK))
        .completeOnTimeout(ResponseEntity.internalServerError().build(), TIMEOUT, MILLISECONDS);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<Boolean>> create(@RequestBody User user) {
    return cacheRepository
        .create(user)
        .thenApply(isNewEntry -> new ResponseEntity<>(isNewEntry, HttpStatus.OK))
        .completeOnTimeout(ResponseEntity.internalServerError().build(), TIMEOUT, MILLISECONDS);
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<Boolean>> update(@RequestBody User user) {
    return cacheRepository
        .update(user)
        .thenApply(isNewEntry -> ResponseEntity.accepted().body(isNewEntry))
        .completeOnTimeout(ResponseEntity.internalServerError().build(), TIMEOUT, MILLISECONDS);
  }

  @DeleteMapping(path = "/{id}")
  public CompletableFuture<ResponseEntity<Boolean>> delete(@PathVariable String id) {
    return cacheRepository
        .delete(id)
        .thenApply(wasDeleted -> ResponseEntity.accepted().body(wasDeleted))
        .completeOnTimeout(ResponseEntity.internalServerError().build(), TIMEOUT, MILLISECONDS);
  }
}
