package com.playgg.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entrada del microservicio chat-service. Feign permite consultar otros servicios por nombre
 * registrado en Eureka.
 */
@EnableFeignClients
@SpringBootApplication
public class ChatServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(ChatServiceApplication.class, args);
  }
}
