package com.example.redistesting.rest;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.model.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.Objects.isNull;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/user")
public class UserController {

    private final CacheRepository cacheRepository;

    public UserController(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @GetMapping
    public CompletionStage<ResponseEntity<List<User>>> getAll(){
        return cacheRepository.getAll()
                .thenApply(list -> ResponseEntity.ok().body(list));
    }

    @GetMapping("/{id}")
    public CompletionStage<ResponseEntity<User>> get(@PathVariable String id){
        return cacheRepository.getById(id)
                .thenApply(user -> (!isNull(user))
                        ? ResponseEntity.accepted().body(user)
                        : ResponseEntity.noContent().build());
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = {POST, PUT})
    public CompletionStage<ResponseEntity<Boolean>> set(@RequestBody User user){
        return cacheRepository.set(user)
                .thenApply(isNewEntry -> ResponseEntity.accepted().body(isNewEntry));
    }

    @DeleteMapping("/{id}")
    public CompletionStage<ResponseEntity<Boolean>> delete(@PathVariable String id){
        return cacheRepository.delete(id)
                .thenApply(wasDeleted -> ResponseEntity.accepted().body(wasDeleted));
    }
}
