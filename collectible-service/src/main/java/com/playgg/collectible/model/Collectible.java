package com.playgg.collectible.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * Entidad JPA de collectible-service. Las referencias externas se guardan como ids, no como
 * relaciones entre microservicios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Entidad JPA: esta clase se mapea a una tabla de la base de datos.
@Entity
// Nombre de la tabla asociada a esta entidad.
@Table(name = "collectibles")
public class Collectible {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long collectibleId;

  @NotNull private Long userId;
  @NotNull private Long gameId;
  @NotBlank private String name;

  @Size(max = 300)
  private String description;

  // Guarda el enum como texto para que sea mas legible en MySQL.
  @Enumerated(EnumType.STRING)
  private Rarity rarity;

  private LocalDateTime unlockedAt;
}
