/* (C)2022 Brendan Lackey */
package com.example.redistesting.contract;

import com.example.redistesting.model.User;
import java.util.List;
import java.util.concurrent.CompletionStage;

public interface CacheRepository {

  CompletionStage<List<User>> getAll();

  CompletionStage<User> getById(String id);

  CompletionStage<Boolean> create(User user);

  CompletionStage<Boolean> update(User user);

  CompletionStage<Boolean> delete(String id);
}
