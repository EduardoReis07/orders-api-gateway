package br.com.interno.orders_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orders API")
                        .version("1.0.0")
                        .description("""
                                API interna para gerenciamento de pedidos.
                                
                                Esta API é responsável por todas as operações de CRUD de pedidos e seus itens.
                                
                                **Funcionalidades:**
                                - Criar, consultar, atualizar e excluir pedidos
                                - Gerenciar itens de pedidos
                                - Cálculo automático do valor total do pedido
                                
                                **Observação:** Esta API deve ser acessada apenas através do Gateway API, 
                                que é responsável pela autenticação.
                                """)
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@exemplo.com")));
    }
}
