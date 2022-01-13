package com.example.redistesting.rest;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.Objects.isNull;

@RestController("/user")
public class UserController {

    private final CacheRepository cacheRepository;

    public UserController(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @GetMapping
    public CompletionStage<ResponseEntity<List<User>>> getUsers(){
        return cacheRepository.getAll()
                .thenApply(list -> ResponseEntity.ok().body(list));
    }

    @GetMapping("/{id}")
    public CompletionStage<ResponseEntity<User>> getUser(@PathVariable String id){
        return cacheRepository.getById(id)
                .thenApply(user -> (!isNull(user))
                        ? ResponseEntity.accepted().body(user)
                        : ResponseEntity.noContent().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<ResponseEntity<Boolean>> addUser(User user){
        return cacheRepository.add(user)
                .thenApply(isSuccess -> ResponseEntity.accepted().body(isSuccess));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompletionStage<ResponseEntity<Boolean>> updateUser(User user) {
        return cacheRepository.update(user)
                .thenApply(isSuccess -> ResponseEntity.accepted().body(isSuccess));
    }
}
