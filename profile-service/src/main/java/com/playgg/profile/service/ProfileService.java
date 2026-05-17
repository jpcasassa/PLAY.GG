package com.playgg.profile.service;

import com.playgg.profile.dto.*;
import com.playgg.profile.exception.ResourceNotFoundException;
import com.playgg.profile.model.*;
import com.playgg.profile.repository.ProfileRepository;
import com.playgg.profile.util.DateUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service con logica de negocio. Flujo CSR: Controller -> Service -> Repository. */
@Service
@RequiredArgsConstructor
public class ProfileService {
  private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
  private final ProfileRepository repository;

  public ProfileResponseDTO create(CreateProfileDTO dto) {
    Profile e = new Profile();
    e.setUserId(dto.getUserId());
    e.setAvatarUrl(dto.getAvatarUrl());
    e.setBannerUrl(dto.getBannerUrl());
    e.setBio(dto.getBio());
    e.setSteamUsername(dto.getSteamUsername());
    e.setDiscordUsername(dto.getDiscordUsername());
    e.setFavoriteGameId(dto.getFavoriteGameId());
    e.setRank(dto.getRank());
    e.setLevel(dto.getLevel());
    e.setCreatedAt(DateUtil.now());
    logger.info("Creando Profile");
    return toResponse(repository.save(e));
  }

  public List<ProfileResponseDTO> findAll() {
    logger.info("Listando Profile");
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public ProfileResponseDTO findById(Long id) {
    return toResponse(get(id));
  }

  public ProfileResponseDTO update(Long id, UpdateProfileDTO dto) {
    Profile e = get(id);
    e.setUserId(dto.getUserId());
    e.setAvatarUrl(dto.getAvatarUrl());
    e.setBannerUrl(dto.getBannerUrl());
    e.setBio(dto.getBio());
    e.setSteamUsername(dto.getSteamUsername());
    e.setDiscordUsername(dto.getDiscordUsername());
    e.setFavoriteGameId(dto.getFavoriteGameId());
    e.setRank(dto.getRank());
    e.setLevel(dto.getLevel());
    e.setUpdatedAt(DateUtil.now());
    logger.info("Actualizando Profile {}", id);
    return toResponse(repository.save(e));
  }

  public void delete(Long id) {
    repository.delete(get(id));
    logger.info("Eliminando Profile {}", id);
  }

  private Profile get(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Profile no encontrado con id: " + id));
  }

  private ProfileResponseDTO toResponse(Profile e) {
    return ProfileResponseDTO.builder()
        .profileId(e.getProfileId())
        .userId(e.getUserId())
        .avatarUrl(e.getAvatarUrl())
        .bannerUrl(e.getBannerUrl())
        .bio(e.getBio())
        .steamUsername(e.getSteamUsername())
        .discordUsername(e.getDiscordUsername())
        .favoriteGameId(e.getFavoriteGameId())
        .rank(e.getRank())
        .level(e.getLevel())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }
}
