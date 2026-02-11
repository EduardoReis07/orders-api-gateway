package br.com.interno.orders_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para criação de um novo pedido")
public record CreateOrderRequest(
        @Schema(
                description = "Nome completo do cliente",
                example = "João da Silva",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O nome do cliente é obrigatório")
        String customerName,

        @Schema(
                description = "E-mail do cliente para contato",
                example = "joao.silva@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Email(message = "E-mail inválido")
        @NotBlank(message = "O e-mail do cliente é obrigatório")
        String customerEmail
) {}

