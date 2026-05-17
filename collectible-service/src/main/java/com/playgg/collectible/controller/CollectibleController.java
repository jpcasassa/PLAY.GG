package com.playgg.collectible.controller;

import com.playgg.collectible.dto.*;
import com.playgg.collectible.service.CollectibleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/** Controller REST: recibe HTTP y siempre responde con ResponseEntity. */
@RestController
// Ruta base del recurso. Todos los endpoints de esta clase comienzan con esta URL.
@RequestMapping("/collectibles")
@RequiredArgsConstructor
public class CollectibleController {
  private final CollectibleService service;

  // POST se usa para crear un nuevo recurso. El cuerpo llega como JSON con @RequestBody.
  @PostMapping
  public ResponseEntity<CollectibleResponseDTO> create(
      @Valid @RequestBody CreateCollectibleDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping
  public ResponseEntity<List<CollectibleResponseDTO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping("/{id}")
  public ResponseEntity<CollectibleResponseDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  // PUT actualiza un recurso existente identificado normalmente por su id.
  @PutMapping("/{id}")
  public ResponseEntity<CollectibleResponseDTO> update(
      @PathVariable Long id, @Valid @RequestBody UpdateCollectibleDTO dto) {
    return ResponseEntity.ok(service.update(id, dto));
  }

  // DELETE elimina un recurso y responde 204 No Content cuando resulta correcto.
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
