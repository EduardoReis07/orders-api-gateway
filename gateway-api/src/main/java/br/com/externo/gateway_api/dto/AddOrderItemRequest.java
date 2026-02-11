package br.com.externo.gateway_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para adicionar um item ao pedido")
public record AddOrderItemRequest(
        @Schema(
                description = "Nome do produto",
                example = "Camiseta Azul M",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O nome do produto é obrigatório")
        String productName,

        @Schema(
                description = "Quantidade do item",
                example = "2",
                minimum = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade mínima é 1")
        Integer quantity,

        @Schema(
                description = "Preço unitário do produto",
                example = "49.90",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O preço unitário é obrigatório")
        @Positive(message = "O preço deve ser positivo")
        BigDecimal unitPrice
) {}
