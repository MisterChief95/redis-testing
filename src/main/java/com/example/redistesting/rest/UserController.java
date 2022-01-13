/* (C)2022 Brendan Lackey */
package com.example.redistesting.rest;

import static java.util.Objects.isNull;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.model.User;
import java.util.List;
import java.util.concurrent.CompletionStage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  private final CacheRepository cacheRepository;

  public UserController(CacheRepository cacheRepository) {
    this.cacheRepository = cacheRepository;
  }

  @GetMapping
  public CompletionStage<ResponseEntity<List<User>>> getAll() {
    return cacheRepository.getAll().thenApply(list -> ResponseEntity.ok().body(list));
  }

  @GetMapping("/{id}")
  public CompletionStage<ResponseEntity<User>> get(@PathVariable String id) {
    return cacheRepository
        .getById(id)
        .thenApply(
            user ->
                (!isNull(user))
                    ? ResponseEntity.ok().body(user)
                    : ResponseEntity.noContent().build());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public CompletionStage<ResponseEntity<Boolean>> create(@RequestBody User user) {
    return cacheRepository
        .create(user)
        .thenApply(isNewEntry -> new ResponseEntity<>(isNewEntry, HttpStatus.CREATED));
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public CompletionStage<ResponseEntity<Boolean>> update(@RequestBody User user) {
    return cacheRepository
        .update(user)
        .thenApply(isNewEntry -> ResponseEntity.accepted().body(isNewEntry));
  }

  @DeleteMapping("/{id}")
  public CompletionStage<ResponseEntity<Boolean>> delete(@PathVariable String id) {
    return cacheRepository
        .delete(id)
        .thenApply(wasDeleted -> ResponseEntity.accepted().body(wasDeleted));
  }
}
