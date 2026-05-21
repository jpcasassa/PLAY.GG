package com.playgg.auth.service;

import com.playgg.auth.client.UserClient;
import com.playgg.auth.dto.*;

import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service de autenticacion basica: registra usuarios y valida credenciales. */
@Service
@RequiredArgsConstructor
public class AuthService {
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
  private final UserClient userClient;

  public AuthResponseDTO register(RegisterRequestDTO dto) {
    UserClient.UserDataDTO user = userClient.create(dto).getBody();
    if (user == null) throw new IllegalArgumentException("No se pudo crear usuario");
    return toResponse(user, "Usuario registrado correctamente");
  }

  public AuthResponseDTO login(LoginRequestDTO dto) {
    UserClient.UserDataDTO user = userClient.findByEmail(dto.getEmail()).getBody();
    if (user == null
        || Boolean.FALSE.equals(user.active())
        || !dto.getPassword().equals(user.password()))
      throw new IllegalArgumentException("Credenciales invalidas");
    logger.info("Login correcto para {}", dto.getEmail());
    return toResponse(user, "Login correcto");
  }

  private AuthResponseDTO toResponse(UserClient.UserDataDTO user, String message) {
    String role = user.role() == null ? "PLAYER" : user.role();
    return AuthResponseDTO.builder()
        .userId(user.userId())
        .nickname(user.nickname())
        .email(user.email())
        .role(role)
        .message(message)
        .build();
  }
}
