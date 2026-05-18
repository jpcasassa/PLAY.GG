package com.playgg.chat.service;

import com.playgg.chat.client.UserClient;
import com.playgg.chat.dto.*;
import com.playgg.chat.exception.ResourceNotFoundException;
import com.playgg.chat.model.*;
import com.playgg.chat.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service con logica de negocio. Flujo CSR: Controller -> Service -> Repository. */
@Service
@RequiredArgsConstructor
public class MessageService {
  private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
  private final MessageRepository repository;
  private final UserClient userClient;

  public MessageResponseDTO create(CreateMessageDTO dto) {
    validateUsers(dto.getSenderId(), dto.getReceiverId());
    Message e = new Message();
    e.setSenderId(dto.getSenderId());
    e.setReceiverId(dto.getReceiverId());
    e.setContent(dto.getContent());
    e.setRead(dto.getRead());
    e.setSentAt(LocalDateTime.now());
    e.setRead(false);
    logger.info("Creando Message");
    return toResponse(repository.save(e));
  }

  public List<MessageResponseDTO> findAll() {
    logger.info("Listando Message");
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public MessageResponseDTO findById(Long id) {
    return toResponse(get(id));
  }

  public MessageResponseDTO update(Long id, UpdateMessageDTO dto) {
    Message e = get(id);
    validateUsers(dto.getSenderId(), dto.getReceiverId());
    e.setSenderId(dto.getSenderId());
    e.setReceiverId(dto.getReceiverId());
    e.setContent(dto.getContent());
    e.setRead(dto.getRead());
    logger.info("Actualizando Message {}", id);
    return toResponse(repository.save(e));
  }

  public void delete(Long id) {
    repository.delete(get(id));
    logger.info("Eliminando Message {}", id);
  }

  private void validateUsers(Long senderId, Long receiverId) {
    userClient.findById(senderId);
    userClient.findById(receiverId);
  }

  private Message get(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Message no encontrado con id: " + id));
  }

  private MessageResponseDTO toResponse(Message e) {
    return MessageResponseDTO.builder()
        .messageId(e.getMessageId())
        .senderId(e.getSenderId())
        .receiverId(e.getReceiverId())
        .content(e.getContent())
        .sentAt(e.getSentAt())
        .read(e.getRead())
        .build();
  }
}
