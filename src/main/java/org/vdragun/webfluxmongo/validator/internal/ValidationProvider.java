package org.vdragun.webfluxmongo.validator.internal;

import reactor.core.publisher.Mono;

public interface ValidationProvider<T> {

    boolean supports(Class<T> subjectClass);

    Mono<T> validate(T subject);
}
