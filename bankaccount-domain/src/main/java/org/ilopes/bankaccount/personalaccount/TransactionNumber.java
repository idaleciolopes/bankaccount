package org.ilopes.bankaccount.personalaccount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ilopes.bankaccount.ddd.BaseValidatable;
import org.ilopes.bankaccount.ddd.DDD;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * This value object is designed to be used as a transaction number. Transaction number is the identifier of an
 * operation. It's a simple class 4 UUID in this example, should be something more "normalized" in a real life scenario.
 */
@DDD.ValueObjectId
@Data @EqualsAndHashCode(callSuper = false)
public class TransactionNumber extends BaseValidatable<TransactionNumber> {
    @NotNull
    private final UUID value;

    public TransactionNumber(@NotNull UUID value) {
        super(TransactionNumber.class);
        this.value = value;
        validate();
    }

    public String asString() {
        return value.toString();
    }
}
