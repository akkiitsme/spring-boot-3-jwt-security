package com.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Akshay singh",
                        email = "akshaysingh069@gmail.com",
                        url = "https://www.linkedin.com/in/akkiitsme/"
                ),
                description = "OpenApi documentation for Capacity User Services",
                title = "OpenApi specification - Akshay singh",
                version = "3.0",
                license = @License(
                        name = "Licence name",
                        url = "https://spring.io/projects/spring-cloud"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "LOCAL ENV",
                        url = "http://localhost:8081"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://www.your-site.com/"
                ),
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api") // Define the group name
                .packagesToScan("com.authservice.user","com.authservice.jwt") // Replace with your package name
                .build();
    }

    // To see swagger - http://localhost:8081/swagger-ui/index.html
}
