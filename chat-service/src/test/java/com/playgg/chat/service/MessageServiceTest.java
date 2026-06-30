package com.playgg.chat.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.playgg.chat.client.UserClient;
import com.playgg.chat.dto.*;
import com.playgg.chat.exception.ResourceNotFoundException;
import com.playgg.chat.model.Message;
import com.playgg.chat.repository.MessageRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @Mock private MessageRepository repository;
  @Mock private UserClient userClient;
  @InjectMocks private MessageService service;

  @Test
  void createShouldSaveMessageAndValidateUsers() {
    when(userClient.findById(any())).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Message.class)))
        .thenAnswer(
            invocation -> {
              Message message = invocation.getArgument(0);
              message.setMessageId(1L);
              return message;
            });

    MessageResponseDTO response = service.create(createDto());

    assertNotNull(response);
    assertEquals(1L, response.getMessageId());
    assertEquals(Boolean.FALSE, response.getRead());
    verify(userClient).findById(10L);
    verify(userClient).findById(20L);
    verify(repository).save(any(Message.class));
  }

  @Test
  void findByIdShouldReturnMessage() {
    when(repository.findById(1L)).thenReturn(Optional.of(message()));

    MessageResponseDTO response = service.findById(1L);

    assertEquals("Hola", response.getContent());
    verify(repository).findById(1L);
  }

  @Test
  void findAllShouldReturnMessages() {
    when(repository.findAll()).thenReturn(List.of(message()));

    List<MessageResponseDTO> response = service.findAll();

    assertEquals(1, response.size());
    assertEquals(20L, response.get(0).getReceiverId());
  }

  @Test
  void updateShouldModifyMessage() {
    when(repository.findById(1L)).thenReturn(Optional.of(message()));
    when(userClient.findById(any())).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

    MessageResponseDTO response = service.update(1L, updateDto());

    assertEquals("Actualizado", response.getContent());
    assertEquals(Boolean.TRUE, response.getRead());
    verify(repository).save(any(Message.class));
  }

  @Test
  void deleteShouldRemoveMessage() {
    Message message = message();
    when(repository.findById(1L)).thenReturn(Optional.of(message));

    service.delete(1L);

    verify(repository).delete(message);
  }

  @Test
  void findByIdShouldThrowWhenMessageDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
  }

  private CreateMessageDTO createDto() {
    CreateMessageDTO dto = new CreateMessageDTO();
    dto.setSenderId(10L);
    dto.setReceiverId(20L);
    dto.setContent("Hola");
    dto.setRead(true);
    return dto;
  }

  private UpdateMessageDTO updateDto() {
    UpdateMessageDTO dto = new UpdateMessageDTO();
    dto.setSenderId(10L);
    dto.setReceiverId(20L);
    dto.setContent("Actualizado");
    dto.setRead(true);
    return dto;
  }

  private Message message() {
    return Message.builder()
        .messageId(1L)
        .senderId(10L)
        .receiverId(20L)
        .content("Hola")
        .read(false)
        .build();
  }
}
