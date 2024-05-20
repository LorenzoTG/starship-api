package com.w2m.starshipapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
public class StarshipApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarshipApiApplication.class, args);
	}

}
