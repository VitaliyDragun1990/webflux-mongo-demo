package org.vdragun.webfluxmongo.api.v1.exception.mapper.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.vdragun.webfluxmongo.api.v1.exception.mapper.internal.provider.ErrorHandlerProvider;
import org.vdragun.webfluxmongo.util.provider.DateTimeProvider;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ErrorHandlerProviderManager")
class ErrorHandlerProviderManagerTest {

    private static final String REQUEST_PATH = "/request/path";

    @Mock
    private DateTimeProvider dateTimeProviderMock;

    private ErrorHandlerProviderManager providerManager;

    @Test
    void shouldUseDefaultHandlerIfNoSuitableProviderFound(@Mock ServerRequest requestMock) {
        providerManager = new ErrorHandlerProviderManager(List.of(), dateTimeProviderMock);

        when(requestMock.path()).thenReturn(REQUEST_PATH);

        StepVerifier
                .create(providerManager.handlerError(new Exception(), requestMock))
                .assertNext(response -> assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }

    @Test
    void shouldUseAppropriateErrorHandlerProvider(@Mock ServerRequest requestMock) {
        providerManager = new ErrorHandlerProviderManager(List.of(new TestErrorHandlerProvider()), dateTimeProviderMock);

        StepVerifier
                .create(providerManager.handlerError(new Exception(), requestMock))
                .assertNext(response -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    static class TestErrorHandlerProvider implements ErrorHandlerProvider {

        @Override
        public boolean canHandle(Throwable t) {
            return true;
        }

        @Override
        public Mono<ServerResponse> handleError(Throwable t, ServerRequest request) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}