package org.ilopes.bankaccount.personalaccount.jpa;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.ilopes.bankaccount.personalaccount.JpaTestsConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "classpath:features/US1_deposit_money_in_my_account.feature"},
        extraGlue = "org.ilopes.bankaccount.personalaccount.jpa.JpaAccountStepsDefs"
)

@SpringBootTest(classes = JpaTestsConfig.class)
@Transactional
public class DepositMoneyInMyAccountJpaIT {
}
