package org.vdragun.webfluxmongo.api.v1.exception.mapper;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ErrorHandler {

    Mono<ServerResponse> handlerError(Throwable t, ServerRequest request);
}
