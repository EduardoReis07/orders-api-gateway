package br.com.externo.gateway_api.auth.controller;

import br.com.externo.gateway_api.dto.AddOrderItemRequest;
import br.com.externo.gateway_api.dto.CreateOrderRequest;
import br.com.externo.gateway_api.dto.UpdateOrderRequest;
import br.com.externo.gateway_api.service.OrdersProxyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos (Proxy)", description = "Proxy para a API de Pedidos - requer autenticação JWT")
@SecurityRequirement(name = "bearerAuth")
public class OrdersProxyController {

    private final OrdersProxyService proxyService;

    @GetMapping
    @Operation(
            summary = "Listar pedidos",
            description = "Retorna uma lista paginada de todos os pedidos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "content": [
                                        {
                                          "id": 1,
                                          "customerName": "João da Silva",
                                          "customerEmail": "joao.silva@email.com",
                                          "orderDate": "2024-03-01T10:30:00",
                                          "status": "PENDING",
                                          "totalAmount": 99.80,
                                          "items": []
                                        }
                                      ],
                                      "page": 0,
                                      "size": 10,
                                      "totalPages": 5
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    public Mono<ResponseEntity<byte[]>> listOrders(
            @Parameter(description = "Número da página (inicia em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        String path = "/api/orders?page=" + page + "&size=" + size;
        return proxyService.proxy(HttpMethod.GET, path, extractHeaders(request), null);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna os dados de um pedido específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "customerName": "João da Silva",
                                      "customerEmail": "joao.silva@email.com",
                                      "orderDate": "2024-03-01T10:30:00",
                                      "status": "PENDING",
                                      "totalAmount": 99.80,
                                      "items": []
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    public Mono<ResponseEntity<byte[]>> findOrderById(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        String path = "/api/orders/" + id;
        return proxyService.proxy(HttpMethod.GET, path, extractHeaders(request), null);
    }

    @PostMapping
    @Operation(
            summary = "Criar pedido",
            description = "Cria um novo pedido com status PENDING e valor total zerado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "customerName": "João da Silva",
                                      "customerEmail": "joao.silva@email.com",
                                      "orderDate": "2024-03-01T10:30:00",
                                      "status": "PENDING",
                                      "totalAmount": 0
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    public Mono<ResponseEntity<byte[]>> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do pedido a ser criado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateOrderRequest.class)
                    )
            )
            @RequestBody @Valid CreateOrderRequest orderRequest,
            HttpServletRequest request
    ) {
        String path = "/api/orders";
        return proxyService.proxy(HttpMethod.POST, path, extractHeaders(request), 
                serializeToBytes(orderRequest));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar pedido",
            description = "Atualiza os dados de um pedido existente (nome, e-mail e status)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "customerName": "João Silva Atualizado",
                                      "customerEmail": "joao.novo@email.com",
                                      "orderDate": "2024-03-01T10:30:00",
                                      "status": "CONFIRMED",
                                      "totalAmount": 99.80,
                                      "items": []
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    public Mono<ResponseEntity<byte[]>> updateOrder(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do pedido",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateOrderRequest.class)
                    )
            )
            @RequestBody @Valid UpdateOrderRequest orderRequest,
            HttpServletRequest request
    ) {
        String path = "/api/orders/" + id;
        return proxyService.proxy(HttpMethod.PUT, path, extractHeaders(request), 
                serializeToBytes(orderRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir pedido",
            description = "Remove um pedido e todos os seus itens"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    public Mono<ResponseEntity<byte[]>> deleteOrder(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        String path = "/api/orders/" + id;
        return proxyService.proxy(HttpMethod.DELETE, path, extractHeaders(request), null);
    }

    @GetMapping("/{id}/items")
    @Operation(
            summary = "Listar itens do pedido",
            description = "Retorna todos os itens de um pedido específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": 1,
                                        "productName": "Camiseta Azul M",
                                        "quantity": 2,
                                        "unitPrice": 49.90,
                                        "subtotal": 99.80
                                      }
                                    ]
                                    """)
                    )),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    public Mono<ResponseEntity<byte[]>> listOrderItems(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        String path = "/api/orders/" + id + "/items";
        return proxyService.proxy(HttpMethod.GET, path, extractHeaders(request), null);
    }

    @PostMapping("/{id}/items")
    @Operation(
            summary = "Adicionar item ao pedido",
            description = "Adiciona um novo item ao pedido e recalcula o valor total automaticamente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "customerName": "João da Silva",
                                      "customerEmail": "joao.silva@email.com",
                                      "orderDate": "2024-03-01T10:30:00",
                                      "status": "PENDING",
                                      "totalAmount": 99.80
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    public Mono<ResponseEntity<byte[]>> addOrderItem(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do item a ser adicionado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AddOrderItemRequest.class)
                    )
            )
            @RequestBody @Valid AddOrderItemRequest itemRequest,
            HttpServletRequest request
    ) {
        String path = "/api/orders/" + id + "/items";
        return proxyService.proxy(HttpMethod.POST, path, extractHeaders(request), 
                serializeToBytes(itemRequest));
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames())
                .forEach(name -> headers.add(name, request.getHeader(name)));
        return headers;
    }

    private byte[] serializeToBytes(Object obj) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = 
                    new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao serializar objeto", e);
        }
    }

}

