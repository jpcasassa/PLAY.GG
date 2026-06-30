package com.playgg.game.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.playgg.game.dto.*;
import com.playgg.game.exception.ResourceNotFoundException;
import com.playgg.game.model.Game;
import com.playgg.game.repository.GameRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

  @Mock private GameRepository repository;
  @InjectMocks private GameService service;

  @Test
  void createShouldSaveGame() {
    when(repository.save(any(Game.class)))
        .thenAnswer(
            invocation -> {
              Game game = invocation.getArgument(0);
              game.setGameId(1L);
              return game;
            });

    GameResponseDTO response = service.create(createDto());

    assertNotNull(response);
    assertEquals(1L, response.getGameId());
    assertEquals("Valorant", response.getTitle());
    verify(repository).save(any(Game.class));
  }

  @Test
  void findByIdShouldReturnGame() {
    when(repository.findById(1L)).thenReturn(Optional.of(game()));

    GameResponseDTO response = service.findById(1L);

    assertEquals("Valorant", response.getTitle());
    verify(repository).findById(1L);
  }

  @Test
  void findAllShouldReturnGames() {
    when(repository.findAll()).thenReturn(List.of(game()));

    List<GameResponseDTO> response = service.findAll();

    assertEquals(1, response.size());
    assertEquals("PC", response.get(0).getPlatform());
  }

  @Test
  void updateShouldModifyGame() {
    when(repository.findById(1L)).thenReturn(Optional.of(game()));
    when(repository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

    GameResponseDTO response = service.update(1L, updateDto());

    assertEquals("Counter-Strike 2", response.getTitle());
    assertEquals(Boolean.TRUE, response.getCompetitive());
    verify(repository).save(any(Game.class));
  }

  @Test
  void deleteShouldRemoveGame() {
    Game game = game();
    when(repository.findById(1L)).thenReturn(Optional.of(game));

    service.delete(1L);

    verify(repository).delete(game);
  }

  @Test
  void findByIdShouldThrowWhenGameDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
  }

  private CreateGameDTO createDto() {
    CreateGameDTO dto = new CreateGameDTO();
    dto.setTitle("Valorant");
    dto.setGenre("Shooter");
    dto.setPlatform("PC");
    dto.setMultiplayer(true);
    dto.setCompetitive(true);
    dto.setImageUrl("valorant.png");
    return dto;
  }

  private UpdateGameDTO updateDto() {
    UpdateGameDTO dto = new UpdateGameDTO();
    dto.setTitle("Counter-Strike 2");
    dto.setGenre("Shooter");
    dto.setPlatform("PC");
    dto.setMultiplayer(true);
    dto.setCompetitive(true);
    dto.setImageUrl("cs2.png");
    return dto;
  }

  private Game game() {
    return Game.builder()
        .gameId(1L)
        .title("Valorant")
        .genre("Shooter")
        .platform("PC")
        .multiplayer(true)
        .competitive(true)
        .imageUrl("valorant.png")
        .build();
  }
}
