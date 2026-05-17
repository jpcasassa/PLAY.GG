package com.playgg.auth.client;

import com.playgg.auth.dto.RegisterRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Auth consulta user-service por Feign para registrar y validar usuarios. */
@FeignClient(name = "user-service")
public interface UserClient {

  @PostMapping("/users")
  ResponseEntity<UserDataDTO> create(@RequestBody RegisterRequestDTO request);

  @GetMapping("/users/internal/auth/email/{email}")
  ResponseEntity<UserDataDTO> findByEmail(@PathVariable String email);

  record UserDataDTO(
      Long userId, String nickname, String email, String password, String role, Boolean active) {}
}
