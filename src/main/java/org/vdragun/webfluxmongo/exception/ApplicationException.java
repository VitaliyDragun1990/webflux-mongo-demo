package org.vdragun.webfluxmongo.exception;

import static java.lang.String.format;

/**
 * Application-specific top-level parent exception
 */
public abstract class ApplicationException extends RuntimeException {

    public ApplicationException(String message, Object... args) {
        super(format(message, args));
    }

    public ApplicationException(Throwable cause, String message, Object... args) {
        super(format(message, args), cause);
    }
}
