package br.com.externo.gateway_api.auth.controller;

import br.com.externo.gateway_api.auth.dto.HealthResponse;
import br.com.externo.gateway_api.client.OrdersClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Tag(name = "Health Check", description = "Endpoints para verificação de saúde dos serviços")
public class HealthController {

    private final OrdersClient ordersClient;

    @Operation(
            summary = "Verificar saúde da Orders API",
            description = "Verifica se a Orders API está disponível e respondendo corretamente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço disponível",
                    content = @Content(schema = @Schema(implementation = HealthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Serviço indisponível"
            )
    })
    @GetMapping("/orders")
    public Mono<HealthResponse> ordersHealth() {
        return ordersClient.checkHealth();
    }
}

