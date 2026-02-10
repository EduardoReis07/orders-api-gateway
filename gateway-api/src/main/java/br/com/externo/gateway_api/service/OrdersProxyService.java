package br.com.externo.gateway_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersProxyService {

    private final WebClient ordersWebClient;

    public Mono<ResponseEntity<byte[]>> proxy(
            HttpMethod method,
            String path,
            HttpHeaders headers,
            byte[] body
    ) {
        log.info("Proxying request to Orders API: {} {}", method, path);

        return ordersWebClient
                .method(method)
                .uri(path)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue(body == null ? new byte[]{} : body)
                .exchangeToMono(response ->
                        response.toEntity(byte[].class)
                );
    }

}

