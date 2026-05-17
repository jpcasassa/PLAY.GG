package com.playgg.chat.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Cliente Feign hacia notification-service. */
@FeignClient(name = "notification-service")
public interface NotificationClient {

  @GetMapping("/notifications/{id}")
  ResponseEntity<Object> findById(@PathVariable Long id);
}
