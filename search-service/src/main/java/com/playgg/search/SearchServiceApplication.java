package com.playgg.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entrada del microservicio search-service. Feign permite consultar otros servicios por nombre
 * registrado en Eureka.
 */
@EnableFeignClients
@SpringBootApplication
public class SearchServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(SearchServiceApplication.class, args);
  }
}
