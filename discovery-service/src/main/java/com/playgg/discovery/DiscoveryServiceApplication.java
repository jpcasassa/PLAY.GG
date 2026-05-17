package com.playgg.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/** Eureka registra los microservicios para que se encuentren por nombre y no por IP fija. */
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(DiscoveryServiceApplication.class, args);
  }
}
