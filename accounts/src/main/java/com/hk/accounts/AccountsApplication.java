package com.hk.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(info = @Info(
        title = "Accounts Microservice REST API Documentation",
        description = "Eazy Bank Accounts Microservice REST API Documentation",
        version = "v1",
        contact = @Contact(name = "Harish Krishnan", email = "harish@hk.com", url = "https://www.hk.com"),
        license = @License(
                name = "Apache 2.0",
                url = "https://www.apache2.com"
        )
), externalDocs = @ExternalDocumentation(
        description = "Eazy Bank Accounts Microservice REST API Documentation",
        url = "https://www.hk.com"
))
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
