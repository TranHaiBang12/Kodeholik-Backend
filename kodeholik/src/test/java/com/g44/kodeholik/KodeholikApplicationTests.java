package com.g44.kodeholik;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@EnableElasticsearchRepositories
@TestPropertySource(locations = "classpath:application-test.yml")

public class KodeholikApplicationTests {

	@Test
	void itShouldAddNumbers() {
		// given
		int numberOne = 20;
		int numberTwo = 23;

		// when
		int result = numberOne + numberTwo;

		// then
		assertEquals(result, 43);
	}

}
