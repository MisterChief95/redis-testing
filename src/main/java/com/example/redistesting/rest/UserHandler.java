/* (C)2022 Brendan Lackey */
package com.example.redistesting.rest;

import static com.example.redistesting.util.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.example.redistesting.contract.CacheService;
import com.example.redistesting.model.User;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class UserHandler {

  private static final String ID_PATH_VAR = "id";

  private final CacheService cacheService;

  public UserHandler(CacheService cacheService) {
    this.cacheService = requireNonNull(cacheService, "cacheService cannot be null");
  }

  public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
    requireNonNull(serverRequest, "serverRequest cannot be null");

    return cacheService
        .getAll()
        .flatMap(lu -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(lu))
        .switchIfEmpty(ServerResponse.noContent().build())
        .onErrorResume(t -> handleError(t, "Error fetching all Users"));
  }

  public Mono<ServerResponse> get(ServerRequest serverRequest) {
    requireNonNull(serverRequest, "serverRequest cannot be null");

    return cacheService
        .getById(serverRequest.pathVariable(ID_PATH_VAR))
        .flatMap(u -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(u))
        .switchIfEmpty(ServerResponse.noContent().build())
        .onErrorResume(t -> handleError(t, "Error fetching User"));
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    requireNonNull(serverRequest, "serverRequest cannot be null");

    return serverRequest
        .bodyToMono(User.class)
        .flatMap(cacheService::create)
        .flatMap(b -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(b))
        .onErrorResume(t -> handleError(t, "Error adding User entry"));
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {
    requireNonNull(serverRequest, "serverRequest cannot be null");

    return serverRequest
        .bodyToMono(User.class)
        .flatMap(cacheService::update)
        .flatMap(b -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(b))
        .onErrorResume(t -> handleError(t, "Error updating User entry"));
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    requireNonNull(serverRequest, "serverRequest cannot be null");

    return cacheService
        .delete(serverRequest.pathVariable(ID_PATH_VAR))
        .flatMap(b -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(b))
        .onErrorResume(t -> handleError(t, "Error deleting User entry"));
  }

  /**
   * Utility function for handling reactor errors
   *
   * @param throwable {@link Throwable}
   * @param message to pass to {@link ServerResponse}
   * @return {@link Mono} of {@link ServerResponse}
   */
  private static Mono<ServerResponse> handleError(Throwable throwable, String message) {
    requireNonNull(throwable, "throwable cannot be null");
    checkArgument(!message.isBlank(), "message cannot be blank or null");

    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(APPLICATION_JSON)
        .bodyValue(Map.of("error", message, "reason", throwable.getMessage()));
  }
}
