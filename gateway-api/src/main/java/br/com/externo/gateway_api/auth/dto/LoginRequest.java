package br.com.externo.gateway_api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "Dados de autenticação do usuário")
@Data
public class LoginRequest {

    @Schema(example = "usuario")
    @NotBlank
    private String username;

    @Schema(example = "senha123")
    @NotBlank
    private String password;
}
