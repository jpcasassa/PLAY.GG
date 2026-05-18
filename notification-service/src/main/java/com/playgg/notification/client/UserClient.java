package com.playgg.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** Feign se usa solo para comprobar que el usuario exista antes de crear una notificacion. */
@FeignClient(name = "user-service")
public interface UserClient {

  @GetMapping("/users/{id}")
  ResponseEntity<Object> findById(@PathVariable Long id);
}