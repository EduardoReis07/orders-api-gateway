package br.com.interno.orders_api.dto;

import br.com.interno.orders_api.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        String customerName,
        String customerEmail,
        LocalDateTime orderDate,
        OrderStatus status,
        BigDecimal totalAmount
) {}

