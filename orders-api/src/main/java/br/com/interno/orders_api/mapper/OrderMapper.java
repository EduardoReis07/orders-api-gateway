package br.com.interno.orders_api.mapper;

import br.com.interno.orders_api.dto.AddOrderItemRequest;
import br.com.interno.orders_api.dto.CreateOrderRequest;
import br.com.interno.orders_api.dto.OrderResponse;
import br.com.interno.orders_api.model.Order;
import br.com.interno.orders_api.model.OrderItem;

public class OrderMapper {

    public static Order toEntity(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setCustomerEmail(request.customerEmail());
        return order;
    }

    public static OrderItem toEntity(AddOrderItemRequest request) {
        OrderItem item = new OrderItem();
        item.setProductName(request.productName());
        item.setQuantity(request.quantity());
        item.setUnitPrice(request.unitPrice());
        return item;
    }

    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount()
        );
    }
}

