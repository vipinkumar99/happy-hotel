package com.happy.hotel.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.happy.hotel.entity.PaymentEntity;

@DataJpaTest
@ActiveProfiles("test")
class PaymentRepositoryTest {

	@Autowired
	private PaymentRepository paymentRepository;

	@Test
	public void saveTest() {
		PaymentEntity entity = new PaymentEntity();
		entity.setPrice(400.0);
		paymentRepository.save(entity);
		assertEquals(1, entity.getId());
		assertEquals(400.0, entity.getPrice());
	}
}
