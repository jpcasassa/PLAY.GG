package com.playgg.forum.controller;

import com.playgg.forum.dto.*;
import com.playgg.forum.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// Marca la clase como controlador REST: sus metodos responden JSON por HTTP.
@RestController
// Ruta base del recurso. Todos los endpoints de esta clase comienzan con esta URL.
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService service;

  // POST se usa para crear un nuevo recurso. El cuerpo llega como JSON con @RequestBody.
  @PostMapping
  public ResponseEntity<CommentResponseDTO> create(@Valid @RequestBody CreateCommentDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  // GET se usa para consultar informacion sin modificar datos.
  @GetMapping("/post/{postId}")
  public ResponseEntity<List<CommentResponseDTO>> findByPost(@PathVariable Long postId) {
    return ResponseEntity.ok(service.findByPost(postId));
  }
}
