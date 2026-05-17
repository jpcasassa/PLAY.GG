package com.playgg.forum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Entidad JPA: esta clase se mapea a una tabla de la base de datos.
@Entity
// Nombre de la tabla asociada a esta entidad.
@Table(name = "posts")
public class Post {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @NotNull private Long userId;
  @NotBlank private String title;

  @NotBlank
  @Column(length = 2000)
  private String content;

  @NotBlank private String category;
  private Integer likes;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Relacion JPA interna para modelar composicion dentro del mismo servicio.
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();
}
