package com.playgg.profile.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.playgg.profile.client.GameClient;
import com.playgg.profile.client.UserClient;
import com.playgg.profile.dto.*;
import com.playgg.profile.exception.ResourceNotFoundException;
import com.playgg.profile.model.Profile;
import com.playgg.profile.repository.ProfileRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

  @Mock private ProfileRepository repository;
  @Mock private UserClient userClient;
  @Mock private GameClient gameClient;
  @InjectMocks private ProfileService service;

  @Test
  void createShouldSaveProfileAndValidateExternalIds() {
    when(userClient.findById(10L)).thenReturn(ResponseEntity.ok().build());
    when(gameClient.findById(20L)).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Profile.class)))
        .thenAnswer(
            invocation -> {
              Profile profile = invocation.getArgument(0);
              profile.setProfileId(1L);
              return profile;
            });

    ProfileResponseDTO response = service.create(createDto());

    assertNotNull(response);
    assertEquals(1L, response.getProfileId());
    assertEquals(10L, response.getUserId());
    verify(userClient).findById(10L);
    verify(gameClient).findById(20L);
    verify(repository).save(any(Profile.class));
  }

  @Test
  void findByIdShouldReturnProfile() {
    when(repository.findById(1L)).thenReturn(Optional.of(profile()));

    ProfileResponseDTO response = service.findById(1L);

    assertEquals("Immortal", response.getRank());
    verify(repository).findById(1L);
  }

  @Test
  void findAllShouldReturnProfiles() {
    when(repository.findAll()).thenReturn(List.of(profile()));

    List<ProfileResponseDTO> response = service.findAll();

    assertEquals(1, response.size());
    assertEquals("Riley bio", response.get(0).getBio());
  }

  @Test
  void updateShouldModifyProfile() {
    when(repository.findById(1L)).thenReturn(Optional.of(profile()));
    when(userClient.findById(11L)).thenReturn(ResponseEntity.ok().build());
    when(gameClient.findById(21L)).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ProfileResponseDTO response = service.update(1L, updateDto());

    assertEquals(11L, response.getUserId());
    assertEquals("Radiant", response.getRank());
    verify(repository).save(any(Profile.class));
  }

  @Test
  void deleteShouldRemoveProfile() {
    Profile profile = profile();
    when(repository.findById(1L)).thenReturn(Optional.of(profile));

    service.delete(1L);

    verify(repository).delete(profile);
  }

  @Test
  void findByIdShouldThrowWhenProfileDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
  }

  @Test
  void createShouldNotCallGameClientWhenFavoriteGameIsNull() {
    CreateProfileDTO dto = createDto();
    dto.setFavoriteGameId(null);
    when(userClient.findById(10L)).thenReturn(ResponseEntity.ok().build());
    when(repository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ProfileResponseDTO response = service.create(dto);

    assertEquals(10L, response.getUserId());
    verify(gameClient, never()).findById(any());
  }

  private CreateProfileDTO createDto() {
    CreateProfileDTO dto = new CreateProfileDTO();
    dto.setUserId(10L);
    dto.setAvatarUrl("avatar.png");
    dto.setBannerUrl("banner.png");
    dto.setBio("Riley bio");
    dto.setSteamUsername("rileySteam");
    dto.setDiscordUsername("riley#1234");
    dto.setFavoriteGameId(20L);
    dto.setRank("Immortal");
    dto.setLevel(40);
    return dto;
  }

  private UpdateProfileDTO updateDto() {
    UpdateProfileDTO dto = new UpdateProfileDTO();
    dto.setUserId(11L);
    dto.setAvatarUrl("avatar2.png");
    dto.setBannerUrl("banner2.png");
    dto.setBio("Updated bio");
    dto.setSteamUsername("coderSteam");
    dto.setDiscordUsername("coder#1234");
    dto.setFavoriteGameId(21L);
    dto.setRank("Radiant");
    dto.setLevel(50);
    return dto;
  }

  private Profile profile() {
    return Profile.builder()
        .profileId(1L)
        .userId(10L)
        .avatarUrl("avatar.png")
        .bannerUrl("banner.png")
        .bio("Riley bio")
        .steamUsername("rileySteam")
        .discordUsername("riley#1234")
        .favoriteGameId(20L)
        .rank("Immortal")
        .level(40)
        .build();
  }
}
