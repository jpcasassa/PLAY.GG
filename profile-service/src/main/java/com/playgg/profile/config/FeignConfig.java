package com.playgg.profile.config;

import feign.Logger;
import org.springframework.context.annotation.*;

@Configuration
public class FeignConfig {

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.BASIC;
  }
}
