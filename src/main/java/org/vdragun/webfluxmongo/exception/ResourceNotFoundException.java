package org.vdragun.webfluxmongo.exception;

/**
 * Signals that some resource can not be found
 */
public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
