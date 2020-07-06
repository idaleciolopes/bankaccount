package org.ilopes.bankaccount.personalaccount;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.*;

/**
 * This annotation is a constraint for java Bean validation framework adding the two standard annotations @NotNull
 * and @Positive.
 */
@Documented
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@Positive
@Constraint(validatedBy = {})
public @interface NonNullPositive {
    String message() default "unexpected lower case violation";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
