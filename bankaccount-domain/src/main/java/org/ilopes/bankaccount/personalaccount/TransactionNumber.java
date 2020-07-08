package org.ilopes.bankaccount.personalaccount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ilopes.bankaccount.ddd.BaseValidatable;
import org.ilopes.bankaccount.ddd.DDD;
import org.ilopes.bankaccount.ddd.RestrictedUsage;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * This value object is designed to be used as a transaction number. Transaction number is the identifier of an
 * operation. It's a simple class 4 UUID in this example, should be something more "normalized" in a real life scenario.
 */
@DDD.ValueObjectId
@Data @EqualsAndHashCode(callSuper = false)
public class TransactionNumber extends BaseValidatable<TransactionNumber> implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private UUID value;

    public TransactionNumber(@NotNull UUID value) {
        super(TransactionNumber.class);
        this.value = value;
        validate();
    }

    @RestrictedUsage.ForJpaOnly
    // java:S2637 According to DDD an object can't be instantiate to an unstable state but JPA is not DDD compliant and
    // needs a non args constructor, but JPA will fill value just after instantiation so it never will be null
    @SuppressWarnings("java:S2637")
    protected TransactionNumber() {
        super(TransactionNumber.class);
        value = null;
    }

    @RestrictedUsage.ForJpaOnly
    private void setValue(UUID value) {
        this.value = value;
        validate();
    }

    public TransactionNumber(@NotNull String value) {
        this(UUID.fromString(value));
    }

    public String asString() {
        return value.toString();
    }
}
