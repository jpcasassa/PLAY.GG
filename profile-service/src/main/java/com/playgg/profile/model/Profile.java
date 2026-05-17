package com.playgg.profile.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * Entidad JPA de profile-service. Las referencias externas se guardan como ids, no como relaciones
 * entre microservicios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Entidad JPA: esta clase se mapea a una tabla de la base de datos.
@Entity
// Nombre de la tabla asociada a esta entidad.
@Table(name = "profiles")
public class Profile {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long profileId;

  @NotNull private Long userId;
  private String avatarUrl;
  private String bannerUrl;

  @Size(max = 300)
  private String bio;

  private String steamUsername;
  private String discordUsername;
  private Long favoriteGameId;
  private String rank;
  private Integer level;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
