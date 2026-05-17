package com.playgg.community.controller;

import com.playgg.community.dto.*;
import com.playgg.community.service.CommunityService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// Marca la clase como controlador REST: sus metodos responden JSON por HTTP.
@RestController
// Ruta base del recurso. Todos los endpoints de esta clase comienzan con esta URL.
@RequestMapping("/communities")
@RequiredArgsConstructor
public class CommunityController {
  private final CommunityService service;

  // POST se usa para crear un nuevo recurso. El cuerpo llega como JSON con @RequestBody.
  @PostMapping
  public ResponseEntity<CommunityResponseDTO> create(@Valid @RequestBody CreateCommunityDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping
  public ResponseEntity<List<CommunityResponseDTO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping("/{id}")
  public ResponseEntity<CommunityResponseDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  // PUT actualiza un recurso existente identificado normalmente por su id.
  @PutMapping("/{id}")
  public ResponseEntity<CommunityResponseDTO> update(
      @PathVariable Long id, @Valid @RequestBody UpdateCommunityDTO dto) {
    return ResponseEntity.ok(service.update(id, dto));
  }

  // DELETE elimina un recurso y responde 204 No Content cuando resulta correcto.
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  // POST se usa para crear un nuevo recurso. El cuerpo llega como JSON con @RequestBody.
  @PostMapping("/{id}/members")
  public ResponseEntity<CommunityMemberResponseDTO> addMember(
      @PathVariable Long id, @Valid @RequestBody AddMemberDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.addMember(id, dto));
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping("/{id}/members")
  public ResponseEntity<List<CommunityMemberResponseDTO>> members(@PathVariable Long id) {
    return ResponseEntity.ok(service.members(id));
  }
}
