package org.vdragun.webfluxmongo.api.v1.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.vdragun.webfluxmongo.api.v1.handler.NodeRequestHandler;
import org.vdragun.webfluxmongo.config.ApiVersion;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterProvider {

    public static final String BASE_URL = ApiVersion.VERSION_1 + "/nodes";

    @Bean
    public RouterFunction<ServerResponse> routes(NodeRequestHandler handler) {
        return route(GET(BASE_URL), handler::findAllNodes)
                .andRoute(GET(BASE_URL + "/{id}"), handler::findNodeById)
                .andRoute(POST(BASE_URL).and(accept(MediaType.APPLICATION_JSON)), handler::createNode)
                .andRoute(PUT(BASE_URL + "/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::updateNode)
                .andRoute(DELETE(BASE_URL + "/{id}"), handler::deleteNode);
    }
}
