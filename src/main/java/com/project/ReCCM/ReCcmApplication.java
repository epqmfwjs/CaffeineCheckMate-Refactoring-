package com.project.ReCCM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ReCcmApplication {

	public static void main(String[] args) {

		SpringApplication.run(ReCcmApplication.class, args);
	}

}
