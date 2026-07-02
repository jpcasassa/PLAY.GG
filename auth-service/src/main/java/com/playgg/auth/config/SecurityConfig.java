package com.playgg.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion simple de Spring Security para el proyecto universitario.
 *
 * <p>Login y registro quedan abiertos para las pruebas. Cualquier endpoint futuro quedara protegido
 * con HTTP Basic.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  // Bean administrado por Spring; queda disponible para inyeccion en la aplicacion.
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/auth/login",
                        "/auth/register",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        // Activa autenticacion basica HTTP para endpoints protegidos futuros.
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  /**
   * Usuario en memoria para pruebas. En un sistema productivo estos usuarios se guardarian en base
   * de datos.
   */

  // Bean administrado por Spring; queda disponible para inyeccion en la aplicacion.
  @Bean
  UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails admin =
        User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .roles("ADMIN")
            .build();
    return new InMemoryUserDetailsManager(admin);
  }

  // Bean administrado por Spring; queda disponible para inyeccion en la aplicacion.
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
