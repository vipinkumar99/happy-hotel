package com.happy.hotel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class HappyHotelApplicationTests {

	@Test
	void contextLoads() {
		log.info("Test is started!");
		assertEquals(true, true);
	}

}
