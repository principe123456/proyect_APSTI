package com.example.proyect_APSTI.config;

import com.example.proyect_APSTI.model.Usuario;
import com.example.proyect_APSTI.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService userDetailsService(
            @Value("${LOGIN_USERNAME}") String username,
            @Value("${LOGIN_PASSWORD}") String password,
            PasswordEncoder encoder,
            UsuarioRepository repositorio) {
        if (username.isBlank() || password.isBlank()) {
            throw new IllegalStateException(
                    "Define las variables de entorno APP_LOGIN_USERNAME y APP_LOGIN_PASSWORD.");
        }
        UserDetails admin = User.withUsername(username)
                .password(encoder.encode(password)).roles("ADMIN").build();
        return correo -> {
            Usuario usuario = repositorio.findByCorreo(correo).orElse(null);
            if (usuario != null) {
                return User.withUsername(usuario.getCorreo())
                        .password(usuario.getContrasena())
                        .roles(usuario.getRol())
                        .disabled(!"Activo".equalsIgnoreCase(usuario.getEstado()))
                        .build();
            }
            if (admin.getUsername().equals(correo)) {
                return admin;
            }
            throw new UsernameNotFoundException("No existe un usuario con ese correo.");
        };
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login")
                .defaultSuccessUrl("/usuarios", true).permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
            .build();
    }
}
