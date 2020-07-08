package org.ilopes.bankaccount.ddd;

import javax.validation.ConstraintViolationException;

/**
 * A validatable object is an object with a validation of state each time it state evolves. This way an object can never
 * have an invalid state.
 *
 * @param <V> the concrete type of the validatable object.
 */
public interface Validatable<V extends Validatable<V>> {
    /**
     * Validates the current object.
     *
     * @throws ConstraintViolationException a business rule was not respected while trying to validate current object.
     */
    // java:S1130 : I choice to declare throws ConstraintViolationException even if it is not necessary to improve
    // readability. This exception is expected in case business rules are not respected, so it is a regular result of
    // the method.
    @SuppressWarnings("java:S1130")
    void validate() throws ConstraintViolationException;
}
