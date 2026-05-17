package com.playgg.collectible;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entrada del microservicio collectible-service. Feign permite consultar otros servicios por nombre
 * registrado en Eureka.
 */
@EnableFeignClients
@SpringBootApplication
public class CollectibleServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(CollectibleServiceApplication.class, args);
  }
}
