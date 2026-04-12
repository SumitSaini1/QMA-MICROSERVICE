package com.quantity.quantity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Quantity Service API", version = "v1"))
public class QuantityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuantityServiceApplication.class, args);
    }
}