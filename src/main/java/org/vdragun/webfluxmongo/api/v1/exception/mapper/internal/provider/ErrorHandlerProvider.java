package org.vdragun.webfluxmongo.api.v1.exception.mapper.internal.provider;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ErrorHandlerProvider {

    boolean canHandle(Throwable t);

    Mono<ServerResponse> handleError(Throwable t, ServerRequest request);
}
