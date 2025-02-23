package com.code.shopee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ShopeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopeeApplication.class, args);
		System.out.println("sjkdhjskjdjhskjdskjdskjdskdjsdjs");
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}
	
}
