package br.com.externo.gateway_api.auth.controller;

import br.com.externo.gateway_api.service.OrdersProxyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos (Proxy)", description = "Proxy para a API de Pedidos - requer autenticação JWT")
@SecurityRequirement(name = "bearerAuth")
public class OrdersProxyController {

    private final OrdersProxyService proxyService;

    @Operation(
            summary = "Proxy para Orders API",
            description = """
                    Encaminha todas as requisições para a Orders API interna.
                    
                    **Endpoints disponíveis:**
                    - `GET /api/orders` - Listar pedidos (paginado)
                    - `GET /api/orders/{id}` - Buscar pedido por ID
                    - `POST /api/orders` - Criar novo pedido
                    - `PUT /api/orders/{id}` - Atualizar pedido
                    - `DELETE /api/orders/{id}` - Excluir pedido
                    - `GET /api/orders/{id}/items` - Listar itens do pedido
                    - `POST /api/orders/{id}/items` - Adicionar item ao pedido
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Requisição processada com sucesso"),
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Recurso excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @RequestMapping(value = "/**")
    public Mono<ResponseEntity<byte[]>> proxyOrders(
            HttpMethod method,
            HttpServletRequest request,
            @RequestBody(required = false) byte[] body
    ) {

        String path = request.getRequestURI();

        return proxyService.proxy(
                method,
                path,
                extractHeaders(request),
                body
        );
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();

        Collections.list(request.getHeaderNames())
                .forEach(name ->
                        headers.add(name, request.getHeader(name))
                );

        return headers;
    }

}

