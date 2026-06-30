package com.playgg.collectible.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.playgg.collectible.client.GameClient;
import com.playgg.collectible.client.UserClient;
import com.playgg.collectible.dto.*;
import com.playgg.collectible.exception.ResourceNotFoundException;
import com.playgg.collectible.model.*;
import com.playgg.collectible.repository.CollectibleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CollectibleServiceTest {

  @Mock private CollectibleRepository repository;
  @Mock private UserClient userClient;
  @Mock private GameClient gameClient;
  @InjectMocks private CollectibleService service;

  @Test
  void createShouldSaveCollectibleAndValidateExternalIds() {
    when(userClient.findById(10L)).thenReturn(ResponseEntity.ok().build());
    when(gameClient.findById(20L)).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Collectible.class)))
        .thenAnswer(
            invocation -> {
              Collectible collectible = invocation.getArgument(0);
              collectible.setCollectibleId(1L);
              return collectible;
            });

    CollectibleResponseDTO response = service.create(createDto());

    assertNotNull(response);
    assertEquals(1L, response.getCollectibleId());
    assertEquals(Rarity.EPIC, response.getRarity());
    verify(userClient).findById(10L);
    verify(gameClient).findById(20L);
  }

  @Test
  void findByIdShouldReturnCollectible() {
    when(repository.findById(1L)).thenReturn(Optional.of(collectible()));

    CollectibleResponseDTO response = service.findById(1L);

    assertEquals("Badge", response.getName());
    verify(repository).findById(1L);
  }

  @Test
  void findAllShouldReturnCollectibles() {
    when(repository.findAll()).thenReturn(List.of(collectible()));

    List<CollectibleResponseDTO> response = service.findAll();

    assertEquals(1, response.size());
    assertEquals(20L, response.get(0).getGameId());
  }

  @Test
  void updateShouldModifyCollectible() {
    when(repository.findById(1L)).thenReturn(Optional.of(collectible()));
    when(userClient.findById(11L)).thenReturn(ResponseEntity.ok().build());
    when(gameClient.findById(21L)).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Collectible.class))).thenAnswer(invocation -> invocation.getArgument(0));

    CollectibleResponseDTO response = service.update(1L, updateDto());

    assertEquals("Legend Trophy", response.getName());
    assertEquals(Rarity.LEGENDARY, response.getRarity());
    verify(repository).save(any(Collectible.class));
  }

  @Test
  void deleteShouldRemoveCollectible() {
    Collectible collectible = collectible();
    when(repository.findById(1L)).thenReturn(Optional.of(collectible));

    service.delete(1L);

    verify(repository).delete(collectible);
  }

  @Test
  void findByIdShouldThrowWhenCollectibleDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
  }

  private CreateCollectibleDTO createDto() {
    CreateCollectibleDTO dto = new CreateCollectibleDTO();
    dto.setUserId(10L);
    dto.setGameId(20L);
    dto.setName("Badge");
    dto.setDescription("Win ten matches");
    dto.setRarity(Rarity.EPIC);
    return dto;
  }

  private UpdateCollectibleDTO updateDto() {
    UpdateCollectibleDTO dto = new UpdateCollectibleDTO();
    dto.setUserId(11L);
    dto.setGameId(21L);
    dto.setName("Legend Trophy");
    dto.setDescription("Reach legend rank");
    dto.setRarity(Rarity.LEGENDARY);
    return dto;
  }

  private Collectible collectible() {
    return Collectible.builder()
        .collectibleId(1L)
        .userId(10L)
        .gameId(20L)
        .name("Badge")
        .description("Win ten matches")
        .rarity(Rarity.EPIC)
        .build();
  }
}
