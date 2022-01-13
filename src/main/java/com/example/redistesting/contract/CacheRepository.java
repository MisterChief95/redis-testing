package com.example.redistesting.contract;

import com.example.redistesting.model.User;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface CacheRepository {

    CompletionStage<List<User>> getAll();

    CompletionStage<User> getById(String id);

    CompletionStage<Boolean> add(User user);

    CompletionStage<Boolean> update(User user);

    CompletionStage<Boolean> exists(User user);

    CompletionStage<Boolean> delete(User user);

}
