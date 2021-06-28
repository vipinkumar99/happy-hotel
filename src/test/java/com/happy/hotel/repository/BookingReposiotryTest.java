package com.happy.hotel.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.happy.hotel.entity.BookingEntity;
import com.happy.hotel.enums.BookingStatus;

@DataJpaTest
@ActiveProfiles("test")
class BookingReposiotryTest {

	@Autowired
	private BookingReposiotry bookingReposiotry;

	private BookingEntity entity;

	@BeforeEach
	public void setup() {
		entity = new BookingEntity();
		entity.setId(1);
		entity.setDateFrom(LocalDate.now());
		entity.setDateTo(LocalDate.now().plusDays(2));
		entity.setGuestCount(2);
		entity.setPrepaid(true);
		entity.setRoomId(1);
		entity.setCustomerId(1);
		entity.setStatus(BookingStatus.BOOKED);

	}

	@Test
	public void saveTest() {
		BookingEntity booking = bookingReposiotry.save(entity);
		assertNotNull(booking.getId());
		assertEquals(BookingStatus.BOOKED, booking.getStatus());
	}

	@Test
	public void findByIdTest() {
		BookingEntity booking = bookingReposiotry.save(entity);
		Optional<BookingEntity> optional = bookingReposiotry.findById(booking.getId());
		assertThat(optional).isNotEmpty();
		assertEquals(BookingStatus.BOOKED, booking.getStatus());
	}

	@AfterEach
	public void clear() {
		bookingReposiotry.deleteAll();
	}

}
