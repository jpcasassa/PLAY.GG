package com.playgg.game.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Entidad JPA de game-service. Las referencias externas se guardan como ids, no como relaciones
 * entre microservicios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Entidad JPA: esta clase se mapea a una tabla de la base de datos.
@Entity
// Nombre de la tabla asociada a esta entidad.
@Table(name = "games")
public class Game {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long gameId;

  @NotBlank private String title;
  @NotBlank private String genre;
  @NotBlank private String platform;
  private Boolean multiplayer;
  private Boolean competitive;
  private String imageUrl;
}
