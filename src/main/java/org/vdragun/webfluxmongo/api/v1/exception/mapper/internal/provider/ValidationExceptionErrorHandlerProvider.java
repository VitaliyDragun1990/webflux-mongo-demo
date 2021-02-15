package org.vdragun.webfluxmongo.api.v1.exception.mapper.internal.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.vdragun.webfluxmongo.api.v1.exception.mapper.internal.ApiError;
import org.vdragun.webfluxmongo.exception.ValidationException;
import org.vdragun.webfluxmongo.util.provider.DateTimeProvider;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidationExceptionErrorHandlerProvider implements ErrorHandlerProvider {

    private final DateTimeProvider dateTimeProvider;

    @Override
    public boolean canHandle(Throwable t) {
        return t instanceof ValidationException;
    }

    @Override
    public Mono<ServerResponse> handleError(Throwable t, ServerRequest request) {
        return handleErrorInternal((ValidationException) t, request);
    }

    private Mono<ServerResponse> handleErrorInternal(ValidationException e, ServerRequest request) {
        LOG.warn("Handling ValidationException:{}", e.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                String.join(",", e.getViolationMessages()),
                request.path(),
                dateTimeProvider.currentDateTime());

        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(apiError);
    }
}
