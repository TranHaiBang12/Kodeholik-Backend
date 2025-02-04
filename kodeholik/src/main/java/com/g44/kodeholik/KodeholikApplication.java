package com.g44.kodeholik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class KodeholikApplication {

	public static void main(String[] args) {
		SpringApplication.run(KodeholikApplication.class, args);
	}

}
