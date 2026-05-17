package com.playgg.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entrada del microservicio forum-service. Feign permite consultar otros servicios por nombre
 * registrado en Eureka.
 */
@EnableFeignClients
@SpringBootApplication
public class ForumServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(ForumServiceApplication.class, args);
  }
}
