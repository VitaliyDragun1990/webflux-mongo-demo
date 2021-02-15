package org.vdragun.webfluxmongo.api.v1.exception.mapper.internal.provider;

import am.ik.yavi.core.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.vdragun.webfluxmongo.exception.ValidationException;
import org.vdragun.webfluxmongo.util.provider.DateTimeProvider;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidationExceptionErrorHandlerProvider")
class ValidationExceptionErrorHandlerProviderTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2021, 2, 10, 10, 30, 0);

    private static final String VIOLATION_MESSAGE = "Violation message";

    private static final String REQUEST_PATH = "/request/path";

    @Mock
    private DateTimeProvider dateTimeProviderMock;

    @InjectMocks
    private ValidationExceptionErrorHandlerProvider provider;

    @Test
    void shouldReturnServerResponseWithStatusBadRequestWhenHandlingValidationException(
            @Mock ConstraintViolation constraintViolationMock,
            @Mock ServerRequest requestMock) {
        ValidationException exception = new ValidationException(List.of(constraintViolationMock), "Validation error");

        when(constraintViolationMock.message()).thenReturn(VIOLATION_MESSAGE);
        when(requestMock.path()).thenReturn(REQUEST_PATH);
        when(dateTimeProviderMock.currentDateTime()).thenReturn(NOW);

        StepVerifier
                .create(provider.handleError(exception, requestMock))
                .assertNext(response -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }
}