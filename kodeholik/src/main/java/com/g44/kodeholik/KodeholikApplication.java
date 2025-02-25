package com.g44.kodeholik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableElasticsearchRepositories
@EnableScheduling
public class KodeholikApplication {

	public static void main(String[] args) {
		SpringApplication.run(KodeholikApplication.class, args);
	}

}
