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

import com.happy.hotel.entity.RoomEntity;
import com.happy.hotel.enums.RoomStatus;

@DataJpaTest
@ActiveProfiles("test")
class RoomRepositoryTest {

	@Autowired
	private RoomRepository roomRepository;

	private RoomEntity entity;

	@BeforeEach
	public void setup() {
		entity = new RoomEntity();
		entity.setCapacity(5);
		entity.setStatus(RoomStatus.UNBOOKED);
	}

	@Test
	public void saveTest() {
		RoomEntity room = roomRepository.save(entity);
		assertNotNull(room.getId());
		assertEquals(5, room.getCapacity());
	}

	@Test
	public void findByIdTest() {
		RoomEntity room = roomRepository.save(entity);
		Optional<RoomEntity> optional = roomRepository.findById(room.getId());
		assertThat(optional).isNotEmpty();
	}

	@Test
	public void findAllTest() {
		roomRepository.save(entity);
		List<RoomEntity> entities = roomRepository.findAll();
		assertThat(entities).isNotEmpty();
		assertThat(entities).hasSize(1);
	}

	@Test
	public void findByCapacityTest() {
		roomRepository.save(entity);
		Optional<RoomEntity> optional = roomRepository.findByCapacity(5);
		assertThat(optional).isNotEmpty();
		assertEquals(5, optional.get().getCapacity());
	}

	@Test
	public void deleteTest() {
		RoomEntity room = roomRepository.save(entity);
		roomRepository.delete(room);
		Optional<RoomEntity> optional = roomRepository.findById(room.getId());
		assertThat(optional).isEmpty();
	}

	@AfterEach
	public void clear() {
		roomRepository.deleteAll();
	}

}
