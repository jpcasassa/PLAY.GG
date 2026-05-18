package com.playgg.user.service;

import com.playgg.user.dto.*;
import com.playgg.user.exception.ResourceNotFoundException;
import com.playgg.user.model.*;
import com.playgg.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service con logica de negocio. Flujo CSR: Controller -> Service -> Repository. */
@Service
@RequiredArgsConstructor
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserRepository repository;

  public UserResponseDTO create(CreateUserDTO dto) {
    if (repository.existsByNickname(dto.getNickname()))
      throw new IllegalArgumentException("nickname ya existe");
    if (repository.existsByEmail(dto.getEmail()))
      throw new IllegalArgumentException("email ya existe");
    User e = new User();
    e.setNickname(dto.getNickname());
    e.setFirstName(dto.getFirstName());
    e.setLastName(dto.getLastName());
    e.setEmail(dto.getEmail());
    e.setPassword(dto.getPassword());
    e.setCountry(dto.getCountry());
    e.setRole(dto.getRole());
    e.setActive(dto.getActive());
    e.setCreatedAt(LocalDateTime.now());
    e.setActive(true);
    logger.info("Creando User");
    return toResponse(repository.save(e));
  }

  public List<UserResponseDTO> findAll() {
    logger.info("Listando User");
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public UserResponseDTO findById(Long id) {
    return toResponse(get(id));
  }

  public UserResponseDTO findByNickname(String nickname) {
    return repository
        .findByNickname(nickname)
        .map(this::toResponse)
        .orElseThrow(
            () -> new ResourceNotFoundException("Usuario no encontrado con nickname: " + nickname));
  }

  public UserResponseDTO findByEmail(String email) {
    return repository
        .findByEmail(email)
        .map(this::toResponse)
        .orElseThrow(
            () -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
  }

  public UserAuthDTO findAuthDataByEmail(String email) {
    User e =
        repository
            .findByEmail(email)
            .orElseThrow(
                () -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    return UserAuthDTO.builder()
        .userId(e.getUserId())
        .nickname(e.getNickname())
        .email(e.getEmail())
        .password(e.getPassword())
        .role(e.getRole())
        .active(e.getActive())
        .build();
  }

  public UserResponseDTO update(Long id, UpdateUserDTO dto) {
    User e = get(id);
    e.setNickname(dto.getNickname());
    e.setFirstName(dto.getFirstName());
    e.setLastName(dto.getLastName());
    e.setEmail(dto.getEmail());
    e.setPassword(dto.getPassword());
    e.setCountry(dto.getCountry());
    e.setRole(dto.getRole());
    e.setActive(dto.getActive());
    e.setUpdatedAt(LocalDateTime.now());
    logger.info("Actualizando User {}", id);
    return toResponse(repository.save(e));
  }

  public void delete(Long id) {
    repository.delete(get(id));
    logger.info("Eliminando User {}", id);
  }

  private User get(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con id: " + id));
  }

  private UserResponseDTO toResponse(User e) {
    return UserResponseDTO.builder()
        .userId(e.getUserId())
        .nickname(e.getNickname())
        .firstName(e.getFirstName())
        .lastName(e.getLastName())
        .email(e.getEmail())
        .country(e.getCountry())
        .role(e.getRole())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .active(e.getActive())
        .build();
  }
}
