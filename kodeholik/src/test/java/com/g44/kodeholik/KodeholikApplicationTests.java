package com.g44.kodeholik;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KodeholikApplicationTests {

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
