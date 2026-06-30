package com.playgg.notification.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.playgg.notification.client.UserClient;
import com.playgg.notification.dto.*;
import com.playgg.notification.exception.ResourceNotFoundException;
import com.playgg.notification.model.*;
import com.playgg.notification.repository.NotificationRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

  @Mock private NotificationRepository repository;
  @Mock private UserClient userClient;
  @InjectMocks private NotificationService service;

  @Test
  void createShouldSaveNotificationAndValidateUser() {
    when(userClient.findById(10L)).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Notification.class)))
        .thenAnswer(
            invocation -> {
              Notification notification = invocation.getArgument(0);
              notification.setNotificationId(1L);
              return notification;
            });

    NotificationResponseDTO response = service.create(createDto());

    assertNotNull(response);
    assertEquals(1L, response.getNotificationId());
    assertEquals(Boolean.FALSE, response.getRead());
    verify(userClient).findById(10L);
    verify(repository).save(any(Notification.class));
  }

  @Test
  void findByIdShouldReturnNotification() {
    when(repository.findById(1L)).thenReturn(Optional.of(notification()));

    NotificationResponseDTO response = service.findById(1L);

    assertEquals("Nuevo mensaje", response.getTitle());
    verify(repository).findById(1L);
  }

  @Test
  void findAllShouldReturnNotifications() {
    when(repository.findAll()).thenReturn(List.of(notification()));

    List<NotificationResponseDTO> response = service.findAll();

    assertEquals(1, response.size());
    assertEquals(NotificationType.MESSAGE, response.get(0).getType());
  }

  @Test
  void updateShouldModifyNotification() {
    when(repository.findById(1L)).thenReturn(Optional.of(notification()));
    when(userClient.findById(11L)).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

    NotificationResponseDTO response = service.update(1L, updateDto());

    assertEquals("Comentario", response.getTitle());
    assertEquals(Boolean.TRUE, response.getRead());
    verify(repository).save(any(Notification.class));
  }

  @Test
  void deleteShouldRemoveNotification() {
    Notification notification = notification();
    when(repository.findById(1L)).thenReturn(Optional.of(notification));

    service.delete(1L);

    verify(repository).delete(notification);
  }

  @Test
  void findByIdShouldThrowWhenNotificationDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
  }

  private CreateNotificationDTO createDto() {
    CreateNotificationDTO dto = new CreateNotificationDTO();
    dto.setUserId(10L);
    dto.setTitle("Nuevo mensaje");
    dto.setMessage("Tienes un mensaje");
    dto.setType(NotificationType.MESSAGE);
    dto.setRead(true);
    return dto;
  }

  private UpdateNotificationDTO updateDto() {
    UpdateNotificationDTO dto = new UpdateNotificationDTO();
    dto.setUserId(11L);
    dto.setTitle("Comentario");
    dto.setMessage("Comentaron tu post");
    dto.setType(NotificationType.COMMENT);
    dto.setRead(true);
    return dto;
  }

  private Notification notification() {
    return Notification.builder()
        .notificationId(1L)
        .userId(10L)
        .title("Nuevo mensaje")
        .message("Tienes un mensaje")
        .type(NotificationType.MESSAGE)
        .read(false)
        .build();
  }
}
