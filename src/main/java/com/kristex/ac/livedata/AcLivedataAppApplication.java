package com.kristex.ac.livedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AcLivedataAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcLivedataAppApplication.class, args);
	}

}
