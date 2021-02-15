package org.vdragun.webfluxmongo.api.v1.exception.mapper.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.vdragun.webfluxmongo.api.v1.exception.mapper.ErrorHandler;
import org.vdragun.webfluxmongo.api.v1.exception.mapper.internal.provider.ErrorHandlerProvider;
import org.vdragun.webfluxmongo.util.provider.DateTimeProvider;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ErrorHandlerProviderManager implements ErrorHandler {

    private final List<ErrorHandlerProvider> errorHandlerProviders;

    private final DateTimeProvider dateTimeProvider;

    @Override
    public Mono<ServerResponse> handlerError(Throwable t, ServerRequest request) {
        Optional<ErrorHandlerProvider> provider = findSuitableProvider(t);

        return provider.isPresent()
                ? provider.get().handleError(t, request)
                : handleErrorFallback(t, request);
    }

    private Optional<ErrorHandlerProvider> findSuitableProvider(Throwable t) {
        return errorHandlerProviders.stream().filter(p -> p.canHandle(t)).findFirst();
    }

    private Mono<ServerResponse> handleErrorFallback(Throwable t, ServerRequest request) {
        LOG.error("Handling Throwable:{}", t.getMessage(), t);

        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, request.path(), dateTimeProvider.currentDateTime());

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(apiError);
    }
}
