package br.com.externo.gateway_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gateway API")
                        .version("1.0.0")
                        .description("""
                                API Gateway responsável pela autenticação e autorização de requisições.
                                
                                Esta API gerencia tokens JWT e atua como proxy para a Orders API.
                                
                                **Fluxo de autenticação:**
                                1. Faça login no endpoint `/auth/login` com suas credenciais
                                2. Copie o token JWT retornado
                                3. Use o token no header `Authorization: Bearer {token}` para acessar os endpoints protegidos
                                """)
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@exemplo.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT obtido no endpoint /auth/login")));
    }
}
