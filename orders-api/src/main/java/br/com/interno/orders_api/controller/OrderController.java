package br.com.interno.orders_api.controller;

import br.com.interno.orders_api.dto.AddOrderItemRequest;
import br.com.interno.orders_api.dto.CreateOrderRequest;
import br.com.interno.orders_api.dto.OrderResponse;
import br.com.interno.orders_api.mapper.OrderMapper;
import br.com.interno.orders_api.model.Order;
import br.com.interno.orders_api.model.OrderItem;
import br.com.interno.orders_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(
            summary = "Listar pedidos",
            description = "Retorna uma lista paginada de todos os pedidos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    })
    public Page<Order> list(@ParameterObject Pageable pageable) {
        return orderService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna os dados de um pedido específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public Order findById(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id
    ) {
        return orderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Criar pedido",
            description = "Cria um novo pedido com status PENDING e valor total zerado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public OrderResponse create(
            @RequestBody @Valid CreateOrderRequest request
    ) {
        Order order = OrderMapper.toEntity(request);
        return OrderMapper.toResponse(orderService.create(order));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar pedido",
            description = "Atualiza os dados de um pedido existente (nome, e-mail e status)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public Order update(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid Order order
    ) {
        return orderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Excluir pedido",
            description = "Remove um pedido e todos os seus itens"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public void delete(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id
    ) {
        orderService.delete(id);
    }

    @GetMapping("/{id}/items")
    @Operation(
            summary = "Listar itens do pedido",
            description = "Retorna todos os itens de um pedido específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public List<OrderItem> listItems(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id
    ) {
        return orderService.listItems(id);
    }

    @PostMapping("/{id}/items")
    @Operation(
            summary = "Adicionar item ao pedido",
            description = "Adiciona um novo item ao pedido e recalcula o valor total automaticamente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public OrderResponse addItem(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid AddOrderItemRequest request
    ) {
        OrderItem item = OrderMapper.toEntity(request);
        return OrderMapper.toResponse(orderService.addItem(id, item));
    }

}

