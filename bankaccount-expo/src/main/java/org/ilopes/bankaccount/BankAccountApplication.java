package org.ilopes.bankaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.ilopes.bankaccount", "org.ilopes.bankaccount.personalaccount","org.ilopes.bankaccount.rest"})
public class BankAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankAccountApplication.class, args);
    }
}
