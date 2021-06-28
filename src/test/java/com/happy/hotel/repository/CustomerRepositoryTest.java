package com.happy.hotel.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.happy.hotel.entity.CustomerEntity;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	private CustomerEntity entity;

	@BeforeEach
	public void setup() {
		entity = new CustomerEntity();
		entity.setCustomerId("HH-1");
		entity.setName("vipin");
		entity.setAge(26);
		entity.setAddress("Badshahpur");
		entity.setMobileNo("9999452345");
	}

	@Test
	public void saveTest() {
		CustomerEntity customer = customerRepository.save(entity);
		assertNotNull(customer.getId());
		assertEquals("HH-1", customer.getCustomerId());
	}

	@Test
	public void findByIdTest() {
		CustomerEntity customer = customerRepository.save(entity);
		Optional<CustomerEntity> optional = customerRepository.findById(customer.getId());
		assertThat(optional).isNotEmpty();
		assertEquals("HH-1", customer.getCustomerId());
	}

	@Test
	public void findAllTest() {
		customerRepository.save(entity);
		List<CustomerEntity> entities = customerRepository.findAll();
		assertThat(entities).isNotEmpty();
		assertThat(entities).hasSize(1);
	}

	@Test
	public void deleteTest() {
		CustomerEntity customer = customerRepository.save(entity);
		customerRepository.delete(customer);
		Optional<CustomerEntity> optional = customerRepository.findById(customer.getId());
		assertThat(optional).isEmpty();
	}

	@AfterEach
	public void clear() {
		customerRepository.deleteAll();
	}
}
