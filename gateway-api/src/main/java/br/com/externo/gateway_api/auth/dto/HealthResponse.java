package br.com.externo.gateway_api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta do health check de um serviço")
public record HealthResponse(
        @Schema(
                description = "Status do serviço",
                example = "UP",
                allowableValues = {"UP", "DOWN"}
        )
        String status
) {
}
