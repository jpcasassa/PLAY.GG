package com.playgg.community.model;

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
@Table(name = "communities")
public class Community {

  // Clave primaria del registro.
  @Id
  // El id se genera automaticamente en la base de datos.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long communityId;

  @NotNull private Long ownerId;

  @NotBlank
  @Column(unique = true)
  private String name;

  @NotBlank private String description;
  private String bannerUrl;
  private LocalDateTime createdAt;
  private Boolean active;

  // Relacion JPA interna para modelar composicion dentro del mismo servicio.
  @Builder.Default
  @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CommunityMember> members = new ArrayList<>();
}
