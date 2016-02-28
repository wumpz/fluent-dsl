package org.tw.fluentdsl;

/**
 *
 * @author toben
 */
public class FluentDslException extends Exception {

    public FluentDslException() {
    }

    public FluentDslException(String message) {
        super(message);
    }

    public FluentDslException(String message, Throwable cause) {
        super(message, cause);
    }

    public FluentDslException(Throwable cause) {
        super(cause);
    }
}
