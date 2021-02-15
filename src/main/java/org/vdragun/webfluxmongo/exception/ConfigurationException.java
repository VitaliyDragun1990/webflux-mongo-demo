package org.vdragun.webfluxmongo.exception;

public class ConfigurationException extends ApplicationException {

    public ConfigurationException(String message, Object... args) {
        super(message, args);
    }

    public ConfigurationException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
