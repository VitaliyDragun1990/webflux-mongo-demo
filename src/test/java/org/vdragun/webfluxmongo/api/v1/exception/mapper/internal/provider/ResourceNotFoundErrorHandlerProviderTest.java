package org.vdragun.webfluxmongo.api.v1.exception.mapper.internal.provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.vdragun.webfluxmongo.exception.ResourceNotFoundException;
import org.vdragun.webfluxmongo.util.provider.DateTimeProvider;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ResourceNotFoundErrorHandlerProvider")
class ResourceNotFoundErrorHandlerProviderTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2021, 2, 10, 10, 30, 0);

    private static final String REQUEST_PATH = "/request/path";

    @Mock
    private DateTimeProvider dateTimeProviderMock;

    @InjectMocks
    private ResourceNotFoundErrorHandlerProvider provider;

    @Test
    void shouldReturnServerResponseWithStatusNotFoundWhenHandlingResourceNotFoundException(@Mock ServerRequest requestMock) {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        when(requestMock.path()).thenReturn(REQUEST_PATH);
        when(dateTimeProviderMock.currentDateTime()).thenReturn(NOW);

        StepVerifier
                .create(provider.handleError(exception, requestMock))
                .assertNext(response -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }
}