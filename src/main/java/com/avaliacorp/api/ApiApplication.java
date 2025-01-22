package com.avaliacorp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	private static String token;

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(ApiApplication.class);
		app.addListeners(new EnvLoader());
		app.run(args);

	}

	public static String getToken(){
		return token;
	}

	public static void setToken(String content){
		token = content;
	}

}
