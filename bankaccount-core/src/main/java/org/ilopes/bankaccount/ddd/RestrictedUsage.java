package org.ilopes.bankaccount.ddd;

import java.lang.annotation.*;

/**
 * This utility class is designed to group some annotations to document restricted accesses. Unfortunately with some
 * Java features or frameworks (serialization, JPA) we need sometimes to declare constructors or methods we prefer to
 * not exist as they are in infraction with clean code or DDD principles.
 */
public final class RestrictedUsage {
    /**
     * This constructor or method is only designed to be used by JPA. Never use this constructor.
     */
    @Documented
    @Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ForJpaOnly {

    }

    /**
     * This constructor or method is only designed to be used by Serialization. Never use this constructor.
     */
    @Documented
    @Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ForSerializationOnly {

    }

    /**
     * This constructor or method is only designed to be used by Lombok. Never use this constructor.
     */
    @Documented
    @Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ForLombokOnly {

    }

    private RestrictedUsage() {}
}
