package com.playgg.user.controller;

import com.playgg.user.dto.*;
import com.playgg.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/** Controller REST: recibe HTTP y siempre responde con ResponseEntity. */
@RestController
// Ruta base del recurso. Todos los endpoints de esta clase comienzan con esta URL.
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  // POST se usa para crear un nuevo recurso. El cuerpo llega como JSON con @RequestBody.
  @PostMapping
  public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody CreateUserDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping("/nickname/{nickname}")
  public ResponseEntity<UserResponseDTO> findByNickname(@PathVariable String nickname) {
    return ResponseEntity.ok(service.findByNickname(nickname));
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping("/email/{email}")
  public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
    return ResponseEntity.ok(service.findByEmail(email));
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping("/internal/auth/email/{email}")
  public ResponseEntity<UserAuthDTO> findAuthDataByEmail(@PathVariable String email) {
    return ResponseEntity.ok(service.findAuthDataByEmail(email));
  }

  // PUT actualiza un recurso existente identificado normalmente por su id.
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> update(
      @PathVariable Long id, @Valid @RequestBody UpdateUserDTO dto) {
    return ResponseEntity.ok(service.update(id, dto));
  }

  // DELETE elimina un recurso y responde 204 No Content cuando resulta correcto.
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
