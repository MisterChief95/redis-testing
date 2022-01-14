/* (C)2022 Brendan Lackey */
package com.example.redistesting.rest;

import com.example.redistesting.contract.CacheService;
import com.example.redistesting.model.User;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class UserHandler {

  private static final String ID_PATH_VAR = "id";

  private final CacheService cacheService;

  public UserHandler(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
    return cacheService
        .getAll()
        .flatMap(lu -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(lu))
        .switchIfEmpty(ServerResponse.noContent().build())
        .onErrorResume(t -> handleError(t, "Error fetching all Users"));
  }

  public Mono<ServerResponse> get(ServerRequest serverRequest) {
    return cacheService
        .getById(serverRequest.pathVariable(ID_PATH_VAR))
        .flatMap(u -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(u))
        .switchIfEmpty(ServerResponse.noContent().build())
        .onErrorResume(t -> handleError(t, "Error fetching User"));
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(User.class)
        .flatMap(cacheService::create)
        .flatMap(b -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(b))
        .onErrorResume(t -> handleError(t, "Error adding User entry"));
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(User.class)
        .flatMap(cacheService::update)
        .flatMap(b -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(b))
        .onErrorResume(t -> handleError(t, "Error updating User entry"));
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    return cacheService
        .delete(serverRequest.pathVariable(ID_PATH_VAR))
        .flatMap(b -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(b))
        .onErrorResume(t -> handleError(t, "Error deleting User entry"));
  }

  /**
   * Utility function for handling reactor errors
   *
   * @param t {@link Throwable}
   * @param message to pass to {@link ServerResponse}
   * @return {@link Mono} of {@link ServerResponse}
   */
  private static Mono<ServerResponse> handleError(Throwable t, String message) {
    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(Map.of("error", message, "reason", t.getMessage()));
  }
}
