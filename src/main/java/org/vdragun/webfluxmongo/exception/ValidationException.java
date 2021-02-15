package org.vdragun.webfluxmongo.exception;

import am.ik.yavi.core.ConstraintViolation;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ValidationException extends ApplicationException {

    private final List<ConstraintViolation> constraintViolations;

    public ValidationException(List<ConstraintViolation> constraintViolations, String message, Object... args) {
        super(message, args);
        this.constraintViolations = constraintViolations;
    }

    public List<ConstraintViolation> getConstraintViolations() {
        return List.copyOf(constraintViolations);
    }

    public List<String> getViolationMessages() {
        return constraintViolations.stream().map(ConstraintViolation::message).collect(toList());
    }

    @Override
    public String getMessage() {
        return String.format("%s:%s", super.getMessage(), constraintViolations);
    }
}
