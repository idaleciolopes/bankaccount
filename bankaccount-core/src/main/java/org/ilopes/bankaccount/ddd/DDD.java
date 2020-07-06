package org.ilopes.bankaccount.ddd;

import java.lang.annotation.*;

/**
 * This utility class is just used to group annotations used to document DDD patterns. Unfortunately some DDD patterns
 * names collides with very used Java annotations, so I choose to have the DDD prefix to identify this annotations.
 */
public final class DDD {
    /**
     * This annotation is used to mark a value object. A value object is an immutable object without any identity.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface ValueObject {
    }

    /**
     * This annotation is used to mark an entity. An entity is an object with a lifecycle managed in the current bounded
     * context and with an identity.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface Entity {

    }

    /**
     * This annotation is used to mark a value object designed to be an ID of an entity.
     *
     * @see ValueObject
     * @see Entity
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface ValueObjectId {

    }

    /**
     * This annotation is used to mark a domain service.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface DomainService {
    }

    /**
     * This annotation is used to mark an application service. An application service is usually the entry point for
     * an US implementation.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface ApplicationService {
        String usRef() default "";
        String usDescription() default "";
    }

    /**
     * This annotation is used to mark an infrastructure service.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface InfrastructureService {

    }

    /**
     * This annotation is used to mark a DTO.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface Dto {

    }

    private DDD() {}
}
