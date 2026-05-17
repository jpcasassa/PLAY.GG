package com.playgg.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * Entidad JPA de user-service. Las referencias externas se guardan como ids, no como relaciones
 * entre microservicios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Entidad JPA: esta clase se mapea a una tabla de la base de datos.
@Entity
// Nombre de la tabla asociada a esta entidad.
@Table(name = "users")
public class User {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @NotBlank
  @Column(nullable = false, unique = true)
  private String nickname;

  @NotBlank private String firstName;
  @NotBlank private String lastName;

  @Email
  @NotBlank
  @Column(nullable = false, unique = true)
  private String email;

  @NotBlank
  @Size(min = 8)
  private String password;

  @NotBlank private String country;

  // Guarda el enum como texto para que sea mas legible en MySQL.
  @Enumerated(EnumType.STRING)
  private Role role;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean active;
}
