package com.gsalles.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarrentalApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CarrentalApplication.class);
		app.setAdditionalProfiles("prod");
		SpringApplication.run(CarrentalApplication.class, args);
	}

}
