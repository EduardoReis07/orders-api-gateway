package br.com.externo.gateway_api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Resposta com token JWT")
public class LoginResponse {

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
}
