package org.ilopes.bankaccount.personalaccount;

/**
 * This exception is raised when an operation can't be realized.
 */
public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
