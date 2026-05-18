package com.playgg.community.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Feign consulta user-service por id; las comunidades guardan ownerId y userId. */
@FeignClient(name = "user-service")
public interface UserClient {

  @GetMapping("/users/{id}")
  ResponseEntity<Object> findById(@PathVariable Long id);
}
