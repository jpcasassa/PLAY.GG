package com.playgg.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entrada del microservicio game-service. Feign permite consultar otros servicios por nombre
 * registrado en Eureka.
 */
@SpringBootApplication
public class GameServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(GameServiceApplication.class, args);
  }
}
