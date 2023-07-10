package com.Dockerates.BookLending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BookLendingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookLendingApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:5173", "https://library-frontend-9qlzt0p0s-dockerates.vercel.app/")
				.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
				.allowCredentials(true);
			}
		};
	}

}
