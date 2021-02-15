package org.vdragun.webfluxmongo.validator.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vdragun.webfluxmongo.exception.ConfigurationException;
import org.vdragun.webfluxmongo.validator.Validator;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@Component
public class ValidatorProviderManager implements Validator {

    private final List<ValidationProvider> validationProviders;

    @Override
    public <T> Mono<T> validate(T subject) {
        return findProviderFor(subject.getClass())
                .switchIfEmpty(Mono.error(() -> new ConfigurationException("Fail to find validator for %s type", subject.getClass())))
                .flatMap(v -> v.validate(subject));
    }

    private Mono<ValidationProvider> findProviderFor(Class<?> subjectClass) {
        Optional<ValidationProvider> validatorOptional = validationProviders
                .stream()
                .filter(p -> p.supports(subjectClass))
                .findFirst();

        return Mono.justOrEmpty(validatorOptional);
    }
}
