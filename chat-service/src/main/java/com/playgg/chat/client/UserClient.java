package com.playgg.chat.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Feign consulta user-service para validar senderId y receiverId. */
@FeignClient(name = "user-service")
public interface UserClient {

  @GetMapping("/users/{id}")
  ResponseEntity<Object> findById(@PathVariable Long id);
}
