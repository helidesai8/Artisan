package com.example.Artisan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The ArtisanApplication class is the entry point for the Artisan application.
 * It initializes and starts the Spring Boot application.
 */
@SpringBootApplication
public class ArtisanApplication {

	/**
	 * The main method is the entry point for the Artisan application.
	 * It starts the Spring Boot application by calling SpringApplication.run() method.
	 *
	 * @param args The command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ArtisanApplication.class, args);
	}

}
