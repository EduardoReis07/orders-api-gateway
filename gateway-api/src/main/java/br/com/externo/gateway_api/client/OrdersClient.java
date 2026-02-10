package br.com.externo.gateway_api.client;

import br.com.externo.gateway_api.auth.dto.HealthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrdersClient {

    private final WebClient ordersWebClient;

    public Mono<HealthResponse> checkHealth() {
        return ordersWebClient
                .get()
                .uri("/actuator/health")
                .retrieve()
                .bodyToMono(HealthResponse.class);
    }

}

