package com.playgg.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.playgg.auth.client.UserClient;
import com.playgg.auth.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private UserClient userClient;
  @InjectMocks private AuthService service;

  @Test
  void registerShouldCreateUserThroughUserClient() {
    RegisterRequestDTO dto = registerDto();
    when(userClient.create(dto)).thenReturn(ResponseEntity.ok(userData()));

    AuthResponseDTO response = service.register(dto);

    assertNotNull(response);
    assertEquals(1L, response.getUserId());
    assertEquals("Usuario registrado correctamente", response.getMessage());
    verify(userClient).create(dto);
  }

  @Test
  void registerShouldThrowWhenUserClientReturnsEmptyBody() {
    RegisterRequestDTO dto = registerDto();
    when(userClient.create(dto)).thenReturn(ResponseEntity.ok().build());

    assertThrows(IllegalArgumentException.class, () -> service.register(dto));
  }

  @Test
  void loginShouldReturnResponseWhenCredentialsAreValid() {
    LoginRequestDTO dto = loginDto("riley@example.com", "password123");
    when(userClient.findByEmail(dto.getEmail())).thenReturn(ResponseEntity.ok(userData()));

    AuthResponseDTO response = service.login(dto);

    assertEquals("Login correcto", response.getMessage());
    assertEquals("PLAYER", response.getRole());
    verify(userClient).findByEmail("riley@example.com");
  }

  @Test
  void loginShouldThrowWhenPasswordIsInvalid() {
    LoginRequestDTO dto = loginDto("riley@example.com", "bad-password");
    when(userClient.findByEmail(dto.getEmail())).thenReturn(ResponseEntity.ok(userData()));

    assertThrows(IllegalArgumentException.class, () -> service.login(dto));
  }

  @Test
  void loginShouldThrowWhenUserIsInactive() {
    LoginRequestDTO dto = loginDto("riley@example.com", "password123");
    UserClient.UserDataDTO inactive =
        new UserClient.UserDataDTO(1L, "riley", "riley@example.com", "password123", "PLAYER", false);
    when(userClient.findByEmail(dto.getEmail())).thenReturn(ResponseEntity.ok(inactive));

    assertThrows(IllegalArgumentException.class, () -> service.login(dto));
  }

  private RegisterRequestDTO registerDto() {
    RegisterRequestDTO dto = new RegisterRequestDTO();
    dto.setNickname("riley");
    dto.setFirstName("Riley");
    dto.setLastName("Dev");
    dto.setEmail("riley@example.com");
    dto.setPassword("password123");
    dto.setCountry("CL");
    return dto;
  }

  private LoginRequestDTO loginDto(String email, String password) {
    LoginRequestDTO dto = new LoginRequestDTO();
    dto.setEmail(email);
    dto.setPassword(password);
    return dto;
  }

  private UserClient.UserDataDTO userData() {
    return new UserClient.UserDataDTO(1L, "riley", "riley@example.com", "password123", "PLAYER", true);
  }
}
