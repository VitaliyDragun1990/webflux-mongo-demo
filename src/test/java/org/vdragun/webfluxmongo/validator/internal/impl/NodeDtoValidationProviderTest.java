package org.vdragun.webfluxmongo.validator.internal.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.vdragun.webfluxmongo.api.v1.dto.NodeDto;
import org.vdragun.webfluxmongo.exception.ValidationException;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("NodeDtoValidationProvider")
class NodeDtoValidationProviderTest {

    private static final String NODE_NAME = "Node name";

    private static final String NODE_DESCRIPTION = "Node description";

    private NodeDtoValidationProvider provider;

    @BeforeEach
    void setUp() {
        provider = new NodeDtoValidationProvider();
    }

    @Test
    void shouldSupportCorrectValidationSubjectClass() {
        boolean result = provider.supports(NodeDto.class);

        assertTrue(result, "Should support correct subject class");
    }

    @Test
    void shouldReturnMonoWithSubjectIfNoValidationErrorFound() {
        NodeDto subject = new NodeDto(NODE_NAME, NODE_DESCRIPTION);

        StepVerifier
                .create(provider.validate(subject))
                .assertNext(result -> assertThat(result).isEqualTo(subject))
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("invalidNameProvider")
    void shouldReturnValidationErrorIfSubjectNameIsInvalid(String invalidName) {
        NodeDto subject = new NodeDto(invalidName, NODE_DESCRIPTION);

        StepVerifier
                .create(provider.validate(subject))
                .expectError(ValidationException.class)
                .verifyThenAssertThat();
    }

    static Stream<String> invalidNameProvider() {
        return Stream.of("", "   ", null);
    }
}