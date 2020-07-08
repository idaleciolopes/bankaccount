package org.ilopes.bankaccount.personalaccount.rest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.ilopes.bankaccount.BankAccountApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "classpath:features"},
        extraGlue = "org.ilopes.bankaccount.personalaccount.rest.RestAccountStepsDefs",
        tags = "@TestWithRest"
)

@SpringBootTest(classes = BankAccountApplication.class)
@Transactional
public class RunFeaturesWithRestIT {
}
