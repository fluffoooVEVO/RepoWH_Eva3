package com.MainProducto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.MainProducto",
    "model",
    "repository",
    "service",
    "controller",
    "config",
    "DTO",
    "Exception"
})
public class MainProductoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainProductoApplication.class, args);
    }

}
