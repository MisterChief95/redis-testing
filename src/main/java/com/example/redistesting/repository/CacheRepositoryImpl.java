package com.example.redistesting.repository;

import com.example.redistesting.contract.CacheRepository;
import com.example.redistesting.model.User;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class CacheRepositoryImpl implements CacheRepository {

    private static final String USER_SET_KEY = "user_set";

    private final RedisAsyncCommands<String, User> commands;

    public CacheRepositoryImpl(RedisAsyncCommands<String, User> commands) {
        this.commands = commands;
    }

    @Override
    public CompletionStage<List<User>> getAll() {
        return commands.hgetall(USER_SET_KEY)
                .thenApply(Map::values)
                .thenApply(ArrayList::new);
    }

    @Override
    public CompletionStage<User> getById(String id) {
        return commands.hget(USER_SET_KEY, id);
    }

    @Override
    public CompletionStage<Boolean> add(User user) {
        return exists(user).thenCompose(exists -> (!exists)
                    ? commands.hset(USER_SET_KEY, user.getId(), user)
                    : completedFuture(false));
    }

    @Override
    public CompletionStage<Boolean> update(User user) {
        return exists(user).thenCompose(exists -> (exists)
                ? commands.hset(USER_SET_KEY, user.getId(), user)
                : completedFuture(false));
    }

    @Override
    public CompletionStage<Boolean> exists(User user) {
        return commands.hexists(USER_SET_KEY, user.getId());
    }

    @Override
    public CompletionStage<Boolean> delete(User user) {
        return commands.hdel(USER_SET_KEY, user.getId())
                .thenApply(this::longToBoolean);
    }

    private Boolean longToBoolean(Long l){
        return l == 1;
    }

}
