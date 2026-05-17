package com.playgg.notification.service;

import com.playgg.notification.dto.*;
import com.playgg.notification.exception.ResourceNotFoundException;
import com.playgg.notification.model.*;
import com.playgg.notification.repository.NotificationRepository;
import com.playgg.notification.util.DateUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service con logica de negocio. Flujo CSR: Controller -> Service -> Repository. */
@Service
@RequiredArgsConstructor
public class NotificationService {
  private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
  private final NotificationRepository repository;

  public NotificationResponseDTO create(CreateNotificationDTO dto) {
    Notification e = new Notification();
    e.setUserId(dto.getUserId());
    e.setTitle(dto.getTitle());
    e.setMessage(dto.getMessage());
    e.setType(dto.getType());
    e.setRead(dto.getRead());
    e.setCreatedAt(DateUtil.now());
    e.setRead(false);
    logger.info("Creando Notification");
    return toResponse(repository.save(e));
  }

  public List<NotificationResponseDTO> findAll() {
    logger.info("Listando Notification");
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public NotificationResponseDTO findById(Long id) {
    return toResponse(get(id));
  }

  public NotificationResponseDTO update(Long id, UpdateNotificationDTO dto) {
    Notification e = get(id);
    e.setUserId(dto.getUserId());
    e.setTitle(dto.getTitle());
    e.setMessage(dto.getMessage());
    e.setType(dto.getType());
    e.setRead(dto.getRead());
    logger.info("Actualizando Notification {}", id);
    return toResponse(repository.save(e));
  }

  public void delete(Long id) {
    repository.delete(get(id));
    logger.info("Eliminando Notification {}", id);
  }

  private Notification get(Long id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Notification no encontrado con id: " + id));
  }

  private NotificationResponseDTO toResponse(Notification e) {
    return NotificationResponseDTO.builder()
        .notificationId(e.getNotificationId())
        .userId(e.getUserId())
        .title(e.getTitle())
        .message(e.getMessage())
        .type(e.getType())
        .read(e.getRead())
        .createdAt(e.getCreatedAt())
        .build();
  }
}
