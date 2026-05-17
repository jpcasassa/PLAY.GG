package com.playgg.auth.service;

import com.playgg.auth.client.UserClient;
import com.playgg.auth.dto.*;
import com.playgg.auth.exception.ResourceNotFoundException;
import com.playgg.auth.model.AuthSession;
import com.playgg.auth.repository.AuthSessionRepository;
import com.playgg.auth.util.*;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service de autenticacion: valida credenciales, genera JWT y guarda sesiones. */
@Service
@RequiredArgsConstructor
public class AuthService {
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
  private final UserClient userClient;
  private final AuthSessionRepository repository;
  private final JwtUtil jwtUtil;

  public AuthResponseDTO register(RegisterRequestDTO dto) {
    UserClient.UserDataDTO user = userClient.create(dto).getBody();
    if (user == null) throw new IllegalArgumentException("No se pudo crear usuario");
    return createSession(user);
  }

  public AuthResponseDTO login(LoginRequestDTO dto) {
    UserClient.UserDataDTO user = userClient.findByEmail(dto.getEmail()).getBody();
    if (user == null
        || Boolean.FALSE.equals(user.active())
        || !dto.getPassword().equals(user.password()))
      throw new IllegalArgumentException("Credenciales invalidas");
    logger.info("Login correcto para {}", dto.getEmail());
    return createSession(user);
  }

  public AuthResponseDTO refresh(RefreshRequestDTO dto) {
    AuthSession old =
        repository
            .findByRefreshTokenAndRevokedFalse(dto.getRefreshToken())
            .orElseThrow(() -> new ResourceNotFoundException("Refresh token no valido"));
    old.setRevoked(true);
    repository.save(old);
    UserClient.UserDataDTO user =
        new UserClient.UserDataDTO(
            old.getUserId(), null, String.valueOf(old.getUserId()), null, "PLAYER", true);
    return createSession(user);
  }

  public void logout(LogoutRequestDTO dto) {
    AuthSession s =
        repository
            .findByTokenAndRevokedFalse(dto.getToken())
            .orElseThrow(() -> new ResourceNotFoundException("Sesion no encontrada"));
    s.setRevoked(true);
    repository.save(s);
    logger.info("Sesion revocada {}", s.getSessionId());
  }

  private AuthResponseDTO createSession(UserClient.UserDataDTO user) {
    String role = user.role() == null ? "PLAYER" : user.role();
    String token = jwtUtil.generateToken(user.userId(), user.email(), role);
    String refresh = jwtUtil.generateRefreshToken(user.userId());
    AuthSession session =
        AuthSession.builder()
            .userId(user.userId())
            .token(token)
            .refreshToken(refresh)
            .createdAt(DateUtil.now())
            .expiresAt(LocalDateTime.now().plusHours(1))
            .revoked(false)
            .build();
    repository.save(session);
    return AuthResponseDTO.builder()
        .userId(user.userId())
        .token(token)
        .refreshToken(refresh)
        .role(role)
        .build();
  }
}
