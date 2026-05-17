package com.playgg.game.service;

import com.playgg.game.dto.*;
import com.playgg.game.exception.ResourceNotFoundException;
import com.playgg.game.model.*;
import com.playgg.game.repository.GameRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service con logica de negocio. Flujo CSR: Controller -> Service -> Repository. */
@Service
@RequiredArgsConstructor
public class GameService {
  private static final Logger logger = LoggerFactory.getLogger(GameService.class);
  private final GameRepository repository;

  public GameResponseDTO create(CreateGameDTO dto) {
    Game e = new Game();
    e.setTitle(dto.getTitle());
    e.setGenre(dto.getGenre());
    e.setPlatform(dto.getPlatform());
    e.setMultiplayer(dto.getMultiplayer());
    e.setCompetitive(dto.getCompetitive());
    e.setImageUrl(dto.getImageUrl());
    logger.info("Creando Game");
    return toResponse(repository.save(e));
  }

  public List<GameResponseDTO> findAll() {
    logger.info("Listando Game");
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public GameResponseDTO findById(Long id) {
    return toResponse(get(id));
  }

  public GameResponseDTO update(Long id, UpdateGameDTO dto) {
    Game e = get(id);
    e.setTitle(dto.getTitle());
    e.setGenre(dto.getGenre());
    e.setPlatform(dto.getPlatform());
    e.setMultiplayer(dto.getMultiplayer());
    e.setCompetitive(dto.getCompetitive());
    e.setImageUrl(dto.getImageUrl());
    logger.info("Actualizando Game {}", id);
    return toResponse(repository.save(e));
  }

  public void delete(Long id) {
    repository.delete(get(id));
    logger.info("Eliminando Game {}", id);
  }

  private Game get(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Game no encontrado con id: " + id));
  }

  private GameResponseDTO toResponse(Game e) {
    return GameResponseDTO.builder()
        .gameId(e.getGameId())
        .title(e.getTitle())
        .genre(e.getGenre())
        .platform(e.getPlatform())
        .multiplayer(e.getMultiplayer())
        .competitive(e.getCompetitive())
        .imageUrl(e.getImageUrl())
        .build();
  }
}
