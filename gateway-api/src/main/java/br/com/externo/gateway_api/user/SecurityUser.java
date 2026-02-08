package br.com.externo.gateway_api.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SecurityUser {

    private final String username;
    private final String password;
    private final List<String> roles;
}
