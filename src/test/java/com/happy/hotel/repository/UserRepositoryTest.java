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

import com.happy.hotel.entity.UserEntity;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	private UserEntity entity;

	@BeforeEach
	public void setup() {
		entity = new UserEntity();
		entity.setName("vipin");
		entity.setUsername("vipin123");
		entity.setPassword("123456");
	}

	@Test
	public void saveTest() {
		UserEntity user = userRepository.save(entity);
		assertNotNull(user.getId());
		assertEquals("vipin123", user.getUsername());
	}

	@Test
	public void findByIdTest() {
		UserEntity user = userRepository.save(entity);
		Optional<UserEntity> optional = userRepository.findById(user.getId());
		assertThat(optional).isNotEmpty();
		assertEquals("vipin123", user.getUsername());
	}

	@Test
	public void findAllTest() {
		userRepository.save(entity);
		List<UserEntity> entities = userRepository.findAll();
		assertThat(entities).isNotEmpty();
		assertThat(entities).hasSize(1);
	}

	@Test
	public void findByUsernameTest() {
		userRepository.save(entity);
		Optional<UserEntity> optional = userRepository.findByUsername("vipin123");
		assertThat(optional).isNotEmpty();
		assertEquals("vipin123", optional.get().getUsername());
	}

	@Test
	public void deleteTest() {
		UserEntity user = userRepository.save(entity);
		userRepository.delete(user);
		Optional<UserEntity> optional = userRepository.findById(user.getId());
		assertThat(optional).isEmpty();
	}

	@AfterEach
	public void clear() {
		userRepository.deleteAll();
	}

}
