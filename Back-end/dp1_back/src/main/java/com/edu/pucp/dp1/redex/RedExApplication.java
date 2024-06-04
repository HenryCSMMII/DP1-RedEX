package com.edu.pucp.dp1.redex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedExApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedExApplication.class, args);
        System.out.println("Ejecutanding...");
	}
}