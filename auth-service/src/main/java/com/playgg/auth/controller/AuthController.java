package com.playgg.auth.controller;

import com.playgg.auth.dto.*;
import com.playgg.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// Marca la clase como controlador REST: sus metodos responden JSON por HTTP.
@RestController
// Ruta base del recurso. Todos los endpoints de esta clase comienzan con esta URL.
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService service;

  // POST se usa para crear un nuevo recurso. El cuerpo llega como JSON con @RequestBody.
  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
    return ResponseEntity.ok(service.login(dto));
  }

  // POST se usa para crear un nuevo recurso. El cuerpo llega como JSON con @RequestBody.
  @PostMapping("/register")
  public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.register(dto));
  }

}
