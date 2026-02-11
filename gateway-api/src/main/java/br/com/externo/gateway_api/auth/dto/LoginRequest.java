package br.com.externo.gateway_api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "Dados de autenticação do usuário")
@Data
public class LoginRequest {

    @Schema(
            description = "Nome de usuário para autenticação",
            example = "usuario",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "O nome de usuário é obrigatório")
    private String username;

    @Schema(
            description = "Senha do usuário",
            example = "senha123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "A senha é obrigatória")
    private String password;
}
