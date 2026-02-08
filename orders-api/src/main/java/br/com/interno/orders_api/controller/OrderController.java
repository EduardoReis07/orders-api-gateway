package br.com.interno.orders_api.controller;

import br.com.interno.orders_api.dto.AddOrderItemRequest;
import br.com.interno.orders_api.dto.CreateOrderRequest;
import br.com.interno.orders_api.dto.OrderResponse;
import br.com.interno.orders_api.mapper.OrderMapper;
import br.com.interno.orders_api.model.Order;
import br.com.interno.orders_api.model.OrderItem;
import br.com.interno.orders_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    @Operation(summary = "List all orders with pagination")
    public Page<Order> list(@ParameterObject Pageable pageable) {
        return orderService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id")
    public Order findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new order")
    public OrderResponse create(
            @RequestBody @Valid CreateOrderRequest request
    ) {
        Order order = OrderMapper.toEntity(request);
        return OrderMapper.toResponse(orderService.create(order));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update an existing order")
    public Order update(
            @PathVariable Long id,
            @RequestBody @Valid Order order
    ) {
        return orderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an order")
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "List items from an order")
    public List<OrderItem> listItems(@PathVariable Long id) {
        return orderService.listItems(id);
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Add item to an order")
    public OrderResponse addItem(
            @PathVariable Long id,
            @RequestBody @Valid AddOrderItemRequest request
    ) {
        OrderItem item = OrderMapper.toEntity(request);
        return OrderMapper.toResponse(orderService.addItem(id, item));
    }


}

