package com.coffee.coffee_diary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CoffeeDiaryApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeDiaryApplication.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) {
		// Create user table
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users(" +
				"user_id UUID PRIMARY KEY," +
				"email VARCHAR UNIQUE," +
				"password VARCHAR NOT NULL" +
		");"
		);
	}
}
