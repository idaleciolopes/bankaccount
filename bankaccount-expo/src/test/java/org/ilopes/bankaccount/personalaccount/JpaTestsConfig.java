package org.ilopes.bankaccount.personalaccount;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Configuration
@EnableJpaRepositories
@SpringBootApplication(scanBasePackages = "org.ilopes.bankaccount.personalaccount")
public class JpaTestsConfig {
}
