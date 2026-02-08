package br.com.externo.gateway_api.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (!"usuario".equals(username)) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username("usuario")
                .password(passwordEncoder.encode("senha123"))
                .roles("USER")
                .build();
    }
}

