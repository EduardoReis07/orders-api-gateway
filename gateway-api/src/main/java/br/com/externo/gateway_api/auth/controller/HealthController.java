package br.com.externo.gateway_api.auth.controller;

import br.com.externo.gateway_api.auth.dto.HealthResponse;
import br.com.externo.gateway_api.client.OrdersClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final OrdersClient ordersClient;

    @GetMapping("/orders")
    public Mono<HealthResponse> ordersHealth() {
        return ordersClient.checkHealth();
    }
}

