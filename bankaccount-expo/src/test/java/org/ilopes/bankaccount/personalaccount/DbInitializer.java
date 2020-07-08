package org.ilopes.bankaccount.personalaccount;

/**
 * This interface defines contract for DbInitializers. DbInitializer is a service used by tests to initially populate
 * databases.
 */
public interface DbInitializer {
    void saveAccountStatus(AccountStatus accountStatus);
    void saveOperation(Operation<?> operation);
}
