package com.playgg.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Cliente Feign hacia community-service. */
@FeignClient(name = "community-service")
public interface CommunityClient {

  @GetMapping("/communities/{id}")
  ResponseEntity<Object> findById(@PathVariable Long id);
}
