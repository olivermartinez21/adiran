package com.tmm.myre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyreApplication.class, args);
	}

}
