package br.com.externo.gateway_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para atualização de um pedido")
public record UpdateOrderRequest(
        @Schema(
                description = "Nome completo do cliente",
                example = "João da Silva Atualizado",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O nome do cliente é obrigatório")
        String customerName,

        @Schema(
                description = "E-mail do cliente para contato",
                example = "joao.novo@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Email(message = "E-mail inválido")
        @NotBlank(message = "O e-mail do cliente é obrigatório")
        String customerEmail,

        @Schema(
                description = "Status do pedido",
                example = "CONFIRMED",
                allowableValues = {"PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"},
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O status é obrigatório")
        String status
) {}
