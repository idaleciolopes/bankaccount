package org.ilopes.bankaccount.ddd;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Base class for validatable objects with an implementation of validation based on Java Bean Validation Framework.
 */
public abstract class BaseValidatable<V extends BaseValidatable<V>> implements Validatable<V> {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final Class<V> concreteType;

    public BaseValidatable(Class<V> concreteType) {
        this.concreteType = concreteType;
    }

    @RestrictedUsage.ForLombokOnly
    private BaseValidatable() {
        concreteType = null;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void validate() {
        Set<ConstraintViolation<V>> violations = validator.validate(concreteType.cast(this));
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
