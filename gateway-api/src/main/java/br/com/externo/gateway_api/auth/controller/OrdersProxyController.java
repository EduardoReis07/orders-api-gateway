package br.com.externo.gateway_api.auth.controller;

import br.com.externo.gateway_api.service.OrdersProxyService;
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
public class OrdersProxyController {

    private final OrdersProxyService proxyService;

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

