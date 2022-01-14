/* (C)2022 Brendan Lackey */
package com.example.redistesting.rest;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
            .andRoute(method(POST).and(accept(MediaType.APPLICATION_JSON)), userHandler::create)
            .andRoute(method(PUT).and(accept(MediaType.APPLICATION_JSON)), userHandler::update));
  }
}
