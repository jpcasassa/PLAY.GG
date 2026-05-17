package com.playgg.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entrada del microservicio community-service. Feign permite consultar otros servicios por nombre
 * registrado en Eureka.
 */
@EnableFeignClients
@SpringBootApplication
public class CommunityServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(CommunityServiceApplication.class, args);
  }
}
