package com.playgg.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.playgg.user.dto.*;
import com.playgg.user.exception.ResourceNotFoundException;
import com.playgg.user.model.*;
import com.playgg.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository repository;
  @InjectMocks private UserService service;

  @Test
  void createShouldSaveUser() {
    CreateUserDTO dto = createDto();
    when(repository.existsByNickname(dto.getNickname())).thenReturn(false);
    when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
    when(repository.save(any(User.class)))
        .thenAnswer(
            invocation -> {
              User user = invocation.getArgument(0);
              user.setUserId(1L);
              return user;
            });

    UserResponseDTO response = service.create(dto);

    assertNotNull(response);
    assertEquals(1L, response.getUserId());
    assertEquals("riley", response.getNickname());
    assertEquals(Boolean.TRUE, response.getActive());
    verify(repository).save(any(User.class));
  }

  @Test
  void createShouldThrowWhenNicknameExists() {
    CreateUserDTO dto = createDto();
    when(repository.existsByNickname(dto.getNickname())).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> service.create(dto));
    verify(repository, never()).save(any(User.class));
  }

  @Test
  void findByIdShouldReturnUser() {
    when(repository.findById(1L)).thenReturn(Optional.of(user()));

    UserResponseDTO response = service.findById(1L);

    assertEquals("riley", response.getNickname());
    verify(repository).findById(1L);
  }

  @Test
  void findAllShouldReturnUsers() {
    when(repository.findAll()).thenReturn(List.of(user()));

    List<UserResponseDTO> response = service.findAll();

    assertEquals(1, response.size());
    assertEquals("riley@example.com", response.get(0).getEmail());
  }

  @Test
  void updateShouldModifyUser() {
    UpdateUserDTO dto = updateDto();
    when(repository.findById(1L)).thenReturn(Optional.of(user()));
    when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    UserResponseDTO response = service.update(1L, dto);

    assertEquals("coder", response.getNickname());
    assertEquals(Role.ADMIN, response.getRole());
    verify(repository).save(any(User.class));
  }

  @Test
  void deleteShouldRemoveUser() {
    User user = user();
    when(repository.findById(1L)).thenReturn(Optional.of(user));

    service.delete(1L);

    verify(repository).delete(user);
  }

  @Test
  void findByIdShouldThrowWhenUserDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
  }

  private CreateUserDTO createDto() {
    CreateUserDTO dto = new CreateUserDTO();
    dto.setNickname("riley");
    dto.setFirstName("Riley");
    dto.setLastName("Dev");
    dto.setEmail("riley@example.com");
    dto.setPassword("password123");
    dto.setCountry("CL");
    dto.setRole(Role.PLAYER);
    dto.setActive(true);
    return dto;
  }

  private UpdateUserDTO updateDto() {
    UpdateUserDTO dto = new UpdateUserDTO();
    dto.setNickname("coder");
    dto.setFirstName("Code");
    dto.setLastName("Admin");
    dto.setEmail("coder@example.com");
    dto.setPassword("password456");
    dto.setCountry("CL");
    dto.setRole(Role.ADMIN);
    dto.setActive(true);
    return dto;
  }

  private User user() {
    return User.builder()
        .userId(1L)
        .nickname("riley")
        .firstName("Riley")
        .lastName("Dev")
        .email("riley@example.com")
        .password("password123")
        .country("CL")
        .role(Role.PLAYER)
        .active(true)
        .build();
  }
}
