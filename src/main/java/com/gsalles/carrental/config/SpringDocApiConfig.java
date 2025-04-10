package com.gsalles.carrental.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("security", securityScheme()))
                .info(new Info().title("SpringRental API").description("API para aluguel de automóveis").version("v1")
                .contact(new Contact().name("Gabriel Couzzi").email("gabrielcouzzihb20@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme().description("Insira um bearer token válido para prosseguir.")
                .type(SecurityScheme.Type.HTTP).in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("security");
    }
}
