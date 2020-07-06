package org.ilopes.bankaccount.personalaccount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ilopes.bankaccount.ddd.BaseValidatable;
import org.ilopes.bankaccount.ddd.DDD;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * This value object is designed to be used as a personal account ID. It's a simple class 4 UUID in this example, should
 * be something more "normalized" in a real life scenario.
 */
@DDD.ValueObjectId
@Data @EqualsAndHashCode(callSuper = false)
public class AccountNumber extends BaseValidatable<AccountNumber> {
    @NotNull
    private final UUID value;

    public AccountNumber(@NotNull UUID value) {
        super(AccountNumber.class);
        this.value = value;
        validate();
    }

    public String asString() {
        return value.toString();
    }
}
