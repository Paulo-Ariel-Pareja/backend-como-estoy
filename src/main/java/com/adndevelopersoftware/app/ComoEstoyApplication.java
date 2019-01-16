package com.adndevelopersoftware.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ComoEstoyApplication implements CommandLineRunner{

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(ComoEstoyApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		String clave = "123456789123456789";

		for (int i = 0; i < 4; i++) {
			String passwordBcrypt = passwordEncoder.encode(clave);
			System.out.println(passwordBcrypt);
		}

	}

}

