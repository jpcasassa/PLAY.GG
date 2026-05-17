package com.playgg.chat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * Entidad JPA de chat-service. Las referencias externas se guardan como ids, no como relaciones
 * entre microservicios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Entidad JPA: esta clase se mapea a una tabla de la base de datos.
@Entity
// Nombre de la tabla asociada a esta entidad.
@Table(name = "messages")
public class Message {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long messageId;

  @NotNull private Long senderId;
  @NotNull private Long receiverId;

  @NotBlank
  @Size(max = 1000)
  private String content;

  private LocalDateTime sentAt;
  private Boolean read;
}
