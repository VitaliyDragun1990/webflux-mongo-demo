package org.vdragun.webfluxmongo.api.v1.exception.mapper.internal.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.vdragun.webfluxmongo.api.v1.exception.mapper.internal.ApiError;
import org.vdragun.webfluxmongo.exception.ResourceNotFoundException;
import org.vdragun.webfluxmongo.util.provider.DateTimeProvider;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class ResourceNotFoundErrorHandlerProvider implements ErrorHandlerProvider {

    private final DateTimeProvider dateTimeProvider;

    @Override
    public boolean canHandle(Throwable t) {
        return t instanceof ResourceNotFoundException;
    }

    @Override
    public Mono<ServerResponse> handleError(Throwable t, ServerRequest request) {
        return handleErrorInternal((ResourceNotFoundException) t, request);
    }

    private Mono<ServerResponse> handleErrorInternal(ResourceNotFoundException e, ServerRequest request) {
        LOG.warn("Handling ResourceNotFoundException:{}", e.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.path(),
                dateTimeProvider.currentDateTime());

        return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(apiError);
    }
}
