package com.playgg.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Entidad JPA: esta clase se mapea a una tabla de la base de datos.
@Entity
// Nombre de la tabla asociada a esta entidad.
@Table(name = "auth_sessions")
public class AuthSession {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long sessionId;

  @NotNull private Long userId;

  @Column(length = 600)
  private String token;

  @Column(length = 600)
  private String refreshToken;

  private LocalDateTime createdAt;
  private LocalDateTime expiresAt;
  private Boolean revoked;
}
