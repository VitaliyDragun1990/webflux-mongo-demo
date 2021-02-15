package org.vdragun.webfluxmongo.validator.internal.impl;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vdragun.webfluxmongo.api.v1.dto.NodeDto;
import org.vdragun.webfluxmongo.exception.ValidationException;
import org.vdragun.webfluxmongo.validator.internal.ValidationProvider;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class NodeDtoValidationProvider implements ValidationProvider<NodeDto> {

    private final Validator<NodeDto> validator;

    public NodeDtoValidationProvider() {
        this.validator = ValidatorBuilder.of(NodeDto.class)
                .constraint(NodeDto::getName, "name", c -> c.notBlank().message("node name is required"))
                .build();
    }

    @Override
    public boolean supports(Class<NodeDto> subjectClass) {
        return NodeDto.class.equals(subjectClass);
    }

    @Override
    public Mono<NodeDto> validate(NodeDto subject) {
        ConstraintViolations validationConstraints = validator.validate(subject);

        return validationConstraints.isValid()
                ? Mono.just(subject)
                : Mono.error(new ValidationException(validationConstraints.violations(), "Provided %s is not valid", subject));
    }
}
