package com.playgg.profile.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Feign consulta game-service por id; profile guarda solo favoriteGameId. */
@FeignClient(name = "game-service")
public interface GameClient {

  @GetMapping("/games/{id}")
  ResponseEntity<Object> findById(@PathVariable Long id);
}
