package br.com.externo.gateway_api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Resposta contendo o token JWT de autenticação")
public class LoginResponse {

    @Schema(
            description = "Token JWT válido por 1 hora. Use no header Authorization: Bearer {token}",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c3VhcmlvIiwiaWF0IjoxNzA5MjM0NTY3LCJleHAiOjE3MDkyMzgxNjd9.abc123"
    )
    private String token;
}
