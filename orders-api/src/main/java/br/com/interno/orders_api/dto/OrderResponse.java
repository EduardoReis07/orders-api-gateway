package br.com.interno.orders_api.dto;

import br.com.interno.orders_api.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados do pedido retornado pela API")
public record OrderResponse(
        @Schema(description = "ID único do pedido", example = "1")
        Long id,

        @Schema(description = "Nome do cliente", example = "João da Silva")
        String customerName,

        @Schema(description = "E-mail do cliente", example = "joao.silva@email.com")
        String customerEmail,

        @Schema(description = "Data e hora de criação do pedido", example = "2024-03-01T10:30:00")
        LocalDateTime orderDate,

        @Schema(description = "Status atual do pedido", example = "PENDING")
        OrderStatus status,

        @Schema(description = "Valor total do pedido", example = "99.80")
        BigDecimal totalAmount
) {}

