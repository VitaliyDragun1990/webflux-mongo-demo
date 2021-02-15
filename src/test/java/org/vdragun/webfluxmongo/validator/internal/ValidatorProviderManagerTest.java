package org.vdragun.webfluxmongo.validator.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vdragun.webfluxmongo.exception.ConfigurationException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidatorProviderManager")
class ValidatorProviderManagerTest {

    private ValidatorProviderManager manager;

    @Test
    void shouldReturnExceptionIfNoValidationProvidedFound() {
        manager = new ValidatorProviderManager(List.of());

        StepVerifier
                .create(manager.validate(new Subject()))
                .expectError(ConfigurationException.class)
                .verifyThenAssertThat();
    }

    @Test
    void shouldUseAppropriateValidationProvider(@Mock ValidationProvider<Subject> providerMock) {
        manager = new ValidatorProviderManager(List.of(providerMock));
        Subject subject = new Subject();

        when(providerMock.supports(eq(Subject.class))).thenReturn(true);
        when(providerMock.validate(eq(subject))).thenReturn(Mono.just(subject));

        StepVerifier
                .create(manager.validate(subject))
                .assertNext(result -> assertThat(result).isEqualTo(subject))
                .verifyComplete();
        verify(providerMock, times(1)).validate(eq(subject));
    }

    static class Subject {

    }
}