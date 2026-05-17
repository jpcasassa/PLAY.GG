package com.playgg.collectible.service;

import com.playgg.collectible.dto.*;
import com.playgg.collectible.exception.ResourceNotFoundException;
import com.playgg.collectible.model.*;
import com.playgg.collectible.repository.CollectibleRepository;
import com.playgg.collectible.util.DateUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service con logica de negocio. Flujo CSR: Controller -> Service -> Repository. */
@Service
@RequiredArgsConstructor
public class CollectibleService {
  private static final Logger logger = LoggerFactory.getLogger(CollectibleService.class);
  private final CollectibleRepository repository;

  public CollectibleResponseDTO create(CreateCollectibleDTO dto) {
    Collectible e = new Collectible();
    e.setUserId(dto.getUserId());
    e.setGameId(dto.getGameId());
    e.setName(dto.getName());
    e.setDescription(dto.getDescription());
    e.setRarity(dto.getRarity());
    e.setUnlockedAt(DateUtil.now());
    logger.info("Creando Collectible");
    return toResponse(repository.save(e));
  }

  public List<CollectibleResponseDTO> findAll() {
    logger.info("Listando Collectible");
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public CollectibleResponseDTO findById(Long id) {
    return toResponse(get(id));
  }

  public CollectibleResponseDTO update(Long id, UpdateCollectibleDTO dto) {
    Collectible e = get(id);
    e.setUserId(dto.getUserId());
    e.setGameId(dto.getGameId());
    e.setName(dto.getName());
    e.setDescription(dto.getDescription());
    e.setRarity(dto.getRarity());
    logger.info("Actualizando Collectible {}", id);
    return toResponse(repository.save(e));
  }

  public void delete(Long id) {
    repository.delete(get(id));
    logger.info("Eliminando Collectible {}", id);
  }

  private Collectible get(Long id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Collectible no encontrado con id: " + id));
  }

  private CollectibleResponseDTO toResponse(Collectible e) {
    return CollectibleResponseDTO.builder()
        .collectibleId(e.getCollectibleId())
        .userId(e.getUserId())
        .gameId(e.getGameId())
        .name(e.getName())
        .description(e.getDescription())
        .rarity(e.getRarity())
        .unlockedAt(e.getUnlockedAt())
        .build();
  }
}
