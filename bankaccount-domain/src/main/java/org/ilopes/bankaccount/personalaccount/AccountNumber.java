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
 * This value object is designed to be used as a personal account ID. It's a simple class 4 UUID in this example, should
 * be something more "normalized" in a real life scenario.
 */
@DDD.ValueObjectId
@Data @EqualsAndHashCode(callSuper = false)
public class AccountNumber extends BaseValidatable<AccountNumber> implements Serializable {
    @RestrictedUsage.ForSerializationOnly
    private static final long serialVersionUID = 1L;

    @NotNull
    private UUID value;

    public AccountNumber(@NotNull UUID value) {
        super(AccountNumber.class);
        this.value = value;
        validate();
    }

    @RestrictedUsage.ForJpaOnly
    @RestrictedUsage.ForSerializationOnly
    // java:S2637 According to DDD an object can't be instantiate to an unstable state, but JPA is not DDD compliant and
    // needs a no args constructor. JPA will populate value after instantiation so it will not be null
    @SuppressWarnings("java:S2637")
    private AccountNumber() {
        super(AccountNumber.class);
        value = null;
    }

    @RestrictedUsage.ForJpaOnly
    @RestrictedUsage.ForSerializationOnly
    private void setValue(UUID value) {
        this.value = value;
        validate();
    }

    public AccountNumber(@NotNull String value) {
        this(UUID.fromString(value));
    }

    public String asString() {
        return value.toString();
    }
}
