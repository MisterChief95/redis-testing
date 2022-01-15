package com.example.redistesting.rest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class UserRouter {

  @Bean
  public RouterFunction<ServerResponse> userRoute(UserHandler userHandler) {
    return RouterFunctions.nest(
        path("/user"),
        route(GET("/{id}"), userHandler::get)
            .andRoute(DELETE("/{id}"), userHandler::delete)
            .andRoute(method(GET), userHandler::getAll)
            .andRoute(method(POST).and(accept(APPLICATION_JSON)), userHandler::create)
            .andRoute(method(PUT).and(accept(APPLICATION_JSON)), userHandler::update));
  }
}
