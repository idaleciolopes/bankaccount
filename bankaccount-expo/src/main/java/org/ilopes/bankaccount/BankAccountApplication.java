package org.ilopes.bankaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"org.ilopes.bankaccount"})
@EnableSwagger2
public class BankAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankAccountApplication.class, args);
    }
}
