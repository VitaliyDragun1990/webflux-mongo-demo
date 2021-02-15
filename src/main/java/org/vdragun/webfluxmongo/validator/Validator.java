package org.vdragun.webfluxmongo.validator;

import reactor.core.publisher.Mono;

public interface Validator {

    <T>Mono<T> validate(T subject);
}
