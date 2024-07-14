package com.edu.pucp.dp1.redex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;


import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.edu.pucp.dp1.redex.Algorithm.BD;

@SpringBootApplication
@EnableCaching
public class RedExApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(RedExApplication.class, args);
        	System.out.println("Ejecutanding...");
			BD.readContinents();
			BD.readCountries();
			BD.readCities();
			BD.readAirports();
			BD.readFlights();
		} catch(Exception ex) {

		}
		
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");
		config.addAllowedMethod("*");
		config.addAllowedHeader("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST","PUT", "DELETE");
                registry.addMapping("/**").allowedOrigins("http://localhost:8080").allowedMethods("GET", "POST","PUT", "DELETE");
                // Configura aquí el patrón de URL de tu API addMapping("/api/**")
                // Permite solicitudes desde el dominio del frontend allowedOrigins("http://localhost:3000"
                // Especifica los métodos HTTP permitidos allowedMethods("G
            }
        };
    }

}