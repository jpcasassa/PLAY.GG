package com.playgg.notification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * Entidad JPA de notification-service. Las referencias externas se guardan como ids, no como
 * relaciones entre microservicios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Entidad JPA: esta clase se mapea a una tabla de la base de datos.
@Entity
// Nombre de la tabla asociada a esta entidad.
@Table(name = "notifications")
public class Notification {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long notificationId;

  @NotNull private Long userId;
  @NotBlank private String title;
  @NotBlank private String message;

  // Guarda el enum como texto para que sea mas legible en MySQL.
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  private Boolean read;
  private LocalDateTime createdAt;
}
