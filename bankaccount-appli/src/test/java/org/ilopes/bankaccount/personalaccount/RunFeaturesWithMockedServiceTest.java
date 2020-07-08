package org.ilopes.bankaccount.personalaccount;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "classpath:features"},
        extraGlue = "org.ilopes.bankaccount.personalaccount.AccountStepsDefs",
        tags = "@TestWithMocks"
)
public class RunFeaturesWithMockedServiceTest {
}
