package org.ilopes.bankaccount.personalaccount;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "classpath:features/US1_deposit_money_in_my_account.feature"},
        extraGlue = "org.ilopes.bankaccount.personalaccount.AccountStepsDefs"
)
public class DepositMoneyInMyAccountTest {
}
