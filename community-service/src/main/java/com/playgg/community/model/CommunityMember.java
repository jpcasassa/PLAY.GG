package com.playgg.community.model;

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
@Table(name = "community_members")
public class CommunityMember {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  // Relacion JPA interna del mismo microservicio. No cruza bases de datos.
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "community_id")
  private Community community;

  @NotNull private Long userId;
  private LocalDateTime joinedAt;

  // Guarda el enum como texto para que sea mas legible en MySQL.
  @Enumerated(EnumType.STRING)
  private CommunityRole role;
}
